package com.example.tourapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tourapp.R;
import com.example.tourapp.application.MyApplication;
import com.example.tourapp.viewAndItem.BrowseItem;

import java.util.List;

public class BrowseAdapter extends BaseAdapter {
    private List<BrowseItem> mData;

    public BrowseAdapter(List<BrowseItem> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder viewholder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.browse_item_layout2, parent, false);
            viewholder = new viewHolder();
            viewholder.imageView = convertView.findViewById(R.id.browse_item_iv);
            viewholder.textViewPlace = convertView.findViewById(R.id.browse_item_place);
            viewholder.textViewTime = convertView.findViewById(R.id.browse_item_time);
            convertView.setTag(viewholder);
        } else {
            viewholder = (viewHolder) convertView.getTag();
        }
        viewholder.imageView.setImageResource(mData.get(position).getPhoto());
        viewholder.textViewPlace.setText(mData.get(position).getPlace());
        viewholder.textViewTime.setText(mData.get(position).getTime());
        return convertView;
    }

    public static class viewHolder {
        public ImageView imageView;
        public TextView textViewPlace;
        public TextView textViewTime;
    }
}
