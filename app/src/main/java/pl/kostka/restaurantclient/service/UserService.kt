package pl.kostka.restaurantclient.service

import pl.kostka.restaurantclient.model.Address
import pl.kostka.restaurantclient.model.Restaurant
import pl.kostka.restaurantclient.model.User
import pl.kostka.restaurantclient.service.callback.*


object UserService{

    fun registerUser(user: User, callback: UserCallback) {
        Http.post("/users", user, User::class.java, callback)
    }

    fun checkIfUsernameIsFree(username: String, callback: BooleanCallback) {
        Http.post("/users/check-username", username, Boolean::class.java, callback)
    }

    fun checkIfEmailIsFree(email: String, callback: BooleanCallback) {
        Http.post("/users/check-email", email, Boolean::class.java, callback)
    }

    fun getUser(callback: UserCallback) {
        Http.authGet("/users/user", User::class.java, callback)
    }

    fun editUser(user: User, callback: UserCallback) {
        Http.authPut("/users/${user.id}", user, User::class.java, callback)
    }

    fun getSelectedAddress(callback: AddressCallback) {
        Http.authGet("/users/selected-delivery-address", Address::class.java, callback)
    }

    fun getSelectedRestaurant(callback: RestaurantCallback) {
        Http.authGet("/users/selected-restaurant", Restaurant::class.java, callback)
    }

    fun selectDeliveryAddress(addressId: Long, callback: AddressCallback) {
        Http.authPut("/users/addresses/$addressId/select-delivery-address", null, Address::class.java, callback)
    }

    fun selectRestaurant(restaurantId: Long, callback: RestaurantCallback) {
        Http.authPut("/users/restaurants/$restaurantId/select-restaurant", null, Restaurant::class.java, callback)
    }

}