package com.binean.zhihudaily.control;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.binean.zhihudaily.DetailActivity;

/**
 * Created by 彬旭 on 2017/5/27.
 */

public class StoryClickListener implements OnRecyclerViewItemClickListener {

    Context context;
    public StoryClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onItemClick(View v, String tag) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.STORY_ID, tag);
        context.startActivity(intent);
    }
}
