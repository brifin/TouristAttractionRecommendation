package com.example.tourapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tourapp.R;
import com.example.tourapp.application.MyApplication;
import com.example.tourapp.viewAndItem.LoveItem;

import java.util.List;

public class LoveAdapter extends BaseAdapter {
    private List<LoveItem> mdata;

    public LoveAdapter(List<LoveItem> data) {
        mdata = data;
    }

    @Override
    public int getCount() {
        return mdata.size();
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.love_item_layout, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.love_item_iv);
            holder.textViewPlace = (TextView) convertView.findViewById(R.id.love_item_place);
            holder.textViewTime = (TextView) convertView.findViewById(R.id.love_item_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageView.setImageResource(mdata.get(position).getPhoto());
        holder.textViewPlace.setText(mdata.get(position).getPlace());
        holder.textViewTime.setText(mdata.get(position).getTime());
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textViewPlace;
        TextView textViewTime;
    }
}
