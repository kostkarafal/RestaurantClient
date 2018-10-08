package pl.kostka.restaurantclient

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.menu_row.view.*
import pl.kostka.restaurantclient.model.Product
import java.io.File

class MenuAdapter(val menuProducts: List<Product>): RecyclerView.Adapter<CustomViewHolder>() {

    override fun getItemCount(): Int {
        return menuProducts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.menu_row_outside, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val product = menuProducts.get(position)
        holder.view.textView_productName?.text = product.name
        holder.view.textView_productDescription?.text = product.description
        holder.view.textView_priceAmount?.text = String.format("%.2f", product.price)
        val imageView = holder.view.imageView_product
        Picasso.with(holder.view.context).load(BuildConfig.HOST_URL + "/downloadFile/" + product.imageId.toString()).into(imageView)
    }
}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener {
            println("test")

            val intent = Intent(view.context, ProductActivity::class.java)

            view.context.startActivity(intent)
        }
    }
}