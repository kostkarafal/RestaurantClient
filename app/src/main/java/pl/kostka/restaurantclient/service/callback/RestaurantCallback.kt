package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.Restaurant

interface RestaurantCallback: MainCallback<Restaurant>{
    override fun onResponse(response: Restaurant)
}