package com.example.zzt.okhttp.net;

import com.example.zzt.okhttp.entity.Repo;
import com.example.zzt.okhttp.entity.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author: zeting
 * @date: 2023/9/14
 */
public interface GithubApi {

    @GET("users/{user}/repos")
    Call<List<Repo>> listRepos(@Path("user") String user);


    @GET("group/{id}/users")
    Call<List<User>> groupList(@Path("id") int groupId);

}