package com.android.datingapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {

    private val repository = Repository()
    private val compositeDisposable = CompositeDisposable()
    val memberList = MutableLiveData<ArrayList<MemberParent>>()
    val loading = MutableLiveData<Boolean>()

    fun getMemberList() {
        loading.postValue(true)
        val disposable = object : DisposableSingleObserver<MemberResponse>() {
            override fun onSuccess(t: MemberResponse) {
                loading.postValue(false)
                memberList.postValue(t.members)
            }

            override fun onError(e: Throwable) {
                loading.postValue(false)
            }
        }
        repository.getMembers().subscribeOn(Schedulers.io())
            .subscribe(disposable)
        compositeDisposable.add(disposable)

    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}