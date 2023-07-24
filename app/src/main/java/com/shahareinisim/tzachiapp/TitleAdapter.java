package com.shahareinisim.tzachiapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TitleAdapter extends BaseAdapter {

    public final ArrayList<TfilahTitlePart> mDataSource;
    private final LayoutInflater layoutInflater;
    ClickListener clickListener;

    public TitleAdapter(Activity activity, ArrayList<TfilahTitlePart> dataSource) {
        this.mDataSource = dataSource;
        this.layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public TfilahTitlePart getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.tfilah_part, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.text);
            holder.tvTitle.setPadding(10, 20, 10, 20);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // bind data
        holder.tvTitle.setText(getItem(position).getTitle());

        holder.tvTitle.setOnClickListener(v -> clickListener.onPostClick(getItem(position).getPosition()));

        return convertView;
    }

    public static class ViewHolder {
        private TextView tvTitle;
    }

    public interface ClickListener {
        void onPostClick(int position);
    }

    public void setPostClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

}