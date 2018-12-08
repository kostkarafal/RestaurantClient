package pl.kostka.restaurantclient.model

import pl.kostka.restaurantclient.model.enums.OrderStatus
import pl.kostka.restaurantclient.model.enums.OrderType
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

data class Order (var id: Long? = null,
                  var status: OrderStatus,
                  var totalPrice: Float,
                  var orderType: OrderType,
                  var products: List<Product>,
                  var restaurant: Restaurant,
                  var deliveryAddress: Address? = null,
                  var createDate: Timestamp? = null) {

    fun getDateString(): String{
        if(createDate!= null) {
            val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            return format.format(createDate)
        } else {
            return "Brak daty"
        }
    }
}