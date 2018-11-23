package pl.kostka.restaurantclient.service

import com.google.gson.GsonBuilder
import okhttp3.*
import pl.kostka.restaurantclient.BuildConfig
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.service.callback.ProductListCallback
import java.io.IOException

object ProductService {
        private const val hostUrl = BuildConfig.HOST_URL
        private val gson = GsonBuilder().create()
    //    private val mediaType = MediaType.parse("application/json; charset=utf-8")
        private val client = OkHttpClient()

        fun getMenu(callback: ProductListCallback) {
                    val request = Request.Builder()
                            .url("$hostUrl/products").get().build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onResponse(call: Call?, response: Response?) {
                            val body = response?.body()?.string()
                            val menu = ProductService.gson.fromJson(body, Array<Product>::class.java).toList()
                            callback.onResponse(menu)
                        }

                        override fun onFailure(call: Call?, e: IOException?) {
                            callback.onFailure("Błąd połączenia z serwerem")
                        }
                    })

        }
}