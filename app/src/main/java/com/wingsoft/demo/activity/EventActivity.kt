package com.wingsoft.demo.activity

import android.os.Bundle
import com.wingsoft.demo.R
import com.wingsoft.demo.global.BaseActivity
import com.wingsoft.demo.model.TodayEvent
import kotlinx.android.synthetic.main.event_activity.*

/**
 * EventActivity
 *
 * @author 祁连山
 * @date 2019-04-25
 * @version 1.0
 */
class EventActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_activity)

        val event = intent.getSerializableExtra("event") as TodayEvent
        title = event.title
        textView.text = event.des
    }
}