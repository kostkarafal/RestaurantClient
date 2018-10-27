package pl.kostka.restaurantclient.ui.order

import android.app.Activity
import android.app.AlertDialog
import android.support.transition.Visibility
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.order_product_row.view.*
import kotlinx.android.synthetic.main.order_total_prize_item.*
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Basket
import pl.kostka.restaurantclient.model.Order
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.service.OrderService
import pl.kostka.restaurantclient.service.callback.OrderCallback

class OrderAdapter(val basket: Basket, val activity: Activity): RecyclerView.Adapter<OrderViewHolder>() {

    override fun getItemCount(): Int {
        return basket.products.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return OrderViewHolder(layoutInflater.inflate(R.layout.order_product_row, parent, false), activity)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
            val product = basket.products.get(position)
            holder.view.textView_order_product_title?.text = product.name
            holder.view.textView_order_product_details?.text = product.description
            holder.view.textView_order_product_price?.text = String.format("%.2f", basket.productsAmount.get(position) * product.price)
            holder.view.textView_order_product_amount?.text = basket.productsAmount.get(position).toString()

            holder.view.imageButton_order_edit.setOnClickListener {
                handleEditOrderModal(holder, position, product)

            }
    }

    private fun handleEditOrderModal(holder: OrderViewHolder, position: Int, product: Product) {
        Toast.makeText(activity, product.name, Toast.LENGTH_LONG).show()
        val mBuilder = AlertDialog.Builder(activity)
        val mView = activity.layoutInflater.inflate(R.layout.dialog_edit_order_product, null)

        val title = mView.findViewById<TextView>(R.id.textView_dialog_title)
        val price = mView.findViewById<TextView>(R.id.textView_dialog_price)
        val buttonMinus = mView.findViewById<Button>(R.id.button_dialog_minus)
        val buttonPlus = mView.findViewById<Button>(R.id.button_dialog_plus)
        val textAmount = mView.findViewById<EditText>(R.id.editText_dialog_amount)
        val cancelButton = mView.findViewById<Button>(R.id.button_cancel_dialog_order)
        val okButton = mView.findViewById<Button>(R.id.button_ok_dialog_order)


        textAmount.setText(basket.productsAmount.get(position).toString())
        title.text = product.name
        price.text = String.format("%.2f", basket.productsAmount.get(position) * product.price)

        mBuilder.setView(mView)
        val dialog = mBuilder.create()
        dialog.show()

        buttonMinus.setOnClickListener {
            var result = textAmount.text.toString().toInt()
            if (result > 0) result--
            textAmount.setText(result.toString())
            price.text = String.format("%.2f", textAmount.text.toString().toInt() * product.price)
        }

        buttonPlus.setOnClickListener {
            var result = textAmount.text.toString().toInt()
            if (result < 100) result++
            textAmount.setText(result.toString())
            price.text = String.format("%.2f", textAmount.text.toString().toInt() * product.price)
        }

        cancelButton.setOnClickListener {
            dialog.hide()
        }

        okButton.setOnClickListener {
            val amount = textAmount.text.toString().toInt()
            val removeIndex = position

            OrderService.changeProductAmount(product, amount)
            if(amount == 0) {
                notifyItemRemoved(removeIndex)
                notifyItemRangeChanged(removeIndex, basket.products.size)
                if(basket.products.size == 0) {
                    activity.container_order_summary.visibility = View.INVISIBLE
                    activity.textView_order_empty_basket.visibility = View.VISIBLE
                }
            } else {
                holder.view.textView_order_product_price?.text = String.format("%.2f", textAmount.text.toString().toInt() * product.price)
                holder.view.textView_order_product_amount?.text = textAmount.text.toString()
            }
            activity.textView_order_total_price.text = String.format("%.2f", OrderService.getBasket().totalPrize)
            dialog.hide()
        }

    }
}

class OrderViewHolder(val view: View,val activity: Activity, var product: Product? = null): RecyclerView.ViewHolder(view) {

    init {
            if(product != null){

            }
    }


}