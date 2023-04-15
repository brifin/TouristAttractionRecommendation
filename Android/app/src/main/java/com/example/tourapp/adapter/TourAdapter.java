package com.example.tourapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tourapp.R;
import com.example.tourapp.viewAndItem.TourItem;

import java.util.List;

public class TourAdapter extends ArrayAdapter<TourItem>{
    private int resourceId;
    public TourAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<TourItem> objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TourItem tourItem = getItem(position);
        View view;
        final ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder  = new ViewHolder();
            viewHolder.tv_isScatteredGroups = view.findViewById(R.id.tv_isScatteredGroups);
            viewHolder.tv_tourName = view.findViewById(R.id.tv_tourName);
            viewHolder.iv_pic = view.findViewById(R.id.iv_pic);
            viewHolder.tv_positionName = view.findViewById(R.id.tv_positionName);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.iv_pic.setImageResource(tourItem.getImageId());
        viewHolder.tv_tourName.setText(tourItem.getTourName());
        viewHolder.tv_positionName.setText(tourItem.getPositionName());

        if(tourItem.isIsScatteredGroups()) {
            viewHolder.tv_isScatteredGroups.setText("散拼团");
        }else if(!tourItem.isIsScatteredGroups()) {
            viewHolder.tv_isScatteredGroups.setText("精品团");
        }
        //标记
        viewHolder.tv_isScatteredGroups.setTag(position);
        viewHolder.tv_positionName.setTag(position);
        viewHolder.tv_tourName.setTag(position);
        return view;
    }


    public final static class ViewHolder {
        ImageView iv_pic;
        TextView tv_isScatteredGroups;
        TextView tv_tourName;
        TextView tv_positionName;
    }

    @Nullable
    @Override
    public TourItem getItem(int position) {
        return super.getItem(position);
    }

}
