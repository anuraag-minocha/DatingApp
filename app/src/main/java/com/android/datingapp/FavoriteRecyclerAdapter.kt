package com.android.datingapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class FavoriteRecyclerAdapter(
    var list: ArrayList<MemberData>,
    var context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_favorite, parent, false)
        return MemberViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as MemberViewHolder
        var picture = list[position].picture
        if (!picture.startsWith("https"))
            picture = picture.replace("http", "https")
        Picasso.with(context)
            .load(picture)
            .placeholder(R.drawable.ic_profile).into(viewHolder.image)

        viewHolder.title.text = "My name is"
        viewHolder.description.text =
            list[position].name
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

    inner class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {

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

        init {
            name.setOnClickListener {
                title.text = "My name is"
                description.text =
                    list[adapterPosition].name
                vname.visibility = View.VISIBLE
                vbday.visibility = View.INVISIBLE
                vlocation.visibility = View.INVISIBLE
                vphone.visibility = View.INVISIBLE
                vemail.visibility = View.INVISIBLE
                name.setColorFilter(
                    ContextCompat.getColor(context, R.color.green),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                bday.setColorFilter(null)
                location.setColorFilter(null)
                phone.setColorFilter(null)
                email.setColorFilter(null)
            }
            bday.setOnClickListener {
                title.text = "My birthday is"
                description.text =
                    getDate(list[adapterPosition].dob.toLong(), "mm/dd/yyyy")
                vname.visibility = View.INVISIBLE
                vbday.visibility = View.VISIBLE
                vlocation.visibility = View.INVISIBLE
                vphone.visibility = View.INVISIBLE
                vemail.visibility = View.INVISIBLE
                bday.setColorFilter(
                    ContextCompat.getColor(context, R.color.green),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                name.setColorFilter(null)
                location.setColorFilter(null)
                phone.setColorFilter(null)
                email.setColorFilter(null)
            }
            location.setOnClickListener {
                title.text = "My address is"
                description.text =
                    list[adapterPosition].location
                vname.visibility = View.INVISIBLE
                vbday.visibility = View.INVISIBLE
                vlocation.visibility = View.VISIBLE
                vphone.visibility = View.INVISIBLE
                vemail.visibility = View.INVISIBLE
                location.setColorFilter(
                    ContextCompat.getColor(context, R.color.green),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                name.setColorFilter(null)
                bday.setColorFilter(null)
                phone.setColorFilter(null)
                email.setColorFilter(null)
            }
            phone.setOnClickListener {
                title.text = "My phone is"
                description.text = list[adapterPosition].mobile
                vname.visibility = View.INVISIBLE
                vbday.visibility = View.INVISIBLE
                vlocation.visibility = View.INVISIBLE
                vphone.visibility = View.VISIBLE
                vemail.visibility = View.INVISIBLE
                phone.setColorFilter(
                    ContextCompat.getColor(context, R.color.green),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                name.setColorFilter(null)
                location.setColorFilter(null)
                bday.setColorFilter(null)
                email.setColorFilter(null)
            }
            email.setOnClickListener {
                title.text = "My email is"
                description.text = list[adapterPosition].email
                vname.visibility = View.INVISIBLE
                vbday.visibility = View.INVISIBLE
                vlocation.visibility = View.INVISIBLE
                vphone.visibility = View.INVISIBLE
                vemail.visibility = View.VISIBLE
                email.setColorFilter(
                    ContextCompat.getColor(context, R.color.green),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                name.setColorFilter(null)
                location.setColorFilter(null)
                phone.setColorFilter(null)
                bday.setColorFilter(null)
            }
        }

    }

    fun updateList(arrayList: ArrayList<MemberData>) {
        list.clear()
        list.addAll(arrayList)
        notifyDataSetChanged()
    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds)
        return formatter.format(calendar.getTime())
    }

}
