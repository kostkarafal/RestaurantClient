package pl.kostka.restaurantclient.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import kotlinx.android.synthetic.main.content_login.*
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.ErrorResponse
import pl.kostka.restaurantclient.model.TokenResponse
import pl.kostka.restaurantclient.service.JwtService
import pl.kostka.restaurantclient.service.callback.LoginResponseCallback
import pl.kostka.restaurantclient.service.callback.TokenResponeCallback
import pl.kostka.restaurantclient.ui.main.MainFragment
import java.util.*


class LoginFragment: Fragment(){

    private val EMAIL = "email"
    private val callbackManager = CallbackManager.Factory.create()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        println(FacebookSdk.getApplicationSignature(this@LoginFragment.context))
        val view = inflater.inflate(R.layout.content_login, container, false)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar_login)
        val buttonLogin = view.findViewById<Button>(R.id.buttonLogin)
        val buttonLoginFb = view.findViewById<LoginButton>(R.id.button_login_fb)
        buttonLoginFb.setReadPermissions(Arrays.asList(EMAIL))
        buttonLoginFb.fragment = this





        buttonLoginFb.registerCallback(callbackManager, object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                progressBar.visibility = View.VISIBLE
                 JwtService.loginFacebook(result!!.accessToken.token, object: TokenResponeCallback{
                        override fun onResponse(response: TokenResponse) {
                            JwtService.changeToLoggedIn(response)
                            activity?.runOnUiThread {
                                fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, MainFragment())?.commit()
                                LoginManager.getInstance().logOut()
                            }
                            progressBar.visibility = View.INVISIBLE
                        }

                        override fun onFailure(error: ErrorResponse) {
                            activity?.runOnUiThread {
                                println(error)
                                LoginManager.getInstance().logOut()
                                progressBar.visibility = View.INVISIBLE
                                Toast.makeText(this@LoginFragment.context, error.message, Toast.LENGTH_LONG).show()
                            }                        }
                    })
            }

            override fun onCancel() {
                activity?.runOnUiThread {
                    Toast.makeText(this@LoginFragment.context, "Logging with facebook was cancel", Toast.LENGTH_LONG).show()
                }
            }

            override fun onError(error: FacebookException?) {
                activity?.runOnUiThread {
                    println(error.toString())
                    Toast.makeText(this@LoginFragment.context, error.toString(), Toast.LENGTH_LONG).show()
                }
            }
        } )


        buttonLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            JwtService.login(editTextLogin.text.toString(),editTextPassword.text.toString(), object: LoginResponseCallback {
                override fun onResponse() {
                    activity?.runOnUiThread {
                        fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, MainFragment())?.commit()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


}