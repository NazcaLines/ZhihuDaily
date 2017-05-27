package com.binean.zhihudaily.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.binean.zhihudaily.R;
import com.binean.zhihudaily.model.Story;
import com.binean.zhihudaily.model.Theme;
import com.binean.zhihudaily.network.Net_utils;
import com.bumptech.glide.Glide;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 彬旭 on 2017/5/26.
 */

public class ThemeFragment extends BaseFragment {

    public static final String TAG = "PsyFragment";
    public static final String KEY = "THEME";

    final ItemAdapter adapter = new ItemAdapter();

    Observer<Theme> observer = new Observer<Theme>() {
        @Override
        public void onCompleted() {
            Log.d(TAG, "url complete.");
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(Theme theme) {
            Log.d(TAG, "onNext");
            stories = theme.getStories();
            adapter.setItems(stories);
        }
    };

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String number = getArguments().getString(KEY);
        mSubscription = Net_utils.getApi()
                .getTheme(number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override public View onCreateView(LayoutInflater layoutInflater, ViewGroup vg, Bundle bundle) {
        View v = super.onCreateView(layoutInflater, vg, bundle);
        mRecycler.setAdapter(adapter);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                //TODO refresh a new item.
                adapter.notifyDataSetChanged();
                mSwipe.setRefreshing(false);
            }
        });
        return v;
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        List<Story> items;

        @Override public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity())
                    .inflate(R.layout.item_recycler, parent, false);
            return new ItemHolder(v);
        }

        @Override public void onBindViewHolder(ItemHolder holder, int position) {
            Story item = items.get(position);
            holder.mText.setText(item.getTitle());
            if (item.hasImage()) {
                Glide.with(holder.mImage.getContext())
                        .load(item.getImages().get(0))
                        .into(holder.mImage);
            }
        }

        @Override public int getItemCount() {
            return items == null? 0: items.size();
        }

        public void setItems(List<Story> stories) {
            items = stories;
            notifyDataSetChanged();
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        TextView mText;
        ImageView mImage;

        ItemHolder(View itemView) {
            super(itemView);
            mText = (TextView)itemView.findViewById(R.id.item_title);
            mImage = (ImageView)itemView.findViewById(R.id.item_image);
        }
    }

}
