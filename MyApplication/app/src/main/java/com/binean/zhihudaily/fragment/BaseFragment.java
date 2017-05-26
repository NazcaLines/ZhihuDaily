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

import com.binean.zhihudaily.model.Story;
import com.bumptech.glide.Glide;

import java.util.List;

import rx.Subscription;

/**
 * Created by 彬旭 on 2017/5/24.
 */

public class BaseFragment extends Fragment {

    protected Subscription mSubscription;
    protected RecyclerView mRecycler;
    protected SwipeRefreshLayout mSwipe;

    List<Story>stories;
    final ItemAdapter adapter = new ItemAdapter();

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

    @Override public void onDestroyView() {
        super.onDestroyView();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        List<Story> items;

//        ItemAdapter(List<Article>items) {
//            this.items = items;
//        }

        @Override public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity())
                    .inflate(R.layout.item_recycler, parent, false);
            return new ItemHolder(v);
        }

        @Override public void onBindViewHolder(ItemHolder holder, int position) {
            Story item = items.get(position);
            holder.mText.setText(item.getTitle());
            if (item.hasImage()) {
                Glide.with(holder.mImage.getContext())
                        .load(item.getImages().get(0))
                        .into(holder.mImage);
            }
        }

        @Override public int getItemCount() {
            return items == null? 0: items.size();
        }

        public void setItems(List<Story> stories) {
            items = stories;
            notifyDataSetChanged();
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
