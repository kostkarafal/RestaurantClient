package pl.kostka.restaurantclient.ui.main

import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_row.view.*
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.HomeElement
import pl.kostka.restaurantclient.service.JwtService
import pl.kostka.restaurantclient.ui.menu.MenuFragment
import pl.kostka.restaurantclient.ui.orders.OrdersFragment
import pl.kostka.restaurantclient.ui.restaurants.RestaurantsFragment

class MainAdapter(val homeElements: List<HomeElement>,val fragmentManager: FragmentManager): RecyclerView.Adapter<MainViewHolder>() {

    override fun getItemCount(): Int {
        return homeElements.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.main_row_outside, parent, false)
        return MainViewHolder(cellForRow)

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val homeElement = homeElements.get(position)
        holder.view.textView_main_name?.text = homeElement.name
        holder.view.textView_main_description?.text = homeElement.description
        holder.view.imageView_main_icon.setImageResource(homeElement.iconResId!!.toInt())
        holder.view.imageView_main_image.setImageResource(homeElement.imageResId!!.toInt())

        holder.view.setOnClickListener {
            when(position) {
                0 -> fragmentManager.beginTransaction().replace(R.id.fragment_container, MenuFragment()).commit()
                1 -> fragmentManager.beginTransaction().replace(R.id.fragment_container, RestaurantsFragment()).commit()
                2 -> fragmentManager.beginTransaction().replace(R.id.fragment_container, OrdersFragment()).commit()
            }
        }
    }
}

class MainViewHolder(val view: View): RecyclerView.ViewHolder(view)