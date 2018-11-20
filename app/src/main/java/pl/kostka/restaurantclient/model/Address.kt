package pl.kostka.restaurantclient.model


data class Address (var id: Long? = null,
                    var city: String,
                    var street: String,
                    var buildingNumber: String,
                    var apartmentNumber: String,
                    var title: String)