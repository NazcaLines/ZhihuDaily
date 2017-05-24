package com.binean.zhihudaily.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.binean.zhihudaily.R;
import com.binean.zhihudaily.adapter.MyBaseAdapter;
import com.binean.zhihudaily.adapter.MyPsyAdapter;

/**
 * Created by 彬旭 on 2017/5/24.
 */

public class PsyFragment extends Fragment {
    private RecyclerView mRecycler;
    private SwipeRefreshLayout mSwipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO maybe get urls
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup vg, Bundle bundle) {
        View v = layoutInflater.inflate(R.layout.fragment_base, vg, false);
        mSwipe = (SwipeRefreshLayout)v.findViewById(R.id.swipe_layout);
        mSwipe.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwipe.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
                R.color.colorPrimaryDark);

        mRecycler = (RecyclerView)v.findViewById(R.id.content_display);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        mRecycler.setAdapter(new MyPsyAdapter(getActivity()));
        return v;
    }
    public static Fragment createFragment(Fragment fragment) {
        return new PsyFragment();

    }
}