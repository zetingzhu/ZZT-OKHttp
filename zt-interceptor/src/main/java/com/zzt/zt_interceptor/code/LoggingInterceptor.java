package com.zzt.zt_interceptor.code;

// 日志拦截器
class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) {
        Request request = chain.request();
        System.out.println("LoggingInterceptor: Sending request to " + request.getUrl());
        Response response = chain.proceed(request);
        System.out.println("LoggingInterceptor: Received response with status code " + response.getStatusCode());
        return response;
    }
}    