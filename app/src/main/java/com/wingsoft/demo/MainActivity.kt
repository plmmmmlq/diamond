package com.wingsoft.demo

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wingsoft.demo.activity.EventActivity
import com.wingsoft.demo.adapter.RealAdapter
import com.wingsoft.demo.global.BaseActivity
import com.wingsoft.demo.global.ServiceFactory
import com.wingsoft.demo.model.TodayEvent
import com.wingsoft.demo.utils.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * MainActivity
 *
 * @author 祁连山
 * @date 2019-04-24
 * @version 1.0
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ca = Calendar.getInstance()
        val year = ca.get(Calendar.YEAR)
        val month = ca.get(Calendar.MONTH) + 1
        val date = ca.get(Calendar.DATE)

        title = "历史上的今天 $year-$month-$date"

        val adapter = RealAdapter()
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter.setOnItemClickListener { adapter, view, position ->
            // toast((adapter.data[position] as TodayEvent).title)
            val event = adapter.data[position] as TodayEvent
            val it = Intent(this, EventActivity::class.java)
            it.putExtra("event", event)
            startActivity(it)
        }

        val map = HashMap<String, String>().apply {
            put("key", "72271f4ee0b4854a9e83e3be333bb728")
            put("month", "$month")
            put("day", "$date")
            put("v", "1.0")
        }

        val flowable = ServiceFactory.create().getEventOfToday(map)
        addSubscribe(
            flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({})
                .doOnTerminate({})
                .subscribe({
                    // throw NullPointerException("NullPointerException")
                    adapter.addData(it.result)
                }, {
                    toast(it.localizedMessage)
                })
        )
    }
}
