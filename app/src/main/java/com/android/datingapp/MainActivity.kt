package com.android.datingapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import com.lorentzos.flingswipe.SwipeFlingAdapterView.onFlingListener
import com.squareup.picasso.Picasso
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var myAppAdapter: MyAppAdapter
    var viewHolder: ViewHolder? = null
    private var array: ArrayList<MemberParent> = ArrayList()
    private var flingContainer: SwipeFlingAdapterView? = null
    lateinit var mainViewModel: MainViewModel
    var loaded = false
    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        flingContainer = findViewById(R.id.frame)
        myAppAdapter = MyAppAdapter(array, this@MainActivity)
        flingContainer!!.adapter = myAppAdapter
        flingContainer!!.setFlingListener(object : onFlingListener {
            override fun removeFirstObjectInAdapter() {}
            override fun onLeftCardExit(dataObject: Any) {
                array.removeAt(0)
                myAppAdapter.notifyDataSetChanged()
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
            }

            override fun onRightCardExit(dataObject: Any) {
                insertInDb(array[0].member)
                array.removeAt(0)
                myAppAdapter.notifyDataSetChanged()
            }

            override fun onAdapterAboutToEmpty(itemsInAdapter: Int) {
                if (loaded)
                    mainViewModel.getMemberList()
            }

            override fun onScroll(scrollProgressPercent: Float) {
                val view = flingContainer!!.selectedView
                view.findViewById<View>(R.id.iDislike)
                    .setAlpha(if (scrollProgressPercent < 0) -scrollProgressPercent else 0f)
                view.findViewById<View>(R.id.iLike)
                    .setAlpha(if (scrollProgressPercent > 0) scrollProgressPercent else 0f)
            }
        })


        // Optionally add an OnItemClickListener
        flingContainer!!.setOnItemClickListener { itemPosition, dataObject ->
            val view = flingContainer!!.selectedView
            myAppAdapter.notifyDataSetChanged()
        }

        mainViewModel.getMemberList()

        mainViewModel.memberList.observe(this, Observer {
            array.addAll(it)
            myAppAdapter.updateList(array)
            loaded = true
            progress_bar.visibility = View.GONE
        })
    }

    class ViewHolder(view: View) {

        var title: TextView = view.findViewById(R.id.title)
        var description: TextView = view.findViewById(R.id.description)
        var image: ImageView = view.findViewById(R.id.image)
        var name: ImageView = view.findViewById(R.id.iName)
        var bday: ImageView = view.findViewById(R.id.iBday)
        var location: ImageView = view.findViewById(R.id.iLocation)
        var phone: ImageView = view.findViewById(R.id.iPhone)
        var email: ImageView = view.findViewById(R.id.iEmail)
        var vname: View = view.findViewById(R.id.vName)
        var vbday: View = view.findViewById(R.id.vBday)
        var vlocation: View = view.findViewById(R.id.vLocation)
        var vphone: View = view.findViewById(R.id.vPhone)
        var vemail: View = view.findViewById(R.id.vEmail)

    }

    inner class MyAppAdapter(list: List<MemberParent>, context: Context) :
        BaseAdapter() {
        var parkingList: List<MemberParent> = list
        var context: Context = context
        override fun getCount(): Int {
            return parkingList!!.size
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {
            var rowView = convertView
            var viewHolder: ViewHolder? = null
            if (rowView == null) {
                val inflater: LayoutInflater = LayoutInflater.from(parent.context)
                rowView = inflater.inflate(R.layout.list_item_card, parent, false)
                // configure view holder
                viewHolder = ViewHolder(rowView)
                rowView.tag = viewHolder
            } else {
                viewHolder = convertView!!.tag as ViewHolder
            }
            var url = parkingList[position].member.picture
            if (!url.startsWith("https"))
                url = url.replace("http", "https")
            Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.ic_profile).into(viewHolder.image)

            viewHolder.title.text = "My name is"
            viewHolder.description.text =
                parkingList[position].member.name.first.capitalize() + " " + parkingList[position].member.name.last.substring(
                    0,
                    1
                ).capitalize()
            viewHolder.vname.visibility = View.VISIBLE
            viewHolder.name.setColorFilter(
                ContextCompat.getColor(context, R.color.green),
                android.graphics.PorterDuff.Mode.SRC_IN
            )

            viewHolder.name.setOnClickListener {
                viewHolder.title.text = "My name is"
                viewHolder.description.text =
                    parkingList[position].member.name.first.capitalize() + " " + parkingList[position].member.name.last.substring(
                        0,
                        1
                    ).capitalize()
                viewHolder.vname.visibility = View.VISIBLE
                viewHolder.vbday.visibility = View.INVISIBLE
                viewHolder.vlocation.visibility = View.INVISIBLE
                viewHolder.vphone.visibility = View.INVISIBLE
                viewHolder.vemail.visibility = View.INVISIBLE
                viewHolder.name.setColorFilter(
                    ContextCompat.getColor(context, R.color.green),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                viewHolder.bday.setColorFilter(null)
                viewHolder.location.setColorFilter(null)
                viewHolder.phone.setColorFilter(null)
                viewHolder.email.setColorFilter(null)
            }
            viewHolder.bday.setOnClickListener {
                viewHolder.title.text = "My birthday is"
                viewHolder.description.text =
                    getDate(parkingList[position].member.dob.toLong(), "mm/dd/yyyy")
                viewHolder.vname.visibility = View.INVISIBLE
                viewHolder.vbday.visibility = View.VISIBLE
                viewHolder.vlocation.visibility = View.INVISIBLE
                viewHolder.vphone.visibility = View.INVISIBLE
                viewHolder.vemail.visibility = View.INVISIBLE
                viewHolder.bday.setColorFilter(
                    ContextCompat.getColor(context, R.color.green),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                viewHolder.name.setColorFilter(null)
                viewHolder.location.setColorFilter(null)
                viewHolder.phone.setColorFilter(null)
                viewHolder.email.setColorFilter(null)
            }
            viewHolder.location.setOnClickListener {
                viewHolder.title.text = "My address is"
                viewHolder.description.text =
                    parkingList[position].member.location.city.capitalize() + ", " + parkingList[position].member.location.state.capitalize()
                viewHolder.vname.visibility = View.INVISIBLE
                viewHolder.vbday.visibility = View.INVISIBLE
                viewHolder.vlocation.visibility = View.VISIBLE
                viewHolder.vphone.visibility = View.INVISIBLE
                viewHolder.vemail.visibility = View.INVISIBLE
                viewHolder.location.setColorFilter(
                    ContextCompat.getColor(context, R.color.green),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                viewHolder.name.setColorFilter(null)
                viewHolder.bday.setColorFilter(null)
                viewHolder.phone.setColorFilter(null)
                viewHolder.email.setColorFilter(null)
            }
            viewHolder.phone.setOnClickListener {
                viewHolder.title.text = "My phone is"
                viewHolder.description.text = parkingList[position].member.mobile
                viewHolder.vname.visibility = View.INVISIBLE
                viewHolder.vbday.visibility = View.INVISIBLE
                viewHolder.vlocation.visibility = View.INVISIBLE
                viewHolder.vphone.visibility = View.VISIBLE
                viewHolder.vemail.visibility = View.INVISIBLE
                viewHolder.phone.setColorFilter(
                    ContextCompat.getColor(context, R.color.green),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                viewHolder.name.setColorFilter(null)
                viewHolder.location.setColorFilter(null)
                viewHolder.bday.setColorFilter(null)
                viewHolder.email.setColorFilter(null)
            }
            viewHolder.email.setOnClickListener {
                viewHolder.title.text = "My email is"
                viewHolder.description.text = parkingList[position].member.email
                viewHolder.vname.visibility = View.INVISIBLE
                viewHolder.vbday.visibility = View.INVISIBLE
                viewHolder.vlocation.visibility = View.INVISIBLE
                viewHolder.vphone.visibility = View.INVISIBLE
                viewHolder.vemail.visibility = View.VISIBLE
                viewHolder.email.setColorFilter(
                    ContextCompat.getColor(context, R.color.green),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                viewHolder.name.setColorFilter(null)
                viewHolder.location.setColorFilter(null)
                viewHolder.phone.setColorFilter(null)
                viewHolder.bday.setColorFilter(null)
            }
            return rowView!!
        }

        fun updateList(arrayList: ArrayList<MemberParent>) {
            parkingList = arrayList
            notifyDataSetChanged()
        }

    }

    fun finish(view: View) {
        finish()
    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds)
        return formatter.format(calendar.getTime())
    }

    fun insertInDb(member: Member) {
        val dbObservable: Single<Boolean> = Single.create { emitter ->
            try {

                db.memberDao().insertAll(
                    MemberData(
                        0, member.name.first.capitalize() + " " + member.name.last.substring(0, 1).capitalize(),
                        member.dob, member.location.city.capitalize() + ", " + member.location.state.capitalize(),
                        member.mobile, member.email, member.picture
                    )
                )
                emitter.onSuccess(true)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
        val disposable = object : DisposableSingleObserver<Boolean>() {
            override fun onSuccess(t: Boolean) {

            }

            override fun onError(e: Throwable) {
            }
        }
        dbObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(disposable)

    }

    fun favorites(view: View) {
        startActivity(Intent(this, FavoriteActivity::class.java))
    }

}