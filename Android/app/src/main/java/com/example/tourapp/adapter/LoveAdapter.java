package com.example.tourapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.UiThread;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.tourapp.R;
import com.example.tourapp.application.MyApplication;
import com.example.tourapp.viewAndItem.LoveItem;

import java.util.List;

public class LoveAdapter extends BaseAdapter {
    private List<LoveItem> mData;

    public LoveAdapter(List<LoveItem> data) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.love_item_layout, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.love_item_iv);
            holder.textViewPlace = (TextView) convertView.findViewById(R.id.love_item_place);
            holder.textViewTime = (TextView) convertView.findViewById(R.id.love_item_time);
            holder.textViewPosition = (TextView) convertView.findViewById(R.id.love_item_position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageView.setImageResource(mData.get(position).getPhoto());
        holder.textViewPlace.setText(mData.get(position).getPlace());
        holder.textViewTime.setText(mData.get(position).getTimestamp());

        String latStr;
        String lonStr;
        if (mData.get(position).getLatitude() >= 0) {
            latStr = String.format("%.2f° N", mData.get(position).getLatitude());
        } else {
            latStr = String.format("%.2f° S", -mData.get(position).getLatitude());
        }

        if (mData.get(position).getLongitude() >= 0) {
            lonStr = String.format("%.2f° E", mData.get(position).getLongitude());
        } else {
            lonStr = String.format("%.2f° W", -mData.get(position).getLongitude());
        }
        String tv_position = "(" + latStr + "," + lonStr + ")";
        holder.textViewPosition.setText(tv_position);

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textViewPlace;
        TextView textViewTime;
        TextView textViewPosition;
    }
}
