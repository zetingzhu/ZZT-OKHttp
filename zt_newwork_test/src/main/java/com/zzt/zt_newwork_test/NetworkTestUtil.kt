package com.zzt.zt_newwork_test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Dns
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.dnsoverhttps.DnsOverHttps
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.net.InetAddress
import java.net.UnknownHostException

/**
 * @author: zeting
 * @date: 2025/4/21
 *
 */
object NetworkTestUtil {

    fun isCheckUrlScope(
        scope: CoroutineScope,
        viewModel: NetworkTestVM,
        urlList: MutableList<NetworkTestObj>
    ) {
        var index = 0

        isCheckUrlScopeLoop(scope, viewModel, index, urlList)
    }

    fun isCheckUrlScopeLoop(
        scope: CoroutineScope,
        viewModel: NetworkTestVM,
        index: Int = 0,
        urlList: MutableList<NetworkTestObj>,
    ) {
        if (index >= 0 && index < urlList.size) {
            val getUrl = urlList.get(index)
            scope.launch {
                withContext(context = scope.coroutineContext) {
                    showCheckStatus(getUrl, viewModel, true, false, "")
                }
                isCheckUrlByOkHttp(
                    getUrl,
                    success = { it ->
                        showCheckStatus(getUrl, viewModel, false, true, "")

                        // 轮询下一个
                        var nextIndex = index + 1
                        isCheckUrlScopeLoop(scope, viewModel, index = nextIndex, urlList)
                    },
                    failed = { it1, it2 ->
                        showCheckStatus(getUrl, viewModel, false, false, it2)

                        // 轮询下一个
                        var nextIndex = index + 1
                        isCheckUrlScopeLoop(scope, viewModel, index = nextIndex, urlList)
                    })
            }
        }
    }


    /**
     *
     * @param getUrl String
     * @param viewModel NetworkTestVM
     * @param startBoo Boolean 开始测试
     * @param resultBoo Boolean 测试结果
     */
    fun showCheckStatus(
        netObjOld: NetworkTestObj,
        viewModel: NetworkTestVM,
        startBoo: Boolean,
        resultBoo: Boolean,
        errMsg: String
    ) {

        var sendObj = NetworkTestObj(netObjOld.requestUrl, startBoo, "", netObjOld.dnsType)
        if (!startBoo) {
            if (resultBoo) {
                sendObj.showState = "Network test successful"
            } else {
                sendObj.showState = "Network test failed \n" + errMsg
            }
        }

        when (sendObj.requestUrl) {
            "https://www.google.com" -> {
                viewModel.setGoogle(sendObj)
            }

            "https://www.cloudflare.com" -> {
                viewModel.setCloudflare(sendObj)
            }

            "https://github.com" -> {
                viewModel.setGithub(sendObj)
            }

            "https://m.xtspd.com" -> {
                if (NetworkTestObj.DNS_TYPE_GOOGLE == sendObj.dnsType) {
                    viewModel.setXtspdDnsGoogle(sendObj)
                } else if (NetworkTestObj.DNS_TYPE_CLOUDFLARE == sendObj.dnsType) {
                    viewModel.setXtspdDnsCloudflare(sendObj)
                } else {
                    viewModel.setXtspd(sendObj)
                }
            }
        }
    }

    /**
     * 检测地址是否可以访问
     * @param requestUrl String
     * @return Boolean
     */
    fun isCheckUrlByOkHttp(
        requestUrl: NetworkTestObj, success: (String) -> Unit, failed: (String, String) -> Unit
    ) {
        val urlStr = requestUrl.requestUrl
        var timeoutMillis: Long = 5000L

        var builder = OkHttpClient.Builder()
            .connectTimeout(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS)
            .readTimeout(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS)
            .writeTimeout(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS)


        var client = builder.build()


        // 打印日志
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)


        if (NetworkTestObj.DNS_TYPE_GOOGLE == requestUrl.dnsType) {
            // 添加 google dns
            val googleDns = buildGoogle(client)
            client = OkHttpClient.Builder()
                .dns(googleDns).addInterceptor(httpLoggingInterceptor)
                .build()
        } else if (NetworkTestObj.DNS_TYPE_CLOUDFLARE == requestUrl.dnsType) {
            // 添加 cloudflare Dns
            val cloudflareDns = buildCloudflare(client)
            client = OkHttpClient.Builder()
                .dns(cloudflareDns).addInterceptor(httpLoggingInterceptor)
                .build()
        } else {
            client = OkHttpClient.Builder()
                .dns(Dns.SYSTEM).addInterceptor(httpLoggingInterceptor)
                .build()
        }


        val request = Request.Builder().head()
            .url(urlStr).build()

        try {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    failed.invoke(urlStr, e.message.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    success.invoke(urlStr)
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
            failed.invoke(urlStr, e.message.toString())
        }
    }


    fun buildGoogle(bootstrapClient: OkHttpClient): DnsOverHttps {
        return DnsOverHttps.Builder().client(bootstrapClient)
            .url("https://dns.google/dns-query".toHttpUrl())
            .bootstrapDnsHosts(
                getByIp("8.8.4.4"),
                getByIp("8.8.8.8")
            )
            .build()
    }

    fun buildCloudflare(bootstrapClient: OkHttpClient): DnsOverHttps {
        return DnsOverHttps.Builder().client(bootstrapClient)
            .url("https://cloudflare-dns.com/dns-query".toHttpUrl())
            .bootstrapDnsHosts(
                getByIp("1.1.1.1"),
                getByIp("1.0.0.1")
            )
            .build()
    }


    fun getByIp(host: String): InetAddress {
        try {
            return InetAddress.getByName(host)
        } catch (e: UnknownHostException) {
            // unlikely
            throw RuntimeException(e)
        }
    }
}