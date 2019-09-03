package com.bigtime.ui.add_product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bigtime.R
import com.bigtime.data.model.DataImage
import com.bigtime.utils.FrescoUtils
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.inflate_image_upload.view.*

/**
 * Created by Tony Augustine on 02,September,2019
 * tonyaugustine47@gmail.com
 */
class UploadedImageAdapter : RecyclerView.Adapter<UploadedImageAdapter.ViewHolderUpload>() {

    var images = ArrayList<DataImage>()

    fun setData(images: ArrayList<DataImage>) {
        this.images = images
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUpload {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.inflate_image_upload, parent, false)
        return ViewHolderUpload(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHolderUpload, position: Int) {
        FrescoUtils.setImageToFrescoDraweeView(holder.imageView, images[position].imagePath)
    }


    inner class ViewHolderUpload(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.image
        val spinnerColor = itemView.spinnerColor
        val spinnerArticle = itemView.spinnerArticle
    }
}