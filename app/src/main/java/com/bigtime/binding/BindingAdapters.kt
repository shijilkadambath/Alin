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

package com.bigtime.binding;

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bigtime.R

/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */

/**
 * Data Binding adapters specific to the app.
 */
object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }


    /*@JvmStatic
    @BindingAdapter("app:addIconChange")
    fun addIconChange(view: ImageView, value: String) {
        when(value) {
            "frag1" -> {
                view.setImageResource(R.drawable.ic_file_upload_black_24dp)
            }

            "frag2" -> {
                view.setImageResource(R.drawable.ic_file_upload_black_24dp)
            }

            "frag3" -> {
                view.setImageResource(R.drawable.ic_file_upload_black_24dp)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("app:addColorChange")
    fun addColorChange(view: View, value: String) {
        when(value) {
            "frag1" -> {
                view.setBackgroundColor(R.color.colorAccent)
            }

            "frag2" -> {
                view.setImageResource(R.drawable.ic_file_upload_black_24dp)
            }

            "frag3" -> {
                view.setImageResource(R.drawable.ic_file_upload_black_24dp)
            }
        }
    }*/
}
