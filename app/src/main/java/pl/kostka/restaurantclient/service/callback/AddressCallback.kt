package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.Address

interface AddressCallback {
    fun onResponse(address: Address)
    fun onFailure(errMessage: String)
}