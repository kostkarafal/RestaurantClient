package pl.kostka.restaurantclient.ui.basket

import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_basket.*
import kotlinx.android.synthetic.main.basket_product_row.view.*
import kotlinx.android.synthetic.main.basket_total_price_item.*
import kotlinx.android.synthetic.main.order_row.view.*
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Basket
import pl.kostka.restaurantclient.model.Order
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.model.enums.OrderStatus
import pl.kostka.restaurantclient.service.OrderService

class OrdersAdapter(val orders: List<Order>, val icons: HashMap<OrderStatus,Int>, val statuses: HashMap<OrderStatus, String>): RecyclerView.Adapter<OrdersViewHolder>() {

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
        holder.view.textView_orders_adress?.text = "ul.Testowa 45, Gliwice"
        holder.view.textView_orders_date?.text = "20.10.2018"
        holder.view.imageView_orders_status.setImageResource(icons[order.status]!!)



    }

}

class OrdersViewHolder(val view: View, var product: Product? = null): RecyclerView.ViewHolder(view) {

    init {

    }
}