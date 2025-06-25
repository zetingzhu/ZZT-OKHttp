package com.example.zt_jave_websocket;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import org.java_websocket.client.WebSocketClient;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

public class HoldWSSocketImpl {
    String TAG = "Hold-Socket";

    private boolean isInited = false;
    /**
     * 发送心跳的间隔时间
     */
    private static long heartTime = 5 * 1000L;
    private static long TCP_TIME_OUT = 15 * 1000L;
    /*断开心跳到达次数，发起重连*/
    private static int checkCount = 3;
    /*上一次收到tcp 行情的时间*/
    private long lasteTcpTime = 0;
    /*上一次收到tcp 任一响应的时间*/
    private long lasteTcpResTime = 0;

    HoldWSWebSocketClient wsClient;

    public static WebHandler handler;
    // 当前长链接下标
    private int mIndex = 0;
    // 是否首次连接成功
    private Integer isFirstConnect;
    // 是否连接中
    private boolean isConnecting = false;
    // 重连次数限制
    private int reconnectCount = 0;

    public HoldWSSocketImpl() {
        if (handler == null) {
            handler = new WebHandler();
        }
        handler.setSocketImpl(this);
    }

    public void init() {
        isInited = true;
        start();
    }

    public WebSocketClient getWebSocketClient() {
        return wsClient;
    }

    public void setWebSocketClient(WebSocketClient webSocketClient) {
        this.wsClient = wsClient;
    }

    /**
     * 第一次建立连接
     */
    public void validateConnect() {
        if (wsClient == null)
            return;
        if (wsClient.isOpen()) {
            isConnecting = false;
        }

    }

    /**
     * 成功和异常都需要设置连接结束标识符
     */
    public void setConnectClose() {
        isConnecting = false;
        Log.d(TAG, ">>>>  isConnecting set false 2");
    }

    /**
     * 停止行情推送
     * 不断开连接，仅仅不推送
     */
    public void stopWrite() {
        if (wsClient == null)
            return;
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            if (wsClient != null && wsClient.isOpen()) {
                wsClient.close();
            }
            isInited = false;
            isConnecting = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送需要推送的行情
     *
     * @param codes String codes="HPME|OIL,HPME|XAG1"; 多个,分隔
     */
    public void write(String codes) {
        if (wsClient == null || !wsClient.isOpen()) {
            reStart();
            return;
        }

    }


    public boolean isInited() {
        return isInited;
    }

    /**
     * 重新连接
     */
    public void reStart() {
        if (!isInited()) {
            Log.d(TAG, "启动创建连接 start 33");
            init();
        } else {
            //长连接已经断开，重新连接
            if (wsClient == null || !wsClient.isOpen()) {
                Log.d(TAG, "启动创建连接 start 2");
                start();
            } else {
                Log.d(TAG, "重连失败，没有断");
            }
        }
    }

    public boolean isOpenStatus() {
        if (wsClient != null && wsClient.isOpen()) {
            return true;
        }
        if (isConnecting) {
            // 正在连接中，重连了
            return true;
        }
        return false;
    }

    public boolean isConnectIng() {
        return false;
    }

    public Integer firstConnectCount() {
        return isFirstConnect;
    }

    public boolean wsRealStatus() {
        try {
            if (wsClient != null) {
                return wsClient.isOpen();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 开启连接
     */
    public void start() {
        if (wsClient != null && wsClient.isOpen()) {
            return;
        }
        if (isConnecting) {
            return;
        }
        isConnecting = true;
        Dispatcher.getInstance().enqueue(new Runnable() {
            @Override
            public void run() {
                connect();
            }
        });
    }

    private synchronized void connect() {
        if (wsClient != null && wsClient.isOpen()) {
            isConnecting = false;
            return;
        }
        try {
            isConnecting = true;
            URI uri = URI.create("ws://test-quo-push-ws.yinyu.tech/ws");
            wsClient = new HoldWSWebSocketClient(uri);
            wsClient.setWsSocketImpl(this);
            wsClient.connect();

            String hostName = uri.getHost();
            Log.d(TAG, ">>>> connect hostName:" + hostName + " uri:" + uri);

            //Verify
            if (wsClient.getSocket() instanceof SSLSocket) {
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                SSLSocket socket = (SSLSocket) wsClient.getSocket();
                SSLSession s = socket.getSession();
                if (!hv.verify(hostName, s)) {
                    Log.e(TAG, "Expected " + hostName + ", found " + s.getPeerPrincipal());
                    throw new SSLHandshakeException("Expected " + hostName + ", found " + s.getPeerPrincipal());
                } else {
                    Log.i(TAG, "Success");
                }
            }
        } catch (SSLHandshakeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void setInited(boolean inited) {
        isInited = inited;
    }


    /**
     * ================心跳逻辑=================
     * 1、每隔一分钟发一条 -1到服务端通信，服务端返回一个-1过来。
     * 2、客户端超过3分钟收不到-1，并且页没有tcp连接数据过来。就重新连接
     * 3、加了心跳机制后，本地的NettyTimeOutHandler 不再重连
     * 4、后台超过五分钟没有收到-1的心跳，会清理掉没用的长链接。
     */
    /**
     * 发送心跳数据
     */
    public void writeHeart() {
        if (wsClient == null)
            return;
    }

    /**
     * 开启心跳
     */
    public void startHeart() {
        Log.d(TAG, "开启心跳");

    }

    /**
     * 停止心跳
     */
    public void stopHeart() {
    }


    /**
     * 处理轮询信息
     *
     * @param msg
     */
    public void handleMessage(@NonNull Message msg) {

    }


    private static class WebHandler extends Handler {
        private WeakReference<HoldWSSocketImpl> weakReference;

        public WebHandler() {
            super(Looper.getMainLooper());
        }

        public void setSocketImpl(HoldWSSocketImpl socketImpl) {
            weakReference = new WeakReference<>(socketImpl);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (weakReference == null) {
                return;
            }
            HoldWSSocketImpl socketImpl = weakReference.get();
            if (socketImpl == null) {
                return;
            }
            socketImpl.handleMessage(msg);
        }
    }

    public long getLasteTcpTime() {
        return lasteTcpTime;
    }

    public void setLasteTcpTime(long lasteTcpTime) {
        this.lasteTcpTime = lasteTcpTime;
    }

    public long getLasteTcpResTime() {
        return lasteTcpResTime;
    }

    public void setLasteTcpResTime(long lasteTcpResTime) {
        this.lasteTcpResTime = lasteTcpResTime;
    }

    /**
     * 启用http刷新
     */
    public void sendNetRefeshEvent(String tag) {

    }

    /**
     * 处理链接成功后重新发送code请求
     */
    public void connectSuccessSendCode() {

    }

    public void setSocketIndex(int index) {
        this.mIndex = index;
    }

    public int getSocketIndex() {
        return mIndex;
    }

    private void reconnectWsSocket() {
        reStart();
    }

    /**
     * 发送重连
     */
    public void sendErrorReconnect(int conStatus) {
        sendErrorReconnect(conStatus, "");
    }

    public void sendErrorReconnect(int conStatus, String wsRemark) {

    }

    /**
     * 连接成功发送，用来统计时间
     */
    public void sendReconnectSuccess() {

    }

    public Integer getIsFirstConnect() {
        return isFirstConnect;
    }

    public void setIsFirstConnect(Integer isFirstConnect) {
        this.isFirstConnect = isFirstConnect;
    }
}
