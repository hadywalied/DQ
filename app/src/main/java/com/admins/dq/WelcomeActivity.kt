package com.admins.dq

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.admins.dq.loginui.AuthActivity
import com.admins.dq.utils.ConnectivityReceiver
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class WelcomeActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        snackbar = Snackbar.make(
            findViewById(R.id.rootLayout),
            "You are offline",
            Snackbar.LENGTH_LONG
        )
        registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

//        checkForConnection()


    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showNetworkMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    private fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            //Assume "rootLayout" as the root layout of every activity.
            snackbar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackbar.setAction("exit") {
                moveTaskToBack(true)
                finish()
            }
            snackbar?.show()
        } else {
            snackbar?.dismiss()
            Handler().postDelayed({
                startActivity(Intent(this, AuthActivity::class.java))
            }, 1500)

        }
    }

}
