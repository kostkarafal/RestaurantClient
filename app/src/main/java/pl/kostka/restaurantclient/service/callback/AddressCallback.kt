package pl.kostka.restaurantclient.service.callback

import pl.kostka.restaurantclient.model.Address

interface AddressCallback : MainCallback<Address> {
    override fun onResponse(response: Address)
}

interface AddressArrayCallback: MainCallback<Array<Address>> {
    override fun onResponse(response: Array<Address>)
}