package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.Order

interface OrderListCallback {
    fun onResponse(orders: List<Order>)
    fun onFailure(errMessage: String)
}