package com.dragonlz.oxygenread.UI.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dragonlz.oxygenread.R;
import com.dragonlz.oxygenread.UI.Model.HistoryToday;
import com.dragonlz.oxygenread.UI.Utils.GetUrlUtil;
import com.dragonlz.oxygenread.UI.Utils.ParseUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sdm on 2015/8/18.
 */
public class HistoryTodayFragment extends Fragment {

    private List mDataSet = new ArrayList();
    private ItemAdapter mAdapter;
    private RecyclerView mHistoryListView;
    private OkHttpClient client;
    private String readHistoryData;
    private static final int UPDATA = -1;

    private LinearLayoutManager mLayoutManager;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATA:
                    ParseUtil history = new ParseUtil();
                    String data = readHistoryData;
                    Log.d("data",data);
                    mDataSet = history.parseHistory(data);
                    if (mDataSet != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.historytoday_fragment,container,false);
        mHistoryListView = (RecyclerView) view.findViewById(R.id.historyList);

        Log.d("historyFragment","succeed");
        setData();
        init();
        return view;
    }

    private void setData(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        mHistoryListView.setLayoutManager(mLayoutManager);
        mAdapter = new ItemAdapter(getActivity());
        mHistoryListView.setAdapter(mAdapter);
    }

    private void init(){
        GetUrlUtil getUrlUtil = new GetUrlUtil();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");//时间显示格式
        Date curDate = new Date(System.currentTimeMillis());
        String Time = formatter.format(curDate);
        String url = getUrlUtil.getHistoryUrl(Time);
        Log.d("url",url);
        initDataSet(url);
    }

    public void initDataSet(String url){
        client = new OkHttpClient();
        final Request request = new Request.Builder().url(url)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    Log.d("OkHttpClient","ok");
                    response = client.newCall(request).execute();
                    readHistoryData = response.body().string();
                    Log.d("OkHttpClient",readHistoryData);
                    Message message = new Message();
                    message.what = UPDATA;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
            View view = mInflater.inflate(R.layout.historytoday_item, viewGroup, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
            HistoryToday historyToday = (HistoryToday) mDataSet.get(i);
            itemViewHolder.content.setText(historyToday.getContent());
            itemViewHolder.year.setText(historyToday.getTheYear());
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView year;


        public ItemViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.history_content);
            year = (TextView) itemView.findViewById(R.id.history_year);
        }
    }
}
