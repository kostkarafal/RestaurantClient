package pl.kostka.restaurantclient.ui.menu

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.menu_row.view.*
import pl.kostka.restaurantclient.BuildConfig
import pl.kostka.restaurantclient.ui.product.ProductActivity
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Product

class MenuAdapter(val menuProducts: List<Product>, val fragment:Fragment): RecyclerView.Adapter<CustomViewHolder>() {

    override fun getItemCount(): Int {
        return menuProducts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow: View
        cellForRow = layoutInflater.inflate(R.layout.menu_row_outside, parent, false)
        return CustomViewHolder(cellForRow, fragment)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {


            val product = menuProducts.get(position)
            holder.view.textView_productName?.text = product.name
            holder.view.textView_productDescription?.text = product.description
            holder.view.textView_priceAmount?.text = String.format("%.2f", product.price)
            val imageView = holder.view.imageView_product
            Picasso
                    .with(holder.view.context)
                    .load(BuildConfig.HOST_URL + "/downloadFile/" + product.imageId.toString())
                    .into(imageView, object : Callback{
                        override fun onSuccess() {
                            holder.view.progressBar_menu_item.visibility = View.INVISIBLE
                        }

                        override fun onError() {
                            holder.view.progressBar_menu_item.visibility = View.INVISIBLE
                        }
                    })
            holder.product = product
    }
}

class CustomViewHolder(val view: View, val fragment: Fragment, var product: Product? = null): RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener {
            if(product != null){
                val intent = Intent(view.context, ProductActivity::class.java)
                intent.putExtra("Product",product)
                fragment.startActivityForResult(intent, 1)
            }
        }
    }
}