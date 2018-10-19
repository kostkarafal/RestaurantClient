package pl.kostka.restaurantclient.service.callback

interface GetAuthHeaderCallback {
    fun onResponse(accesToken: String)
    fun onFailure(errMessage: String)
}

interface LoginResponseCallback {
    fun onResponse()
    fun onFailure(errMessage: String? = null)
}