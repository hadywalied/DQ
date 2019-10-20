package com.admins.dq.loginui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.admins.dq.R
import com.admins.dq.drawerui.PatientsActivity
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginViewModel =
            ViewModelProviders.of(this).get(LoginViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_login, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_login.setOnClickListener {

            /*if (TextUtils.equals(til_username.text.toString(), "admin") && TextUtils.equals(
                    til_password.text.toString(),
                    "admin"
                )
            )*/
            startActivity(Intent(activity, PatientsActivity::class.java))
            /*else
                Toast.makeText(activity, "Wrong Credentials", Toast.LENGTH_SHORT).show()
*/
        }


    }
}