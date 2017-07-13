package com.binean.zhihudaily.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.binean.zhihudaily.R;
import com.binean.zhihudaily.presenter.StoriesContract;
import com.binean.zhihudaily.model.Story;
import com.binean.zhihudaily.model.Theme;
import com.binean.zhihudaily.presenter.ThemePresenter;
import com.bumptech.glide.Glide;

import java.util.List;

public class ThemeFragment extends BaseFragment implements StoriesContract.ThemeView {

    public static final String TAG = "ThemeFragment";
    public static final String KEY = "THEME";

    final ItemAdapter mItemdapter = new ItemAdapter(mClickListener);

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refresh();
    }

    @Override protected void refresh() {
        String number = String.valueOf(getArguments().getInt(KEY, 2));
        ((ThemePresenter)mPresenter).refresh(number);
    }

    @Override protected void loadMore() {}

    @Override public View onCreateView(LayoutInflater layoutInflater, ViewGroup vg, Bundle bundle) {
        View v = super.onCreateView(layoutInflater, vg, bundle);
        mItemdapter.setmClickListener(mClickListener);
        mRecycler.setAdapter(mItemdapter);
        return v;
    }

    @Override
    public void handleRefresh(Theme theme) {
        mStories = theme.getStories();
        mItemdapter.setmItems(mStories);
    }

    @Override
    public void handleLoadMore() {}

    class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        List<Story> mItems;
        private OnRecyclerViewItemClickListener mClickListener;

        ItemAdapter(OnRecyclerViewItemClickListener listener) {
            mClickListener = listener;
        }

        @Override public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity())
                    .inflate(R.layout.item_recycler, parent, false);
            v.setOnClickListener(view -> {
                if (mClickListener != null)
                    mClickListener.onItemClick(view, (String)view.getTag());
            });
            return new ItemHolder(v, mClickListener);
        }

        @Override public void onBindViewHolder(ItemHolder holder, int position) {
            Story item = mItems.get(position);
            holder.mText.setText(item.getTitle());
            holder.mCardView.setTag(String.valueOf(item.getId()));
            if (item.hasImage()) {
                Glide.with(holder.mImage.getContext())
                        .load(item.getImages().get(0))
                        .into(holder.mImage);
            }
        }

        @Override public int getItemCount() {
            return mItems == null? 0: mItems.size();
        }

        void setmItems(List<Story> stories) {
            mItems = stories;
            notifyDataSetChanged();
        }

        void setmClickListener(OnRecyclerViewItemClickListener listener) {
            mClickListener = listener;
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        TextView mText;
        ImageView mImage;
        View mCardView;
        private OnRecyclerViewItemClickListener mClickListener;

        ItemHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            mText = (TextView)itemView.findViewById(R.id.item_title);
            mImage = (ImageView)itemView.findViewById(R.id.item_image);
            mCardView = itemView;
            mClickListener = listener;
        }
    }

    public static ThemeFragment createThemeFragment(int id) {
        ThemeFragment fragment = new ThemeFragment();
        Bundle fragmentId = new Bundle();
        fragmentId.putInt(KEY, id);
        fragment.setArguments(fragmentId);
        return fragment;
    }
}
