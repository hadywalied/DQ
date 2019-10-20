package com.admins.dq.diagnosis

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.admins.dq.model.AnswersModel
import com.admins.dq.model.AskNames
import com.admins.dq.model.oldModel.Questions
import com.admins.dq.networkUtils.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiagnosisViewModel : ViewModel() {


    val listMutableLiveData = MutableLiveData<List<Questions>>()
    val resultAnswersSent = MutableLiveData<String>()

    /*private var list = arrayListOf<Questions>(
        Questions("Quest 1 ", "No"),
        Questions("Quest 2 ", "No"),
        Questions("Quest 3", "No"),
        Questions("Quest 4 ", "No")
    )*/
    private var list = arrayListOf<Questions>()
//    private var answerlist = arrayListOf("4", "yes", "no", "yes", "no", "1", "1", "1")
//    private var answersModel = AnswersModel(answerlist)

    val api = ApiUtils()


    fun getQuestions() {
        listMutableLiveData.value = list
    }

    fun storeResult(answer: String, count: Int) {
        list[count].answer = answer
        getQuestions()
    }

    fun getAsks(id: String) {
        api.getServiceClass()?.getComplaintQuestions(id)
            ?.enqueue(object : Callback<List<AskNames>> {
                override fun onFailure(call: Call<List<AskNames>>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<List<AskNames>>,
                    response: Response<List<AskNames>>
                ) {
                    if (response.isSuccessful and (response.body() != null) and (response.body()?.isNotEmpty()!!)) {
//                        resultsAsksLiveData.postValue(response.body())
                        list.clear()
                        for (action in response.body()!!) {
                            list.add(Questions(question = action.askName))
                        }
                        getQuestions()
                    }

                }
            })
    }

    fun sendAnswers(answer: AnswersModel) {
        api.getServiceClass()?.sendAnswers(answer)?.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {

            }

            override fun onResponse(call: Call<String>, response: Response<String>) {

                if (response.isSuccessful and (response.body() != null)) {
                    resultAnswersSent.postValue(response.body())
                }
            }
        })
    }

}