package pl.kostka.restaurantclient.ui.myaccount

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_address.*

import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Address
import pl.kostka.restaurantclient.service.AddressService
import pl.kostka.restaurantclient.service.JwtService
import pl.kostka.restaurantclient.service.callback.AddressListCallback
import pl.kostka.restaurantclient.service.listener.IsLoggdInListener
import pl.kostka.restaurantclient.service.listener.OnChangeListener
import pl.kostka.restaurantclient.ui.orders.OrdersFragment

import java.util.*
import kotlin.properties.Delegates

class AddressActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    var recyclerView2: RecyclerView? = null
    var selectedAddressId = -1L

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
        AddressService.getAddresses(object : AddressListCallback {
            override fun onResponse(address: List<Address> ) {
                runOnUiThread {
                    val result = address.find { address -> address.id!!.equals(selectedAddressId)}
                    recyclerView!!.adapter = AddressAdapter(address, this@AddressActivity, address.indexOf(result), true )
                }
            }

            override fun onFailure(errMessage: String) {
                runOnUiThread {
                    Toast.makeText(this@AddressActivity, errMessage, Toast.LENGTH_LONG).show()
                }
            }
        })
        AddressService.getUnsupportedAddresses(object : AddressListCallback {
            override fun onResponse(address: List<Address> ) {
                runOnUiThread {
                    recyclerView2!!.adapter = AddressAdapter(address, this@AddressActivity, -1, false)
                }
            }

            override fun onFailure(errMessage: String) {
                runOnUiThread {
                    Toast.makeText(this@AddressActivity, errMessage, Toast.LENGTH_LONG).show()
                }
            }
        })
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
        setResult(Activity.RESULT_OK)
        finish()
    }
}