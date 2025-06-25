package com.example.zt_jave_websocket;

import android.os.Build;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.net.ssl.SSLParameters;

public class HoldWSWebSocketClient extends WebSocketClient {
    String TAG = "Hold-Socket";
    String TAG_MSG = "Hold-Socket-msg";

    HoldWSSocketImpl wsSocketImpl;


    public HoldWSWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public HoldWSWebSocketClient(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    public HoldWSWebSocketClient(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    public HoldWSWebSocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders) {
        super(serverUri, protocolDraft, httpHeaders);
    }

    public HoldWSWebSocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }

    @Override
    protected void onSetSSLParameters(SSLParameters sslParameters) {
        if (Build.VERSION.SDK_INT >= 24) {
            super.onSetSSLParameters(sslParameters);
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        if (wsSocketImpl != null) {
            wsSocketImpl.validateConnect();
            wsSocketImpl.sendReconnectSuccess();
        }
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        if (bytes != null) {
            String message = StandardCharsets.UTF_8.decode(bytes).toString();
            webReadMsg(message);
        }
    }

    @Override
    public void onMessage(String message) {
        webReadMsg(message);
    }

    public void webReadMsg(String message) {
        if (message == null) {
            return;
        }
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (wsSocketImpl != null) {
            wsSocketImpl.setConnectClose();
        }
        if (code == 1000) {
            // 业务长链接，正常关闭不在重连
            return;
        }

//        0–999| |保留段, 未使用.
//        1000| CLOSE_NORMAL| 正常关闭; 无论为何目的而创建, 该链接都已成功完成任务.
//        1001| CLOSE_GOING_AWAY| 终端离开, 可能因为服务端错误, 也可能因为浏览器正从打开连接的页面跳转离开.
//        1002| CLOSE_PROTOCOL_ERROR| 由于协议错误而中断连接.
//        1003| CLOSE_UNSUPPORTED| 由于接收到不允许的数据类型而断开连接 (如仅接收文本数据的终端接收到了二进制数据).
//        1004| | 保留. 其意义可能会在未来定义.
//        1005| CLOSE_NO_STATUS| 保留. 表示没有收到预期的状态码.
//        1006| CLOSE_ABNORMAL| 保留. 用于期望收到状态码时连接非正常关闭 (也就是说, 没有发送关闭帧).
//        1007| Unsupported Data| 由于收到了格式不符的数据而断开连接 (如文本消息中包含了非 UTF-8 数据).
//        1008| Policy Violation| 由于收到不符合约定的数据而断开连接. 这是一个通用状态码, 用于不适合使用 1003 和 1009 状态码的场景.
//        1009| CLOSE_TOO_LARGE| 由于收到过大的数据帧而断开连接.
//        1010| Missing Extension| 客户端期望服务器商定一个或多个拓展, 但服务器没有处理, 因此客户端断开连接.
//        1011| Internal Error| 客户端由于遇到没有预料的情况阻止其完成请求, 因此服务端断开连接.
//        1012| Service Restart| 服务器由于重启而断开连接.
//        1013| Try Again Later| 服务器由于临时原因断开连接, 如服务器过载因此断开一部分客户端连接.
//        1014| | 由 WebSocket标准保留以便未来使用.
//        1015| TLS Handshake | 保留. 表示连接由于无法完成 TLS 握手而关闭 (例如无法验证服务器证书).
//        1016–1999| | 由 WebSocket标准保留以便未来使用.
//        2000–2999| | 由 WebSocket拓展保留使用.
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public void setWsSocketImpl(HoldWSSocketImpl wsSocketImpl) {
        this.wsSocketImpl = wsSocketImpl;
    }

    @Override
    public void close() {
        try {
            super.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeBlocking() throws InterruptedException {
        try {
            super.closeBlocking();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
