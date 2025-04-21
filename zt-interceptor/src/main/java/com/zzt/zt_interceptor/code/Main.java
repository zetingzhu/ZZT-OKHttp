package com.zzt.zt_interceptor.code;

// 主类
public class Main {
    public static void main(String[] args) {
        Request request = new Request("https://example.com");
        Interceptor[] interceptors = {new LoggingInterceptor(), new HeaderInterceptor(), new FinalInterceptor()};
        RealChain chain = new RealChain(request, interceptors, 0);
        Response response = chain.proceed(request);
        System.out.println("Final response status code: " + response.getStatusCode());
        System.out.println("Final response body: " + response.getBody());
    }
}    