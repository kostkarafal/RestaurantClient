package pl.kostka.restaurantclient.service.callback

interface VoidCallback {
    fun onResponse()
    fun onFailure(errMessage: String)
}