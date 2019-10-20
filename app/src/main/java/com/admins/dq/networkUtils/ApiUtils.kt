package com.admins.dq.networkUtils

class ApiUtils : RetrofitApi() {

    fun getServiceClass(): RetrofitInterface? {
        return getRetrofit()?.create(RetrofitInterface::class.java)
    }
}