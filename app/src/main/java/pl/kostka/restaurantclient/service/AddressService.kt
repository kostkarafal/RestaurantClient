package pl.kostka.restaurantclient.service

import pl.kostka.restaurantclient.model.Address
import pl.kostka.restaurantclient.service.callback.*

object AddressService {

    fun addAddress(address: Address, callback: AddressCallback) {
        Http.authPost("addresses", address, Address::class.java, callback)
    }

    fun updateAddress(address: Address, callback: AddressCallback) {
        Http.authPut("addresses/${address.id}", address, Address::class.java, callback)
    }

    fun getAddresses(callback: AddressArrayCallback) {
        Http.authGet("addresses", Array<Address>::class.java, callback)
    }

    fun getUnsupportedAddresses(callback: AddressArrayCallback) {
        Http.authGet("addresses/unsupported", Array<Address>::class.java, callback)
    }

    fun deleteAddress(addressId: Long, callback: VoidCallback) {
        Http.authDelete("addresses/$addressId", callback)
    }
}