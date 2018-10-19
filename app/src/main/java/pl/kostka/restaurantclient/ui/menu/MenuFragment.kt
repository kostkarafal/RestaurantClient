package pl.kostka.restaurantclient.ui.menu

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_menu.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.service.ProductService
import pl.kostka.restaurantclient.service.callback.GetMenuCallback
import pl.kostka.restaurantclient.ui.order.OrderActivity
import pl.kostka.restaurantclient.ui.product.ProductActivity
import java.io.IOException

class MenuFragment: Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_menu, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_Menu)

        recyclerView.layoutManager = LinearLayoutManager(view.context)


        ProductService.getMenu(object : GetMenuCallback{
            override fun onResponse(menu: List<Product>) {
                activity?.runOnUiThread {
                    recyclerView.adapter = MenuAdapter(menu)
                }
            }
            override fun onFailure(errMessage: String) {
                Snackbar.make(view, "Błedny użytkownik lub hasło", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            }
        })

        val floatingButton = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingButton.setOnClickListener {
            val intent = Intent(view.context, OrderActivity::class.java)

            view.context.startActivity(intent)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
    }


}