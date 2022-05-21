package com.psq.myjetpck

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/18 21:59
 * @desc   :
 */


/*所有使用 Hilt 的应用都必须包含一个带有 @HiltAndroidApp 注释的 Application 类。
@HiltAndroidApp 会触发 Hilt 的代码生成操作，生成的代码包括应用的一个基类，该基类充当应用级依赖项容器。
 */
@HiltAndroidApp
class App : MultiDexApplication()