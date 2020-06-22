package com.android.datingapp

import com.android.datingapp.MemberResponse
import com.android.datingapp.Networking
import io.reactivex.Single

class Repository {

    private val networkService = Networking.create()

    fun getMembers(): Single<MemberResponse> {
        return networkService.getMembers()
    }

}