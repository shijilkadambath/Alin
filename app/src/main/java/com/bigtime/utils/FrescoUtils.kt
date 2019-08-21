package com.bigtime.utils

import android.net.Uri
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import java.io.File
import javax.inject.Singleton

/**
 * Created by Ȿ₳Ɲ @ NEWAGESMB on Friday, May 03, 2019
 */
@Singleton
class FrescoUtils {
    companion object {
        fun setImageToFrescoDraweeView(view: SimpleDraweeView, file: File) {
            view.controller = Fresco.newDraweeControllerBuilder()
                .setOldController(view.controller)
                .setImageRequest(imageRequest(file))
                .build()
        }

        fun setImageToFrescoDraweeView(view: SimpleDraweeView, url: String?) {
            if (!url.isNullOrEmpty()) {
                view.controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(view.controller)
                    .setImageRequest(imageRequest(url))
                    .build()
            }
        }

        private fun imageRequest(file: File): ImageRequest = ImageRequestBuilder
            .newBuilderWithSource(Uri.fromFile(file))
            .disableDiskCache()
            .disableMemoryCache()
            .build()

        private fun imageRequest(url: String): ImageRequest = ImageRequestBuilder
            .newBuilderWithSource(Uri.parse(url))
            .build()
    }
}