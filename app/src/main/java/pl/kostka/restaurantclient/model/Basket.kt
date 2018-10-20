package pl.kostka.restaurantclient.model

import pl.kostka.restaurantclient.model.enums.OrderStatus

class Basket (val id: Long,
              val totalPrize: Float,
              val orderStatus: OrderStatus,
              val productsAmount: List<Int>,
              val products: List<Product>)