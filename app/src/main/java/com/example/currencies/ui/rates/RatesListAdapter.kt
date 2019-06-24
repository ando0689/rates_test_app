package com.example.currencies.ui.rates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currencies.R
import kotlinx.android.synthetic.main.item_rate.view.*


// FIXME Implemented in UGLY way in a big hurry
class RatesListAdapter(private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<RatesListAdapter.ViewHolder>() {

    private val data = mutableListOf<RateListItem>()

    fun updateItems(items: List<RateListItem>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rate, parent, false),
            itemClickListener
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }


    class ViewHolder(itemView: View, itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(itemView) {
        private var item: RateListItem? = null

        private val nameTv: TextView = itemView.rateItemBankName
        private val distance: TextView = itemView.rateItemDistance
        private val buyRate: TextView = itemView.rateItemBuy
        private val sellRate: TextView = itemView.rateItemSell

        init {
            itemView.setOnClickListener {
                item?.let {
                    itemClickListener.onItemClicked(it)
                }
            }
        }

        fun bind(item: RateListItem) {
            this.item = item
            nameTv.text = item.bankName
            distance.text = "${item.nearestBranchDistance} m"
            buyRate.text = item.rateBuy.toString()
            sellRate.text = item.rateSell.toString()
        }

    }

    interface ItemClickListener {
        fun onItemClicked(item: RateListItem)
    }
}