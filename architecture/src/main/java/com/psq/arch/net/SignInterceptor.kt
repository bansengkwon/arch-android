package com.psq.arch.net

import com.blankj.utilcode.util.EncryptUtils
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

/**
 * author : XL
 * e-mail : xlxs20100214@163.com
 * date: 2021/8/16
 * desc:
 */
/**
 * 请求参数签名
 */
class SignInterceptor : Interceptor {
    private val TAG = "SignInterceptor"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        var newBuilder = original.url.newBuilder()
        val method = original.method
        val body = original.body
        //蒲公英不需要加密--wangxin
        if(original.url.toString() == "https://www.pgyer.com/apiv2/app/check"){
            return chain.proceed(original)
        }
        if (method.equals("POST") && !(body is MultipartBody)) {
            val buffer = Buffer()
            body?.writeTo(buffer)
            var charset = Charset.forName("UTF-8")
            val contentType = body?.contentType();
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"));
            }
            val also = buffer.readString(charset)
            val params =
                ApiSignUtil.addDefaultParamAndSignPost(also)
            val request = original.newBuilder()
                .method(original.method, params.toRequestBody())
                .url(original.url).build()
            return chain.proceed(request)
        }
        /*if (method.equals("POST") && body is MultipartBody) {
            val builder = MultipartBody.Builder()
            builder.setType(body.type)
            val params = TreeMap<String, Any>(ApiSignUtil.getComparator())
            params.put("timestamp", System.currentTimeMillis() / 1000L)
            body.parts.forEach {
                val buffer = Buffer()
                it.body.writeTo(buffer)
                it.body.contentType()
                val toByte = buffer.readByteArray()
                params["md5"] = EncryptUtils.encryptMD5ToString(toByte)
                builder.addPart(it)
            }


            val plus = ApiSignUtil.getUrlParamsByMap(params).plus(ApiSignUtil.secret)
            params.put("sign", EncryptUtils.encryptSHA1ToString(plus))
            val entries = params.entries
            val iterator = entries.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                builder.addFormDataPart(next.key, next.value.toString())
            }
            val request = original.newBuilder()
                .method(original.method, builder.build())
                .url(original.url).build()
            return chain.proceed(request)
        }*/


        if (method.equals("POST") && body is MultipartBody) {
            val builder = MultipartBody.Builder()
            builder.setType(body.type)
            val params = TreeMap<String, Any>(ApiSignUtil.getComparator())
            params.put("timestamp", System.currentTimeMillis() / 1000L)
            body.parts.forEachIndexed { i, part ->
                val buffer = Buffer()
                part.body.writeTo(buffer)
                val name = part.headers?.get("Content-Disposition")?.let {
                    it.substring(17, it.length - 1)
                } ?: ""
                val contentType = part.body.contentType()
                if (contentType != null && contentType.toString()
                        .equals("application/json;charset=UTF-8")
                ) {
                    val value = buffer.readString(Charset.forName("UTF-8"))
                    params.put(name, value)
                } else {
                    val toByte = buffer.readByteArray()
                    params["md5"] = EncryptUtils.encryptMD5ToString(toByte)
                }
                builder.addPart(part)
            }


            val plus = ApiSignUtil.getUrlParamsByMap(params).plus(ApiSignUtil.secret)
            params.put("sign", EncryptUtils.encryptSHA1ToString(plus))
            val entries = params.entries
            val iterator = entries.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                builder.addFormDataPart(next.key, next.value.toString())
            }
            val request = original.newBuilder()
                .method(original.method, builder.build())
                .url(original.url).build()
            return chain.proceed(request)
        }

        return chain.proceed(original)
    }
}

object ApiSignUtil {
    private val TAG = "ApiSignUtil"

    val secret = "1\$w2N0PUBB&HuylB&Op2"

    val gson = Gson()

