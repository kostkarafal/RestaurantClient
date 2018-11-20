package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.Address

interface AddressListCallback {
    fun onResponse(address: List<Address>)
    fun onFailure(errMessage: String)
}