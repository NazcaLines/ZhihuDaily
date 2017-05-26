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