package pl.kostka.restaurantclient.model

import java.io.Serializable


data class Address (var id: Long? = null,
                    var city: String,
                    var street: String,
                    var buildingNumber: String,
                    var apartmentNumber: String? = null,
                    var title: String,
                    var longitude: Double,
                    var latitude: Double) : Serializable