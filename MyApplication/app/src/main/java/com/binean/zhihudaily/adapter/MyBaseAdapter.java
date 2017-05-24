package com.binean.zhihudaily.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.binean.zhihudaily.R;

import java.util.ArrayList;

/**
 * Created by 彬旭 on 2017/5/24.
 */

public class MyBaseAdapter extends RecyclerView.Adapter<MyBaseHolder>{

    public ArrayList<Integer> list;
    Context mContext;

    public MyBaseAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
        for (int i = 0; i < 50; ++i) {
            list.add(i);
        }
    }

    @Override
    public MyBaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_recycler, parent, false);
        return new MyBaseHolder(view);
    }

    @Override
    public void onBindViewHolder(MyBaseHolder holder, int position) {
        holder.mText.setText(String.valueOf(list.get(position)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class MyBaseHolder extends RecyclerView.ViewHolder {
    TextView mText;

    public MyBaseHolder(View itemView) {
        super(itemView);
        mText = (TextView)itemView.findViewById(R.id.textView);
    }
}