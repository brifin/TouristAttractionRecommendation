package com.example.tourapp.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourapp.R;
import com.example.tourapp.viewAndItem.TourItem;

import java.util.List;

public class TourAdapter extends ArrayAdapter<TourItem> implements View.OnClickListener {
    private int resourceId;
    private InnerItemOnclickListener mListener;
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
            viewHolder.iv_collection = view.findViewById(R.id.iv_collection);
            viewHolder.tv_tourName = view.findViewById(R.id.tv_tourName);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.iv_collection.setImageResource(tourItem.getImageId());
        viewHolder.tv_tourName.setText(tourItem.getTourName());
        viewHolder.iv_collection.setOnClickListener(this);
        viewHolder.iv_collection.setTag(position);
        return view;
    }

    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }

    public final static class ViewHolder {
        ImageView iv_collection;
        TextView tv_tourName;
    }
    public interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    @Nullable
    @Override
    public TourItem getItem(int position) {
        return super.getItem(position);
    }

    public void setInnerItemOnclickListener(InnerItemOnclickListener listener) {
        this.mListener = listener;
    }


}
