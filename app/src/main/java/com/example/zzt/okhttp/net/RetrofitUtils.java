package com.example.zzt.okhttp.net;

import android.util.Log;

import com.example.zzt.okhttp.net.factoryv1.LiveDataCallAdapterFactoryV1;
import com.example.zzt.okhttp.net.factoryv1.WanResponse;
import com.example.zzt.okhttp.net.factoryv2.LiveDataCallAdapterFactoryV2;

import java.util.concurrent.TimeUnit;

import kotlin.jvm.functions.Function3;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author: zeting
 * @date: 2023/9/14
 */
public class RetrofitUtils {
    private static final String TAG = RetrofitUtils.class.getSimpleName();

    private static class InnerClass {
        private static final RetrofitUtils INSTANCE = new RetrofitUtils();
    }

    private RetrofitUtils() {
    }

    public static RetrofitUtils getInstance() {
        return InnerClass.INSTANCE;
    }


    private synchronized OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            Log.e(TAG, message);
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                //添加log拦截器
                .addInterceptor(loggingInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public synchronized Retrofit getRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactoryV2())
                .build();
    }

    public <T> T getApiService(String ShowUrl, Class<T> service) {
        Retrofit retrofit = getRetrofit(ShowUrl);
        return retrofit.create(service);
    }
}
