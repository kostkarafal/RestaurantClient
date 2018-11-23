package pl.kostka.restaurantclient.service

import com.google.gson.GsonBuilder
import okhttp3.*
import pl.kostka.restaurantclient.BuildConfig
import pl.kostka.restaurantclient.model.Address
import pl.kostka.restaurantclient.model.Basket
import pl.kostka.restaurantclient.model.Order
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.model.enums.OrderStatus
import pl.kostka.restaurantclient.service.callback.*
import java.io.IOException

object AddressService {
        const val hostUrl = BuildConfig.HOST_URL
        val gson = GsonBuilder().create()
        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val client = OkHttpClient()


        fun addAddress(address: Address, callback: AddressCallback) {
            JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
                override fun onResponse(accesToken: String) {
                    val request = Request.Builder()
                            .url("$hostUrl/addresses")//TODO handle restaurant id
                            .post(RequestBody.create(mediaType, gson.toJson(address)))
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

    fun updateAddress(address: Address, callback: AddressCallback) {
        JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
            override fun onResponse(accesToken: String) {
                val request = Request.Builder()
                        .url("$hostUrl/addresses/${address.id}")//TODO handle restaurant id
                        .put(RequestBody.create(mediaType, gson.toJson(address)))
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

        fun selectMainAddress(addressId: Long, callback: AddressCallback) {
            JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
                override fun onResponse(accesToken: String) {
                    val request = Request.Builder()
                            .url("$hostUrl/addresses/$addressId/select")//TODO handle restaurant id
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

    fun getSelectedAddress(callback: AddressCallback) {
        JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
            override fun onResponse(accesToken: String) {
                val request = Request.Builder()
                        .url("$hostUrl/addresses/selected")
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


    fun getAddresses(callback: AddressListCallback) {
            JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
                override fun onResponse(accesToken: String) {
                    val request = Request.Builder()
                            .url("$hostUrl/addresses")
                            .get()
                            .addHeader("Authorization", "bearer $accesToken").build()

                    client.newCall(request).enqueue(object : Callback {
                        override fun onResponse(call: Call?, response: Response?) {
                            val body = response?.body()?.string()
                            val result = gson.fromJson(body, Array<Address>::class.java).toList()
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

    fun getUnsupportedAddresses(callback: AddressListCallback) {
        JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
            override fun onResponse(accesToken: String) {
                val request = Request.Builder()
                        .url("$hostUrl/addresses/unsupported")
                        .get()
                        .addHeader("Authorization", "bearer $accesToken").build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call?, response: Response?) {
                        val body = response?.body()?.string()
                        val result = gson.fromJson(body, Array<Address>::class.java).toList()
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

    fun deleteAddress(addressId: Long, callback: VoidCallback) {
        JwtService.getAuthorizationHeader(object : GetAuthHeaderCallback {
            override fun onResponse(accesToken: String) {
                val request = Request.Builder()
                        .url("$hostUrl/addresses/$addressId")
                        .delete()
                        .addHeader("Authorization", "bearer $accesToken").build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call?, response: Response?) {
                        if(response!!.isSuccessful) {
                            callback.onResponse()
                        } else {
                            callback.onFailure(response.message())
                        }
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