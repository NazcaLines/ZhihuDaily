package com.binean.zhihudaily.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 彬旭 on 2017/5/24.
 */

public class IndexFragment extends BaseFragment {

    public static final String TAG = "IndexFragment";
    public static IndexFragment Singleton;

    private ViewPager viewPager;

    List<TopStory> topStories;

    final ItemIndexAdapter adapter = new ItemIndexAdapter();
    final HeaderPagerAdapter pagerAdapter = new HeaderPagerAdapter();

    Observer<Lastest> observer = new Observer<Lastest>() {
        @Override
        public void onCompleted() {
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

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscription = NetUtils.getApi()
                .getLastest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    @Override public View onCreateView(LayoutInflater layoutInflater, ViewGroup vg, Bundle bundle) {
        View v = super.onCreateView(layoutInflater, vg, bundle);
//        adapter.setClickListener(new OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View v, String tag) {
//                Toast.makeText(getActivity(), "你好,我的文章ID=" + tag, Toast.LENGTH_SHORT).show();
//            }
//        });
        pagerAdapter.setClickListener(new StoryClickListener(getActivity()));
        adapter.setClickListener(new StoryClickListener(getActivity()));
        mRecycler.setAdapter(adapter);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                index_items.add(0, 10000);
                //TODO refresh a new item.
                adapter.notifyDataSetChanged();
                mSwipe.setRefreshing(false);
            }
        });
        return v;
    }

    class ItemIndexAdapter extends RecyclerView.Adapter<ItemIndexHolder> {

        public static final int HEADER = 0;
        public static final int NORMAL = 1;
        List<Story> items;
        private OnRecyclerViewItemClickListener clickListener;

        public ItemIndexAdapter(){
        }

        public ItemIndexAdapter(OnRecyclerViewItemClickListener listener) {
            clickListener = listener;
        }

        @Override public ItemIndexHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if (viewType == NORMAL) {
                View v = layoutInflater.inflate(R.layout.item_recycler, parent, false);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener != null)
                            clickListener.onItemClick(v, (String) v.getTag());
                    }
                });
                return new ItemIndexHolder(v, NORMAL, clickListener);
            } else {
                View v = layoutInflater.inflate(R.layout.header_index, parent, false);
                return new ItemIndexHolder(v, HEADER, null);
            }
        }

        @Override public void onBindViewHolder(ItemIndexHolder holder, int position) {
            if (getItemViewType(position) == HEADER) {
                holder.mViewPager.setAdapter(pagerAdapter);
            } else {
                Story item = items.get(position -1);
                holder.mText.setText(item.getTitle());
                holder.cardView.setTag(String.valueOf(item.getId()));
                if (item.hasImage()) {
                    Glide.with(holder.mImage.getContext())
                            .load(item.getImages().get(0))
                            .into(holder.mImage);
                }
            }
        }

        @Override public int getItemCount() {
            return items == null? 0: items.size() + 1;
        }

        @Override public int getItemViewType(int positon) {
            return positon > 0? 1: positon;
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
            if (type == ItemIndexAdapter.NORMAL) {
                cardView = itemView;
                mText = (TextView) itemView.findViewById(R.id.item_title);
                mImage = (ImageView) itemView.findViewById(R.id.item_image);
                clickListener = listener;

            } else {
                mViewPager = (ViewPager) itemView.findViewById(R.id.header_pager);
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
        } else {
            return Singleton;
        }
    }

}
