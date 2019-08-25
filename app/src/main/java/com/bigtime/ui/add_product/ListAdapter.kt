/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bigtime.ui.add_product

/**
 * Created by Shijil Kadambath on 03/08/2018
 *Email : shijilkadambath@gmail.com
 */

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.bigtime.AppExecutors
import com.bigtime.R
import com.bigtime.data.model.User
import com.bigtime.databinding.ItemUserBinding
import com.bigtime.ui.BaseDataBindListAdapter

/**
 * A RecyclerView adapter for [Repo] class.
 */
class ListAdapter(
        private val dataBindingComponent: DataBindingComponent,
        appExecutors: AppExecutors,
        private val itemClickCallback: ((User) -> Unit)?
) : BaseDataBindListAdapter<User, ItemUserBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
                        && oldItem.name == newItem.name
            }
        }
) {

    override fun createBinding(parent: ViewGroup): ItemUserBinding {
        val binding = DataBindingUtil.inflate<ItemUserBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_user,
                parent,
                false,
                dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.user?.let {
                itemClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: ItemUserBinding, item: User) {
        binding.user = item
    }
}
