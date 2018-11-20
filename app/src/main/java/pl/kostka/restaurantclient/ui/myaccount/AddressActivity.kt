package pl.kostka.restaurantclient.ui.myaccount

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_address.*

import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Address

import java.util.*

class AddressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_ok_area_adresses)
        val recyclerView2 = findViewById<RecyclerView>(R.id.recycler_wrong_area_adresses)

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView2.layoutManager = LinearLayoutManager(applicationContext)


        val addresses = Arrays.asList(Address(city = "Mikołów", street = "Spyry", buildingNumber = "24b", title = "testowy adres", apartmentNumber = "5"), Address(city = "Mikołów", apartmentNumber = "5", street = "Spyry", buildingNumber = "24b", title = "testowy adres"))

        recyclerView.adapter = AddressAdapter(addresses)
        recyclerView2.adapter = AddressAdapter(addresses)

        button_address_add.setOnClickListener {
            val intent = Intent(it.context, NewAddressActivity::class.java)

            startActivityForResult(intent, 1)
        }

    }
}