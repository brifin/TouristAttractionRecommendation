package com.example.tourapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tourapp.R;
import com.example.tourapp.viewAndItem.ItemGroup;


public class UserFragment extends Fragment implements View.OnClickListener {


    public UserFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        TextView tv_nickname = view.findViewById(R.id.tv_nickname);
        TextView tv_username = view.findViewById(R.id.tv_username);
        ItemGroup ig_id = view.findViewById(R.id.ig_id);
        ItemGroup ig_nickname = view.findViewById(R.id.ig_nickname);
        ItemGroup ig_arrive = view.findViewById(R.id.ig_arrive);
        ItemGroup ig_like = view.findViewById(R.id.ig_like);
        /**id通过后端查询返回设置
         //TODO
         ig_id.getContentEdt().setText(?);
         */
        ig_nickname.setOnClickListener(this);
        ig_arrive.setOnClickListener(this);
        ig_like.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ig_arrive:
                break;
            case R.id.ig_like:
                break;
            case R.id.ig_nickname:
                break;
        }
    }
}