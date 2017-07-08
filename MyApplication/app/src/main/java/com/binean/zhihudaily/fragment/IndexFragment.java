package com.binean.zhihudaily.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IndexFragment extends BaseFragment {

    public static final String TAG = "IndexFragment";

    Calendar mDay = Calendar.getInstance();
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    Subscription mOb;

    List<TopStory> mTopStories;

    final ItemIndexAdapter mItemIndexadapter = new ItemIndexAdapter();
    final HeaderPagerAdapter mPagerAdapter = new HeaderPagerAdapter();

    Map<Integer, String> mHeader_map = new HashMap<>();

    Observer<Lastest> mRefreshObserver = new Observer<Lastest>() {
        @Override public void onCompleted() {
            mHeader_map.put(1, "今日热闻");
            mDay = Calendar.getInstance();
            mItemIndexadapter.notifyDataSetChanged();
            Log.d(TAG, "url complete.");
        }

        @Override public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override public void onNext(Lastest lastest) {
            mStories = lastest.getStories();
            mItemIndexadapter.setItems(mStories);

            mTopStories = lastest.getTop_stories();
            mPagerAdapter.setmTopStories(mTopStories);
        }
    };

    Observer<Lastest> mLoadMoreObserver = new Observer<Lastest>() {
        @Override public void onCompleted() {
            mIsLoading = false;
        }

        @Override public void onError(Throwable e) {}

        @Override public void onNext(Lastest lastest) {
            mDay.add(Calendar.DAY_OF_MONTH, -1);
            mHeader_map.put(mStories.size() + 1, mSimpleDateFormat.format(mDay.getTime()));
            mStories.addAll(lastest.getStories());
            mItemIndexadapter.setItems(mStories);
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
                .subscribe(mLoadMoreObserver);
    }

    @Override protected void observe() {
        mSubscription = NetUtils.getApi()
                .getLastest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mRefreshObserver);
    }

    @Override public View onCreateView(LayoutInflater layoutInflater,
                                       ViewGroup vg, Bundle bundle) {
        View v = super.onCreateView(layoutInflater, vg, bundle);
        mPagerAdapter.setmClickListener(new StoryClickListener(getActivity()));
        mItemIndexadapter.setmClickListener(new StoryClickListener(getActivity()));
        mRecycler.setAdapter(mItemIndexadapter);
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

        List<Story> mItems;
        private OnRecyclerViewItemClickListener mClickListener;

        public ItemIndexAdapter(){
        }

        public ItemIndexAdapter(OnRecyclerViewItemClickListener listener) {
            mClickListener = listener;
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
                        if (mClickListener != null)
                            mClickListener.onItemClick(v, (String)v.getTag());
                    }
                });
                return new ItemIndexHolder(v, CONTENT, mClickListener);
            } else {
                TextView textView = new TextView(getActivity());
                return new ItemIndexHolder(textView, HEADER, null);
            }
        }

        @Override public void onBindViewHolder(ItemIndexHolder holder, int position) {

            int type = getItemViewType(position);
            if (type == PAGER) {
                holder.mViewPager.setAdapter(mPagerAdapter);
            } else if (type == CONTENT){
                Story item = mItems.get(position -1);
                holder.mText.setText(item.getTitle());
                holder.mCardView.setTag(String.valueOf(item.getId()));
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
            return mItems == null? 0: mItems.size() + 1;
        }

        @Override public int getItemViewType(int positon) {
            return positon == 0? PAGER:
                    mHeader_map.containsKey(positon)? HEADER: CONTENT;
        }

        void setItems(List<Story> stories) {
            mItems = stories;
            notifyDataSetChanged();
        }

        void setmClickListener(OnRecyclerViewItemClickListener listener) {
            mClickListener = listener;
        }
    }

    private class ItemIndexHolder extends RecyclerView.ViewHolder {

        TextView mText;
        ImageView mImage;
        ViewPager mViewPager;
        View mCardView;
        private OnRecyclerViewItemClickListener mClickListener;

        ItemIndexHolder(View itemView, int type, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            switch (type) {
                case ItemIndexAdapter.CONTENT:
                    mCardView = itemView;
                    mText = (TextView)itemView.findViewById(R.id.item_title);
                    mImage = (ImageView)itemView.findViewById(R.id.item_image);
                    mClickListener = listener;
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

        List<TopStory> mTopStories;
        OnRecyclerViewItemClickListener mClickListener;

        @Override public int getCount() {
            return mTopStories == null? 0: mTopStories.size();
        }

        @Override public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override public Object instantiateItem(ViewGroup container, int position) {
            TopStory topStory = mTopStories.get(position);
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
                    mClickListener.onItemClick(v, (String)v.getTag());
                }
            });

            container.addView(frameLayout, params);
            return frameLayout;
        }

        @Override public void destroyItem(ViewGroup container, int position, Object obj) {
            ((ViewGroup)container).removeView((View)obj);
            obj = null;
        }

        void setmTopStories(List<TopStory> mTopStories) {
            this.mTopStories = mTopStories;
            notifyDataSetChanged();
        }

        void setmClickListener(OnRecyclerViewItemClickListener listener) {
            mClickListener = listener;
        }
    }

}
