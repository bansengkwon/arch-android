package com.fansmall.helper

import androidx.fragment.app.FragmentActivity
import com.fansmall.widget.dialog.ProgressDialog


object SimpleHUD {

    private var dialog: ProgressDialog? = null

    fun show(activity: FragmentActivity) {
        try {
            dismiss()
            dialog = ProgressDialog()
            dialog?.show(activity.supportFragmentManager)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    fun dismiss() {
        try {
            dialog?.dismissAllowingStateLoss()
            dialog = null
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    val showing: Boolean
        get() {
            return dialog != null
        }
}
