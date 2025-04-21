package com.example.zzt.okhttp.net;

/**
 * @author: zeting
 * @date: 2023/9/14
 */
public class GaodeHelper {
    public final String BASE_SERVER_URL_WEATHER = "https://restapi.amap.com/v3/";

    private static class InnerClass {
        private static final GaodeHelper INSTANCE = new GaodeHelper();
    }

    private GaodeHelper() {
        gitHubApi = RetrofitUtils.getInstance().getApiService(BASE_SERVER_URL_WEATHER, GaodeApi.class);
    }

    public static GaodeHelper getInstance() {
        return InnerClass.INSTANCE;
    }

    private GaodeApi gitHubApi;

    public GaodeApi getGitHubApi() {
        return gitHubApi;
    }
}
