package com.android.datingapp

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Networking {

    var networkService: NetworkService? = null
    var gson = GsonBuilder()
        .setLenient()
        .create()

    fun create(): NetworkService {
        if (networkService == null) {
            networkService = Retrofit.Builder()
                .baseUrl(Endpoints.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(NetworkService::class.java)
        }
        return networkService!!
    }
}