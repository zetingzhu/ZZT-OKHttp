package com.example.zzt.okhttp.net.factoryv1

import androidx.lifecycle.LiveData
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type
import retrofit2.CallAdapter.Factory
import java.lang.reflect.ParameterizedType

/**
 * @author: zeting
 * @date: 2023/9/14
 *
 */
class LiveDataCallAdapterFactoryV1(var creator: (Int, String, Any?) -> Any) : Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class.java) return null
        //获取第一个泛型类型
        val observableType =
            getParameterUpperBound(0, returnType as ParameterizedType)

        val rawType = getRawType(observableType)
        if (rawType != creator(0, "", null).javaClass) {
            throw IllegalArgumentException("type must be ApiResponse")
        }
        if (observableType !is ParameterizedType) {
            throw IllegalArgumentException("resource must be parameterized")
        }
        return LiveDataCallAdapterV2<Any>(observableType, creator)
    }
}