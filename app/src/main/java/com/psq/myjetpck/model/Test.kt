package com.psq.myjetpck.model

import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken
import com.hjq.gson.factory.GsonFactory

/**
 * @author : Anthony.Pan
 * @date   : 2022/6/1 15:43
 * @desc   :
 */

fun main() {

    create {
        1
    }.map {

    }.apply {  }

    val gson = GsonFactory.getSingletonGson()
    val json = "{\"id\":20,\"name\":[]}"
    val result = GsonUtils.fromJson(gson,json,TData::class.java)

    println("----result:${faultTolerantFromJson<List<TData>>(json)}")

}

data class TData(val id:Int?,val name:String?)

 fun < T> faultTolerantFromJson(json: String): T? =
    GsonUtils.fromJson(GsonFactory.getSingletonGson(), json, object : TypeToken<T>() {}.type)

class Helper<T>(var item: T)
fun <T,R> Helper<T>.map(action: T.() -> R) = Helper(action(item))
fun <R> create(action: () -> R) = Helper(action())

fun <T>dd(ins:T,block:T.()->Unit){
    block(ins)
}
