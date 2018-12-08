package pl.kostka.restaurantclient.ui.orders


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.content_orders.*
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.ErrorResponse
import pl.kostka.restaurantclient.model.Order
import pl.kostka.restaurantclient.model.enums.OrderStatus
import pl.kostka.restaurantclient.model.enums.OrderType
import pl.kostka.restaurantclient.service.OrderService
import pl.kostka.restaurantclient.service.callback.OrderArrayCallback
import pl.kostka.restaurantclient.service.listener.OnChangeListener


class OrdersFragment: Fragment(){

    private var icons = emptyMap<OrderStatus, Int>()
    private var statuses = emptyMap<OrderStatus, String>()
    private var orderTypes = emptyMap<OrderType, String>()

    private var recyclerView1: RecyclerView? = null
    private var recyclerView2: RecyclerView? = null

    var ordersListSize: Int = 0
    var finishedOrdersListSize: Int = 0

    private var swipeLayout: SwipeRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_orders, container, false)
        recyclerView1 = view.findViewById(R.id.recycler1)
        recyclerView2 = view.findViewById(R.id.recycler2)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar_orders)
        swipeLayout = view.findViewById(R.id.swipe_order_layout)

         val doneIcon = resources.getIdentifier("@drawable/ic_done_black_24dp", "drawable", view.context.packageName)
         val inProgressIcon = resources.getIdentifier("@drawable/ic_watch_later_black_24dp", "drawable", view.context.packageName)
         val inDeliveryIcon = resources.getIdentifier("@drawable/ic_local_shipping_black_24dp", "drawable", view.context.packageName)
         val canceledIcon = resources.getIdentifier("@drawable/ic_cancel_black_24dp", "drawable", view.context.packageName)

        swipeLayout!!.setOnRefreshListener {
            refreshOrders(null)
        }

        icons = hashMapOf(OrderStatus.COMPLETED to doneIcon,
                                OrderStatus.CONFIRMED to inProgressIcon,
                                OrderStatus.DELIVERY to inDeliveryIcon,
                                OrderStatus.CANCELED to canceledIcon,
                                OrderStatus.PROCESSING to inProgressIcon)

        statuses = hashMapOf(OrderStatus.CANCELED to getString(R.string.canceled),
                                OrderStatus.DELIVERY to getString(R.string.in_delivery),
                                OrderStatus.CONFIRMED to getString(R.string.confirmed),
                                OrderStatus.PROCESSING to getString(R.string.processing),
                                OrderStatus.COMPLETED to getString(R.string.completed))

        orderTypes = hashMapOf(OrderType.DELIVERY to getString(R.string.delivery),
                                OrderType.SELF_PICKUP to getString(R.string.self_pickup))

        recyclerView1!!.layoutManager = LinearLayoutManager(view.context)
        recyclerView2!!.layoutManager = LinearLayoutManager(view.context)

        refreshOrders(progressBar)


        return view
    }


    private fun refreshOrders(progressBar: ProgressBar?){
        progressBar?.visibility = View.VISIBLE
        OrderService.getActualOrders(object : OrderArrayCallback {
            override fun onResponse(response: Array<Order>) {
                activity?.runOnUiThread {
                    ordersListSize = response.size
                    checkIfListsAreEmpty()
                    progressBar?.visibility = View.INVISIBLE
                    swipeLayout!!.isRefreshing = false

                    recyclerView1!!.adapter = OrdersAdapter(response.toList(), icons, statuses, orderTypes)
                }
            }

            override fun onFailure(error: ErrorResponse) {
                activity?.runOnUiThread {
                    progressBar?.visibility = View.INVISIBLE
                    swipeLayout!!.isRefreshing = false
                    Toast.makeText(this@OrdersFragment.context, error.getMsg(), Toast.LENGTH_LONG).show()

                }
            }
        })

        OrderService.getOrderHistory(object : OrderArrayCallback {
            override fun onResponse(response: Array<Order>) {
                activity?.runOnUiThread {
                    finishedOrdersListSize = response.size
                    checkFinishedList()
                    progressBar?.visibility = View.INVISIBLE
                    swipeLayout!!.isRefreshing = false
                    recyclerView2!!.adapter = OrdersAdapter(response.toList(), icons, statuses, orderTypes)
                }
            }

            override fun onFailure(error: ErrorResponse) {
                activity?.runOnUiThread {
                    progressBar?.visibility = View.INVISIBLE
                    swipeLayout!!.isRefreshing = false
                    Toast.makeText(this@OrdersFragment.context, error.getMsg(), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun checkIfListsAreEmpty(){
        if(ordersListSize == 0 && finishedOrdersListSize == 0) {
            textView_orders_empty.visibility = View.VISIBLE
        } else
        {
            textView_orders_empty.visibility = View.INVISIBLE
        }
    }

    private fun checkFinishedList(){
        checkIfListsAreEmpty()
        if(finishedOrdersListSize > 0){
            textView_finished_orders.visibility = View.VISIBLE
        } else {
            textView_finished_orders.visibility = View.INVISIBLE

        }
    }

}