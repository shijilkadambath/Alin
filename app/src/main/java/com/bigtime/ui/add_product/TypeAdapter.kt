package com.bigtime.ui.add_product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bigtime.R
import com.bigtime.data.model.product_details.FootwearTypeItem
import com.bigtime.data.model.product_details.SolesItem

/**
 * Created by Tony Augustine on 18,August,2019
 * tonyaugustine47@gmail.com
 */
class TypeAdapter: BaseAdapter (){

    private val footwearList = ArrayList<FootwearTypeItem?>()

    fun setData(list: ArrayList<FootwearTypeItem?>) {
        this.footwearList.clear()
        footwearList.addAll(list)
        notifyDataSetChanged()
    }
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View
        val vh: SolesViewHolder

        if (p1 == null) {
            view = LayoutInflater.from(p2?.context).inflate(R.layout.inflate_sole_item, p2, false)
            vh = SolesViewHolder(view)
            vh.row
            view?.tag = vh
        }else {
            view = p1
            vh = view.tag as SolesViewHolder
        }

        vh.soleName.text = footwearList[p0]?.type

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return super.getDropDownView(position, convertView, parent)
    }

    override fun getItem(p0: Int): Any {
       return footwearList[p0]!!
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return footwearList.size
    }

    private inner class SolesViewHolder(val row: View?) {
        val soleName: TextView = row?.findViewById(R.id.itemName) as TextView

    }

}