package com.wingsoft.demo.global

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by yanshirong on 2017/6/9.
 */
object Constants {

    val CACHE_DIR = AppContext.i.cacheDir.absolutePath
    val PATH_DATA = "${CACHE_DIR}/data"
    val PATH_CACHE = "${PATH_DATA}/netcache"


}