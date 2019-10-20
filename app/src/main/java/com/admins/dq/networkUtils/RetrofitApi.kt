package com.admins.dq.networkUtils

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


open class RetrofitApi {

    val baseUrl = "http://192.168.0.117/"

    private val moshi: Moshi = Moshi.Builder().build()

    fun getRetrofit(): Retrofit? {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }


}