package com.fansmall.widget.dialog

import com.fansmall.widget.R

class ProgressDialog : BaseDialogFragment() {

    override fun getLayoutRes(): Int {
        return R.layout.dialog_progress
    }

    override fun onStart() {
        super.onStart()
        this.dialog?.setCanceledOnTouchOutside(true)
    }
}


