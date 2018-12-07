package pl.kostka.restaurantclient.ui.myaccount

import android.app.Activity
import android.content.Intent
import android.net.sip.SipAudioCall
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_address.*

import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Address
import pl.kostka.restaurantclient.model.ErrorResponse
import pl.kostka.restaurantclient.service.AddressService
import pl.kostka.restaurantclient.service.JwtService
import pl.kostka.restaurantclient.service.callback.AddressArrayCallback
import pl.kostka.restaurantclient.service.listener.IsLoggdInListener
import pl.kostka.restaurantclient.service.listener.OnChangeListener
import kotlin.properties.Delegates

class AddressActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    var recyclerView2: RecyclerView? = null
    var selectedAddressId = -1L

    var addressListSize: Int = 0
    var unsupportedAddressListSize: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_ok_area_adresses)
        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext)

        recyclerView2 = findViewById<RecyclerView>(R.id.recycler_wrong_area_adresses)
        recyclerView2!!.layoutManager = LinearLayoutManager(applicationContext)

        val result = intent.getSerializableExtra("selectedAddressId")

        if (result !== null) {
             selectedAddressId = result as Long
        }
        refreshAddressList()

        button_new_address_add.setOnClickListener {
            val intent = Intent(it.context, NewAddressActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }



    private fun refreshAddressList() {
        AddressService.getAddresses(object : AddressArrayCallback {
            override fun onResponse(response: Array<Address> ) {
                runOnUiThread {
                    addressListSize = response.size
                    checkIfListsAreEmpty()
                    val result = response.find { address -> address.id!!.equals(selectedAddressId)}
                    recyclerView!!.adapter = AddressAdapter(response.toList(), this@AddressActivity, response.indexOf(result), true, object : OnChangeListener{
                        override fun onChange(size: Int) {
                            addressListSize = size
                            checkIfListsAreEmpty()
                        }
                    })
                }
            }

            override fun onFailure(error: ErrorResponse) {
                runOnUiThread {
                    Toast.makeText(this@AddressActivity, error.getMsg(), Toast.LENGTH_LONG).show()
                }
            }
        })
        AddressService.getUnsupportedAddresses(object : AddressArrayCallback {
            override fun onResponse(response: Array<Address> ) {
                runOnUiThread {
                    unsupportedAddressListSize = response.size
                    checkUnsupportedList()
                    recyclerView2!!.adapter = AddressAdapter(response.toList(), this@AddressActivity, -1, false, object : OnChangeListener{
                        override fun onChange(size: Int) {
                            unsupportedAddressListSize = size
                            checkUnsupportedList()
                        }
                    })
                }
            }

            override fun onFailure(error: ErrorResponse) {
                runOnUiThread {
                    Toast.makeText(this@AddressActivity, error.getMsg(), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun checkIfListsAreEmpty(){
        if(addressListSize == 0 && unsupportedAddressListSize == 0) {
            textView_address_empty_list.visibility = View.VISIBLE
        } else
        {
            textView_address_empty_list.visibility = View.INVISIBLE
        }
    }

    private fun checkUnsupportedList(){
        checkIfListsAreEmpty()
        if(unsupportedAddressListSize > 0){
            textView_adress_to_far.visibility = View.VISIBLE
        } else {
            textView_adress_to_far.visibility = View.INVISIBLE

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            Snackbar.make(addres_view, getString(R.string.address_added), Snackbar.LENGTH_LONG).show()
            refreshAddressList()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId().equals(android.R.id.home)) {
            onBackPressed()
            return true
        }
        return false
    }

     override fun onBackPressed() {
         if(selectedAddressId == -1L){
             setResult(Activity.RESULT_CANCELED)
         } else {
             setResult(Activity.RESULT_OK)
         }
        finish()
    }
}