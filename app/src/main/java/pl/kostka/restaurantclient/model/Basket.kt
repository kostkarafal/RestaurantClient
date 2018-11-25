package pl.kostka.restaurantclient.model

import pl.kostka.restaurantclient.model.enums.OrderType

class Basket (var id: Long? = null,
              var totalPrize: Float,
              var productsAmount: ArrayList<Int>,
              var products: ArrayList<Product>,
              var restaurantId: Long? = null,
              var deliveryAddressId: Long? = null,
              var orderType: OrderType? = null)