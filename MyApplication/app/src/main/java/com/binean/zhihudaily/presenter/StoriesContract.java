package com.binean.zhihudaily.presenter;

import com.binean.zhihudaily.model.Lastest;
import com.binean.zhihudaily.model.Theme;

public interface StoriesContract {

    interface IndexView extends BaseView {
        void handleRefresh(Lastest lastest);
        void handleLoadMore(Lastest lastest);
    }

    interface ThemeView extends BaseView {
        void handleRefresh(Theme theme);
        void handleLoadMore();
    }

    interface IndexPresenter extends BasePresenter {
        void loadMore(String id);
        void refresh();
    }

    interface ThemePresenter extends BasePresenter {
        void refresh(String id);
    }
}
