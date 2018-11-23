package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.model.Restaurant

interface RestaurantListCallback {
    fun onResponse(restaurants: List<Restaurant>)
    fun onFailure(errMessage: String)
}