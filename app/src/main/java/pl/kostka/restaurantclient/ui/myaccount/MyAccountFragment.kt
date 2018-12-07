package pl.kostka.restaurantclient.ui.myaccount

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.content_myaccount.*
import kotlinx.android.synthetic.main.content_myaccount.view.*
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.ErrorResponse
import pl.kostka.restaurantclient.model.User
import pl.kostka.restaurantclient.service.JwtService
import pl.kostka.restaurantclient.service.UserService
import pl.kostka.restaurantclient.service.callback.BooleanCallback
import pl.kostka.restaurantclient.service.callback.UserCallback
import pl.kostka.restaurantclient.ui.basket.BasketActivity


class MyAccountFragment: Fragment(){

    var user:User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_myaccount, container, false)

        refreshView()

        view.button_myaccount_name.setOnClickListener {
           handleChange(R.string.name_c, user!!.name)
        }

        view.button_myaccount_surname.setOnClickListener {
            handleChange(R.string.surname_c, user!!.surname)
        }

        view.button_myaccount_email.setOnClickListener {
            handleChange(R.string.email_c, user!!.email)
        }

        view.button_myaccount_phone.setOnClickListener {
            handleChange(R.string.phone_number_c, user!!.phoneNumber)
        }

        view.button_myaccount_adress.setOnClickListener {
            val intent = Intent(it.context, AddressActivity::class.java)
            intent.putExtra("selectedAddressId", user!!.selectedAddress?.id)
            startActivityForResult(intent, 1)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
           refreshView()
        }
    }

    private fun refreshView() {
        UserService.getUser(object : UserCallback{
            override fun onResponse(response: User) {
                activity?.runOnUiThread {
                    textView_myaccount_name.text = response.name
                    textView_myaccount_surname.text = response.surname
                    textView_myaccount_email.text = response.email
                    textView_myaccount_phone.text = response.phoneNumber

                    if(response.selectedAddress != null){
                        var address = "${response.selectedAddress!!.street} ${response.selectedAddress!!.buildingNumber}"
                        if(response.selectedAddress!!.apartmentNumber != null)
                            address += "/${response.selectedAddress!!.apartmentNumber!!}"
                        address += ", ${response.selectedAddress!!.city}"

                        textView_myaccount_adress.text = address
                    }

                    this@MyAccountFragment.user = response
                }
            }

            override fun onFailure(error: ErrorResponse) {
                activity?.runOnUiThread {
                    Toast.makeText(this@MyAccountFragment.context, error.getMsg(), Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    private fun handleChange(title: Int, value: String) {
        activity?.runOnUiThread {
            val mBuilder = AlertDialog.Builder(activity)
            val mView = activity!!.layoutInflater.inflate(R.layout.dialog_edit_user, null)

            val textView = mView.findViewById<TextView>(R.id.textView_dialog_edit_myaccount)
            val editText = mView.findViewById<EditText>(R.id.editText_dialog_edit_myaccount)
            val cancelButton = mView.findViewById<Button>(R.id.button_dialog_myaccount_cancel)
            val okButton = mView.findViewById<Button>(R.id.button_dialog_myaccount_ok)
            val progressBar = mView.findViewById<ProgressBar>(R.id.progressBar_dialog_myaccount)

            textView.text = getText(title)
            editText.setText(value)
            mBuilder.setView(mView)
            val dialog = mBuilder.create()
            dialog.show()

            okButton.setOnClickListener {
                progressBar.visibility = View.VISIBLE
                when (title) {
                    R.string.name_c -> when(validateName(editText)) {
                        true -> editUser(progressBar, dialog)
                        false -> progressBar.visibility = View.INVISIBLE
                    }
                    R.string.surname_c -> when(validateSurname(editText)) {
                        true -> editUser(progressBar, dialog)
                        false -> progressBar.visibility = View.INVISIBLE
                    }
                    R.string.email_c -> validateEmail(editText, progressBar, dialog)

                    R.string.phone_number_c -> when(validatePhone(editText)) {
                        true -> editUser(progressBar,dialog)
                        false -> progressBar.visibility = View.INVISIBLE
                    }
                }
            }

            cancelButton.setOnClickListener {
                dialog.hide()
            }
        }
    }

    private fun editUser(progressBar: ProgressBar, dialog: AlertDialog) {
        UserService.editUser(user!!, object : UserCallback{
            override fun onResponse(response: User) {
                activity?.runOnUiThread {
                refreshView()
                progressBar.visibility = View.INVISIBLE
                dialog.hide()
                }

            }

            override fun onFailure(error: ErrorResponse) {
                activity?.runOnUiThread {
                    progressBar.visibility = View.INVISIBLE
                }
            }
        })
    }
    private fun validateName(name: EditText): Boolean{
        if (name.text.toString().isNotEmpty()){
           user?.name = name.text.toString()
            return true
        } else {
            name.error = "Pole nie moze być puste"
            return false
        }
    }

    private fun validateSurname(surname: EditText): Boolean {
        if (surname.text.toString().isNotEmpty()){
            user?.surname = surname.text.toString()
            return true
        } else {
            surname.error = "Pole nie moze być puste"
            return false
        }
    }

    private fun validatePhone(phone: EditText): Boolean {
        if (phone.text.toString().isNotEmpty() && phone.text.length in 9..11){
            user?.phoneNumber = phone.text.toString()
            return true
        } else {
            phone.error = "Podaj właściwy numer telefonu"
            return false
        }
    }

    private fun validateEmail(email: EditText, progressBar: ProgressBar, dialog: AlertDialog){
        if (email.text.toString().isValidEmail()){
            UserService.checkIfEmailIsFree(email.text.toString(), object : BooleanCallback {
                override fun onResponse(response: Boolean) {
                    if(response){
                        activity?.runOnUiThread {
                            this@MyAccountFragment.user?.email = email.text.toString()
                            editUser(progressBar, dialog)
                        }
                    } else {
                        activity?.runOnUiThread {
                            email.error = "Email jest już zajęty"
                            progressBar.visibility = View.INVISIBLE
                        }
                    }
                }

                override fun onFailure(error: ErrorResponse) {
                    activity?.runOnUiThread {
                        Toast.makeText(this@MyAccountFragment.context, error.getMsg(), Toast.LENGTH_LONG).show()
                    }
                }
            })

        } else {

            email.error = "Wpisz prawidłowy adres email"
        }
    }

    fun String.isValidEmail(): Boolean
            = this.isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()

}