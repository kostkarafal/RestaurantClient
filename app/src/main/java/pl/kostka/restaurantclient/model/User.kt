package pl.kostka.restaurantclient.model

class User(var id: Long? = null,
           var login: String,
           var password: String,
           var name: String? = null,
           var surname: String? = null,
           var phoneNumber: String? = null,
           val email: String? = null)