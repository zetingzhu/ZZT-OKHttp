package com.example.zzt.okhttp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.zzt.okhttp.entity.BaikelObj
import com.example.zzt.okhttp.entity.GaodeWeather
import com.example.zzt.okhttp.entity.MyWeather
import com.example.zzt.okhttp.entity.Repo
import com.example.zzt.okhttp.net.BaiduHelper
import com.example.zzt.okhttp.net.GaodeHelper
import com.example.zzt.okhttp.net.GithubHelper
import com.example.zzt.okhttp.net.RetrofitUtils
import com.example.zzt.okhttp.net.WeatherHelper
import com.example.zzt.okhttp.net.factoryV3.ApiEmptyResponse
import com.example.zzt.okhttp.net.factoryV3.ApiErrorResponse
import com.example.zzt.okhttp.net.factoryV3.ApiSuccessResponse
import com.example.zzt.okhttp.net.factoryv2.BaseResponseV2
import com.zzt.adapter.StartActivityRecyclerAdapter
import com.zzt.entity.StartActivityDao
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class ZtMainActivity : AppCompatActivity() {
    val TAG = ZtMainActivity::class.java.simpleName
    var rv_list: RecyclerView? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zt_main)
        initView()
    }

    private fun initView() {
        rv_list = findViewById(R.id.rv_list)

        val mListDialog: MutableList<StartActivityDao> = ArrayList()
        mListDialog.add(StartActivityDao("天气，北京", " ", "0"))
        mListDialog.add(StartActivityDao("天气，上海", " ", "1"))
        mListDialog.add(StartActivityDao("天气，襄阳", " ", "2"))
        mListDialog.add(StartActivityDao("调用自己责任链", " ", "3"))
        mListDialog.add(StartActivityDao("百度百科-LiveDataFactoryV3", "", "4"))
        mListDialog.add(StartActivityDao("百度百科-gson", "", "5"))
        mListDialog.add(StartActivityDao("百度百科-LiveDataFactoryV4", "", "6"))
        mListDialog.add(StartActivityDao("百度百科-LiveDataFactoryV2", "", "7"))
        mListDialog.add(StartActivityDao("Github 列表请求", "", "8"))
        mListDialog.add(StartActivityDao("Github 列表请求-okhttp", "", "9"))
        mListDialog.add(StartActivityDao("高德天气", "", "10"))


        StartActivityRecyclerAdapter.setAdapterData(
            rv_list,
            RecyclerView.VERTICAL,
            mListDialog
        ) { itemView: View?, position: Int, data: StartActivityDao ->
            when (data.arouter) {
                "10" -> {
                    val queryWeather =
                        GaodeHelper.getInstance().gitHubApi.queryWeatherLives("310000")
                    queryWeather.enqueue(object : Callback<GaodeWeather> {
                        override fun onResponse(
                            p0: Call<GaodeWeather>,
                            p1: Response<GaodeWeather>
                        ) {
                            Log.d(TAG, "GaodeHelper queryWeather : ${p1.body()}")
                        }

                        override fun onFailure(p0: Call<GaodeWeather>, p1: Throwable) {
                        }
                    })

                }

                "9" -> {
                    val okHttpClient = RetrofitUtils.getInstance().getOkHttpClient()
                    val request: Request = Request.Builder()
                        .url("https://baike.baidu.com/api/openapi/BaikeLemmaCardApi?appid=379020&bk_key=github")
                        .build()
                    okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
                        override fun onFailure(call: okhttp3.Call, e: IOException) {
                            Log.d(TAG, "OkHttpClient  onFailure: $e")
                        }

                        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                            Log.d(TAG, "OkHttpClient onResponse: $response")
                        }
                    })
                }

                "8" -> {
                    val repos = GithubHelper.getInstance().gitHubApi.listRepos("zetingzhu")
                    repos.enqueue(object : Callback<List<Repo>> {
                        override fun onResponse(
                            call: Call<List<Repo>>,
                            response: Response<List<Repo>>
                        ) {
                            println("response-->${response.body()?.get(0)?.name}")
                        }

                        override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                        }
                    })
                }


                "0" -> {
                    val mLiveData = WeatherHelper.getInstance().gitHubApi.getWeatherBeijin("北京")
                    mLiveData.observe(this, Observer {
                        Log.d(TAG, ">$it")
                    })
                }

                "1" -> {
                    val mLiveData = WeatherHelper.getInstance().gitHubApi.getWeatherShangHai("上海")
                    mLiveData.observe(this, Observer {
                        Log.d(TAG, ">$it")
                    })
                }

                "2" -> {
                    val weather = WeatherHelper.getInstance().gitHubApi.getWeather("襄阳")
                    weather.enqueue(object : retrofit2.Callback<MyWeather> {
                        override fun onResponse(
                            call: Call<MyWeather>,
                            response: Response<MyWeather>
                        ) {
                            Log.d(TAG, "onResponse：" + response.body())
                        }

                        override fun onFailure(call: Call<MyWeather>, t: Throwable) {

                        }
                    })
                }

                "4" -> {
                    val queryTitle = BaiduHelper.getInstance().gitHubApi.queryTitle("上海")
                    queryTitle.observe(this, Observer { apiResponse ->
                        when (apiResponse) {
                            is ApiSuccessResponse -> {
                                val user = apiResponse.body
                                Log.d(TAG, "API_SUCCESS : $user")
                            }

                            is ApiErrorResponse -> {
                                val errorMessage = apiResponse.errorMessage
                                Log.e(TAG, "API_ERROR : $errorMessage")
                            }

                            is ApiEmptyResponse -> {
                                Log.d(TAG, "API_EMPTY")
                            }
                        }
                    })
                }

                "5" -> {
                    val queryTitle = BaiduHelper.getInstance().gitHubApi.queryTitleCall("北京")
                    queryTitle.enqueue(object : retrofit2.Callback<BaikelObj> {
                        override fun onResponse(p0: Call<BaikelObj>, p1: Response<BaikelObj>) {
                            Log.d(TAG, "onResponse：" + p1.body())
                        }

                        override fun onFailure(p0: Call<BaikelObj>, p1: Throwable) {
                            Log.d(TAG, "onFailure：" + p1)
                        }
                    })
                }

                "6" -> {
                    val queryTitle = BaiduHelper.getInstance().gitHubApi.queryTitleV4("北京")
                    queryTitle.observe(this, object : Observer<BaseResponseV2<BaikelObj>> {
                        override fun onChanged(value: BaseResponseV2<BaikelObj>) {

                            Log.d(TAG, "request queryTitleV4 ：" + value)
                        }
                    })
                }

                "7" -> {
                    val queryTitle = BaiduHelper.getInstance().gitHubApi.queryTitleV2("北京")
                    queryTitle.observe(this, object : Observer<BaikelObj> {
                        override fun onChanged(value: BaikelObj) {
                            Log.d(TAG, "request queryTitleV2 ：" + value)
                        }
                    })
                }
            }
        }
    }
}

