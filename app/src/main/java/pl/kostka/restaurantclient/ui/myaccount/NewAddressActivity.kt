package pl.kostka.restaurantclient.ui.myaccount

import android.app.Activity
import android.location.Geocoder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_new_address.*

import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Address
import pl.kostka.restaurantclient.service.AddressService
import pl.kostka.restaurantclient.service.callback.AddressCallback
import java.lang.Exception

class NewAddressActivity : AppCompatActivity() {

    var mMapView: MapView? = null
    var googleMap: GoogleMap? = null
    var geocoder: Geocoder? = null
    var radioButton: RadioButton? = null
    var mapAddress: android.location.Address? = null

    var editMode: Boolean = false
    var id: Long? = null

    var title:EditText? = null
    var city:EditText? = null
    var street:EditText? = null
    var building:EditText? = null
    var apartment:EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_address)

        title = findViewById(R.id.editText_address_title)
        city = findViewById(R.id.editText_address_city)
        street = findViewById(R.id.editText_address_street)
        building = findViewById(R.id.editText_address_building)
        apartment = findViewById(R.id.editText_address_apartment)

        radioButton = findViewById(R.id.radioButton_new_address)

        mMapView = findViewById<MapView?>(R.id.mapView_new_address)
        mMapView!!.onCreate(savedInstanceState)

        mMapView!!.onResume()

        try {
            MapsInitializer.initialize(applicationContext)
        } catch (e: Exception){
            e.printStackTrace()
        }
        geocoder = Geocoder(this)


        var startLocation = LatLng(52.237049, 21.017532)
        var mapZoom = 5f

        val result = intent.getSerializableExtra("Address")

        if (result !== null) {
            val address = result as Address
            editMode = true
            id = address.id
            title!!.setText(address.title)
            city!!.setText(address.city)
            street!!.setText(address.street)
            building!!.setText(address.buildingNumber)
            apartment!!.setText(address.apartmentNumber)
            startLocation = LatLng(address.latitude, address.longitude)
            mapZoom = 14f
            button_new_address_add.text = getText(R.string.edit_address)
        }


        mMapView!!.getMapAsync {
            googleMap = it
            googleMap!!.addMarker(MarkerOptions().position(startLocation).title("Marker Title"))
            val cameraPosition = CameraPosition.Builder().target(startLocation).zoom(mapZoom).build()
            googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }



        city!!.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && editText_address_city.text!!.isNotEmpty()) {
              updateMap()
            }
        }

       street!!.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && editText_address_street.text!!.isNotEmpty()) {
                updateMap()
            }
        }

        building!!.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && editText_address_building.text!!.isNotEmpty()) {
                updateMap()
            }
        }

        button_new_address_add.setOnClickListener {
            if(validateAll()) {
                var address = Address(id = id, city = mapAddress!!.locality, street = mapAddress!!.thoroughfare, buildingNumber = mapAddress!!.subThoroughfare,
                        title = title!!.text.toString(), longitude = mapAddress!!.longitude, latitude = mapAddress!!.latitude)
                if(apartment!!.text.isNotEmpty()) {
                    address.apartmentNumber = apartment!!.text.toString()
                }
                if(editMode) {
                    AddressService.updateAddress(address, object : AddressCallback {
                        override fun onResponse(address: Address) {
                            runOnUiThread {
                                this@NewAddressActivity.setResult(Activity.RESULT_OK)
                                this@NewAddressActivity.finish()
                            }
                        }

                        override fun onFailure(errMessage: String) {
                            runOnUiThread {
                                Toast.makeText(this@NewAddressActivity, errMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                } else {
                    AddressService.addAddress(address, object : AddressCallback {
                        override fun onResponse(address: Address) {
                            runOnUiThread {
                                this@NewAddressActivity.setResult(Activity.RESULT_OK)
                                this@NewAddressActivity.finish()
                            }
                        }

                        override fun onFailure(errMessage: String) {
                            runOnUiThread {
                                Toast.makeText(this@NewAddressActivity, errMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                }
            }
        }

    }

    fun checkIfAllRequiredIsFilled(): Boolean {
        return city!!.text.isNotEmpty() && street!!.text.isNotEmpty() && building!!.text.isNotEmpty()
    }

    fun validateAll(): Boolean {
        var result = true
        if(title!!.text.isEmpty()){
            title!!.setError("Pole nie może być puste!")
            result = false
        }
        if(city!!.text.isEmpty()){
            city!!.setError("Pole nie może być puste!")
            result = false
        }
        if(street!!.text.isEmpty()){
            street!!.setError("Pole nie może być puste!")
            result = false
        }
        if(building!!.text.isEmpty()){
            building!!.setError("Pole nie może być puste!")
            result = false
        }
        if(mapAddress == null || radioButton!!.isChecked == false)
        {
            result = false
            runOnUiThread {
                Toast.makeText(this@NewAddressActivity, "Nie wybrano adresu!", Toast.LENGTH_LONG).show()
            }
        }
        return result
    }


    fun updateMap(){
        if(checkIfAllRequiredIsFilled()){
            val result = geocoder!!.getFromLocationName("${editText_address_city.text!!} ${editText_address_street.text!!} ${editText_address_building.text!!}", 1)
            googleMap!!.clear()
            if(result.size > 0) {
                mMapView!!.getMapAsync {
                    googleMap = it

                    radioButton!!.isChecked = false
                    radioButton!!.visibility = View.VISIBLE
                    mapAddress = result.get(0)

                    radioButton!!.text = "${mapAddress!!.locality} ul.${mapAddress!!.thoroughfare} ${mapAddress!!.subThoroughfare}, ${mapAddress!!.postalCode}"
                    val latlngResult = LatLng(mapAddress!!.latitude,mapAddress!!.longitude)
                    googleMap!!.addMarker(MarkerOptions().position(latlngResult).title("Marker Title"))
                    val cameraPosition = CameraPosition.Builder().target(latlngResult).zoom(12f).build()
                    googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                }
            } else {
                radioButton!!.isChecked = false
                radioButton!!.visibility = View.INVISIBLE
                mapAddress = null
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView!!.onLowMemory()
    }
}