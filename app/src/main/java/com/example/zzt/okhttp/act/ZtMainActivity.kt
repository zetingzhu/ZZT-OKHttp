package com.example.zzt.okhttp.act

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.zzt.okhttp.R
import com.example.zzt.okhttp.net.WeatherHelper
import com.zzt.adapter.StartActivityRecyclerAdapter
import com.zzt.entity.StartActivityDao

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


        StartActivityRecyclerAdapter.setAdapterData(
            rv_list,
            RecyclerView.VERTICAL,
            mListDialog
        ) { itemView: View?, position: Int, data: StartActivityDao ->
            when (data.arouter) {
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
            }
        }
    }
}