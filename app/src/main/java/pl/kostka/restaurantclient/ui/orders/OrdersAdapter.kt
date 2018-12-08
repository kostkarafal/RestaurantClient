package pl.kostka.restaurantclient.ui.orders

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.order_row.view.*
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Order
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.model.enums.OrderStatus
import pl.kostka.restaurantclient.model.enums.OrderType

class OrdersAdapter(val orders: List<Order>, val icons: Map<OrderStatus,Int>, val statuses: Map<OrderStatus, String>, val orderTypes: Map<OrderType, String>): RecyclerView.Adapter<OrdersViewHolder>() {

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return OrdersViewHolder(layoutInflater.inflate(R.layout.order_row, parent, false))
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
            val order = orders.get(position)
        holder.view.textView_orders_status?.text = statuses[order.status]
        holder.view.textView_orders_total_price?.text = String.format("%.2f", order.totalPrice)
        holder.view.textView_orders_adress?.text = order.restaurant.getFullAddressString()
        holder.view.textView_orders_date?.text = order.getDateString()
        holder.view.imageView_orders_status.setImageResource(icons[order.status]!!)
        holder.view.textView_order_type?.text = orderTypes[order.orderType]

        if(order.orderType == OrderType.DELIVERY && order.deliveryAddress != null){
            holder.view.textView_orders_delivery_address.visibility = View.VISIBLE
            holder.view.textView_orders_delivery_address?.text = order.deliveryAddress?.getFullAddressString()

        } else {
            holder.view.textView_orders_delivery_address.visibility = View.GONE

        }



    }

}

class OrdersViewHolder(val view: View, var product: Product? = null): RecyclerView.ViewHolder(view) {

    init {

    }
}