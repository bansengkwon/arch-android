package com.psq.myjetpck

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.psq.arch.base.BaseInitializer

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/22 00:37
 * @desc   :
 */
class AppInit:BaseInitializer() {

    override fun init(context: Context) {
       LogUtils.d("-----init-----")
    }
}