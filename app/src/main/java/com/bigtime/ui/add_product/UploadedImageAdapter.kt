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

    fun setImageLinkWRTPosition(position: Int, imagePath: String) {
        images[position].apply {
            this.imagePath = imagePath
        }
        notifyItemChanged(position)
    }

    fun notifyData() {
        notifyDataSetChanged()
    }

    private var listener : ItemclickListener? = null


    fun setListener(listener: ItemclickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUpload {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.inflate_image_upload, parent, false)
        return ViewHolderUpload(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHolderUpload, position: Int) {

        when(images[position].imagePath) {
            "uploading" -> {
                holder.porgressbar.visibility = View.VISIBLE
            }
            "" -> {
                holder.porgressbar.visibility = View.GONE
            }
            else -> {
                holder.porgressbar.visibility = View.GONE
                holder.remove.visibility = View.VISIBLE
                FrescoUtils.setImageToFrescoDraweeView(holder.imageView, images[position].imagePath)
            }
        }

        holder.spinnerColor.adapter = holder.colorAdapter
        images[position].let {
            holder.colorAdapter.setData(it.colorList)
        }

        holder.imageView.setOnClickListener {
            listener?.loadNewImage(position)
        }

        holder.remove.setOnClickListener {
            listener?.removeImage(position)
        }

    }


    inner class ViewHolderUpload(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.image
        val colorAdapter = ColorAdapter()
        var spinnerColor = itemView.spinnerColor
        val articleText = itemView.articleText
        val porgressbar = itemView.progressBar
        val remove = itemView.btnRemove

    }

    interface ItemclickListener {
        fun loadNewImage(position: Int)

        fun removeImage(position: Int)
    }
}