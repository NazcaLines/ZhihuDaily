package com.binean.zhihudaily;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.binean.zhihudaily.model.Detail;
import com.binean.zhihudaily.network.NetUtils;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 彬旭 on 2017/5/27.
 */

public class DetailActivity extends AppCompatActivity {

    public static final String STORY_ID = "id";

    WebView webView;
    Subscription subscription;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = (WebView)findViewById(R.id.story_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        showDetail();
    }

    private void showDetail() {
        String id = getIntent().getStringExtra(STORY_ID);
        Observer<String> observer = new Observer<String>() {
            String data;
            @Override public void onCompleted() {
                webView.loadDataWithBaseURL("file:///android_asset/",
                        data, "text/html", "UTF-8", null);
            }

            @Override public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override public void onNext(String data) {
                this.data = data;
            }
        };
        subscription = NetUtils.getApi()
                .getStoryDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Detail, String>() {
                    @Override public String call(Detail detail) {
                        return NetUtils.transformHTML(detail);
                    }
                })
                .subscribe(observer);
    }

    @Override public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
