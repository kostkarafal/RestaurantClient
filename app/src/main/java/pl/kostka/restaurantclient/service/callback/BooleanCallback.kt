package pl.kostka.restaurantclient.service.callback


interface BooleanCallback {
    fun onResponse(response: Boolean)
    fun onFailure(errMessage: String)
}