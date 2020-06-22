package com.android.datingapp


import com.android.datingapp.MemberResponse
import io.reactivex.Single
import retrofit2.http.GET


interface NetworkService {

    @GET("/api/0.4/?results=20")
    fun getMembers(): Single<MemberResponse>

}