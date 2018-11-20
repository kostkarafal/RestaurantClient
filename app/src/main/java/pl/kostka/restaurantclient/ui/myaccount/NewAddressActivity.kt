package pl.kostka.restaurantclient.ui.myaccount

import android.location.Geocoder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import java.lang.Exception

import java.util.*
import kotlin.collections.ArrayList

class NewAddressActivity : AppCompatActivity() {

    var mMapView: MapView? = null
    var googleMap: GoogleMap? = null
    var geocoder: Geocoder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_address)


        mMapView = findViewById<MapView?>(R.id.mapView_new_address)
        mMapView!!.onCreate(savedInstanceState)

        mMapView!!.onResume()

        try {
            MapsInitializer.initialize(applicationContext)
        } catch (e: Exception){
            e.printStackTrace()
        }
        geocoder = Geocoder(this)

        mMapView!!.getMapAsync {
            googleMap = it

            val warsaw = LatLng(52.237049, 21.017532)
            googleMap!!.addMarker(MarkerOptions().position(warsaw).title("Marker Title"))

            val cameraPosition = CameraPosition.Builder().target(warsaw).zoom(7f).build()

            googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        }



        editText_address_city.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && editText_address_city.text!!.isNotEmpty()) {
              updateMap()
            }
        }

        editText_address_street.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && editText_address_city.text!!.isNotEmpty()) {
                updateMap()
            }
        }

        editText_address_building.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && editText_address_city.text!!.isNotEmpty()) {
                updateMap()
            }
        }


    }

    fun updateMap(){
        if(editText_address_city.text!!.isNotEmpty()){
            val result = geocoder!!.getFromLocationName("${editText_address_city.text!!} ${editText_address_street.text!!} ${editText_address_building.text!!}", 1)
            googleMap!!.clear()
            if(result.size > 0) {
                mMapView!!.getMapAsync {
                    googleMap = it

                    val latlngResult = LatLng(result.get(0).latitude, result.get(0).longitude)
                    googleMap!!.addMarker(MarkerOptions().position(latlngResult).title("Marker Title"))
                    val cameraPosition = CameraPosition.Builder().target(latlngResult).zoom(12f).build()
                    googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                }
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