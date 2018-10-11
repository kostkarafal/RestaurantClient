package pl.kostka.restaurantclient.ui.login

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.content_login.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.service.UserService
import java.io.IOException


class LoginFragment: Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_login, container, false)

        val buttonLogin = view.findViewById<Button>(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
             UserService.login(editTextLogin.text.toString(), editTextPassword.text.toString())
                     .enqueue(object : Callback {
                         override fun onResponse(call: Call?, response: Response?) {
                             val body = response?.body()?.string()
                             if(body.equals("true")) {
                                 Snackbar.make(view, "Zalogowano", Snackbar.LENGTH_LONG)
                                         .setAction("Action", null).show()
                             } else {
                                activity?.runOnUiThread {
                                    Toast.makeText(this@LoginFragment.context, "testtttt", Toast.LENGTH_LONG).show()
                                }
                                 Snackbar.make(view, "Błedny użytkownik lub hasło", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show()
                             }
                         }

                         override fun onFailure(call: Call?, e: IOException?) {
                             Snackbar.make(view, "Serwer nie odpowiada", Snackbar.LENGTH_LONG)
                                     .setAction("Action", null).show()
                             println("Failed to execute")
                         }
                     })
          // Toast.makeText(container?.context, toastMessage, Toast.LENGTH_LONG).show()
//               Snackbar.make(container?.rootView!!, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//            println("test33333333333333333333")
        }

        println("testtttt2222222222222222222222")
        return view
    }


    override fun onResume() {
        super.onResume()
    }


}