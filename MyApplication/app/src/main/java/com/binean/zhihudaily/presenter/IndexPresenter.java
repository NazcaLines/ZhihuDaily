package com.binean.zhihudaily.presenter;

import com.binean.zhihudaily.network.NetUtils;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class IndexPresenter implements StoriesContract.IndexPresenter {

    private StoriesContract.IndexView mStoriesView;
    private CompositeSubscription mCompositeSubscription;

    public IndexPresenter(StoriesContract.IndexView view) {
        mStoriesView = view;
        mStoriesView.setPresenter(this);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override public void loadMore(String id) {
        Subscription loadMoreSubscription = NetUtils.getApi()
                .getBeforeStory(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lastest -> mStoriesView.handleLoadMore(lastest));
        mCompositeSubscription.add(loadMoreSubscription);
    }

    @Override public void refresh() {
        mCompositeSubscription.clear();
        Subscription refreshSubscription = NetUtils.getApi()
                .getLastest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lastest ->mStoriesView.handleRefresh(lastest));
        mCompositeSubscription.add(refreshSubscription);
    }

    @Override
    public void subscribe() {
        refresh();
    }

    @Override
    public void unsubscibe() {
        mCompositeSubscription.clear();
    }

}
