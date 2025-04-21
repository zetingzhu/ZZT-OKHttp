package com.example.zzt.okhttp.net;

/**
 * @author: zeting
 * @date: 2023/9/14
 */
public class BaiduHelper {
    public final String BASE_SERVER_URL_WEATHER = "https://baike.baidu.com/api/";

    private static class InnerClass {
        private static final BaiduHelper INSTANCE = new BaiduHelper();
    }

    private BaiduHelper() {
        gitHubApi = RetrofitUtils.getInstance().getApiService(BASE_SERVER_URL_WEATHER, BaiduApi.class);
    }

    public static BaiduHelper getInstance() {
        return InnerClass.INSTANCE;
    }

    private BaiduApi gitHubApi;

    public BaiduApi getGitHubApi() {
        return gitHubApi;
    }
}
