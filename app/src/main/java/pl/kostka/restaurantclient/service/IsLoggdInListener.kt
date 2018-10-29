package pl.kostka.restaurantclient.service

interface IsLoggdInListener {
    fun onChange(isLoggedIn: Boolean)
}