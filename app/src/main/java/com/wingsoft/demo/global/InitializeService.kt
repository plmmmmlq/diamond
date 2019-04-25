package com.wingsoft.demo.global

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * InitializeService
 *
 * @author 祁连山
 * @date 2019-04-24
 * @version 1.0
 */
class InitializeService : IntentService("InitializeService") {

    companion object {
        val ACTION_INIT = "initApplication"

        fun start(context: Context) {
            val intent = Intent(context, InitializeService::class.java)
            intent.action = ACTION_INIT
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        val action = intent?.action
        if (ACTION_INIT == action) initApplication()
    }

    private fun initApplication() {
        // 初始化错误收集
        // initBugly()

        // 初始化内存泄漏检测
        // LeakCanary.install(App.instance)

        // 初始化过度绘制检测
        // BlockCanary.install(applicationContext,AppBlockCanaryContext()).start()
    }

    private fun initBugly() {
        // val context = applicationContext
        // val packageName = context.packageName
        // val processName = getProcessName(android.os.Process.myPid())
        // val strategy = CrashReport.UserStrategy(context)
        // strategy.isUploadProcess = processName == null || processName == packageName
        // CrashReport.initCrashReport(context, "", true, strategy)
    }

    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                if (reader != null) {
                    reader.close()
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }
}