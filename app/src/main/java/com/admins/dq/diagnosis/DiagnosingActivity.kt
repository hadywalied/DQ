package com.admins.dq.diagnosis

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.admins.dq.R
import com.admins.dq.model.AnswersModel
import com.admins.dq.model.oldModel.Questions
import com.admins.dq.result.ResultsActivity
import com.admins.dq.utils.MyLinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_diagnosing.*
import net.gotev.speech.*

class DiagnosingActivity : AppCompatActivity(), SpeechDelegate,
    DiagnosisRecyclerAdapter.OnButtonClicked {

    private val listObserver: Observer<List<Questions>> = Observer { t -> showList(t) }
    private val answersObserver: Observer<String> = Observer { t -> answersSupmittedCheck(t) }

    private fun answersSupmittedCheck(t: String?) {
        if (TextUtils.equals(t, "ok")) {
            startActivity(Intent(this@DiagnosingActivity, ResultsActivity::class.java))
            finish()
        }
    }

    var count = 0

    lateinit var mViewModel: DiagnosisViewModel

    var list = arrayListOf<Questions>()

    lateinit var dataAdapter: DiagnosisRecyclerAdapter

    var complaintId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnosing)

        Speech.init(this, packageName)

        checkPermission()

        title = "Questions"

        mViewModel = ViewModelProviders.of(this)[DiagnosisViewModel::class.java]

        mViewModel.listMutableLiveData.observe(this, listObserver)
        mViewModel.resultAnswersSent.observe(this, answersObserver)

        dataAdapter = DiagnosisRecyclerAdapter(list, this, this)

        val linearLayoutManager = MyLinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        rv_get_all.layoutManager = linearLayoutManager
        rv_get_all.adapter = dataAdapter


        val colors = intArrayOf(
            ContextCompat.getColor(this, android.R.color.black),
            ContextCompat.getColor(this, android.R.color.darker_gray),
            ContextCompat.getColor(this, android.R.color.black),
            ContextCompat.getColor(this, android.R.color.holo_orange_dark),
            ContextCompat.getColor(this, android.R.color.holo_red_dark)
        )

        progressBar.setColors(colors)
        complaintId = intent.getStringExtra("complaintId")!!

        mViewModel.getAsks(complaintId)

    }

    override fun onResume() {
        super.onResume()
//        mViewModel.getQuestions()
    }

    override fun onDestroy() {
        super.onDestroy()
        Speech.getInstance().shutdown()
    }


    fun startListening() {
        if (Speech.getInstance().isListening) {
            Speech.getInstance().stopListening()
        } else {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                onRecordAudioPermissionGranted()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    MY_PERMISSIONS_REQUEST_PERMISSION
                )

            }
        }

    }


    private fun showList(t: List<Questions>) {


        if (count < t.size) {
            list.clear()
            for (item in 0..count) {
                list.add(t[item])
                dataAdapter.notifyDataSetChanged()
            }
            Handler().postDelayed({

                Speech.getInstance().say(
                    t[count].question,
                    object : TextToSpeechCallback {
                        override fun onError() {

                        }

                        override fun onStart() {

                        }

                        override fun onCompleted() {
                            startListening()
                        }
                    })
            }, 150)
        } else {
            val answersList = arrayListOf<String>()
            for (item in list) {
                answersList.add(item.answer!!)
            }
            answersList.add(0, answersList.size.toString())
            answersList.add(complaintId)
            answersList.add("1")
            answersList.add("1")
            mViewModel.sendAnswers(AnswersModel(answersList))
        }

    }

    override fun onYesClicked(position: Int) {
        resultingAnswer = "yes"
        mViewModel.storeResult(resultingAnswer, position)
        Speech.getInstance().stopListening()
    }

    override fun onNoClicked(position: Int) {
        resultingAnswer = "no"
        mViewModel.storeResult(resultingAnswer, position)
        Speech.getInstance().stopListening()
    }

    override fun onCardClicked(position: Int) {
        Handler().postDelayed({
            Speech.getInstance().say(
                list[position].question,
                object : TextToSpeechCallback {
                    override fun onError() {

                    }

                    override fun onStart() {

                    }

                    override fun onCompleted() {
                        startListening()
                        startListening()
                    }
                })
        }, 150)
    }


    override fun onStartOfSpeech() {

    }

    override fun onSpeechPartialResults(results: MutableList<String>?) {

    }

    override fun onSpeechRmsChanged(value: Float) {

    }

    override fun onSpeechResult(result: String?) {


        if (TextUtils.isEmpty(result)) {
            Speech.getInstance().say("Sorry I Didn't Understand")
            startListening()
        } else {
            Speech.getInstance().say(result)
            textView.text = result
            if (result?.contains("Yes", ignoreCase = true)!!) {

                resultingAnswer = "yes"
                if (count < list.size) count++
                mViewModel.storeResult(resultingAnswer, count - 1)
                Speech.getInstance().stopListening()

            } else if (result.contains("No", ignoreCase = true)) {

                resultingAnswer = "no"
                if (count < list.size) count++
                mViewModel.storeResult(resultingAnswer, count - 1)
                Speech.getInstance().stopListening()


            } else {
                Speech.getInstance().say("Sorry I Didn't Understand")
                mViewModel.getQuestions()

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.diagnosis_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_results -> {
                val answersList = arrayListOf<String>()
                for (item in list) {
                    answersList.add(item.answer!!)
                }
                answersList.add(0, answersList.size.toString())
                answersList.add(complaintId)
                answersList.add("1")
                answersList.add("1")
                mViewModel.sendAnswers(AnswersModel(answersList))
                true
            }
            R.id.action_stop -> {
                Speech.getInstance().stopListening()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    //region permission extras
    private fun onRecordAudioPermissionGranted() {
        try {
            Speech.getInstance().stopTextToSpeech()
            Speech.getInstance().startListening(progressBar, this@DiagnosingActivity)

        } catch (exc: SpeechRecognitionNotAvailable) {
            showSpeechNotSupportedDialog()

        } catch (exc: GoogleVoiceTypingDisabledException) {
            showEnableGoogleVoiceTyping()
        }

    }

    private fun showSpeechNotSupportedDialog() {
        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> SpeechUtil.redirectUserToGoogleAppOnPlayStore(
                    this@DiagnosingActivity
                )

                DialogInterface.BUTTON_NEGATIVE -> {
                }
            }
        }

        val builder = MaterialAlertDialogBuilder(this)
        builder.setMessage("Speech Not Available")
            .setCancelable(false)
            .setPositiveButton("yes", dialogClickListener)
            .setNegativeButton("no", dialogClickListener)
            .show()
    }

    private fun showEnableGoogleVoiceTyping() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setMessage("Enable Google Voice Typing")
            .setCancelable(false)
            .setPositiveButton("yes",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    // do nothing
                })
            .show()
    }
//endregion

    //region permissions
    private fun checkPermission() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                this@DiagnosingActivity,
                Manifest.permission.RECORD_AUDIO
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@DiagnosingActivity,
                    Manifest.permission.RECORD_AUDIO
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this@DiagnosingActivity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    MY_PERMISSIONS_REQUEST_PERMISSION
                )
            }
        } else {
            // Permission has already been granted
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    onRecordAudioPermissionGranted()
                } else {
                    finish()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }

    }
    //endregion


    companion object {
        var resultingAnswer = "no"
        private val MY_PERMISSIONS_REQUEST_PERMISSION: Int = 100
    }

}
