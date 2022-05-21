package com.psq.arch.base

import android.content.Context
import androidx.startup.Initializer
import com.blankj.utilcode.util.LogUtils
import com.psq.architecture.BuildConfig

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 10:26
 * @desc   :
 */
abstract class BaseInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        LogUtils.getConfig().isLogSwitch = BuildConfig.DEBUG
        init(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()

    abstract fun init(context: Context)
}