package com.binean.zhihudaily;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class SettingActivity extends AppCompatActivity {

    public static final String DEFAULT_WEB_CACHE = "/data/data";
    TextView clearImage;
    TextView clearWeb;
    TextView clearCookie;

    @Override public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        clearImage = (TextView)findViewById(R.id.clear_image_cache);
        clearImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.get(SettingActivity.this)
                        .clearMemory();
                AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        Glide.get(SettingActivity.this).clearDiskCache();
                        return null;
                    }
                    @Override public void onPostExecute(Void n) {
                        Toast.makeText(SettingActivity.this,
                                R.string.clear_cache_success, Toast.LENGTH_SHORT).show();
                    }
                };
                asyncTask.execute();
            }
        });

        clearWeb = (TextView)findViewById(R.id.clear_web_cache);
        clearWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private static void deleteCacheByDirectory(String path) {
//        File directory = new File();
    }
}
