package com.admins.dq.networkUtils

import com.admins.dq.model.AnswersModel
import com.admins.dq.model.AskNames
import com.admins.dq.model.BaseComplaints
import com.admins.dq.model.ResultsModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query


interface RetrofitInterface {

    @GET("Api/Test")
    fun getAllComplaints(): Call<BaseComplaints>

    @GET("api/Test")
    fun getComplaintQuestions(@Query("id") id: String): Call<List<AskNames>>

    @PUT("api/Test")
    fun sendAnswers(@Body answersModel: AnswersModel): Call<String>

    @GET("api/Test")
    fun getResults(@Query("word") id: String = "okkkkkk"): Call<List<ResultsModel>>


}