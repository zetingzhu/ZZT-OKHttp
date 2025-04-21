package com.zzt.zt_interceptor.code;

// 责任链类
class RealChain implements Interceptor.Chain {
    private Request request;
    private Interceptor[] interceptors;
    private int index;

    public RealChain(Request request, Interceptor[] interceptors, int index) {
        this.request = request;
        this.interceptors = interceptors;
        this.index = index;
    }

    @Override
    public Request request() {
        return request;
    }

    @Override
    public Response proceed(Request request) {
        if (index >= interceptors.length) {
            throw new AssertionError("No more interceptors in the chain");
        }
        RealChain next = new RealChain(request, interceptors, index + 1);
        Interceptor interceptor = interceptors[index];
        return interceptor.intercept(next);
    }
}    