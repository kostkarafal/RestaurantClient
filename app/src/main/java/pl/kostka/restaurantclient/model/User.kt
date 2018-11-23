package pl.kostka.restaurantclient.model

class User(var id: Long? = null,
           var username: String,
           var password: String,
           var name: String,
           var surname: String,
           var phoneNumber: String,
           var email: String,
           var selectedAddress: Address? = null)