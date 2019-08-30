package com.bigtime.ui.add_product

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.bigtime.AppExecutors
import com.bigtime.R
import com.bigtime.data.model.Brand
import com.bigtime.databinding.InflateBrandItemBinding
import com.bigtime.ui.BaseDataBindListAdapter
import com.bigtime.ui.BaseDataBindListAdapterWithPosition

/**
 * Created by Tony Augustine on 18,August,2019
 * tonyaugustine47@gmail.com
 */
class BrandsAdapter(private val dataBindingComponent: DataBindingComponent,
                    appExecutors: AppExecutors,
                    private val itemClickCallback: ((Brand) -> Unit)) : BaseDataBindListAdapterWithPosition<Brand, InflateBrandItemBinding>(
        appExecutors, object : DiffUtil.ItemCallback<Brand>() {
    override fun areItemsTheSame(oldItem: Brand, newItem: Brand): Boolean {
        return oldItem.brandId == newItem.brandId
    }

    override fun areContentsTheSame(oldItem: Brand, newItem: Brand): Boolean {
        return oldItem.brandName.equals(newItem.brandName)
    }
}
) {

    private var itemSelected = -1

    override fun createBinding(parent: ViewGroup): InflateBrandItemBinding {

        val binding = DataBindingUtil.inflate<InflateBrandItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.inflate_brand_item, parent,
                false,
                dataBindingComponent)

        /*binding.radioButton.setOnClickListener {
            binding.radioButton.isChecked = binding.radioButton.isChecked
        }*/

        return binding

    }

    override fun bind(binding: InflateBrandItemBinding, item: Brand, position: Int) {

        binding.brandItem = item

        if (itemSelected != -1) {
            binding.radioButton.isChecked = itemSelected == position
        }

        binding.root.setOnClickListener {
            binding.brandItem?.let {
                itemSelected = position
                notifyDataSetChanged()
                itemClickCallback.invoke(it)
            }
        }
    }
}