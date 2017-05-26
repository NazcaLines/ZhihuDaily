package com.binean.zhihudaily.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.binean.zhihudaily.model.Article;
import com.binean.zhihudaily.model.Story;
import com.binean.zhihudaily.model.Theme;
import com.binean.zhihudaily.network.Net_utils;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 彬旭 on 2017/5/24.
 */

//public class PsyFragment extends BaseFragment {
//
//    public static final String TAG = "PsyFragment";
//    public static final String KEY = "THEME";
//    public static PsyFragment Singleton;
//
//    Observer<Theme> observer = new Observer<Theme>() {
//        @Override
//        public void onCompleted() {
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            e.printStackTrace();
//        }
//
//        @Override
//        public void onNext(Theme theme) {
//            Log.d(TAG, "onNext");
//            stories = theme.getStories();
//            adapter.setItems(stories);
//        }
//    };
//
//    @Override public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        String number = getArguments().getString(KEY);
//        mSubscription = Net_utils.getApi()
//                .getTheme(number)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer);
//    }
//
//    @Override public View onCreateView(LayoutInflater layoutInflater, ViewGroup vg, Bundle bundle) {
//        View v = super.onCreateView(layoutInflater, vg, bundle);
//        mRecycler.setAdapter(adapter);
//        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override public void onRefresh() {
//                //TODO refresh a new item.
//                adapter.notifyDataSetChanged();
//                mSwipe.setRefreshing(false);
//            }
//        });
//        return v;
//    }
//
//    public static Fragment createFragment(String number) {
//        Bundle bundle = new Bundle();
//        bundle.putString(KEY, number);
//        if (Singleton == null) {
//            Singleton = new PsyFragment();
//            Singleton.setArguments(bundle);
//            return Singleton;
//        } else {
//            Singleton.setArguments(bundle);
//            return Singleton;
//        }
//    }
//}

public class PsyFragment extends ThemeFragment {

    public static PsyFragment Singleton;
    public static Fragment createFragment(String number) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY, number);
        if (Singleton == null) {
            Singleton = new PsyFragment();
            Singleton.setArguments(bundle);
            return Singleton;
        } else {
            return Singleton;
        }
    }

}