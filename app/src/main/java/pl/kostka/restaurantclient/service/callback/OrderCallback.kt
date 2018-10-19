package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.Order

interface OrderCallback {
    fun onResponse(menu: Order)
    fun onFailure(errMessage: String)
}