package com.example.zzt.okhttp.net;

import androidx.lifecycle.LiveData;

import com.example.zzt.okhttp.entity.BaikelObj;
import com.example.zzt.okhttp.entity.MyWeather;
import com.example.zzt.okhttp.net.factoryV3.ApiResponseV3;
import com.example.zzt.okhttp.net.factoryv2.BaseResponseV2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author: zeting
 * @date: 2023/9/14
 */
public interface BaiduApi {

    /**
     * 百度百科接口
     * http://baike.baidu.com/api/openapi/BaikeLemmaCardApi?scope=103&format=json&appid=379020&bk_key=github&bk_length=600
     */
    @GET("openapi/BaikeLemmaCardApi?scope=103&format=json&appid=379020&bk_length=600")
    LiveData<ApiResponseV3<BaikelObj>> queryTitle(@Query("bk_key") String queryStr);

    @GET("openapi/BaikeLemmaCardApi?scope=103&format=json&appid=379020&bk_length=600")
    LiveData<BaikelObj> queryTitleV2(@Query("bk_key") String queryStr);


    @GET("openapi/BaikeLemmaCardApi?scope=103&format=json&appid=379020&bk_length=600")
    Call<BaikelObj> queryTitleCall(@Query("bk_key") String queryStr);

    @GET("openapi/BaikeLemmaCardApi?scope=103&format=json&appid=379020&bk_length=600")
//    LiveData<BaseResponseV2<BaikelObj>> queryTitleV4(@Query("bk_key") String queryStr);
    LiveData<BaseResponseV2<BaikelObj>> queryTitleV4(@Query("bk_key") String queryStr);

}