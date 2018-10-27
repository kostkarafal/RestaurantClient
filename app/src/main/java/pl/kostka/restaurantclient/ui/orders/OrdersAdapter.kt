package pl.kostka.restaurantclient.ui.basket

import android.app.Activity
import android.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_basket.*
import kotlinx.android.synthetic.main.basket_product_row.view.*
import kotlinx.android.synthetic.main.basket_total_price_item.*
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Basket
import pl.kostka.restaurantclient.model.Order
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.service.OrderService

class OrdersAdapter(val orders: List<Order>, val activity: Activity): RecyclerView.Adapter<OrdersViewHolder>() {

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return OrdersViewHolder(layoutInflater.inflate(R.layout.order_row, parent, false), activity)
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
            val order = orders.get(position)
         /*   holder.view.textView_basket_product_title?.text = product.name
            holder.view.textView_basket_product_details?.text = product.description
            holder.view.textView_basket_product_price?.text = String.format("%.2f", basket.productsAmount.get(position) * product.price)
            holder.view.textView_basket_product_amount?.text = basket.productsAmount.get(position).toString()*/

    }

}

class OrdersViewHolder(val view: View,val activity: Activity, var product: Product? = null): RecyclerView.ViewHolder(view) {

    init {

    }
}