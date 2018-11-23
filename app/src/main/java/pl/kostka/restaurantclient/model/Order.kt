package pl.kostka.restaurantclient.model

import pl.kostka.restaurantclient.model.enums.OrderStatus
import pl.kostka.restaurantclient.model.enums.OrderType

data class Order (var id: Long? = null,
                  var status: OrderStatus,
                  var totalPrice: Float,
                  var orderType: OrderType,
                  var products: List<Product>,
                  var restaurant: Restaurant? = null)