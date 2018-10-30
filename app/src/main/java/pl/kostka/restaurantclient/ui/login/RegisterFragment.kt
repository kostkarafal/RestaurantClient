package pl.kostka.restaurantclient.ui.login

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Patterns
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
import pl.kostka.restaurantclient.service.callback.UserCallback
import pl.kostka.restaurantclient.ui.main.MainFragment


class RegisterFragment: Fragment(){

    private var ok_username: Boolean = false
    private var ok_name: Boolean = false
    private var ok_surname: Boolean = false
    private var ok_phone: Boolean = false
    private var ok_email: Boolean = false
    private var ok_password_length: Boolean = false
    private var ok_repassword: Boolean = false

    private var username: EditText? = null
    private var password: EditText? = null
    private var repassword: EditText? = null
    private var name: EditText? = null
    private var surname: EditText? = null
    private var phone: EditText? = null
    private var email: EditText? = null

    private var usernameProgressBar: ProgressBar? = null
    private var emailProgressBar: ProgressBar? = null

    private var usernameImage: ImageView? = null
    private var passwordImage: ImageView? = null
    private var repasswordImage: ImageView? = null
    private var nameImage: ImageView? = null
    private var surnameImage: ImageView? = null
    private var phoneImage: ImageView? = null
    private var emailImage: ImageView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_register, container, false)

         username = view.findViewById(R.id.editText_register_username)
         password = view.findViewById(R.id.editText_register_password)
         repassword = view.findViewById(R.id.editText_register_repassword)
         name = view.findViewById(R.id.editText_register_name)
         surname = view.findViewById(R.id.editText_register_surname)
         phone = view.findViewById(R.id.editText_register_phone)
         email = view.findViewById(R.id.editText_register_email)

        val buttonRegister = view.findViewById<Button>(R.id.button_register)


        usernameProgressBar = view.findViewById(R.id.progressBar_register_username)
        emailProgressBar = view.findViewById(R.id.progressBar_register_email)

         usernameImage = view.findViewById(R.id.imageView_register_username)
         passwordImage = view.findViewById(R.id.imageView_register_password)
         repasswordImage = view.findViewById(R.id.imageView_register_repassword)
         nameImage = view.findViewById(R.id.imageView_register_name)
         surnameImage = view.findViewById(R.id.imageView_register_surname)
         phoneImage = view.findViewById(R.id.imageView_register_phone)
         emailImage = view.findViewById(R.id.imageView_register_email)

        username?.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                validateUsername()
            }
        }

        password?.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
               validatePassword()
            }
        }

        repassword?.setOnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
               validateRepassword()
            }
        }

        name?.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
               validateName()
            }
        }

        surname?.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                validateSurname()
            }
        }

        phone?.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
               validatePhone()
            }
        }


        email?.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                validateEmail()
            }
        }


        buttonRegister .setOnClickListener {

            validateAll()
            if(ok_username && ok_email && ok_name && ok_password_length && ok_repassword && ok_phone && ok_surname) {
               val user = User(username = username!!.text.toString(),
                                password = password!!.text.toString(),
                                name = name!!.text.toString(),
                                surname = surname!!.text.toString(),
                                email = email!!.text.toString(),
                                phoneNumber = phone!!.text.toString())
                UserService.registerUser(user, object: UserCallback {
                    override fun onResponse(user: User) {
                        activity?.runOnUiThread {
                            Snackbar.make(view, "Utworzono nowe konto, możesz się zalogować", Snackbar.LENGTH_LONG).show()
                            fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, MainFragment())?.commit()
                        }
                    }

                    override fun onFailure(errMessage: String) {
                        activity?.runOnUiThread {
                            Toast.makeText(this@RegisterFragment.context, errMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                })
            } else {
                Toast.makeText(this@RegisterFragment.context, "Popraw wprowadzone dane", Toast.LENGTH_LONG).show()

            }
        }




        return view
    }

    fun validateAll(){
        validateEmail()
        validateName()
        validatePassword()
        validatePhone()
        validateRepassword()
        validateSurname()
        validateUsername()
    }

    fun validateUsername(){
        if (username!!.text.isNotEmpty()) {
            usernameProgressBar!!.visibility = View.VISIBLE
            usernameImage!!.visibility = View.INVISIBLE

            UserService.checkIfUsernameIsFree(username!!.text.toString(), object: BooleanCallback {
                override fun onResponse(response: Boolean) {
                    if (response) {
                        activity?.runOnUiThread {
                            usernameProgressBar!!.visibility = View.INVISIBLE
                            usernameImage!!.visibility = View.VISIBLE
                            ok_username = true
                        }
                    } else {
                        activity?.runOnUiThread {
                            usernameProgressBar!!.visibility = View.INVISIBLE
                            username!!.error = "Nazwa uzytkownika jest już zajęta"
                            ok_username = false
                        }
                    }
                }

                override fun onFailure(errMessage: String) {
                    activity?.runOnUiThread {
                        Toast.makeText(this@RegisterFragment.context, errMessage, Toast.LENGTH_LONG).show()
                    }
                }
            })
        } else {
            username!!.error = "Pole nie może być puste"
        }
    }

    fun validateEmail(){
        if (email!!.text.toString().isValidEmail()){
            emailProgressBar!!.visibility = View.VISIBLE
            UserService.checkIfEmailIsFree(email!!.text.toString(), object : BooleanCallback {
                override fun onResponse(response: Boolean) {
                    if(response){
                        activity?.runOnUiThread {
                            emailProgressBar!!.visibility = View.INVISIBLE
                            emailImage!!.visibility = View.VISIBLE
                            ok_email = true
                        }
                    } else {
                        activity?.runOnUiThread {
                            emailProgressBar!!.visibility = View.INVISIBLE
                            emailImage!!.visibility = View.INVISIBLE
                            email!!.error = "Email jest już zajęty"
                            ok_email = false
                        }
                    }
                }

                override fun onFailure(errMessage: String) {
                    activity?.runOnUiThread {
                        Toast.makeText(this@RegisterFragment.context, errMessage, Toast.LENGTH_LONG).show()
                    }
                }
            })

        } else {
            ok_email = false
            emailImage!!.visibility = View.INVISIBLE
            email!!.error = "Wpisz prawidłowy adres email"
        }
    }

    fun validatePassword(){
        if(password!!.text.toString().length in 8..24) {
            passwordImage!!.visibility = View.VISIBLE
            ok_password_length = true
        } else {
            ok_password_length = false
            passwordImage!!.visibility = View.INVISIBLE
            password!!.error = "Hasło musi mieć conajmniej 8 znaków!"
        }

        if(repassword!!.text.toString().isNotEmpty()) {
            if (password!!.text.toString().equals(repassword!!.text.toString())) {
                repasswordImage!!.visibility = View.VISIBLE
                ok_repassword = true
            } else {
                repasswordImage!!.visibility = View.INVISIBLE
                ok_repassword = false
                repassword!!.error = "Hasła nie są takie same!"
            }
        }
    }

    fun validateRepassword(){
        if (password!!.text.toString().equals(repassword!!.text.toString())) {
            repasswordImage!!.visibility = View.VISIBLE
            ok_repassword = true
        } else {
            repasswordImage!!.visibility = View.INVISIBLE
            ok_repassword = false
            repassword!!.error = "Hasła nie są takie same!"
        }
    }

    fun validateName(){
        if (name!!.text.toString().isNotEmpty()){
            nameImage!!.visibility = View.VISIBLE
            ok_name = true
        } else {
            ok_name = false
            nameImage!!.visibility = View.INVISIBLE
            name!!.error = "Pole nie moze być puste"
        }
    }

    fun validateSurname() {
        if (surname!!.text.toString().isNotEmpty()){
            surnameImage!!.visibility = View.VISIBLE
            ok_surname = true

        } else {
            ok_surname = false
            surnameImage!!.visibility = View.INVISIBLE
            surname!!.error = "Pole nie moze być puste"
        }
    }

    fun validatePhone() {
        if (phone!!.text.toString().isNotEmpty() && phone!!.text.length in 9..11){
            phoneImage!!.visibility = View.VISIBLE
            ok_phone = true
        } else {
            ok_phone = false
            phoneImage!!.visibility = View.INVISIBLE
            phone!!.error = "Podaj właściwy numer telefonu"
        }
    }

    fun String.isValidEmail(): Boolean
            = this.isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()


    override fun onResume() {
        super.onResume()
    }


}