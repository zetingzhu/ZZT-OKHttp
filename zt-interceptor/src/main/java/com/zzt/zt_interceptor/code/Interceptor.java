package com.zzt.zt_interceptor.code;

// 拦截器接口
interface Interceptor {
    Response intercept(Chain chain);

    interface Chain {
        Request request();

        Response proceed(Request request);
    }
}    