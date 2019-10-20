package com.admins.dq.result

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.admins.dq.R
import com.admins.dq.diagnosis.ResultsAdapter
import com.admins.dq.model.ResultsModel
import com.admins.dq.networkUtils.ApiUtils
import com.admins.dq.utils.MyLinearLayoutManager
import kotlinx.android.synthetic.main.activity_results.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultsActivity : AppCompatActivity() {

    val resultsObserver = Observer<List<ResultsModel>> { t -> handleListOfResults(t) }

    private fun handleListOfResults(t: List<ResultsModel>?) {
        list.clear()
        list.addAll(t!!)
        dataAdapter.notifyDataSetChanged()
    }

    lateinit var mViewModel: ResultsViewModel
    var list = arrayListOf<ResultsModel>()

    lateinit var dataAdapter: ResultsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        mViewModel = ViewModelProviders.of(this)[ResultsViewModel::class.java]
        mViewModel.resultsLiveData.observe(this, resultsObserver)

        title = "Results"

        dataAdapter = ResultsAdapter(list, this)

        val linearLayoutManager = MyLinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rv_get_results.layoutManager = linearLayoutManager
        rv_get_results.adapter = dataAdapter

    }

    override fun onResume() {
        super.onResume()
        mViewModel.getResults()
    }
}


class ResultsViewModel : ViewModel() {

    val api = ApiUtils()

    val resultsLiveData = MutableLiveData<List<ResultsModel>>()

    fun getResults() {
        api.getServiceClass()?.getResults()?.enqueue(object : Callback<List<ResultsModel>> {
            override fun onFailure(call: Call<List<ResultsModel>>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<List<ResultsModel>>,
                response: Response<List<ResultsModel>>
            ) {
                if (response.isSuccessful and (response.body() != null))
                    resultsLiveData.postValue(response.body())
            }
        })
    }


}
