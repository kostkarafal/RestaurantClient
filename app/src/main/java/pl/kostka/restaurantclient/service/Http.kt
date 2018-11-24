package pl.kostka.restaurantclient.service

import com.google.gson.GsonBuilder
import okhttp3.*
import pl.kostka.restaurantclient.BuildConfig
import pl.kostka.restaurantclient.service.callback.MainCallback
import java.io.IOException

object Http {
    val hostUrl = BuildConfig.HOST_URL
    val gson = GsonBuilder().create()
    val mediaType = MediaType.parse("application/json; charset=utf-8")
    val client = OkHttpClient()


    private fun put(url: String, body: Any?, responseClass: Class<out Any>, callback: MainCallback<Any>, accesToken: String? = null) {
        var requestBuilder = Request.Builder()
                .url("$hostUrl/$url").put(RequestBody.create(mediaType, gson.toJson(body)))
        if (accesToken != null)
            requestBuilder = requestBuilder.addHeader("Authorization", "bearer $accesToken")
        val request = requestBuilder.build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                handleResponse(response, responseClass, callback)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure("Błąd połączenia z serwerem")
            }
        })

    }


    private fun handleResponse(response: Response, bodyClass: Class<out Any>, callback: MainCallback<Any>) {
        if (response.code() == 200) {
            val body = response.body()?.string()
            val result = UserService.gson.fromJson(body, bodyClass)
            callback.onResponse(result)
        } else {
            callback.onFailure(response.message())
        }
    }
}