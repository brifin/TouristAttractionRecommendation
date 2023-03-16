package com.example.tourapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

public class PlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        getWindow().setBackgroundDrawable(new BitmapDrawable());
    }
}