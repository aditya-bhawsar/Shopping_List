package com.aditya.shoppinglist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView

class ItemAdapter (private val ctx: Context, private val listener: ItemListener): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private var list: List<Item> = emptyList()
    private val inflater: LayoutInflater = LayoutInflater.from(ctx)

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameTv: MaterialTextView = itemView.findViewById(R.id.name_tv)
        val amountTv: MaterialTextView = itemView.findViewById(R.id.amount_tv)
        val editImg: ImageView = itemView.findViewById(R.id.edit_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder { return ItemViewHolder(inflater.inflate(R.layout.list_item, parent, false)) }

    internal fun setItems(list : List<Item>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]
        holder.nameTv.text = item.name
        holder.amountTv.text = item.amount
        holder.editImg.setOnClickListener { listener.editClick(item) }
    }

    interface ItemListener{
        fun editClick(item: Item)
    }
}