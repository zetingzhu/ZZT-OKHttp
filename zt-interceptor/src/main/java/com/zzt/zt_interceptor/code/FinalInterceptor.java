package com.zzt.zt_interceptor.code;

// 最终拦截器，负责发送请求并返回响应
class FinalInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) {
        Request request = chain.request();
        // 这里模拟发送请求并返回响应
        return new Response(200, "Response body for " + request.getUrl());
    }
}    