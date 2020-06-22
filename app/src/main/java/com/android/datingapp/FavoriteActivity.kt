package com.android.datingapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {

    lateinit var db: AppDatabase
    lateinit var favoriteRecyclerAdapter: FavoriteRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rv.layoutManager = layoutManager
        favoriteRecyclerAdapter = FavoriteRecyclerAdapter(ArrayList(), this)
        rv.adapter = favoriteRecyclerAdapter

        fetchFromDb()
    }

    fun finish(view: View) {
        finish()
    }

    fun clearDb(view: View) {
        val dbObservable: Single<Boolean> = Single.create { emitter ->
            try {
                db.memberDao().deleteAll()
                emitter.onSuccess(true)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
        val disposable = object : DisposableSingleObserver<Boolean>() {
            override fun onSuccess(t: Boolean) {
                if(t)
                favoriteRecyclerAdapter.updateList(arrayListOf())
            }

            override fun onError(e: Throwable) {
            }
        }
        dbObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(disposable)
    }

    fun fetchFromDb() {
        val dbObservable: Single<ArrayList<MemberData>> = Single.create { emitter ->
            try {

                emitter.onSuccess(db.memberDao().getAll() as ArrayList<MemberData>)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
        val disposable = object : DisposableSingleObserver<ArrayList<MemberData>>() {
            override fun onSuccess(t: ArrayList<MemberData>) {
                favoriteRecyclerAdapter.updateList(t)
            }

            override fun onError(e: Throwable) {
            }
        }
        dbObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(disposable)

    }

}