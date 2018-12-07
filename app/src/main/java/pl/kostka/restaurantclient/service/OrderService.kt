package pl.kostka.restaurantclient.service

import pl.kostka.restaurantclient.model.Basket
import pl.kostka.restaurantclient.model.Order
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.model.ProductAmount
import pl.kostka.restaurantclient.model.enums.OrderType
import pl.kostka.restaurantclient.service.callback.OrderCallback
import pl.kostka.restaurantclient.service.callback.OrderArrayCallback

object OrderService {

        private var basket: Basket = Basket(products = mutableListOf(), totalPrize = 0f, restaurantId = 10)
        private var recentlyAdded: ProductAmount? = null

        fun getBasket(): Basket {
            return basket
        }

        fun addProductToBasket(product: Product, amount: Int) {
            val index = basket.products.indexOfFirst { it -> it.product == product }
            if(index >= 0){
                basket.products[index].amount += amount
            } else {
                basket.products.add(ProductAmount(product, amount))
            }
            recentlyAdded = ProductAmount(product,amount)

            this.refreshTotalPrice()
        }

        fun changeProductAmount(product: Product, amount: Int) {

            val index = basket.products.indexOfFirst { it -> it.product == product }

            if(index >= 0){
                if(amount == 0){
                    basket.products.removeAt(index)
                } else {
                    basket.products[index].amount = amount
                }
            } else {
                println("Cannot edit product amount of product which doesn't exist")
            }
            this.refreshTotalPrice()
        }

        fun undoRecentlyAddedProduct(): Boolean{
            val index = basket.products.indexOfFirst { it -> it.product == recentlyAdded?.product }
            if(recentlyAdded != null && recentlyAdded!!.amount > 0 && index > -1){
                if(basket.products[index].amount == recentlyAdded!!.amount){
                    basket.products.removeAt(index)
                } else {
                    basket.products[index].amount -= recentlyAdded!!.amount
                }
                refreshTotalPrice()
                return true
            } else {
                return false
            }
        }


        fun makeDeliveryOrder(deliveryAddressId: Long, callback: OrderCallback) {
            basket.deliveryAddressId = deliveryAddressId
            basket.orderType = OrderType.DELIVERY

            Http.authPost("/orders/make-order", basket, Order::class.java, callback)
            clearBasket()
        }

        fun makeSelfPickupOrder(restaurantId: Long, callback: OrderCallback) {
            basket.restaurantId = restaurantId
            basket.orderType = OrderType.SELF_PICKUP

            Http.authPost("/orders/make-order", basket, Order::class.java, callback)
            clearBasket()
        }

        fun getActualOrders(callback: OrderArrayCallback) {
            Http.authGet("/orders", Array<Order>::class.java, callback)
        }

        fun getOrderHistory(callback: OrderArrayCallback) {
            Http.authGet("/orders/history", Array<Order>::class.java, callback)
        }

        private fun refreshTotalPrice() {
            basket.totalPrize = 0f
            basket.products.forEach {
                basket.totalPrize += it.amount * it.product.price
            }
        }

    private fun clearBasket(){
        basket = Basket(products = mutableListOf(), totalPrize = 0f, orderType = null, restaurantId = null, deliveryAddressId = null)

    }

}