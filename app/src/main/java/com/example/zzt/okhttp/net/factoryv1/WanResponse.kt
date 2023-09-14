package com.example.zzt.okhttp.net.factoryv1

import androidx.annotation.Keep

/**
 * @author: zeting
 * @date: 2023/9/14
 *
 */
@Keep
data class WanResponse<T>(
    var data: T?,
    var errorCode: Int,
    var errorMsg: String
)