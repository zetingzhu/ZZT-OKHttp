package com.example.zzt.okhttp.net;

import android.util.Log;

import com.example.zzt.okhttp.BuildConfig;
import com.example.zzt.okhttp.net.factoryv2.LiveDataCallAdapterFactoryV2;

import java.util.concurrent.TimeUnit;

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
    private static final int DEFAULT_TIMEOUT = 20; // 20  SECONDS
    private static final int DEFAULT_READ_TIMEOUT = 30; // 20  SECONDS

    private static class InnerClass {
        private static final RetrofitUtils INSTANCE = new RetrofitUtils();
    }

    private RetrofitUtils() {
    }

    public static RetrofitUtils getInstance() {
        return InnerClass.INSTANCE;
    }


    public synchronized OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                .cookieJar(new CustomCookieJar())
//                .addInterceptor(new TokenInterceptor())
//                .addInterceptor(new PublicParamInterceptor())
        ;

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor);
        }

        return builder.build();
    }

    private synchronized Retrofit getRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // 失败
                .addCallAdapterFactory(new LiveDataCallAdapterFactoryV2()) // 成功
//                .addCallAdapterFactory(new LiveDataCallAdapterFactoryV3()) // 成功
//                .addCallAdapterFactory(new LiveDataCallAdapterFactoryV4()) // 成功
                .build();
    }

    public <T> T getApiService(String ShowUrl, Class<T> service) {
        Retrofit retrofit = getRetrofit(ShowUrl);
        return retrofit.create(service);
    }
}
