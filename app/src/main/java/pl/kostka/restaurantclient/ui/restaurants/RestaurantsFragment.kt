package pl.kostka.restaurantclient.ui.restaurants

import android.app.Activity

import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.content_restaurants.*
import kotlinx.android.synthetic.main.restaurant_details.*
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Restaurant
import pl.kostka.restaurantclient.service.RestaurantService
import pl.kostka.restaurantclient.service.callback.RestaurantArrayCallback
import java.lang.Exception
import android.widget.Toast
import com.google.android.gms.location.*
import pl.kostka.restaurantclient.model.ErrorResponse
import pl.kostka.restaurantclient.service.UserService
import pl.kostka.restaurantclient.service.callback.RestaurantCallback
import pl.kostka.restaurantclient.ui.main.MainActivity
import pl.kostka.restaurantclient.ui.menu.MenuFragment
import java.util.*


class RestaurantsFragment: Fragment(){

    private var mMapView: MapView? = null
    var googleMap: GoogleMap? = null

    var restaurants: List<Restaurant> = Arrays.asList()
    var selectedRestaurant: Restaurant? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_restaurants, container, false)
        mMapView = view.findViewById(R.id.mapView) as MapView?
        mMapView!!.onCreate(savedInstanceState)
        mMapView!!.onResume()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

        mMapView!!.getMapAsync {
            googleMap = it
        }

        if(checkSelfPermission(activity!!, android.Manifest.permission.ACCESS_FINE_LOCATION) == -1){
            val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(activity!!, permissions,0)
        }

        fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    val cameraPosition = CameraPosition.Builder().target(LatLng(location!!.latitude, location.longitude)).zoom(10f).build()
                    googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))                }
        try {
            MapsInitializer.initialize(activity?.applicationContext)
        } catch (e: Exception){
            e.printStackTrace()
        }

        RestaurantService.getRestaurants(object: RestaurantArrayCallback {
            override fun onResponse(response: Array<Restaurant>) {
                restaurants = response.toList()
               restaurants.forEach {
                   val latLng = LatLng(it.latitude, it.longitude)
                   val restaurantId = it.id
                   activity?.runOnUiThread {
                       googleMap!!.addMarker(MarkerOptions().position(latLng).title(restaurantId.toString()))
                   }
               }
                activity?.runOnUiThread {
                    googleMap!!.setOnMarkerClickListener(object: GoogleMap.OnMarkerClickListener {
                        override fun onMarkerClick(p0: Marker?): Boolean {
                            activity?.runOnUiThread {
                                include_restaurant_details.visibility = View.VISIBLE
                                val result = restaurants.filter { restaurant -> restaurant.id == p0?.title?.toLong()}
                                if(result.size == 1){
                                    val restaurant = result[0]
                                    val address = "${restaurant.street} ${restaurant.buildingNumber}, ${restaurant.city}"
                                    textView_restaurant_details_name.text = restaurant.name
                                    textView_restaurant_details_address.text = address
                                    textView_restaurant_details_open_hours.text = restaurant.openHours
                                    selectedRestaurant = restaurant
                                } else {
                                    activity?.runOnUiThread {
                                        Toast.makeText(this@RestaurantsFragment.context, "Błąd podczas prztwarzania danych restauracji", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                            return true
                        }
                    })

                    button_restaurant_details_select.setOnClickListener {
                        if(activity!! is RestaurantActivity) {
                            UserService.selectRestaurant(selectedRestaurant!!.id, object : RestaurantCallback{
                                override fun onResponse(response: Restaurant) {
                                    activity?.setResult(Activity.RESULT_OK)
                                    activity?.finish()
                                }

                                override fun onFailure(error: ErrorResponse) {
                                    this@RestaurantsFragment.activity!!.runOnUiThread {
                                        Toast.makeText(this@RestaurantsFragment.activity, error.getMsg(), Toast.LENGTH_LONG).show()
                                    }
                                }
                            })

                        } else if (activity!! is MainActivity) {
                            UserService.selectRestaurant(selectedRestaurant!!.id, object : RestaurantCallback{
                                override fun onResponse(response: Restaurant) {
                                    activity!!.supportFragmentManager!!.beginTransaction().replace(R.id.fragment_container, MenuFragment()).commit()
                                }

                                override fun onFailure(error: ErrorResponse) {
                                    this@RestaurantsFragment.activity!!.runOnUiThread {
                                        Toast.makeText(this@RestaurantsFragment.activity, error.getMsg(), Toast.LENGTH_LONG).show()
                                    }
                                }
                            })
                        }
                    }
                }

            }

            override fun onFailure(error: ErrorResponse) {
                this@RestaurantsFragment.activity!!.runOnUiThread {
                    Toast.makeText(this@RestaurantsFragment.activity, error.getMsg(), Toast.LENGTH_LONG).show()
                }
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