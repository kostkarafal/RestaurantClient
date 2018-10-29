package pl.kostka.restaurantclient.service

import com.google.gson.GsonBuilder
import okhttp3.*
import pl.kostka.restaurantclient.BuildConfig
import pl.kostka.restaurantclient.model.Basket
import pl.kostka.restaurantclient.model.Order
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.model.enums.OrderStatus
import pl.kostka.restaurantclient.service.callback.BasketCallback
import pl.kostka.restaurantclient.service.callback.GetAuthHeaderCallback
import pl.kostka.restaurantclient.service.callback.OrderCallback
import pl.kostka.restaurantclient.service.callback.OrderListCallback
import java.io.IOException

object OrderService {
        val hostUrl = BuildConfig.HOST_URL
        val gson = GsonBuilder().create()
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val client = OkHttpClient()
        private var basket: Basket = Basket(products = arrayListOf(), productsAmount = arrayListOf(), totalPrize = 0f, restaurantId = 10)

        fun getMenu(): Call {
            val request = Request.Builder()
                    .url("$hostUrl/products").get().build()

            return client.newCall(request)
        }

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

        fun makeOrder(callback: OrderCallback) {
            JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
                override fun onResponse(accesToken: String) {
                    val request = Request.Builder()
                            .url("${OrderService.hostUrl}/orders/make-order")//TODO handle restaurant id
                            .post(RequestBody.create(mediaType, gson.toJson(basket)))
                            .addHeader("Authorization", "bearer $accesToken").build()

                    OrderService.client.newCall(request).enqueue(object : Callback {
                        override fun onResponse(call: Call?, response: Response?) {
                            var body = response?.body()?.string()
                            val order = OrderService.gson.fromJson(body, Order::class.java)
                            basket = Basket(products = arrayListOf(), productsAmount = arrayListOf(), totalPrize = 0f)
                            callback.onResponse(order)
                        }

                        override fun onFailure(call: Call, e: IOException) {
                            callback.onFailure("Błąd połączenia z serwerem")
                        }
                    })
                }

                override fun onFailure(errMessage: String) {
                    callback.onFailure(errMessage)
                }
            })

        }

        fun getOrderHistory(callback: OrderListCallback) {
            JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
                override fun onResponse(accesToken: String) {
                    val request = Request.Builder()
                            .url("${OrderService.hostUrl}/orders")
                            .get()
                            .addHeader("Authorization", "bearer $accesToken").build()

                    OrderService.client.newCall(request).enqueue(object : Callback {
                        override fun onResponse(call: Call?, response: Response?) {
                            var body = response?.body()?.string()
                            val orderList = OrderService.gson.fromJson(body, Array<Order>::class.java).toList()
                            callback.onResponse(orderList)
                        }

                        override fun onFailure(call: Call, e: IOException) {
                            callback.onFailure("Błąd połączenia z serwerem")
                        }
                    })
                }

                override fun onFailure(errMessage: String) {
                    callback.onFailure(errMessage)
                }
            })
        }

        private fun refreshTotalPrice() {
            basket.totalPrize = 0f
            for (i in 0 until basket.products.size) {
                basket.totalPrize += basket.products[i].price * basket.productsAmount[i]
            }
        }
}