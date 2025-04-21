package com.example.zzt.okhttp.net.factoryv2;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactoryV2 extends CallAdapter.Factory {

    private static final String TAG = LiveDataCallAdapterFactoryV2.class.getSimpleName();

    @SuppressWarnings("ClassGetClass")
    @Nullable
    @Override
    public CallAdapter<?, ?> get(@NotNull Type returnType, @NotNull Annotation[] annotations, @NotNull Retrofit retrofit) {
        if (getRawType(returnType) != LiveData.class) {
            return null;
        }
        //获取第一个泛型类型
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class<?> rawType = getRawType(observableType);
        Log.d(TAG, "rawType = " + rawType.getClass().getSimpleName());
        boolean isApiResponse = true;
        if (rawType != BaseResponseV2.class) {
            //不是返回ApiResponse类型的返回值
            isApiResponse = false;
        }
        if (observableType instanceof ParameterizedType) {
            throw new IllegalArgumentException("resource must be parameterized");
        }
        return new LiveDataCallAdapterV2<>(observableType, isApiResponse);
    }

    private static class LiveDataCallAdapterV2<T> implements CallAdapter<T, LiveData<T>> {

        private Type mResponseType;
        private boolean isApiResponse;

        LiveDataCallAdapterV2(Type mResponseType, boolean isApiResponse) {
            this.mResponseType = mResponseType;
            this.isApiResponse = isApiResponse;
        }

        @NotNull
        @Override
        public Type responseType() {
            return mResponseType;
        }

        @NotNull
        @Override
        public LiveData<T> adapt(@NotNull final Call<T> call) {
            return new MyLiveData<>(call, isApiResponse);
        }

    }

    private static class MyLiveData<T> extends LiveData<T> {

        private AtomicBoolean stared = new AtomicBoolean(false);
        private final Call<T> call;
        private boolean isApiResponse;

        MyLiveData(Call<T> call, boolean isApiResponse) {
            this.call = call;
            this.isApiResponse = isApiResponse;
        }

        @Override
        protected void onActive() {
            super.onActive();
            //确保执行一次
            if (stared.compareAndSet(false, true)) {
                call.enqueue(new Callback<T>() {
                    @Override
                    public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
                        T body = response.body();
                        postValue(body);
                    }

                    @Override
                    public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
                        if (isApiResponse) {
                            //noinspection unchecked
                            postValue((T) new BaseResponseV2<>(BaseResponseV2.CODE_ERROR, t.getMessage()));
                        } else {
                            postValue(null);
                        }
                    }
                });
            }
        }
    }
}