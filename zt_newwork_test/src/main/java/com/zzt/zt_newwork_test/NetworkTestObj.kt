package com.zzt.zt_newwork_test

import java.io.Serializable

/**
 * @author: zeting
 * @date: 2025/4/21
 *
 */
data class NetworkTestObj(
    var requestUrl: String = "",
    var showProgress: Boolean = false,
    var showState: String = "",
    var dnsType: Int = 0
) : Serializable {
    companion object {
        const val DNS_TYPE_GOOGLE = 1
        const val DNS_TYPE_CLOUDFLARE = 2
    }

    override fun toString(): String {
        return "NetworkTestObj(requestUrl='$requestUrl', showProgress=$showProgress, showState='$showState')"
    }
}
