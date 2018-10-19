package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.Order

interface OrderListCallback {
    fun onResponse(menu: List<Order>)
    fun onFailure(errMessage: String)
}