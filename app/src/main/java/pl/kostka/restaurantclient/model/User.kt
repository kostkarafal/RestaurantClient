package pl.kostka.restaurantclient.model

class User(var id: Long? = null,
           var login: String,
           var password: String,
           var name: String,
           var surname: String,
           var phoneNumber: String,
           val email: String)