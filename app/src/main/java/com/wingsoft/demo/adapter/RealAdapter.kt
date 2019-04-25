package com.wingsoft.demo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wingsoft.demo.R
import com.wingsoft.demo.model.TodayEvent
import kotlinx.android.synthetic.main.item_view.view.*

/**
 * RealAdapter
 *
 * @author 祁连山
 * @date 2019-04-25
 * @version 1.0
 */
class RealAdapter : BaseQuickAdapter<TodayEvent, BaseViewHolder>(R.layout.item_view) {

    override fun convert(helper: BaseViewHolder?, item: TodayEvent?) {
        helper?.itemView?.textView?.text = item?.title
    }
}