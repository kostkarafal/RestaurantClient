package pl.kostka.restaurantclient.service

import com.google.gson.GsonBuilder
import okhttp3.*
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.model.User
import java.io.IOException

class ProductService {
    companion object {
        val hostUrl = "http://192.168.8.107:8060"
        val gson = GsonBuilder().create()
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val client = OkHttpClient()
        var loggedIn: Boolean = false

        fun getMenu(): Call {
            val request = Request.Builder()
                    .url("$hostUrl/products").get().build()

            return client.newCall(request)
        }

    }
}