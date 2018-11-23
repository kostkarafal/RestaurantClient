package pl.kostka.restaurantclient.service

import com.google.gson.GsonBuilder
import okhttp3.*
import pl.kostka.restaurantclient.BuildConfig
import pl.kostka.restaurantclient.model.Restaurant
import pl.kostka.restaurantclient.service.callback.RestaurantListCallback
import java.io.IOException

object RestaurantService {
    private const val hostUrl = BuildConfig.HOST_URL
    private val gson = GsonBuilder().create()
    //    private val mediaType = MediaType.parse("application/json; charset=utf-8")
    private val client = OkHttpClient()


    fun getRestaurants(callback: RestaurantListCallback) {
        val request = Request.Builder()
                .url("$hostUrl/restaurants").get().build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                val restaurants = gson.fromJson(body, Array<Restaurant>::class.java).toList()
                callback.onResponse(restaurants)
            }

            override fun onFailure(call: Call?, e: IOException?) {
                callback.onFailure("Błąd połączenia z serwerem")
            }
        })

    }
}