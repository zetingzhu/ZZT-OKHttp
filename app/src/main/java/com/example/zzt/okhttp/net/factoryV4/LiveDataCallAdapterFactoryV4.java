package com.example.zzt.okhttp.net.factoryV4;

import androidx.lifecycle.LiveData;

import com.example.zzt.okhttp.net.factoryv2.BaseResponseV2;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: zeting
 * @date: 2025/4/10
 * <p>
 * 失败了，不能用
 */
public class LiveDataCallAdapterFactoryV4 extends CallAdapter.Factory {
    @Override
    public CallAdapter<?, ?> get(Type returnType, java.lang.annotation.Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != LiveData.class) {
            return null;
        }
        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Type rawObservableType = getRawType(observableType);
        if (rawObservableType != BaseResponseV2.class) {
            throw new IllegalArgumentException("Type must be a BaseResponseV2");
        }
        if (!(observableType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("BaseResponseV2 must be parameterized");
        }
        Type bodyType = getParameterUpperBound(0, (ParameterizedType) observableType);
        return new LiveDataCallAdapterV4<>(bodyType);
    }

    private static final class LiveDataCallAdapterV4<R> implements CallAdapter<R, LiveData<BaseResponseV2<R>>> {
        private final Type responseType;

        public LiveDataCallAdapterV4(Type responseType) {
            this.responseType = responseType;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public LiveData<BaseResponseV2<R>> adapt(final Call<R> call) {
            return new LiveData<BaseResponseV2<R>>() {
                AtomicBoolean started = new AtomicBoolean(false);

                @Override
                protected void onActive() {
                    super.onActive();
                    if (started.compareAndSet(false, true)) {
                        call.enqueue(new retrofit2.Callback<R>() {
                            @Override
                            public void onResponse(Call<R> call, Response<R> response) {
                                R body = response.body();
                                if (response.isSuccessful() && body != null) {
                                    postValue(new BaseResponseV2<>(BaseResponseV2.CODE_SUCCESS, "", body));
                                } else {
                                    String errorMsg = response.message();
                                    postValue(new BaseResponseV2<>(BaseResponseV2.CODE_ERROR, errorMsg));
                                }
                            }

                            @Override
                            public void onFailure(Call<R> call, Throwable throwable) {
                                postValue(new BaseResponseV2<>(BaseResponseV2.CODE_ERROR, throwable.getMessage()));
                            }
                        });
                    }
                }
            };
        }
    }
}
