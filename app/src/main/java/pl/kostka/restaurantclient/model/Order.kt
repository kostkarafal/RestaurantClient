package pl.kostka.restaurantclient.model

import pl.kostka.restaurantclient.model.enums.OrderStatus

data class Order (var id: Long? = null,
                  var status: OrderStatus,
                  var totalPrice: Float,
                  var products: List<Product>? = null,
                  var restaurant: Restaurant? = null)