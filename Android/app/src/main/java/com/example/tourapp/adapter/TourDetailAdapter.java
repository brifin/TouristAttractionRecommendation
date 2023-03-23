package com.example.tourapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tourapp.R;
import com.example.tourapp.viewAndItem.RecordItem;
import com.example.tourapp.viewAndItem.TourItem;

import java.util.List;

public class TourDetailAdapter extends ArrayAdapter<RecordItem> {
    private int resourceId;

    public TourDetailAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<RecordItem> objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RecordItem recordItem = getItem(position);
        View view;
        final ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_id = view.findViewById(R.id.tv_id);
            viewHolder.tv_place = view.findViewById(R.id.tv_place);
            viewHolder.tv_time = view.findViewById(R.id.tv_time);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_id.setText(recordItem.getId()+"");
        viewHolder.tv_place.setText(recordItem.getPlace());
        viewHolder.tv_time.setText(recordItem.getTime());

        return view;
    }

    @Nullable
    @Override
    public RecordItem getItem(int position) {
        return super.getItem(position);
    }

    public final class ViewHolder {
        TextView tv_id;
        TextView tv_place;
        TextView tv_time;
    }
}
