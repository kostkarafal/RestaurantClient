package pl.kostka.restaurantclient.model

import pl.kostka.restaurantclient.model.enums.OrderType

class Basket (var id: Long? = null,
              var totalPrize: Float,
              var products:  MutableList<ProductAmount>,
              var restaurantId: Long? = null,
              var deliveryAddressId: Long? = null,
              var orderType: OrderType? = null)