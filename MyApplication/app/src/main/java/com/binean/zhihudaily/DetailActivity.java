package com.binean.zhihudaily;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.binean.zhihudaily.network.NetUtils;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity {

    public static final String STORY_ID = "id";

    WebView mWebView;
    Subscription mSubscription;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWebView = (WebView)findViewById(R.id.story_detail);
        showDetail();
    }

    private void showDetail() {
        String id = getIntent().getStringExtra(STORY_ID);
        Observer<String> observer = new Observer<String>() {
            String data;
            @Override public void onCompleted() {
                mWebView.loadDataWithBaseURL("file:///android_asset/",
                        data, "text/html", "UTF-8", null);
            }

            @Override public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override public void onNext(String data) {
                this.data = data;
            }
        };

        mSubscription = NetUtils.getApi()
                .getStoryDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(NetUtils::transformHTML)
                .subscribe(observer);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
