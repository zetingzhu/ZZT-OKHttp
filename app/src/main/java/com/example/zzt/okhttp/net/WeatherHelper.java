package com.example.zzt.okhttp.net;

/**
 * @author: zeting
 * @date: 2023/9/14
 */
public class WeatherHelper {
    public final String BASE_SERVER_URL_WEATHER = "https://search.heweather.com/";

    private static class InnerClass {
        private static final WeatherHelper INSTANCE = new WeatherHelper();
    }

    private WeatherHelper() {
        gitHubApi = RetrofitUtils.getInstance().getApiService(BASE_SERVER_URL_WEATHER, WeatherApi.class);
    }

    public static WeatherHelper getInstance() {
        return InnerClass.INSTANCE;
    }

    private WeatherApi gitHubApi;

    public WeatherApi getGitHubApi() {
        return gitHubApi;
    }
}
