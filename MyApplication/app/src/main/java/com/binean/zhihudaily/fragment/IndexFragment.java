package com.binean.zhihudaily.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.binean.zhihudaily.model.Article;
import com.binean.zhihudaily.model.Lastest;
import com.binean.zhihudaily.model.Story;
import com.binean.zhihudaily.network.Net_utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 彬旭 on 2017/5/24.
 */

public class IndexFragment extends BaseFragment {

    public static final String TAG = "IndexFragment";
    public static IndexFragment Singleton;

   List<Story>stories;
    final ItemAdapter adapter = new ItemAdapter();
    Observer<Lastest> observer = new Observer<Lastest>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(Lastest lastest) {
            stories = lastest.getStories();
            adapter.setItems(stories);
        }
    };

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscription = Net_utils.getApi()
                .getLastest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    @Override public View onCreateView(LayoutInflater layoutInflater, ViewGroup vg, Bundle bundle) {
        View v = super.onCreateView(layoutInflater, vg, bundle);
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

    public static Fragment createFragment(Fragment fragment) {
        if (fragment == null) {
            if (Singleton == null) {
                Singleton = new IndexFragment();
                return Singleton;
            } else {
                return Singleton;
            }
        } else {
            return fragment;
        }
    }
}
