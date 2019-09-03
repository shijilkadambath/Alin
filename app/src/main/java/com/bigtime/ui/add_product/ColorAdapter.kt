package com.bigtime.ui.add_product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bigtime.R
import com.bigtime.data.model.ColorListItem
import com.bigtime.data.model.product_details.DataItem

/**
 * Created by Tony Augustine on 18,August,2019
 * tonyaugustine47@gmail.com
 */
class ColorAdapter: BaseAdapter (){

    private val dataList = ArrayList<ColorListItem?>()

    fun setData(list: ArrayList<ColorListItem?>) {
        this.dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View
        val vh: SolesViewHolder

        if (p1 == null) {
            view = LayoutInflater.from(p2?.context).inflate(R.layout.inflate_color_item, p2, false)
            vh = SolesViewHolder(view)
            vh.row
            view?.tag = vh
        }else {
            view = p1
            vh = view.tag as SolesViewHolder
        }

        vh.colorName.text = dataList[p0]?.colorName

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return super.getDropDownView(position, convertView, parent)
    }

    override fun getItem(p0: Int): Any {
       return dataList[p0]!!
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return dataList.size
    }

    private inner class SolesViewHolder(val row: View?) {
        val colorName: TextView = row?.findViewById(R.id.itemName) as TextView

    }

}