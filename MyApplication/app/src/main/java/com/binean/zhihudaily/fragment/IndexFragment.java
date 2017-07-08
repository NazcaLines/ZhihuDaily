package com.binean.zhihudaily.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.binean.zhihudaily.R;
import com.binean.zhihudaily.control.OnRecyclerViewItemClickListener;
import com.binean.zhihudaily.control.StoryClickListener;
import com.binean.zhihudaily.model.Lastest;
import com.binean.zhihudaily.model.Story;
import com.binean.zhihudaily.model.TopStory;
import com.binean.zhihudaily.network.NetUtils;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 彬旭 on 2017/5/24.
 */

public class IndexFragment extends BaseFragment {

    public static final String TAG = "IndexFragment";
    public static IndexFragment Singleton;

    Calendar mDay = Calendar.getInstance();
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    int mHeaderIndex = 1;
    Subscription mOb;

    List<TopStory> topStories;

    final ItemIndexAdapter adapter = new ItemIndexAdapter();
    final HeaderPagerAdapter pagerAdapter = new HeaderPagerAdapter();

    Map<Integer, String> mHeader_map = new HashMap<>();

    Observer<Lastest> observer = new Observer<Lastest>() {
        @Override
        public void onCompleted() {
            mHeader_map.put(1, "今日热闻");
            adapter.notifyDataSetChanged();
            Log.d(TAG, "url complete.");
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(Lastest lastest) {
            stories = lastest.getStories();
            adapter.setItems(stories);

            topStories = lastest.getTop_stories();
            pagerAdapter.setTopStories(topStories);
        }
    };

    Observer<Lastest> loadMoreObserver = new Observer<Lastest>() {
        @Override
        public void onCompleted() {
            mIsLoading = false;
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Lastest lastest) {
            mDay.add(Calendar.DAY_OF_MONTH, -1);
            mHeader_map.put(stories.size() + 1, mSimpleDateFormat.format(mDay.getTime()));
            stories.addAll(lastest.getStories());
            adapter.setItems(stories);
        }
    };
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        observe();

    }

    @Override protected void loadMore() {
        String id = mSimpleDateFormat.format(mDay.getTime());
        mOb = NetUtils.getApi()
                .getBeforeStory(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loadMoreObserver);
    }

    @Override protected void observe() {
        mSubscription = NetUtils.getApi()
                .getLastest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override public View onCreateView(LayoutInflater layoutInflater, ViewGroup vg, Bundle bundle) {
        View v = super.onCreateView(layoutInflater, vg, bundle);
        pagerAdapter.setClickListener(new StoryClickListener(getActivity()));
        adapter.setClickListener(new StoryClickListener(getActivity()));
        mRecycler.setAdapter(adapter);

        return v;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if (mOb != null && !mOb.isUnsubscribed()) {
            mOb.unsubscribe();
        }
    }

    class ItemIndexAdapter extends RecyclerView.Adapter<ItemIndexHolder> {

        static final int PAGER = 0;
        static final int CONTENT = 1;
        static final int HEADER = 2;

        List<Story> items;
        private OnRecyclerViewItemClickListener clickListener;

        public ItemIndexAdapter(){
        }

        public ItemIndexAdapter(OnRecyclerViewItemClickListener listener) {
            clickListener = listener;
        }

        @Override public ItemIndexHolder onCreateViewHolder(
                ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater
                    .from(getActivity());

            if (viewType == PAGER) {
                return new ItemIndexHolder(
                        layoutInflater.inflate(R.layout.header_index,
                                parent, false), PAGER, null);
            } else if (viewType == CONTENT){
                View v = layoutInflater
                        .inflate(R.layout.item_recycler, parent, false);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener != null)
                            clickListener.onItemClick(v, (String)v.getTag());
                    }
                });
                return new ItemIndexHolder(v, CONTENT, clickListener);
            } else {
                TextView textView = new TextView(getActivity());
                return new ItemIndexHolder(textView, HEADER, null);
            }
        }

        @Override public void onBindViewHolder(ItemIndexHolder holder, int position) {

            int type = getItemViewType(position);
            if (type == PAGER) {
                holder.mViewPager.setAdapter(pagerAdapter);
            } else if (type == CONTENT){
                Story item = items.get(position -1);
                holder.mText.setText(item.getTitle());
                holder.cardView.setTag(String.valueOf(item.getId()));
                if (item.hasImage()) {
                    Glide.with(holder.mImage.getContext())
                            .load(item.getImages().get(0))
                            .into(holder.mImage);
                }
            } else {
                holder.mText.setText(mHeader_map.get(position));
            }
        }

        @Override public int getItemCount() {
            return items == null? 0: items.size() + 1;
        }

        @Override public int getItemViewType(int positon) {
            return positon == 0? PAGER:
                    mHeader_map.containsKey(positon)? HEADER: CONTENT;
        }

        public void setItems(List<Story> stories) {
            items = stories;
            notifyDataSetChanged();
        }

        public void setClickListener(OnRecyclerViewItemClickListener listener) {
            clickListener = listener;
        }
    }

    private class ItemIndexHolder extends RecyclerView.ViewHolder {

        TextView mText;
        ImageView mImage;
        ViewPager mViewPager;
        View cardView;
        private OnRecyclerViewItemClickListener clickListener;

        ItemIndexHolder(View itemView, int type, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            switch (type) {
                case ItemIndexAdapter.CONTENT:
                    cardView = itemView;
                    mText = (TextView)itemView.findViewById(R.id.item_title);
                    mImage = (ImageView)itemView.findViewById(R.id.item_image);
                    clickListener = listener;
                    break;
                case ItemIndexAdapter.PAGER:
                    mViewPager = (ViewPager)itemView.findViewById(R.id.header_pager);
                    break;
                case ItemIndexAdapter.HEADER:
                    mText = (TextView) itemView;
                    mText.setPadding(15, 10, 0, 10);
                    break;
            }
        }
    }

    private class HeaderPagerAdapter extends PagerAdapter {

        List<TopStory> topStories;
        OnRecyclerViewItemClickListener clickListener;

        @Override public int getCount() {
            return topStories == null? 0: topStories.size();
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override public Object instantiateItem(ViewGroup container, int position) {
            TopStory topStory = topStories.get(position);
            Context context = getActivity();

            FrameLayout frameLayout = new FrameLayout(context);
            ViewPager.LayoutParams params = new ViewPager.LayoutParams();

            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            FrameLayout.LayoutParams image_param = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER
            );

            TextView textView = new TextView(context);
            textView.setText(topStory.getTitle());
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(20);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            FrameLayout.LayoutParams text_param = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
            );

            Glide.with(context)
                    .load(topStory.getImage())
                    .into(imageView);
            frameLayout.addView(imageView, image_param);
            frameLayout.addView(textView, text_param);
            frameLayout.setTag(String.valueOf(topStory.getId()));
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, (String)v.getTag());
                }
            });

            container.addView(frameLayout, params);
            return frameLayout;
        }

        @Override public void destroyItem(ViewGroup container, int position, Object obj) {
            ((ViewGroup)container).removeView((View)obj);
            obj = null;
        }

        public void setTopStories(List<TopStory> topStories) {
            this.topStories = topStories;
            notifyDataSetChanged();
        }

        public void setClickListener(OnRecyclerViewItemClickListener listener) {
            clickListener = listener;
        }
    }


    public static Fragment createFragment() {
        if (Singleton == null) {
            Singleton = new IndexFragment();
            return Singleton;
        }
        return Singleton;
    }

}
