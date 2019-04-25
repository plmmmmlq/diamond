package com.wingsoft.demo.global

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.multidex.MultiDex

/**
 * AppContext
 *
 * @author 祁连山
 * @date 2019-04-25
 * @version 1.0
 */
class AppContext : Application() {

    private var allActivities: MutableSet<Activity>? = null

    companion object {

        lateinit var i: AppContext

        var SCREEN_WIDTH = -1
        var SCREEN_HEIGHT = -1
        var DIMEN_RATE = -1.0f
        var DIMEN_DPI = -1
    }

    override fun onCreate() {
        super.onCreate()
        i = this
        // 初始化屏幕宽高
        getScreenSize()
        // 初始化数据库
        // Realm.init(applicationContext)

        // 在子线程中完成其他初始化
        InitializeService.start(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // 方法数超过64k 调用这个
        MultiDex.install(this)
    }

    fun addActivity(act: Activity) {
        if (allActivities == null) {
            allActivities = mutableSetOf()
        }
        allActivities?.add(act)
    }

    fun removeActivity(act: Activity) {
        allActivities?.remove(act)
    }

    fun exitAppCompletely() {
        if (allActivities != null) {
            synchronized(allActivities!!) {
                allActivities?.forEach { it.finish() }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }

    fun getScreenSize() {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        val display = windowManager.defaultDisplay
        display.getMetrics(dm)
        DIMEN_RATE = dm.density / 1.0f
        DIMEN_DPI = dm.densityDpi
        SCREEN_WIDTH = dm.widthPixels
        SCREEN_HEIGHT = dm.heightPixels
        if (SCREEN_WIDTH > SCREEN_HEIGHT) {
            val t = SCREEN_HEIGHT
            SCREEN_HEIGHT = SCREEN_WIDTH
            SCREEN_WIDTH = t
        }
    }

    fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null
    }
}