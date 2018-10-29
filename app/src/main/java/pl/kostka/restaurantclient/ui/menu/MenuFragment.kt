package pl.kostka.restaurantclient.ui.menu

import android.app.Activity
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
import android.widget.ProgressBar
import android.widget.Toast
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Product
import pl.kostka.restaurantclient.service.ProductService
import pl.kostka.restaurantclient.service.callback.ProductListCallback
import pl.kostka.restaurantclient.ui.basket.BasketActivity


class MenuFragment: Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_menu, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_Menu)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar_menu)
        recyclerView.layoutManager = LinearLayoutManager(view.context)


        ProductService.getMenu(object : ProductListCallback{
            override fun onResponse(menu: List<Product>) {
                activity?.runOnUiThread {
                    recyclerView.adapter = MenuAdapter(menu, this@MenuFragment)
                    progressBar.visibility = View.INVISIBLE
                }
            }
            override fun onFailure(errMessage: String) {
                activity?.runOnUiThread {
                    Toast.makeText(this@MenuFragment.context, errMessage, Toast.LENGTH_LONG).show()
                }
                progressBar.visibility = View.INVISIBLE
            }
        })
        val floatingButton = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatingButton.setOnClickListener {
            val intent = Intent(view.context, BasketActivity::class.java)

            view.context.startActivity(intent)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Snackbar.make(this@MenuFragment.view!!, getString(R.string.AddedItemToBasket), Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo) {
                        println("canceling recently added order")
                        //TODO cancel recently added order
                    }.show()
        }
    }

}