package com.example.zzt.okhttp.net.factoryV3


import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: zeting
 * @date: 2025/4/10
 *
 */
class LiveDataCallAdapterFactoryV3 : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class.java) {
            return null
        }
        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = getRawType(observableType)
        if (rawObservableType != ApiResponseV3::class.java) {
            throw IllegalArgumentException("Type must be a resource")
        }
        if (observableType !is ParameterizedType) {
            throw IllegalArgumentException("Resource must be parameterized")
        }
        val bodyType = getParameterUpperBound(0, observableType)
        return LiveDataCallAdapter<Any>(bodyType)
    }

    private class LiveDataCallAdapter<R>(private val responseType: Type) :
        CallAdapter<R, LiveData<ApiResponseV3<R>>> {

        override fun responseType() = responseType

        override fun adapt(call: Call<R>): LiveData<ApiResponseV3<R>> {
            return object : LiveData<ApiResponseV3<R>>() {
                private var started = AtomicBoolean(false)
                override fun onActive() {
                    super.onActive()
                    if (started.compareAndSet(false, true)) {
                        call.enqueue(object : retrofit2.Callback<R> {
                            override fun onResponse(
                                call: Call<R>,
                                response: retrofit2.Response<R>
                            ) {
                                postValue(ApiResponseV3.create(response))
                            }

                            override fun onFailure(call: Call<R>, throwable: Throwable) {
                                postValue(ApiResponseV3.create(throwable))
                            }
                        })
                    }
                }
            }
        }
    }
}