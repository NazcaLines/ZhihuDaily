package com.binean.zhihudaily.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.binean.zhihudaily.R;
import com.binean.zhihudaily.adapter.MyBaseAdapter;

/**
 * Created by 彬旭 on 2017/5/24.
 */

public class IndexFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO maybe get urls
    }

//    @Override
//    public View onCreateView(LayoutInflater layoutInflater, ViewGroup vg, Bundle bundle) {
//        View v = layoutInflater.inflate(R.layout.fragment_base, vg, false);
//        mSwipe = (SwipeRefreshLayout)v.findViewById(R.id.swipe_layout);
//        mSwipe.setProgressBackgroundColorSchemeResource(android.R.color.white);
//        mSwipe.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
//                R.color.colorPrimaryDark);
//
//        mRecycler = (RecyclerView)v.findViewById(R.id.content_display);
//        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),
//                LinearLayoutManager.VERTICAL, false));
//        mRecycler.setAdapter(new MyBaseAdapter(getActivity()));
//        return v;
//    }

    @Override public View onCreateView(LayoutInflater layoutInflater, ViewGroup vg, Bundle bundle) {
        View v = super.onCreateView(layoutInflater, vg, bundle);
        final MyBaseAdapter adapter = new MyBaseAdapter(getActivity());
        mRecycler.setAdapter(adapter);
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.list.add(0, 10000);
                adapter.notifyDataSetChanged();
                mSwipe.setRefreshing(false);
            }
        });
        return v;
    }

    public static Fragment createFragment(Fragment fragment) {
        if (fragment == null) {
            return new IndexFragment();
        } else if (!(fragment instanceof IndexFragment)) {
            return new IndexFragment();
        }
        return fragment;
    }
}
