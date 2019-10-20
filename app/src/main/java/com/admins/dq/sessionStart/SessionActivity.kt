package com.admins.dq.sessionStart

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.admins.dq.R
import com.admins.dq.model.BaseComplaints
import com.admins.dq.networkUtils.ApiUtils
import kotlinx.android.synthetic.main.activity_session.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SessionActivity : AppCompatActivity() {

    lateinit var mViewModel: SessionViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProviders.of(this)[SessionViewModel::class.java]


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_REQUEST_CODE
            )
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_AUDIO_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mViewModel.getBaseComplaints()
                }
            }
        }
    }

    companion object {
        private const val RECORD_AUDIO_REQUEST_CODE = 101
    }

}


class SessionViewModel : ViewModel() {


    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    fun startrecognizingvoices() {
        _text.value = "start"
    }

    val resultsComplaintsLiveData = MutableLiveData<BaseComplaints>()
    val resultsComplaintsIdLiveData = MutableLiveData<String>()

    val api = ApiUtils()

    fun getBaseComplaints() {
        api.getServiceClass()?.getAllComplaints()?.enqueue(object : Callback<BaseComplaints> {
            override fun onFailure(call: Call<BaseComplaints>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<BaseComplaints>,
                response: Response<BaseComplaints>
            ) {
                if (response.isSuccessful) {
                    resultsComplaintsLiveData.postValue(response.body())
                }

            }
        })
    }

    fun getAsks(id: String) {
        resultsComplaintsIdLiveData.value = id
    }
}