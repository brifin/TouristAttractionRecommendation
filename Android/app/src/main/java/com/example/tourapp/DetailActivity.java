package com.example.tourapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;

public class DetailActivity extends AppCompatActivity {

    private LinearLayout linearLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        linearLayout = (LinearLayout) findViewById(R.id.detail_linearlayout);
        getWindow().setBackgroundDrawable(new BitmapDrawable());


    }
}