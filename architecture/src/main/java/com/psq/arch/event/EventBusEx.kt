package com.psq.arch.event

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * @author : Anthony.Pan
 * @date   : 2022/8/30 15:10
 * @desc   :
 */

/**
 * 发送事件
 * @receiver T
 * @param key String
 */
inline fun <reified T> T.postEventBus(key: String) {
    LiveEventBus.get(key, T::class.java).post(this)
}

/**
 * 监听事件
 * @receiver AppCompatActivity
 * @param key String
 * @param observe Function1<T, Unit>
 */
inline fun <reified T> AppCompatActivity.onLiveEventBus(
    key: String,
    crossinline observe: (T) -> Unit
) {
    LiveEventBus.get(key, T::class.java).observe(this) {
        observe.invoke(it)
    }
}

/**
 * 监听事件
 * @receiver Fragment
 * @param key String
 * @param observe Function1<T, Unit>
 */
inline fun <reified T> Fragment.onLiveEventBus(key: String, crossinline observe: (T) -> Unit) {
    LiveEventBus.get(key, T::class.java).observe(this.viewLifecycleOwner) {
        observe.invoke(it)
    }
}

/**
 * 监听事件
 * @receiver AppCompatActivity
 * @param key String
 * @param observe Function1<T, Unit>
 */
inline fun <reified T> LifecycleOwner.onLiveEventBus(
    key: String,
    crossinline observe: (T) -> Unit
) {
    LiveEventBus.get(key, T::class.java).observe(this) {
        observe.invoke(it)
    }
}