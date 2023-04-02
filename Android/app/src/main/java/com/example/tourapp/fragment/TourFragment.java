package com.example.tourapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tourapp.R;
import com.example.tourapp.activity.TourDetailActivity;
import com.example.tourapp.adapter.TourAdapter;
import com.example.tourapp.application.MyApplication;
import com.example.tourapp.data.DataResult;
import com.example.tourapp.data.GroupResult;
import com.example.tourapp.data.MySchedule;
import com.example.tourapp.httpInterface.GroupInterface;
import com.example.tourapp.httpInterface.UserInterface;
import com.example.tourapp.viewAndItem.TourItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TourFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private List<TourItem> tourItemList = new ArrayList<>();
    private TourAdapter tourAdapter;
    private ImageView iv_back3;
    private int i;

    public View view;

    public TourFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tour, container, false);
        initTourItems();
        return view;
    }

    private void initTourItems() {
        //TODO 接受后端数据
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://47.107.38.208:8090/user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserInterface userInterface = retrofit.create(UserInterface.class);
        Call<DataResult> dataResultCall = userInterface.tourGroup();
        dataResultCall.enqueue(new Callback<DataResult>() {
            @Override
            public void onResponse(Call<DataResult> call, Response<DataResult> response) {
                DataResult dataResult = response.body();
                if (dataResult != null) {
                    long code = dataResult.getCode();
                    Log.d("YANG", dataResult.getMsg());
                    if (code == 200) {
                        i = 1;
                        String[] schedules = dataResult.getData();
                        Gson gson = new Gson();

                        for (String schedule : schedules) {
                            MySchedule mySchedule = new MySchedule(schedule.trim());
                            String json = gson.toJson(mySchedule);

                            //System.out.println(json);
                            //System.out.println();

                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                            Retrofit build = new Retrofit.Builder()
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .baseUrl("http://121.37.67.235:8000/app01/")
                                    .build();

                            GroupInterface groupInterface = build.create(GroupInterface.class);
                            Call<GroupResult> GroupCall = groupInterface.groupClass(requestBody);

                            GroupCall.enqueue(new Callback<GroupResult>() {

                                @Override
                                public void onResponse(Call<GroupResult> call, Response<GroupResult> response) {
                                    Boolean isScatteredGroups = response.body().getScatteredGroups();
                                    String tourName = "旅游团" + i++;
                                    TourItem tourItem;
                                    if (!isScatteredGroups) {
                                        tourItem = new TourItem(tourName, R.drawable.collection, schedule, false);
                                    } else {
                                        tourItem = new TourItem(tourName, R.drawable.uncollection, schedule, true);
                                    }

                                    tourItemList.add(tourItem);
                                    tourAdapter = new TourAdapter(MyApplication.getContext(), R.layout.tour_item, tourItemList);
                                    iv_back3 = view.findViewById(R.id.iv_back3);
                                    ListView list_view = view.findViewById(R.id.list_view);
                                    list_view.setAdapter(tourAdapter);

                                    list_view.setOnItemClickListener(TourFragment.this);
                                    iv_back3.setOnClickListener(TourFragment.this);

                                }

                                @Override
                                public void onFailure(Call<GroupResult> call, Throwable t) {
                                    System.out.println("请求失败!");
                                    Log.e("YANG", "app01" + t.getMessage());
                                }

                            });
                        }

                    } else {
                        Toast.makeText(MyApplication.getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResult> call, Throwable t) {
                System.out.println("请求失败！");
                Log.e("YANG", "user:" + t.getMessage());
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), TourDetailActivity.class);
        intent.putExtra("schedule", tourItemList.get(position).getSchedule());
        System.out.println("###" + tourItemList.get(position).getSchedule());
        intent.putExtra("isScatteredGroups", tourItemList.get(position).isIsScatteredGroups());
        //valueListener.sendSchedule(tourItemList.get(position).getSchedule());
        //valueListener.sendIsScatteredGroups(tourItemList.get(position).isIsScatteredGroups());

        intent.putExtra("isScatteredGroups",tourItemList.get(position).isIsScatteredGroups());

        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back3) {
            Toast.makeText(getActivity(), "退出应用", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }
}