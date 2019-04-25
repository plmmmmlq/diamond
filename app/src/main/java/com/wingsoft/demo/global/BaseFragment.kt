package com.wingsoft.demo.global

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * BaseActivity
 *
 * @author 祁连山
 * @date 2019-04-24
 * @version 1.0
 */
abstract class BaseFragment : Fragment() {

    protected lateinit var activity: Context
    protected lateinit var rootView: View

    val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onAttach(context: Context) {
        activity = context
        super.onAttach(context)
    }

    fun addSubscribe(subscription: Disposable) {
        compositeDisposable.add(subscription)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

}