package com.zzt.zt_interceptor.code;

// 请求头拦截器
class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) {
        Request originalRequest = chain.request();
        Request newRequest = new Request(originalRequest.getUrl() + "?header=custom");
        System.out.println("HeaderInterceptor: Adding custom header to request");
        return chain.proceed(newRequest);
    }
}    