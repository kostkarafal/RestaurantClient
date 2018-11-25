package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.Order

interface OrderCallback: MainCallback<Order> {
    override fun onResponse(response: Order)
}

interface OrderArrayCallback: MainCallback<Array<Order>> {
    override fun onResponse(response: Array<Order>)
}