package pl.kostka.restaurantclient.service

import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import pl.kostka.restaurantclient.BuildConfig

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


    }
}