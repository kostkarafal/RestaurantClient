package pl.kostka.restaurantclient.service

import pl.kostka.restaurantclient.model.Basket
import pl.kostka.restaurantclient.model.Order
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.model.enums.OrderType
import pl.kostka.restaurantclient.service.callback.OrderCallback
import pl.kostka.restaurantclient.service.callback.OrderArrayCallback

object OrderService {

        private var basket: Basket = Basket(products = arrayListOf(), productsAmount = arrayListOf(), totalPrize = 0f, restaurantId = 10)

        fun getBasket(): Basket {
            return basket
        }

        fun addProductToBasket(product: Product) {
            val index = basket.products.indexOf(product)
            if (index >= 0) {
                basket.productsAmount.add(index, basket.productsAmount.get(index).plus(1))
                basket.productsAmount.removeAt(index + 1)
            } else {
                basket.products.add(product)
                basket.productsAmount.add(1)
            }
            this.refreshTotalPrice()
        }

        fun changeProductAmount(product: Product, amount: Int) {
            val index = basket.products.indexOf(product)

            if (amount == 0) {
                basket.products.removeAt(index)
                basket.productsAmount.removeAt(index)
            } else {
                basket.productsAmount.add(index, amount)
                basket.productsAmount.removeAt(index + 1)
            }
            this.refreshTotalPrice()
        }


        fun makeDeliveryOrder(deliveryAddressId: Long, callback: OrderCallback) {
            basket.deliveryAddressId = deliveryAddressId
            basket.orderType = OrderType.DELIVERY

            Http.authPost("/orders/make-order", basket, Order::class.java, callback)
            crearBasket()
        }

        fun makeSelfPickupOrder(restaurantId: Long, callback: OrderCallback) {
            basket.restaurantId = restaurantId
            basket.orderType = OrderType.SELF_PICKUP

            Http.authPost("/orders/make-order", basket, Order::class.java, callback)
            crearBasket()
        }

        fun getActualOrders(callback: OrderArrayCallback) {
            Http.authGet("/orders", Array<Order>::class.java, callback)
        }

        fun getOrderHistory(callback: OrderArrayCallback) {
            Http.authGet("/orders/history", Array<Order>::class.java, callback)
        }

        private fun refreshTotalPrice() {
            basket.totalPrize = 0f
            for (i in 0 until basket.products.size) {
                basket.totalPrize += basket.products[i].price * basket.productsAmount[i]
            }
        }

    private fun crearBasket(){
        basket = Basket(products = arrayListOf(), productsAmount = arrayListOf(), totalPrize = 0f, orderType = null, restaurantId = null, deliveryAddressId = null)

    }
}