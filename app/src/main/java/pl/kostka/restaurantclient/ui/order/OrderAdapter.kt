package pl.kostka.restaurantclient.ui.order

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.order_product_row.view.*
import kotlinx.android.synthetic.main.order_total_prize_item.view.*
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Basket
import pl.kostka.restaurantclient.model.Product

class OrderAdapter(val basket: Basket, val activity: Activity): RecyclerView.Adapter<OrderViewHolder>() {

    var summaryFlag: Boolean = false
    var position: Int = 0
    override fun getItemCount(): Int {
        return basket.products.size + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        when(summaryFlag) {
            true -> return OrderViewHolder(layoutInflater.inflate(R.layout.order_total_prize_item, parent, false), activity)
            false -> return OrderViewHolder(layoutInflater.inflate(R.layout.order_product_row, parent, false), activity)
        }
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
//        this.position = position
        if(position < basket.products.size) {
            val product = basket.products.get(position/* - 1*/)
            holder.view.textView_order_product_title?.text = product.name
            holder.view.textView_order_product_details?.text = product.description
            holder.view.textView_order_product_price?.text = String.format("%.2f", product.price)
            holder.view.textView_order_product_amount?.text = basket.productsAmount.get(position).toString()
            holder.view.imageButton_order_edit.setOnClickListener {
                Toast.makeText(activity, product!!.name, Toast.LENGTH_LONG).show()
            }

            holder?.product = product
            if (position == basket.products.size - 1) {
                summaryFlag = true
            }
        } else {
            holder.view.textView_order_total_price?.text = String.format("%.2f", basket.totalPrize)
        }
    }
}

class OrderViewHolder(val view: View,val activity: Activity, var product: Product? = null): RecyclerView.ViewHolder(view) {

    init {
            if(product != null){

            }
    }


}