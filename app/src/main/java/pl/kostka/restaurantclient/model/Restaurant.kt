package pl.kostka.restaurantclient.model

class Restaurant (val id: Long,
                  val name: String,
                  val description: String,
                  val city: String,
                  val street: String,
                  val buildingNumber: String,
                  val openHours: String,
                  val latitude: Double,
                  val longitude: Double) {

    fun getFullAddressString():String {
        var result = "$name: $street $buildingNumber, $city "
        return result
    }
}