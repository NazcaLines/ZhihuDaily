package com.binean.zhihudaily.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.binean.zhihudaily.model.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 彬旭 on 2017/5/24.
 */

public class IndexFragment extends BaseFragment {
    public static IndexFragment Singleton;

    List<Article>index_items;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        index_items = new ArrayList<>();
        //TODO maybe get urls
    }

    @Override public View onCreateView(LayoutInflater layoutInflater, ViewGroup vg, Bundle bundle) {
        View v = super.onCreateView(layoutInflater, vg, bundle);
        final ItemAdapter adapter = new ItemAdapter(index_items);
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
