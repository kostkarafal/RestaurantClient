package pl.kostka.restaurantclient

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.main_row.view.*
import kotlinx.android.synthetic.main.menu_row.view.*
import pl.kostka.restaurantclient.model.HomeElement
import pl.kostka.restaurantclient.model.Product

class MainAdapter(val homeElements: List<HomeElement>,val fragmentManager: FragmentManager): RecyclerView.Adapter<MainViewHolder>() {

    override fun getItemCount(): Int {
        return homeElements.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.main_row_outside, parent, false)
        return MainViewHolder(cellForRow, fragmentManager)

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val homeElement = homeElements.get(position)
        holder.view.textView_main_name?.text = homeElement.name
        holder.view.textView_main_description?.text = homeElement.description
        holder.view.imageView_main_icon.setImageResource(homeElement.iconResId!!.toInt())
        holder.view.imageView_main_image.setImageResource(homeElement.imageResId!!.toInt())
    }
}

class MainViewHolder(val view: View, val fragmentManager: FragmentManager): RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener {
            if (view.textView_main_name.text.equals("Menu")) {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, MenuFragment()).commit()
            }
        }
    }
}