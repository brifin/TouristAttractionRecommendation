package com.example.tourapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.tourapp.R;
import com.example.tourapp.application.MyApplication;
import com.example.tourapp.viewAndItem.BrowseItem;

import java.util.List;

public class BrowseAdapter extends BaseAdapter {
    private List<BrowseItem> mBrowseData;

    public BrowseAdapter(List<BrowseItem> data) {
        mBrowseData = data;
    }

    @Override
    public int getCount() {
        return mBrowseData.size();
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
            viewholder.textViewPosition = convertView.findViewById(R.id.browse_item_position);
            viewholder.textViewTime = convertView.findViewById(R.id.browse_item_time);
            convertView.setTag(viewholder);
        } else {
            viewholder = (viewHolder) convertView.getTag();
        }
        //viewholder.imageView.setImageResource(mData.get(position).getPhoto());
        viewholder.textViewPlace.setText(mBrowseData.get(position).getPlace());
        viewholder.textViewTime.setText(mBrowseData.get(position).getTimestamp());

        String latStr;
        String lonStr;
        if(mBrowseData.get(position).getLatitude() >= 0) {
            latStr = String.format("%.2f° N", mBrowseData.get(position).getLatitude());
        }else {
            latStr = String.format("%.2f° S", -mBrowseData.get(position).getLatitude());
        }

        if(mBrowseData.get(position).getLongitude() >= 0) {
            lonStr = String.format("%.2f° E", mBrowseData.get(position).getLongitude());
        }else {
            lonStr = String.format("%.2f° W", -mBrowseData.get(position).getLongitude());
        }
        String tv_position = "(" + latStr + "," + lonStr + ")";
        viewholder.textViewPosition.setText(tv_position);

        return convertView;
    }

    public static class viewHolder {
        public ImageView imageView;
        public TextView textViewPlace;
        public TextView textViewTime;
        public TextView textViewPosition;
    }
}
