package com.example.tourapp.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.tourapp.viewAndItem.RecordItem;

public class TourDetailAdapter extends ArrayAdapter<RecordItem> {
    public TourDetailAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
