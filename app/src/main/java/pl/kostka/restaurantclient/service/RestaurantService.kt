package pl.kostka.restaurantclient.service

import pl.kostka.restaurantclient.model.Restaurant
import pl.kostka.restaurantclient.service.callback.RestaurantArrayCallback

object RestaurantService {

    fun getRestaurants(callback: RestaurantArrayCallback) {
        Http.get("restaurants", Array<Restaurant>::class.java, callback)
    }

}