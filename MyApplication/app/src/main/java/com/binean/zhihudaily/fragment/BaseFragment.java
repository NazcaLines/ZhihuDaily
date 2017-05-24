package com.binean.zhihudaily.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.binean.zhihudaily.R;
import com.binean.zhihudaily.model.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 彬旭 on 2017/5/24.
 */

public class BaseFragment extends Fragment {

    protected RecyclerView mRecycler;
    protected SwipeRefreshLayout mSwipe;

    @Override public View onCreateView(LayoutInflater layoutInflater,
                                       ViewGroup vg, Bundle bundle) {
        View v = layoutInflater.inflate(R.layout.fragment_base, vg, false);
        mSwipe = (SwipeRefreshLayout)v.findViewById(R.id.swipe_layout);
        mSwipe.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwipe.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary,
                R.color.colorPrimaryDark);

        mRecycler = (RecyclerView)v.findViewById(R.id.content_display);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

        return v;
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        List<Article> items;

        ItemAdapter(List<Article>items) {
            this.items = items;
        }

        @Override public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity())
                    .inflate(R.layout.item_test, parent, false);
            return new ItemHolder(v);
        }

        @Override public void onBindViewHolder(ItemHolder holder, int position) {
            Article item = items.get(position);
            holder.mText.setText(item.getTitle());
            //TODO set bitmap
            //just for test
            holder.mImage.setImageResource(R.drawable.test1);
        }

        @Override public int getItemCount() {
            return items.size();
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        TextView mText;
        ImageView mImage;

        ItemHolder(View itemView) {
            super(itemView);
            mText = (TextView)itemView.findViewById(R.id.item_title);
            mImage = (ImageView)itemView.findViewById(R.id.item_image);
        }
    }
}
