package com.example.zzt.okhttp.net;

/**
 * @author: zeting
 * @date: 2023/9/14
 */
public class GithubHelper {
    public final String BASE_SERVER_URL_WEATHER = "https://api.github.com/";

    private static class InnerClass {
        private static final GithubHelper INSTANCE = new GithubHelper();
    }

    private GithubHelper() {
        gitHubApi = RetrofitUtils.getInstance().getApiService(BASE_SERVER_URL_WEATHER, GithubApi.class);
    }

    public static GithubHelper getInstance() {
        return InnerClass.INSTANCE;
    }

    private GithubApi gitHubApi;

    public GithubApi getGitHubApi() {
        return gitHubApi;
    }
}
