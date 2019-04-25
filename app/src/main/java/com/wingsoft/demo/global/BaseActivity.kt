package com.wingsoft.demo.global

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * BaseActivity
 *
 * @author 祁连山
 * @date 2019-04-24
 * @version 1.0
 */
abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var context: Activity

    val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        AppContext.i.addActivity(this)
    }

    fun addSubscribe(subscription: Disposable) {
        compositeDisposable.add(subscription)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        // ImmersionBar.with(this).destroy()
        AppContext.i.removeActivity(this)
        super.onDestroy()
    }
}