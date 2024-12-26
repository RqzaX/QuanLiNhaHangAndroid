package com.example.nhahang

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(
    val context: Context,
    val items: List<RecyclerItem>,
    private val onClick: (Int, RecyclerItem) -> Unit
) : RecyclerView.Adapter<RecyclerAdapter.ItemVewHolder>() {
    class ItemVewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val textView : TextView = itemView.findViewById(R.id.tv_topnav)
        val imageview : ImageView = itemView.findViewById(R.id.img_topnav)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ItemVewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recycler_item,parent,false)
        return ItemVewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemVewHolder, position: Int) {
        val currentItem = items[position]
        holder.imageview.setImageResource(currentItem.img)
        holder.textView.text = currentItem.title
        holder.itemView.setOnClickListener {
            onClick(position, currentItem)
        }
        //set event
//        holder.itemView.setOnClickListener {
//            when(currentItem.title){
//                "All"->{
//                    Toast.makeText(context,"Ban chon All",Toast.LENGTH_SHORT).show()
//                }
//                "Dinner"->{
//                    Toast.makeText(context,"Ban chon Dinner",Toast.LENGTH_SHORT).show()
//                }
//                "Breakfast"->{
//                    Toast.makeText(context,"Ban chon Breakfast",Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        }
    }
}