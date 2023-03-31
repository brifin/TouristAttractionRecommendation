package com.example.tourapp.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tourapp.R;
import com.example.tourapp.adapter.TourDetailAdapter;
import com.example.tourapp.data.DataResult;
import com.example.tourapp.data.Result;
import com.example.tourapp.httpInterface.GroupInterface;
import com.example.tourapp.httpInterface.UserInterface;
import com.example.tourapp.viewAndItem.RecordItem;
import com.google.gson.Gson;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
    private ImageView iv_ScatteredGroups;
    private TextView tv_introduce;
    private String[] introduce = {
            "遇见最美希腊——雅典+圣托里尼+梅黛奥拉天空之城六日游;希腊6日跟团游。",
            "冰与火之歌，冰岛六日五晚深度游;冰岛6日跟团游。",
            "7日纵贯拉普兰极光破冰之旅;瑞典+芬兰7日跟团游。",
            "北欧之珠四国六日游 ; 德国+丹麦+瑞典+芬兰+挪威6日跟团游。"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
        initTourDetailItems();
        iv_back = findViewById(R.id.iv_back);
        tv_introduce = findViewById(R.id.tv_introduce);
        iv_ScatteredGroups = findViewById(R.id.iv_ScatteredGroups);
        ListView listView = findViewById(R.id.list_item_detail);
        adapter = new TourDetailAdapter(this, R.layout.record_item, recordItemList);
        listView.setAdapter(adapter);
        iv_back.setOnClickListener(v -> {
            finish();
        });

        Random random = new Random();
        int nextInt = random.nextInt(3);
        tv_introduce.setText(introduce[nextInt]);

        hideStable();
    }

    private void initTourDetailItems() {
        //TODO 接受后端数据
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://47.107.38.208:8090/user/")
                .build();

        UserInterface userInterface = retrofit.create(UserInterface.class);
        Call<DataResult> dataResultCall = userInterface.tourGroup();
        dataResultCall.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                DataResult dataResult = response.body();
                Integer code = dataResult.getCode();
                Log.d("YANG", dataResult.getMsg());
                if (code == 200) {
                    String[] schedules = dataResult.getData();
                    Gson gson = new Gson();

                    for (String schedule : schedules) {
                        String json = gson.toJson(schedule);

                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                        Retrofit build = new Retrofit.Builder()
                                .addConverterFactory(GsonConverterFactory.create())
                                .baseUrl("http://121.37.67.235:8000/app01")
                                .build();

                        GroupInterface groupInterface = build.create(GroupInterface.class);
                        Call<String> stringCall = groupInterface.groupClass(requestBody);

                        stringCall.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String isScatteredGroups = response.body();
                                if (isScatteredGroups.contains("false")) {
                                    iv_ScatteredGroups.setImageResource(R.drawable.collection);
                                } else {
                                    iv_ScatteredGroups.setImageResource(R.drawable.uncollection);
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                System.out.println("请求失败！");
                                Log.e("YANG", t.getMessage());
                            }
                        });


                        String[] str = schedule.split("\\s+");
                        for (int i = 1; i < str.length; i++) {

                            String[] simple = str[i].split(",");
                            double latStart = 30;
                            double lonStart = 120;
                            Date nowDate = new Date();
                            long startTime = nowDate.getTime() - 2000 * 60 * 1000;
                            RecordItem recordItem;
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            String timeStr = simple[2];
                            long timeOffset = Long.parseLong(timeStr) * 60 * 1000;
                            long time = startTime + timeOffset;
                            Date date = new Date(time);
                            String formatTime = format.format(date);
                            double lat = latStart + Long.parseLong(simple[0]);
                            double lon = lonStart + Long.parseLong(simple[1]);
                            String latStr;
                            String lonStr;
                            if(lat >= 0) {
                                latStr = String.format("%.2f° N", lat);
                            }else {
                                latStr = String.format("%.2f° S", -lat);
                            }

                            if(lon >= 0) {
                                lonStr = String.format("%.2f° E", lon);
                            }else {
                                lonStr = String.format("%.2f° W", -lon);
                            }


                            String place = "(" + latStr + "," + lonStr + ")";
                            recordItem = new RecordItem(i, place, formatTime, Integer.parseInt(simple[3]));
                            recordItemList.add(recordItem);

                        }
                    }


                } else {
                    Toast.makeText(TourDetailActivity.this, "加载失败！", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {
                System.out.println("请求失败！");
                Log.e("YANG", t.getMessage());
            }
        });

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