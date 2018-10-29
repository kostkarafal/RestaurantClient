package pl.kostka.restaurantclient.ui.login

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.content_login.*
import kotlinx.android.synthetic.main.content_register.*
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.User
import pl.kostka.restaurantclient.service.JwtService
import pl.kostka.restaurantclient.service.UserService
import pl.kostka.restaurantclient.service.callback.BooleanCallback
import pl.kostka.restaurantclient.service.callback.LoginResponseCallback
import pl.kostka.restaurantclient.ui.main.MainFragment


class RegisterFragment: Fragment(){
    var ok_username: String? = null
    var ok_password: String? = null
    var ok_name: String? = null
    var ok_surname: String? = null
    var ok_phone: String? = null
    var ok_email: String? = null
    var ok_password_length: Boolean? = null
    var ok_repassword: Boolean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_register, container, false)

        val username = view.findViewById<EditText>(R.id.editText_register_username)
        val password = view.findViewById<EditText>(R.id.editText_register_password)
        val repassword = view.findViewById<EditText>(R.id.editText_register_repassword)
        val name = view.findViewById<EditText>(R.id.editText_register_name)
        val surname = view.findViewById<EditText>(R.id.editText_register_surname)
        val phone = view.findViewById<EditText>(R.id.editText_register_phone)
        val email = view.findViewById<EditText>(R.id.editText_register_email)

        val okIcon = resources.getIdentifier("@drawable/ic_done_black_24dp", "drawable", view.context.packageName)
        val wrongIcon = resources.getIdentifier("@drawable/ic_cancel_black_24dp", "drawable", view.context.packageName)

        val usernameProgresBar = view.findViewById<ProgressBar>(R.id.progressBar_register_username)
        val usernameImage = view.findViewById<ImageView>(R.id.imageView_register_username_ok)

        username.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                usernameProgresBar.visibility = View.VISIBLE
                usernameImage.visibility = View.INVISIBLE
                UserService.checkIfUsernameIsFree(username.text.toString(), object: BooleanCallback {
                    override fun onResponse(response: Boolean) {
                       if (response) {
                           activity?.runOnUiThread {
                               usernameProgresBar.visibility = View.INVISIBLE
                               usernameImage.setImageResource(okIcon)
                               usernameImage.visibility = View.VISIBLE
                           }
                       } else {
                           activity?.runOnUiThread {
                               usernameProgresBar.visibility = View.INVISIBLE
                               usernameImage.setImageResource(wrongIcon)
                               usernameImage.visibility = View.VISIBLE
                           }
                       }
                    }

                    override fun onFailure(errMessage: String) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
            }
        }

        password.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                ok_password_length = password.text.toString().length in 8..24
            } else {
                ok_password_length = null
            }
        }

        repassword.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
               ok_repassword = password.text.toString().equals(repassword.text.toString())
            } else
                ok_repassword = null
        }

        name.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                if (name.text.toString().isNotEmpty()){
                    ok_name = name.text.toString()
                } else {
                    ok_name = null
                }
            }
        }

        surname.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                if(surname.text.toString().isNotEmpty()) {
                    ok_surname = surname.text.toString()
                } else
                    ok_surname = null
            }
        }

      /*  button_register.setOnClickListener {


            val user = User(
                    login = editText_register_username.text.toString(),
                    password = editText_register_password.text.toString(),
                    name = editText_register_name.text.toString()


            )
        }*/




        return view
    }



   /* private fun checkUsername(): String{

    }



    private fun checkEmail():String {

    }

*/
    override fun onResume() {
        super.onResume()
    }


}