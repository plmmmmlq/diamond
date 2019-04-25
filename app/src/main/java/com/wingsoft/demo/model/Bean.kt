package com.wingsoft.demo.model

import java.io.Serializable

data class Bean<T>(
        var error_code: Int,
        var reason: String,
        var result: T
)

data class TodayEvent(
        var _id: String,
        var title: String,
        var pic: String,
        var year: Int,
        var month: Int,
        var day: Int,
        var des: String,
        var lunar: String
) : Serializable