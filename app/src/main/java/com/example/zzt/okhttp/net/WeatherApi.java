package com.example.zzt.okhttp.net;

import android.database.Observable;

import androidx.lifecycle.LiveData;

import com.example.zzt.okhttp.entity.MyWeather;
import com.example.zzt.okhttp.net.factoryv2.BaseResponseV2;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author: zeting
 * @date: 2023/9/14
 */
public interface WeatherApi {

    // http://api.map.baidu.com/telematics/v3/weather?location=上海&output=json&ak=5slgyqGDENN7Sy7pw29IUvrZ

    /**
     * 天气预报
     */
    // https://search.heweather.com/find?location=北京&key=4b61a68895b149f1a5ea53fe43782e17
    @GET("find?key=4b61a68895b149f1a5ea53fe43782e17")
    LiveData<BaseResponseV2<MyWeather>> getWeatherBeijin(@Query("location") String location);

    @GET("find?key=4b61a68895b149f1a5ea53fe43782e17")
    LiveData< MyWeather>  getWeatherShangHai(@Query("location") String location);

    /**
     * 天气预报
     */
    //https://search.heweather.com/top?group=cn&key=4b61a68895b149f1a5ea53fe43782e17&number=20
    @GET("top?key=4b61a68895b149f1a5ea53fe43782e17")
    LiveData<MyWeather> getWeatherGroup(@Query("group") String group, @Query("number") String number);


}