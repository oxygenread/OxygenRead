package com.dragonlz.oxygenread.UI.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.dragonlz.oxygenread.R;
import com.dragonlz.oxygenread.UI.Acitvity.FindCinameActivity;
import com.dragonlz.oxygenread.UI.Acitvity.FindMovieActivity;
import com.dragonlz.oxygenread.UI.Model.MovieBoxOffice;
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
 * Created by sdm on 2015/8/20.
 */
public class MovieFragment extends Fragment {

    private List mDataSet = new ArrayList();
    private ItemAdapter mAdapter;
    private RecyclerView mListView;
    private OkHttpClient client;
    private String readHealtyData;
    private static final int UPDATA = -1;
    private Button search_btn;
    private AppCompatEditText et_city;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private LinearLayoutManager mLayoutManager;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATA:
                    ParseUtil movieNumber = new ParseUtil();
                    String data = readHealtyData;
                    mDataSet = movieNumber.parseMovieNumber(data);
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
        View view = inflater.inflate(R.layout.fragment_movie,container,false);
        mListView = (RecyclerView) view.findViewById(R.id.lv_rank);
        search_btn= (Button) view.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref = getActivity().getSharedPreferences("read",getActivity().MODE_PRIVATE);
                String city = pref.getString("city","");
                if (city.equals("")){
                    DiaLog();
                }else {
                    Intent search_ = new Intent(getActivity(),FindCinameActivity.class);
                    startActivity(search_);
                }

            }
        });
        setData();
        init();
        return view;
    }


    //请求部分

    private void setData(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(mLayoutManager);
        mAdapter = new ItemAdapter(getActivity());
        mListView.setAdapter(mAdapter);
    }

    private void init(){
        GetUrlUtil getUrlUtil = new GetUrlUtil();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");//时间显示格式
        Date curDate = new Date(System.currentTimeMillis());
        String Time = formatter.format(curDate);
        String url = getUrlUtil.getMovieNumberUrl(Time);
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
                    response = client.newCall(request).execute();
                    readHealtyData = response.body().string();
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
            View view = mInflater.inflate(R.layout.rank_item, viewGroup, false);//这里把你的布局文件放进去
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
            MovieBoxOffice movies = (MovieBoxOffice) mDataSet.get(i);
            itemViewHolder.movieName.setText(movies.getMovieName());
            itemViewHolder.movieRianking.setText("评分"+movies.getMovieTheNumber());
            itemViewHolder.movieBoxOffice.setText("周票房："+movies.getMovieMoney()+"万元");
            itemViewHolder.movieGrade.setText("周排名"+movies.getMovieComment());
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView movieName;
        TextView movieRianking;
        TextView movieBoxOffice;//票房...注意这里是（万元）
        TextView movieGrade;

        public ItemViewHolder(View itemView) {
            super(itemView);
            movieName = (TextView) itemView.findViewById(R.id.tv_rank_name);//这里需要该id
            movieRianking = (TextView) itemView.findViewById(R.id.tv_rank_grade);
            movieBoxOffice = (TextView) itemView.findViewById(R.id.tv_rank_money);
            movieGrade = (TextView) itemView.findViewById(R.id.tv_rank);
        }
    }

    private void DiaLog(){
        View ChangeCity = LayoutInflater.from(getActivity()).inflate(R.layout.change_city, null);
        et_city = (AppCompatEditText) ChangeCity.findViewById(R.id.et_city);

        new MaterialDialog.Builder(getActivity())
                .title("获取您的地理位置")
                .customView(ChangeCity, false)
                .positiveText("确定")
                .negativeText("取消")
                .titleColor(getResources().getColor(R.color.blue_sky))
                .backgroundColor(getResources().getColor(R.color.white))
                .positiveColor(getResources().getColor(R.color.blue_sky))
                .negativeColor(getResources().getColor(R.color.dialog_nav))
                .cancelable(false)
                .theme(Theme.LIGHT)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        final String city_et = et_city.getText().toString();
                        editor.putString("city", city_et);
                        editor.commit();
                        Intent search = new Intent(getActivity(),FindCinameActivity.class);
                        startActivity(search);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {

                    }
                })
                .show();
    }
}
