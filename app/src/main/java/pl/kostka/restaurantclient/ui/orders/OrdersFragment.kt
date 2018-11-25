package pl.kostka.restaurantclient.ui.orders


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Order
import pl.kostka.restaurantclient.model.enums.OrderStatus
import pl.kostka.restaurantclient.service.OrderService
import pl.kostka.restaurantclient.service.callback.OrderArrayCallback
import pl.kostka.restaurantclient.ui.basket.OrdersAdapter


class OrdersFragment: Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_orders, container, false)
        val recyclerView1 = view.findViewById<RecyclerView>(R.id.recycler1)
        val recyclerView2 = view.findViewById<RecyclerView>(R.id.recycler2)

        val doneIcon = resources.getIdentifier("@drawable/ic_done_black_24dp", "drawable", view.context.packageName)
        val inProgressIcon = resources.getIdentifier("@drawable/ic_watch_later_black_24dp", "drawable", view.context.packageName)
        val inDeliveryIcon = resources.getIdentifier("@drawable/ic_local_shipping_black_24dp", "drawable", view.context.packageName)
        val canceledIcon = resources.getIdentifier("@drawable/ic_cancel_black_24dp", "drawable", view.context.packageName)

        val icons = hashMapOf(OrderStatus.COMPLETED to doneIcon,
                                OrderStatus.CONFIRMED to inProgressIcon,
                                OrderStatus.DELIVERY to inDeliveryIcon,
                                OrderStatus.CANCELED to canceledIcon,
                                OrderStatus.PROCESSING to inProgressIcon)

        val statuses = hashMapOf(OrderStatus.CANCELED to getString(R.string.canceled),
                                OrderStatus.DELIVERY to getString(R.string.in_delivery),
                                OrderStatus.CONFIRMED to getString(R.string.confirmed),
                                OrderStatus.PROCESSING to getString(R.string.processing),
                                OrderStatus.COMPLETED to getString(R.string.completed))

        recyclerView1.layoutManager = LinearLayoutManager(view.context)
        recyclerView2.layoutManager = LinearLayoutManager(view.context)

        OrderService.getOrderHistory(object : OrderArrayCallback {
            override fun onResponse(response: Array<Order>) {
                activity?.runOnUiThread {
                    recyclerView1.adapter = OrdersAdapter(response.toList(), icons, statuses)
                    recyclerView2.adapter = OrdersAdapter(response.toList(), icons, statuses)
                }
            }

            override fun onFailure(errMessage: String) {
                activity?.runOnUiThread {
                    Toast.makeText(this@OrdersFragment.context, errMessage, Toast.LENGTH_LONG).show()
                }
            }
        })

        return view
    }

}