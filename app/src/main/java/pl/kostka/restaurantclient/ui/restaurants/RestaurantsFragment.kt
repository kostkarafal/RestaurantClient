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
import pl.kostka.restaurantclient.model.Restaurant
import pl.kostka.restaurantclient.service.ProductService
import pl.kostka.restaurantclient.service.RestaurantService
import pl.kostka.restaurantclient.service.callback.RestaurantListCallback
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

        var startLocation = LatLng(52.237049, 21.017532)
        var mapZoom = 5f

        mMapView!!.getMapAsync {
            googleMap = it

            val cameraPosition = CameraPosition.Builder().target(startLocation).zoom(mapZoom).build()
            googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }

        //TODO handle case of having more than one restaurant
        RestaurantService.getRestaurants(object: RestaurantListCallback {
            override fun onResponse(restaurants: List<Restaurant>) {
               restaurants.forEach {
                   val latLng = LatLng(it.latitude, it.longitude)
                    val restaurantName = it.name
                   activity?.runOnUiThread {
                       mMapView!!.getMapAsync {
                           googleMap = it

                           googleMap!!.addMarker(MarkerOptions().position(latLng).title(restaurantName))

                           val cameraPosition = CameraPosition.Builder().target(latLng).zoom(8f).build()

                           googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                       }
                   }
               }
            }

            override fun onFailure(errMessage: String) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })



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