    @Suppress("UNCHECKED_CAST")
    fun addDefaultParamAndSignPost(content: String): Map<String, *> {
        val timestamp = System.currentTimeMillis() / 1000L
        val jsonObject = if (content.equals("\"\"") || content.isEmpty()) {
            JSONObject()
        } else {
            JSONObject(content)
        }
        jsonObject.put("timestamp", timestamp)
        val plus = getUrlParamsByJson(sortJsonForKey(jsonObject.toString())).plus(secret)
        jsonObject.put(
            "sign",
            EncryptUtils.encryptSHA1ToString(plus)
        )
//        val let = gson.fromJson("$jsonObject", Map::class.java).toMutableMap()
        val let = com.alibaba.fastjson.JSONObject.parseObject("$jsonObject", Map::class.java).toMutableMap()
        let["timestamp"] = timestamp
        return let as Map<String, *>
    }

    fun getUrlParamsByJson(json: String): String {
        val sb = StringBuffer()
        val jsonObject = JSONObject(json)
        val keysIterator = jsonObject.keys()
        while (keysIterator.hasNext()) {
            val key = keysIterator.next()
            sb.append("$key=${jsonObject.opt(key).toString().trimStart().trimEnd()}")
            sb.append("&")
        }
        var s = sb.toString()
        if (s.endsWith("&")) {
            s = s.substring(0, s.length - 1)
        }
        s = s.replace("=true", "=1")
        s = s.replace("=false", "=0")
        return s
    }

    fun getUrlParamsByMap(map: Map<String, Any>): String {
        val sb = StringBuffer()
        for ((key, value) in map) {
            sb.append("$key=$value")
            sb.append("&")
        }
        var s = sb.toString()
        if (s.endsWith("&")) {
            s = s.substring(0, s.length - 1)
        }
        s = s.replace("=true", "=1")
        s = s.replace("=false", "=0")
        return s
    }


    fun sortJsonForKey(json: String): String {
        val p = JsonParser()
        val e: JsonElement = p.parse(json)
        sort(e)
        return gson.toJson(e)
    }

    internal fun getComparator(): Comparator<String?> {
        return object : Comparator<String?> {
            override fun compare(o1: String?, o2: String?): Int {
                val vaule = o1?.let {
                    o2?.let {
                        o1.compareTo(o2)
                    }
                } ?: 0
                return if (vaule == 0) 1 else vaule
            }
        }
    }

    private fun sortArray(e: JsonElement, a: JsonArray): Boolean {
        if (a.size() > 0 && a.first().isJsonPrimitive) {
            a.sortedBy { i ->
                i.asCharacter.toString().first().let { f -> if (f.isLowerCase()) f.toInt() else (f.toInt() + f.toLowerCase().toInt()) }
            }.let { r ->
                e.asJsonArray.let { ja -> ja.removeAll { true }.let { for (ent in r) ja.add(ent) } }
            }
            return true
        }
        return false
    }

    fun sort(e: JsonElement) {
        if (e.isJsonNull()) {
            return
        }
        if (e.isJsonPrimitive()) {
            return
        }
        if (e.isJsonArray()) {
            val a: JsonArray = e.getAsJsonArray()
            val it: Iterator<JsonElement> = a.iterator()
//            if (sortArray(e, a)) {
//                return
//            }
            while (it.hasNext()) {
                sort(it.next())
            }
            return
        }
        if (e.isJsonObject()) {
            val p = JsonParser()
            val tm: MutableMap<String?, JsonElement> =
                TreeMap<String?, JsonElement>(getComparator())
            for ((key, value) in e.getAsJsonObject().entrySet()) {
               if(value.toString() == "true"){
                    tm[key] = p.parse("1")
                }else if(value.toString() == "false"){
                    tm[key] = p.parse("0")
                }
               else {
                    tm[key] = value
                }
            }
            for ((key, value) in tm) {
                e.getAsJsonObject().remove(key)
                e.getAsJsonObject().add(key, value)
                sort(value)
            }
            return
        }
    }


}

data class SortTemp(val key: Int, val value: JsonElement)