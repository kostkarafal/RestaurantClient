package pl.kostka.restaurantclient.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.HomeElement

class MainFragment: Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.content_main, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_main)

        recyclerView.layoutManager = LinearLayoutManager(view.context)

        val homeElements = resources.getStringArray(R.array.home_elements)
        val homeElementsDescription = resources.getStringArray(R.array.home_elements_description)
        val homeElementsIcons = resources.getStringArray(R.array.home_elements_icons)
        val homeElementsImages = resources.getStringArray(R.array.home_elements_images)
        var home = Array(homeElements.size){HomeElement()}
        for(i in 0..homeElements.size - 1) {
            val iconId = resources.getIdentifier("@drawable/" + homeElementsIcons[i], "drawable", view.context.packageName)
            val imgId = resources.getIdentifier("@drawable/"+ homeElementsImages[i], "drawable", view.context.packageName)
            val element = HomeElement(name = homeElements[i],
                                    description = homeElementsDescription[i],
                                    iconResId = iconId,
                                    imageResId = imgId)
            home.set(i,element)
        }

        recyclerView.adapter = MainAdapter(home.toList(), fragmentManager!!)

        return view
    }
}