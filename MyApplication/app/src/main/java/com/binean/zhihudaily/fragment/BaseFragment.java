package com.binean.zhihudaily.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.binean.zhihudaily.DetailActivity;
import com.binean.zhihudaily.R;
import com.binean.zhihudaily.model.Story;
import com.binean.zhihudaily.presenter.BasePresenter;
import com.binean.zhihudaily.presenter.BaseView;

import java.util.List;

public abstract class BaseFragment extends Fragment implements BaseView{

    protected BasePresenter mPresenter;

    protected RecyclerView mRecycler;
    protected SwipeRefreshLayout mSwipe;
    protected FloatingActionButton mFab;
    private int mVisibleThreshold = 1;
    protected boolean mIsLoading = false;

    List<Story> mStories;

    OnRecyclerViewItemClickListener mClickListener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View v, String tag) {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(DetailActivity.STORY_ID, tag);
            startActivity(intent);
        }
    };

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.subscribe();
    }

    @Override public View onCreateView(final LayoutInflater layoutInflater,
                                       ViewGroup vg, Bundle bundle) {
        View v = layoutInflater.inflate(R.layout.fragment_base, vg, false);
        mSwipe = (SwipeRefreshLayout)v.findViewById(R.id.swipe_layout);
        mSwipe.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwipe.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
                R.color.colorPrimaryDark);
        mSwipe.setOnRefreshListener(() -> {
            refresh();
            mSwipe.setRefreshing(false);
        });

        mRecycler = (RecyclerView)v.findViewById(R.id.content_display);
        final LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(linearLayoutManager);

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrollStateChanged(
                    RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mFab.hide();
                } else {
                    mFab.show();
                }
            }

            @Override public void onScrolled(RecyclerView recyclerView,
                                             int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastVisibleItemPosition();
                if (!mIsLoading && totalItem <= lastVisible + mVisibleThreshold) {
                    mIsLoading = true;
                    loadMore();
                }
            }
        });

        mFab = (FloatingActionButton)v.findViewById(R.id.fab);
        mFab.setOnClickListener(view -> mRecycler.smoothScrollToPosition(0));
        return v;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unsubscibe();
    }

    @Override public void setPresenter(BasePresenter presenter) {
        mPresenter = presenter;
    }

    protected abstract void refresh();

    protected abstract void loadMore();

    interface OnRecyclerViewItemClickListener {
        void onItemClick(View v, String tag);
    }

}


