package com.binean.zhihudaily.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.binean.zhihudaily.R;
import com.binean.zhihudaily.control.OnRecyclerViewItemClickListener;
import com.binean.zhihudaily.control.StoryClickListener;
import com.binean.zhihudaily.model.Story;
import com.binean.zhihudaily.model.Theme;
import com.binean.zhihudaily.network.NetUtils;
import com.bumptech.glide.Glide;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 彬旭 on 2017/5/26.
 */

public class ThemeFragment extends BaseFragment {

    public static final String TAG = "ThemeFragment";
    public static final String KEY = "THEME";

    final ItemAdapter adapter = new ItemAdapter(new StoryClickListener(getActivity()));

    Observer<Theme> observer = new Observer<Theme>() {
        @Override public void onCompleted() {
            adapter.notifyDataSetChanged();
            Log.d(TAG, "url complete.");
        }

        @Override public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override public void onNext(Theme theme) {
            Log.d(TAG, "onNext");
            stories = theme.getStories();
            adapter.setItems(stories);
        }
    };

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        observe();
    }

    @Override protected void observe() {
        String number = String.valueOf(getArguments().getInt(KEY, 2));
        mSubscription = NetUtils.getApi()
                .getTheme(number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override protected void loadMore() {}

    @Override public View onCreateView(LayoutInflater layoutInflater, ViewGroup vg, Bundle bundle) {
        View v = super.onCreateView(layoutInflater, vg, bundle);
        adapter.setClickListener(new StoryClickListener(getActivity()));
        mRecycler.setAdapter(adapter);

        return v;
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        List<Story> items;
        private OnRecyclerViewItemClickListener clickListener;

        public ItemAdapter(OnRecyclerViewItemClickListener listener) {
            clickListener = listener;
        }

        @Override public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity())
                    .inflate(R.layout.item_recycler, parent, false);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onItemClick(v, (String)v.getTag());
                    }
                }
            });
            return new ItemHolder(v, clickListener);
        }

        @Override public void onBindViewHolder(ItemHolder holder, int position) {
            Story item = items.get(position);
            holder.mText.setText(item.getTitle());
            holder.mCardView.setTag(String.valueOf(item.getId()));
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

        public void setClickListener(OnRecyclerViewItemClickListener listener) {
            clickListener = listener;
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        TextView mText;
        ImageView mImage;
        View mCardView;
        private OnRecyclerViewItemClickListener clickListener;

        ItemHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            mText = (TextView)itemView.findViewById(R.id.item_title);
            mImage = (ImageView)itemView.findViewById(R.id.item_image);
            mCardView = itemView;
            clickListener = listener;
        }
    }

    public static ThemeFragment createThemeFragment(int id) {
        ThemeFragment fragment = new ThemeFragment();
        Bundle fragmentId = new Bundle();
        fragmentId.putInt(KEY, id);
        fragment.setArguments(fragmentId);
        return fragment;
    }

}
