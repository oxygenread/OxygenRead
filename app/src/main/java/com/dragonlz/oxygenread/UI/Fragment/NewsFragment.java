package com.dragonlz.oxygenread.UI.Fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragonlz.oxygenread.R;
import com.dragonlz.oxygenread.UI.Acitvity.SampleItemAnimator;
import com.race604.flyrefresh.FlyRefreshLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.dragonlz.oxygenread.UI.Model.News;

/**
 * Created by Dragon丶Lz on 2015/8/11.
 */
public class NewsFragment extends Fragment implements FlyRefreshLayout.OnPullRefreshListener {


    private FlyRefreshLayout mFlylayout;
    private RecyclerView mListView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<News> mDataSet = new ArrayList<>();
    private ItemAdapter mAdapter;
    private Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View newsView = inflater.inflate(R.layout.news_layout, container, false);
        initDataSet();
        initData(newsView);
        return newsView;
    }

    private void initDataSet() {
        /**
         * 这里做网络请求
         */
    }

    private void initData(View v){

        mFlylayout = (FlyRefreshLayout) v. findViewById(R.id.fly_layout);

        mFlylayout.setOnPullRefreshListener(this);

        mListView = (RecyclerView)v. findViewById(R.id.list);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(mLayoutManager);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(mLayoutManager);
        mAdapter = new ItemAdapter(getActivity());

        mListView.setAdapter(mAdapter);

        mListView.setItemAnimator(new SampleItemAnimator());


    }

    @Override
    public void onRefresh(FlyRefreshLayout flyRefreshLayout) {

        View child = mListView.getChildAt(0);
        if (child != null) {
            bounceAnimateView(child.findViewById(R.id.icon));
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFlylayout.onRefreshFinish();
            }
        }, 2000);

    }

    @Override
    public void onRefreshAnimationEnd(FlyRefreshLayout flyRefreshLayout) {

    }

    private void bounceAnimateView(View view) {
        if (view == null) {
            return;
        }

        Animator swing = ObjectAnimator.ofFloat(view, "rotationX", 0, 30, -20, 0);
        swing.setDuration(400);
        swing.setInterpolator(new AccelerateInterpolator());
        swing.start();
    }
    private class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        private LayoutInflater mInflater;
        private DateFormat dateFormat;

        public ItemAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            dateFormat = SimpleDateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = mInflater.inflate(R.layout.news_item, viewGroup, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
            final News news = mDataSet.get(i);


            itemViewHolder.icon.setImageResource(news.getNewsImage());
            itemViewHolder.title.setText(news.getNewsImage());
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView title;


        public ItemViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.news_image);
            title = (TextView) itemView.findViewById(R.id.news_title);

        }

    }
}
