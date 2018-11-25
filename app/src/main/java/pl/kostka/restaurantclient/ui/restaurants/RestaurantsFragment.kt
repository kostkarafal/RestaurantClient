package pl.kostka.restaurantclient.ui.restaurants

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
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
import pl.kostka.restaurantclient.service.UserService
import pl.kostka.restaurantclient.service.callback.RestaurantCallback
import pl.kostka.restaurantclient.ui.main.MainActivity
import pl.kostka.restaurantclient.ui.menu.MenuFragment
import java.util.*


class RestaurantsFragment: Fragment(){

    var mMapView: MapView? = null
    var googleMap: GoogleMap? = null

    var restaurants: List<Restaurant> = Arrays.asList()
    var selectedRestaurant: Restaurant? = null



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
        RestaurantService.getRestaurants(object: RestaurantArrayCallback {
            override fun onResponse(response: Array<Restaurant>) {
                restaurants = response.toList()
               restaurants.forEach {
                   val latLng = LatLng(it.latitude, it.longitude)
                   val restaurantId = it.id
                   activity?.runOnUiThread {
                       mMapView!!.getMapAsync {
                           googleMap = it

                           googleMap!!.addMarker(MarkerOptions().position(latLng).title(restaurantId.toString()))

                           val cameraPosition = CameraPosition.Builder().target(latLng).zoom(15f).build()

                           googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                       }
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
                                override fun onResponse(restaurant: Restaurant) {
                                    activity?.setResult(Activity.RESULT_OK)
                                    activity?.finish()
                                }

                                override fun onFailure(errMessage: String) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }
                            })

                        } else if (activity!! is MainActivity) {
                            activity!!.supportFragmentManager!!.beginTransaction().replace(R.id.fragment_container, MenuFragment()).commit()
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