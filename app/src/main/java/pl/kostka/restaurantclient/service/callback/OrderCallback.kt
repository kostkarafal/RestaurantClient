package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.Order

interface OrderCallback {
    fun onResponse(order: Order)
    fun onFailure(errMessage: String)
}