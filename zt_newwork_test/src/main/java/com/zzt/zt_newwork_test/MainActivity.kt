package com.zzt.zt_newwork_test

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzt.zt_newwork_test.ui.theme.ZZTOKHttpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZZTOKHttpTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = remember { NetworkTestVM() }
                    Greeting(
                        name = "Android", modifier = Modifier.padding(innerPadding), viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String, modifier: Modifier = Modifier, viewModel: NetworkTestVM
) {
    val scope = rememberCoroutineScope()

    val showGoogle by viewModel.showGoogle.collectAsStateWithLifecycle()

    val showCloudflare by viewModel.showCloudflare.collectAsStateWithLifecycle()

    val showGithub by viewModel.showGithub.collectAsStateWithLifecycle()

    val showXtspd by viewModel.showXtspd.collectAsStateWithLifecycle()

    val showXtspdDnsGoogle by viewModel.showXtspdDnsGoogle.collectAsStateWithLifecycle()

    val showXtspdDnsCloudflare by viewModel.showXtspdDnsCloudflare.collectAsStateWithLifecycle()

    var urlList = mutableListOf(
        NetworkTestObj("https://www.google.com"),
        NetworkTestObj("https://www.cloudflare.com"),
        NetworkTestObj("https://github.com"),
        NetworkTestObj("https://m.xtspd.com"),
        NetworkTestObj("https://m.xtspd.com", dnsType = NetworkTestObj.DNS_TYPE_GOOGLE),
        NetworkTestObj("https://m.xtspd.com", dnsType = NetworkTestObj.DNS_TYPE_CLOUDFLARE),
    )

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = "Network connectivity test", modifier = Modifier.padding(start = 8.dp)
            )
            Button(
                onClick = {
                    NetworkTestUtil.isCheckUrlScope(scope, viewModel, urlList)
                }, modifier = Modifier.offset(x = 20.dp)
            ) {
                Text("Start testing")
            }
        }


        testItemMsg(urlList.get(0), showGoogle.showProgress, showGoogle.showState)

        testItemMsg(
            urlList.get(1), showCloudflare.showProgress, showCloudflare.showState
        )

        testItemMsg(urlList.get(2), showGithub.showProgress, showGithub.showState)

        testItemMsg(urlList.get(3), showXtspd.showProgress, showXtspd.showState)

        testItemMsg(
            urlList.get(4), showXtspdDnsGoogle.showProgress, showXtspdDnsGoogle.showState
        )

        testItemMsg(
            urlList.get(5), showXtspdDnsCloudflare.showProgress, showXtspdDnsCloudflare.showState
        )

    }
}


@Composable
fun testItemMsg(urlObj: NetworkTestObj, showProgress: Boolean, showStatus: String) {
    var url = urlObj.requestUrl
    if (NetworkTestObj.DNS_TYPE_GOOGLE == urlObj.dnsType) {
        url = "DnsGoogle -> ${urlObj.requestUrl}"
    } else if (NetworkTestObj.DNS_TYPE_CLOUDFLARE == urlObj.dnsType) {
        url = "DnsCloudflare -> ${urlObj.requestUrl}"
    }
    HorizontalDivider(modifier = Modifier.padding(top = 20.dp))
    Row(
        modifier = Modifier.padding(top = 20.dp)
    ) {
        Text(
            url, modifier = Modifier.padding(
                start = 8.dp, end = 8.dp
            )
        )

        if (showProgress) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .width(20.dp)
                    .height(20.dp),
            )
        }
    }
    // 状态
    Text(
        color = Color(0xFFFF0000),
        text = showStatus,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ZZTOKHttpTheme {
        val viewModel = remember { NetworkTestVM() }
        Greeting("Android", viewModel = viewModel)
    }
}