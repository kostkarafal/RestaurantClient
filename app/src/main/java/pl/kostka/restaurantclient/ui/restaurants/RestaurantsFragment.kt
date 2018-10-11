package pl.kostka.restaurantclient.ui.restaurants

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.service.ProductService
import java.io.IOException
import java.lang.Exception

class RestaurantsFragment: Fragment(){

    var mMapView: MapView? = null
    var googleMap: GoogleMap? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_restaurants, container, false)

        mMapView = view.findViewById(R.id.mapView) as MapView?
        mMapView!!.onCreate(savedInstanceState)

        mMapView!!.onResume()

        try {
            MapsInitializer.initialize(activity?.applicationContext)
        } catch (e: Exception){
            e.printStackTrace()
        }

        mMapView!!.getMapAsync {
            googleMap = it

            val sydney = LatLng(-34.0, 151.0)
            googleMap!!.addMarker(MarkerOptions().position(sydney).title("Marker Title"))

            val cameraPosition = CameraPosition.Builder().target(sydney).zoom(12f).build()

            googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        }

        return view
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