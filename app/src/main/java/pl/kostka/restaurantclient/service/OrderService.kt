package pl.kostka.restaurantclient.service

import com.google.gson.GsonBuilder
import okhttp3.*
import pl.kostka.restaurantclient.BuildConfig
import pl.kostka.restaurantclient.model.Order
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.service.callback.GetAuthHeaderCallback
import pl.kostka.restaurantclient.service.callback.OrderCallback
import pl.kostka.restaurantclient.service.callback.OrderListCallback
import java.io.IOException

class OrderService {
    companion object {
        val hostUrl = BuildConfig.HOST_URL
        val gson = GsonBuilder().create()
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val client = OkHttpClient()
        var loggedIn: Boolean = false

        fun getMenu(): Call {
            val request = Request.Builder()
                    .url("$hostUrl/products").get().build()

            return client.newCall(request)
        }

        fun addProductToBasket(products: List<Product>, callback: OrderCallback) {
            JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
                override fun onResponse(accesToken: String) {
                    val request = Request.Builder()
                            .url("${OrderService.hostUrl}/orders/basket")
                            .post(RequestBody.create(mediaType, gson.toJson(products)))
                            .addHeader("Authorization", "bearer $accesToken").build()

                    OrderService.client.newCall(request).enqueue(object : Callback {
                        override fun onResponse(call: Call?, response: Response?) {
                            var body = response?.body()?.string()
                            val order = OrderService.gson.fromJson(body, Order::class.java)
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


        fun getBasket(callback: OrderCallback) {
            JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
                override fun onResponse(accesToken: String) {
                    val request = Request.Builder()
                            .url("${OrderService.hostUrl}/orders/basket")
                            .get()
                            .addHeader("Authorization", "bearer $accesToken").build()

                    OrderService.client.newCall(request).enqueue(object : Callback {
                        override fun onResponse(call: Call?, response: Response?) {
                            var body = response?.body()?.string()
                            val order = OrderService.gson.fromJson(body, Order::class.java)
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

        fun confirmOrder(callback: OrderCallback) {
            JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
                override fun onResponse(accesToken: String) {
                    val request = Request.Builder()
                            .url("${OrderService.hostUrl}/orders/confirm")
                            .get()
                            .addHeader("Authorization", "bearer $accesToken").build()

                    OrderService.client.newCall(request).enqueue(object : Callback {
                        override fun onResponse(call: Call?, response: Response?) {
                            var body = response?.body()?.string()
                            val order = OrderService.gson.fromJson(body, Order::class.java)
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
    }
}