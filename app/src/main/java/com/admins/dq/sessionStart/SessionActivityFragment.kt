package com.admins.dq.sessionStart

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.admins.dq.R
import com.admins.dq.diagnosis.DiagnosingActivity
import com.admins.dq.model.BaseComplaints
import com.github.stephenvinouze.core.interfaces.RecognitionCallback
import com.github.stephenvinouze.core.managers.KontinuousRecognitionManager
import com.github.stephenvinouze.core.models.RecognitionStatus
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_session.*

/**
 * A placeholder fragment containing a simple view.
 */
class SessionActivityFragment : Fragment(), RecognitionCallback {

    lateinit var mViewModel: SessionViewModel

    val complaintsObserver = Observer<BaseComplaints> { t -> handleBaseComplaints(t) }
    val askObserver = Observer<String> { t -> handleAskNamesRecieved(t) }

    private fun handleAskNamesRecieved(complaintId: String) {
        val intent = Intent(activity?.baseContext, DiagnosingActivity::class.java)
        intent.putExtra("complaintId", complaintId)
        startActivity(intent)
    }

    private fun handleBaseComplaints(t: BaseComplaints?) {
        baseComplaints = t!!
        mViewModel.startrecognizingvoices()
        baseComplaints.table?.forEach { t->textView.append("\n ${t!!.baseComplainName}")}
    }

    var baseComplaints = BaseComplaints()

    private val recognitionManager: KontinuousRecognitionManager by lazy {
        KontinuousRecognitionManager(
            activity?.baseContext!!,
            shouldMute = true, activationKeyword = ACTIVATION_KEYWORD,
            callback = this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProviders.of(this)[SessionViewModel::class.java]
        mViewModel.text.observe(
            this,
            Observer { if (TextUtils.equals(it, "start")) startRecognition() })
        mViewModel.resultsComplaintsLiveData.observe(this, complaintsObserver)
        mViewModel.resultsComplaintsIdLiveData.observe(this, askObserver)
        mViewModel.getBaseComplaints()

        return inflater.inflate(R.layout.fragment_session, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar.visibility = View.INVISIBLE
        progressBar.max = 10

    }

    private fun startRecognition() {
        progressBar.isIndeterminate = false
        progressBar.visibility = View.VISIBLE
        recognitionManager.startRecognition()
    }

    private fun stopRecognition() {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.INVISIBLE
        recognitionManager.stopRecognition()
    }


    override fun onDestroy() {
        super.onDestroy()
        recognitionManager.destroyRecognizer()
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                activity?.baseContext!!,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startRecognition()
        }

    }

    override fun onPause() {
        super.onPause()
        recognitionManager.destroyRecognizer()
    }

    private fun getErrorText(errorCode: Int): String = when (errorCode) {
        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
        SpeechRecognizer.ERROR_CLIENT -> "Client side error"
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
        SpeechRecognizer.ERROR_NETWORK -> "Network error"
        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
        SpeechRecognizer.ERROR_NO_MATCH -> "No match"
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
        SpeechRecognizer.ERROR_SERVER -> "Error from server"
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
        else -> "Didn't understand, please try again."
    }


    override fun onBeginningOfSpeech() {

    }

    override fun onBufferReceived(buffer: ByteArray) {
    }

    override fun onEndOfSpeech() {

    }

    override fun onError(errorCode: Int) {
        val errorMessage = getErrorText(errorCode)
        Log.i("Recognition", "onError: $errorMessage")
        textView.setText(errorMessage)
    }

    override fun onEvent(eventType: Int, params: Bundle) {

    }

    override fun onKeywordDetected() {
        Log.i("Recognition", "keyword detected !!!")
        textView.text = "Keyword detected"
    }

    override fun onPartialResults(results: List<String>) {

    }

    override fun onPrepared(status: RecognitionStatus) {
        when (status) {
            RecognitionStatus.SUCCESS -> {
                Log.i("Recognition", "onPrepared: Success")

            }
            RecognitionStatus.FAILURE,
            RecognitionStatus.UNAVAILABLE -> {
                Log.i("Recognition", "onPrepared: Failure or unavailable")
                MaterialAlertDialogBuilder(activity?.baseContext!!)
                    .setTitle("Speech Recognizer unavailable")
                    .setMessage("Your device does not support Speech Recognition. Sorry!")
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            }
        }
    }

    override fun onReadyForSpeech(params: Bundle) {

    }

    override fun onResults(results: List<String>, scores: FloatArray?) {
        val text = results.joinToString(separator = "\n")
        Log.i("Recognition", "onResults : $text")
        textView.text = text
        results.forEach { action ->
            baseComplaints.table?.forEach { innerAction ->
                if (action.contains(innerAction?.baseComplainName!!, ignoreCase = true)) {
                    mViewModel.getAsks(innerAction.baseComplainD.toString())
                    stopRecognition()
                }
            }
/*
            if (action.contains("abdominal")) {
                startActivity(Intent(activity?.baseContext, DiagnosingActivity::class.java))
                stopRecognition()
            }*/

        }

    }

    override fun onRmsChanged(rmsdB: Float) {
        progressBar.progress = rmsdB.toInt()
    }


    companion object {
        private const val ACTIVATION_KEYWORD = "hello"
    }
}
