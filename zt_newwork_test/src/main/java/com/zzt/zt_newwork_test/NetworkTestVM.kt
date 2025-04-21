package com.zzt.zt_newwork_test

import androidx.compose.runtime.ScopeUpdateScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author: zeting
 * @date: 2025/4/21
 *
 */
class NetworkTestVM : ViewModel() {
    var showGoogle: MutableStateFlow<NetworkTestObj> =
        MutableStateFlow(NetworkTestObj())
    fun setGoogle(testObj: NetworkTestObj ) {
        showGoogle.value  = testObj
    }
 
    var showCloudflare: MutableStateFlow<NetworkTestObj> = MutableStateFlow(NetworkTestObj())
    fun setCloudflare(testObj: NetworkTestObj) {
        showCloudflare.value = testObj
    }
 


    var showGithub: MutableStateFlow<NetworkTestObj> = MutableStateFlow(NetworkTestObj())
    fun setGithub(testObj: NetworkTestObj) {
        showGithub.value = testObj
    }


    var showXtspd: MutableStateFlow<NetworkTestObj> = MutableStateFlow(NetworkTestObj())
    fun setXtspd(testObj: NetworkTestObj) {
        showXtspd.value = testObj
    }


    var showXtspdDnsGoogle: MutableStateFlow<NetworkTestObj> = MutableStateFlow(NetworkTestObj())
    fun setXtspdDnsGoogle(testObj: NetworkTestObj) {
        showXtspdDnsGoogle.value = testObj
    }


    var showXtspdDnsCloudflare: MutableStateFlow<NetworkTestObj> = MutableStateFlow(NetworkTestObj())
    fun setXtspdDnsCloudflare(testObj: NetworkTestObj) {
        showXtspdDnsCloudflare.value = testObj
    }



}