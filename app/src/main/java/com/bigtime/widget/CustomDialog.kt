package com.bigtime.widget

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.Window
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bigtime.R
import org.jetbrains.anko.find



object CustomDialog {

    fun with(
        @NonNull activity: Activity,
        statusIcon: Int?,
        title: String,
        message: String,
        isActionRequired: Boolean = false,
        actionText: String = "",
        iCustomDialogListener: ICustomDialogListener? = null,
        cancelable: Boolean = true,
        autoCloseableDialogListener: AutoCloseableDialogListener? = null
    ) {

        val dialog = Dialog(activity, android.R.style.Theme_Light)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.color.black))
        dialog.setContentView(R.layout.dialog_common_alert)
        dialog.setCancelable(cancelable)

        val ivStatus = dialog.find<AppCompatImageView>(R.id.iv_status)
        val tvTitle = dialog.find<AppCompatTextView>(R.id.tv_title)
        val tvMessage = dialog.find<AppCompatTextView>(R.id.tv_message)
        val btnAction = dialog.find<AppCompatButton>(R.id.btn_action)

        if (statusIcon != null) {
            ivStatus.setImageDrawable(ContextCompat.getDrawable(activity, statusIcon))
        } else {
            ivStatus.visibility = View.GONE
        }
        tvTitle.text = title
        tvMessage.text = message

        if (isActionRequired && iCustomDialogListener != null) {
            btnAction.text = actionText

            btnAction.setOnClickListener {
                iCustomDialogListener.onActionClicked()
                if (dialog.isShowing) dialog.dismiss()
            }
        } else {
            btnAction.visibility = View.GONE
        }

        autoCloseableDialogListener?.autoClose(dialog)

        dialog.find<ConstraintLayout>(R.id.cl_root)
            .setOnClickListener { if (cancelable && dialog.isShowing) dialog.dismiss() }

        dialog.find<ConstraintLayout>(R.id.cl_container).setOnClickListener { }

        if (!dialog.isShowing) dialog.show()

    }

    interface ICustomDialogListener {
        fun onActionClicked()
    }
    interface AutoCloseableDialogListener {
        fun autoClose(dialog: Dialog)
    }

}