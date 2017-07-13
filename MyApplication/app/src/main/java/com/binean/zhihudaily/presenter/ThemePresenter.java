package com.binean.zhihudaily.presenter;

import com.binean.zhihudaily.network.NetUtils;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ThemePresenter implements StoriesContract.ThemePresenter{

    private StoriesContract.ThemeView mStoriesView;
    private Subscription mRefreshSub;

    public ThemePresenter(StoriesContract.ThemeView view) {
        mStoriesView = view;
        mStoriesView.setPresenter(this);
    }

    @Override
    public void refresh(String id) {
        mRefreshSub = NetUtils.getApi()
                .getTheme(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(theme -> mStoriesView.handleRefresh(theme));
    }

    @Override
    public void subscribe() {}

    @Override
    public void unsubscibe() {
        if (!mRefreshSub.isUnsubscribed()) {
            mRefreshSub.unsubscribe();
        }
    }
}
