package pl.kostka.restaurantclient.ui.login

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.content_login.*
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.service.JwtService
import pl.kostka.restaurantclient.service.callback.LoginResponseCallback


class LoginFragment: Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_login, container, false)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar_login)
        val buttonLogin = view.findViewById<Button>(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            JwtService.login(editTextLogin.text.toString(),editTextPassword.text.toString(), object: LoginResponseCallback {
                override fun onResponse() {
                    activity?.runOnUiThread {
                        Toast.makeText(this@LoginFragment.context, "Zalogowano", Toast.LENGTH_LONG).show()
                    }
                    progressBar.visibility = View.INVISIBLE
                }

                override fun onFailure(errMessage: String?) {
                    if (errMessage == null) {
                        activity?.runOnUiThread {
                            Toast.makeText(this@LoginFragment.context, "Błąd połączenia z serwerem", Toast.LENGTH_LONG).show()
                        }
                        progressBar.visibility = View.INVISIBLE
                    } else {
                        activity?.runOnUiThread {
                            Toast.makeText(this@LoginFragment.context, "Błedny użytkownik lub hasło", Toast.LENGTH_LONG).show()
                        }
                        progressBar.visibility = View.INVISIBLE
                    }
                }
            })
        }
        return view
    }


    override fun onResume() {
        super.onResume()
    }


}