package pl.kostka.restaurantclient.ui.restaurants

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

class RestaurantActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        supportFragmentManager.addOnBackStackChangedListener {
            println("test")
        }
    }

}