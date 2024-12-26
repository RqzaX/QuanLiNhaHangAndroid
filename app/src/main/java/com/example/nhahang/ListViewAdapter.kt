package com.example.nhahang

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

class ListViewAdapter (val context: Context,var items : List<MonAn>):BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(p0: Int): MonAn {
        return items[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        var view = p1
        if(view == null) view  = inflater.inflate(R.layout.listview_item,p2,false)
        val imgAnhMon : ImageView = view!!.findViewById(R.id.imgAnhMon)
        val tvSTT : TextView = view!!.findViewById(R.id.tvSTT)
        val tvTenMon : TextView = view!!.findViewById(R.id.tvTenMon)

        var currentItem = items[p0]
        imgAnhMon.setImageResource(currentItem.anh)
        tvTenMon.text = (currentItem.tenMon)
        tvSTT.text = (p0 + 1).toString()

        return view
    }
    fun updateData(newList : List<MonAn>){
        this.items = newList
        notifyDataSetChanged()
    }
}