package pl.kostka.restaurantclient.ui.myaccount

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.address_row.view.*

import pl.kostka.restaurantclient.R
import pl.kostka.restaurantclient.model.Address

class AddressAdapter(val addresses: List<Address>): RecyclerView.Adapter<AddresViewHolder>() {

    override fun getItemCount(): Int {
        return addresses.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddresViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AddresViewHolder(layoutInflater.inflate(R.layout.address_row, parent, false))
    }

    override fun onBindViewHolder(holder: AddresViewHolder, position: Int) {
            val address = addresses.get(position)
        holder.view.textView_address_title?.text = address.title
        holder.view.textView_addres?.text = "${address.street} ${address.buildingNumber} , ${address.city}"


    }

}

class AddresViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    init {

    }
}