package com.binean.zhihudaily.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by 彬旭 on 2017/5/26.
 */

public class InterestFragment extends ThemeFragment {

    public static InterestFragment Singleton;
    public static Fragment createFragment(String number) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY, number);
        if (Singleton == null) {
            Singleton = new InterestFragment();
            Singleton.setArguments(bundle);
            return Singleton;
        } else {
            return Singleton;
        }
    }
}
