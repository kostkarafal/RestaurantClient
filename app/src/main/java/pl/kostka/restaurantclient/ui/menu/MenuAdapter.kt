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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.menu_row.view.*
import pl.kostka.restaurantclient.BuildConfig
import pl.kostka.restaurantclient.ui.product.ProductActivity
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Product

class MenuAdapter(val menuProducts: List<Product>, val fragment:Fragment): RecyclerView.Adapter<CustomViewHolder>() {

    var categoryFlag: Boolean = false
    var position: Int = 0
    override fun getItemCount(): Int {
        return menuProducts.size + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow: View
        if (!categoryFlag) {
            categoryFlag = true
            cellForRow = layoutInflater.inflate(R.layout.menu_category_row, parent, false)

        }
        else
            cellForRow = layoutInflater.inflate(R.layout.menu_row_outside, parent, false)

        return CustomViewHolder(cellForRow, fragment)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        this.position = position
        if(position > 0) {
            val product = menuProducts.get(position - 1)
            holder.view.textView_productName?.text = product.name
            holder.view.textView_productDescription?.text = product.description
            holder.view.textView_priceAmount?.text = String.format("%.2f", product.price)
            val imageView = holder.view.imageView_product
            Picasso.with(holder.view.context).load(BuildConfig.HOST_URL + "/downloadFile/" + product.imageId.toString()).into(imageView)
            holder?.product = product
        } else {
            val spinner: Spinner = holder.view.findViewById(R.id.spinner2)

            ArrayAdapter.createFromResource(
                    holder.view.context,
                    R.array.category_array,
                    R.layout.my_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }
        }
    }
}

class CustomViewHolder(val view: View, val fragment: Fragment, var product: Product? = null): RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener {
            if(product != null){
                val intent = Intent(view.context, ProductActivity::class.java)
                intent.putExtra("Product",product)
                fragment.startActivityForResult(intent, 1)
                //view.context.startActivity(intent)

            }
        }
    }
}