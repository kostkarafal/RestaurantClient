package pl.kostka.restaurantclient.service

import android.net.http.HttpResponseCache
import com.google.gson.GsonBuilder
import okhttp3.*
import pl.kostka.restaurantclient.BuildConfig
import pl.kostka.restaurantclient.model.Address
import pl.kostka.restaurantclient.model.Restaurant
import pl.kostka.restaurantclient.model.User
import pl.kostka.restaurantclient.service.callback.*
import java.io.IOException


object UserService{
        val hostUrl = BuildConfig.HOST_URL
        val gson = GsonBuilder().create()
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val client = OkHttpClient()


        fun registerUser(user: User, callback: UserCallback) {
            client.newCall(Request.Builder().url("$hostUrl/users")
                    .post(RequestBody.create(mediaType, gson.toJson(user))).build())
                    .enqueue(object : Callback {
                        override fun onResponse(call: Call?, response: Response?) {
                            val body = response?.body()?.string()
                            val result = gson.fromJson(body, User::class.java)
                            callback.onResponse(result)
                        }

                        override fun onFailure(call: Call?, e: IOException?) {
                            callback.onFailure("Błąd połączenia z serwerem")

                        }
                    })
        }

    fun checkIfUsernameIsFree(username: String, callback: BooleanCallback) {
        client.newCall(Request.Builder().url("$hostUrl/users/check-username")
                .post(RequestBody.create(mediaType, username)).build())
                .enqueue(object : Callback {
                    override fun onResponse(call: Call?, response: Response?) {
                        val body = response?.body()?.string()
                        val result = gson.fromJson(body, Boolean::class.java)
                        callback.onResponse(result)
                    }

                    override fun onFailure(call: Call?, e: IOException?) {
                        callback.onFailure("Błąd połączenia z serwerem")

                    }
                })
    }

    fun checkIfEmailIsFree(email: String, callback: BooleanCallback) {
        client.newCall(Request.Builder().url("$hostUrl/users/check-email")
                .post(RequestBody.create(mediaType, email)).build())
                .enqueue(object : Callback {
                    override fun onResponse(call: Call?, response: Response?) {
                        val body = response?.body()?.string()
                        val result = gson.fromJson(body, Boolean::class.java)
                        callback.onResponse(result)
                    }

                    override fun onFailure(call: Call?, e: IOException?) {
                        callback.onFailure("Błąd połączenia z serwerem")

                    }
                })
    }

    fun getUser(callback: UserCallback) {
        JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
            override fun onResponse(accesToken: String) {
                val request = Request.Builder()
                        .url("$hostUrl/users/user")
                        .get()
                        .addHeader("Authorization", "bearer $accesToken").build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call?, response: Response?) {
                        val body = response?.body()?.string()
                        val result = gson.fromJson(body, User::class.java)
                        callback.onResponse(result)
                    }

                    override fun onFailure(call: Call?, e: IOException?) {
                        callback.onFailure("Błąd połączenia z serwerem")

                    }
                })
            }

            override fun onFailure(errMessage: String) {
                callback.onFailure(errMessage)
            }
        })
    }

    fun editUser(user: User, callback: UserCallback) {
        JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
            override fun onResponse(accesToken: String) {
                val request = Request.Builder()
                        .url("${OrderService.hostUrl}/users/${user.id}")//TODO handle restaurant id
                        .put(RequestBody.create(mediaType, gson.toJson(user)))
                        .addHeader("Authorization", "bearer $accesToken").build()

                OrderService.client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call?, response: Response?) {
                        var body = response?.body()?.string()
                        val user = OrderService.gson.fromJson(body, User::class.java)
                        callback.onResponse(user)
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

    fun getSelectedAddress(callback: AddressCallback) {
        JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
            override fun onResponse(accesToken: String) {
                val request = Request.Builder()
                        .url("$hostUrl/users/selected-delivery-address")
                        .get()
                        .addHeader("Authorization", "bearer $accesToken").build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call?, response: Response?) {
                        val body = response?.body()?.string()
                        val result = gson.fromJson(body, Address::class.java)
                        callback.onResponse(result)
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

    fun getSelectedRestaurant(callback: RestaurantCallback) {
        JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
            override fun onResponse(accesToken: String) {
                val request = Request.Builder()
                        .url("$hostUrl/users/selected-restaurant")
                        .get()
                        .addHeader("Authorization", "bearer $accesToken").build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call?, response: Response?) {
                        val body = response?.body()?.string()
                        val result = gson.fromJson(body, Restaurant::class.java)
                        callback.onResponse(result)
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

    fun selectDeliveryAddress(addressId: Long, callback: AddressCallback) {
        JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
            override fun onResponse(accesToken: String) {
                val request = Request.Builder()
                        .url("$hostUrl/users/addresses/$addressId/select-delivery-address")//TODO handle restaurant id
                        .put(RequestBody.create(mediaType, gson.toJson(null)))
                        .addHeader("Authorization", "bearer $accesToken").build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call?, response: Response?) {
                        val body = response?.body()?.string()
                        val result = gson.fromJson(body, Address::class.java)
                        callback.onResponse(result)
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

  /*  fun selectRestaurant(restaurantId: Long, callback: RestaurantCallback) {
        JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
            override fun onResponse(accesToken: String) {
                val request = Request.Builder()
                        .url("$hostUrl/users/restaurants/$restaurantId/select-restaurant")//TODO handle restaurant id
                        .put(RequestBody.create(mediaType, gson.toJson(null)))
                        .addHeader("Authorization", "bearer $accesToken").build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call?, response: Response?) {
                        val body = response?.body()?.string()
                        val result = gson.fromJson(body, Restaurant::class.java)
                        callback.onResponse(result)
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

    }*/
    @Suppress("UNCHECKED_CAST")
    fun selectRestaurant(restaurantId: Long, callback: RestaurantCallback) {
        JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
            override fun onResponse(accesToken: String) {
                putRequest("users/restaurants/$restaurantId/select-restaurant", null, Restaurant::class.java, callback as MainCallback<Any>, accesToken)
            }

            override fun onFailure(errMessage: String) {
                callback.onFailure(errMessage)
            }
        })
    }

    private fun putRequest(url: String, body: Any?, responseClass: Class<out Any>, callback: MainCallback<Any>, accesToken: String? = null ) {
        var requestBuilder = Request.Builder()
                .url("$hostUrl/$url").put(RequestBody.create(mediaType, gson.toJson(body)))
        if(accesToken != null)
            requestBuilder = requestBuilder.addHeader("Authorization", "bearer $accesToken")
        val request = requestBuilder.build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                handleResponse(response, responseClass, callback )
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure("Błąd połączenia z serwerem")
            }
        })

    }



private fun handleResponse(response: Response, bodyClass: Class<out Any>, callback: MainCallback<Any>) {
    if(response.code() == 200) {
        val body = response.body()?.string()
        val result = gson.fromJson(body, bodyClass)
        callback.onResponse(result)
    } else {
        callback.onFailure(response.message())
    }
}
}