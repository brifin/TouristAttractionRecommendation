package com.example.tourapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;


import com.example.tourapp.R;
import com.example.tourapp.adapter.TourDetailAdapter;
import com.example.tourapp.httpInterface.GroupInterface;
import com.example.tourapp.viewAndItem.RecordItem;
import com.google.gson.Gson;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TourDetailActivity extends AppCompatActivity {

    private List<RecordItem> recordItemList = new ArrayList<>();
    private TourDetailAdapter adapter;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
        initTourDetailItems();
        iv_back = findViewById(R.id.iv_back);
        ListView listView = findViewById(R.id.list_item_detail);
        adapter = new TourDetailAdapter(this,R.layout.record_item,recordItemList);
        listView.setAdapter(adapter);
        iv_back.setOnClickListener(v->{
            finish();
        });

        hideStable();
    }

    private void initTourDetailItems() {
        //TODO 接受后端数据
        /*Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://121.37.67.235:8000/app01/")
                .build();

        GroupInterface groupInterface = retrofit.create(GroupInterface.class);
        Gson gson = new Gson();
        String schedule = "0,0,0,6\t-49.907573783658734,-49.74057486206787,30.10983,6\t98.58553813248184,94.92306076321475,365.77519,6\t-19.641496057187915,-24.26275838053216,310.10032,6\t67.93334218344029,62.63711840489767,455.50418,6\t-72.03732480156616,-79.66057121686764,447.70617,6\t-54.09974075229123,-61.770180501988584,596.76486,6\t-104.05643513479322,-112.63712528678491,650.18333,2.735627336284759\t-30.30506792076895,-40.86986225386075,767.42693,2.7749609457733504\t99.27609871046671,91.04705662935157,1050.89679,2.7749609457733504\t-39.878962888214936,-47.44792492652744,941.86509,7.150372263114527\t57.27893519975369,49.935735258305144,1129.5734,7.150372263114527\t8.945312973919993,-0.354594790617341,1162.86274,4.185603998442407\t-36.17305370122579,-45.325826837036,1129.92327,5.534236316565537\t156.72587976552683,148.95395044973043,1463.61313,5.534236316565537\t251.44479344010819,243.8237146670662,1770.77602,5.534236316565537\t-13.859306232714022,-20.425890849426853,1558.40834,5.534236316565537\t18.67770538020646,10.546885129123158,1755.1179,5.534236316565537\t-16.39518316874273,-22.0070551211546,1816.38743,5.534236316565537\t8.795778080987482,0.8978487472492751,1953.65626,8.297147178851391";

        String json = gson.toJson(schedule);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Call<String> stringCall = groupInterface.groupClass(requestBody);

        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("请求失败！");
                Log.d("TAG",t.getMessage().toString());
            }
        });*/

        String time,place;
        RecordItem recordItem;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for(int i = 1;i < 18;++i) {
            Date nowDate = new Date();
            time = format.format(nowDate);
            int lat = 19 + i;
            int lon = 16 + i;
            place = "(" + lat + "," + lon + ")";
            recordItem = new RecordItem(i,place,time,6+i);
            recordItemList.add(recordItem);
        }
    }
    //隐藏状态栏
    public void hideStable() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ImmersionBar.with(this)
                .transparentBar()
                .statusBarDarkFont(true)
                .statusBarAlpha(0.0f)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
    }
}