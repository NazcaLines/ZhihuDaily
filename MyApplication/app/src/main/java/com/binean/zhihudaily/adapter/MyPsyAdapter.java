package com.binean.zhihudaily.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.binean.zhihudaily.R;

import java.util.ArrayList;

/**
 * Created by 彬旭 on 2017/5/24.
 */

public class MyPsyAdapter extends RecyclerView.Adapter<MyPsyHolder>{
    ArrayList<Integer> list;
    Context mContext;

    public MyPsyAdapter(Context context) {
        mContext = context;
        list = new ArrayList<>();
        for (int i = -1; i > -50; --i) {
            list.add(i);
        }
    }

    @Override public MyPsyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_test, parent, false);
        return new MyPsyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyPsyHolder holder, int position) {
        holder.mText.setText(String.valueOf(list.get(position)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class MyPsyHolder extends RecyclerView.ViewHolder {
    TextView mText;
    ImageView mImage;

    public MyPsyHolder(View itemView) {
        super(itemView);
        mText = (TextView) itemView.findViewById(R.id.item_title);
        mImage = (ImageView) itemView.findViewById(R.id.item_image);
    }
}
