package pl.kostka.restaurantclient.service.listener

interface IsLoggdInListener {
    fun onChange(isLoggedIn: Boolean)
}