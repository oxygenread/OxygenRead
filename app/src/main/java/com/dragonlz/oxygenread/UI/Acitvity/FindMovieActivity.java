package com.dragonlz.oxygenread.UI.Acitvity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dragonlz.oxygenread.R;
import com.dragonlz.oxygenread.UI.Model.Movie;
import com.dragonlz.oxygenread.UI.Utils.GetUrlUtil;
import com.dragonlz.oxygenread.UI.Utils.ParseUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by sdm on 2015/8/22.
 */
public class FindMovieActivity extends AppCompatActivity {

    private List mDataSet = new ArrayList();
    private ItemAdapter mMovieAdapter;
    private RecyclerView mMovieListView;
    private OkHttpClient client;
    private String readMovieData;
    private static final int UPDATA = 0;

    private LinearLayoutManager mLayoutManager;
    private EditText writeMovie;
    private Button mButton;
    private SharedPreferences pref;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ParseUtil movie = new ParseUtil();
            switch (msg.what){
                case UPDATA:
                    String data = readMovieData;
                    Log.d("data", data);
                    mDataSet = movie.parseMovie(data);
                    if (mDataSet != null){
                        mMovieAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findmovie);
        mMovieListView = (RecyclerView) findViewById(R.id.findmovieList);
        pref = getSharedPreferences("read", Context.MODE_PRIVATE);
        setHisotryData();
        setData();
    }

    private void setHisotryData() {
        writeMovie = (EditText) findViewById(R.id.et_findmovie);
        mButton = (Button) findViewById(R.id.bt_findmovie);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String themovie = writeMovie.getText().toString();
                Log.d("Movie__Movie___Movie",themovie);
                init(themovie);
            }
        });
    }

    private void setData(){
        mLayoutManager = new LinearLayoutManager(this);
        mMovieListView.setLayoutManager(mLayoutManager);
        mMovieAdapter = new ItemAdapter(this);
        mMovieListView.setAdapter(mMovieAdapter);
    }

    private void init(String moviename){
        GetUrlUtil getUrlUtil = new GetUrlUtil();
        String city = pref.getString("city", "");
        if (city != null) {
            String url = getUrlUtil.getMovieUrl(moviename, city , "1");
            initDataSet(url);
        }
    }

    public void initDataSet(String url){
        client = new OkHttpClient();
        final Request request = new Request.Builder().url(url)
                .header("apikey", "a535145037f63f973c5aad9e7ba1331d")
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    Log.d("OkHttpClient","ok");
                    response = client.newCall(request).execute();
                    readMovieData = response.body().string();
                    Log.d("OkHttpClient",readMovieData);
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
            View view = mInflater.inflate(R.layout.findmovie_item, viewGroup, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
            Movie movie = (Movie) mDataSet.get(i);
            itemViewHolder.cinemaName.setText(movie.getCinema());
            itemViewHolder.cinemaAdress.setText(movie.getCinemaAddress());
            itemViewHolder.cinemaPhone.setText(movie.getCinemaPhone());
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView cinemaName;
        TextView cinemaAdress;
        TextView cinemaPhone;


        public ItemViewHolder(View itemView) {
            super(itemView);
            cinemaName = (TextView) itemView.findViewById(R.id.tv_cinemaName);
            cinemaAdress = (TextView) itemView.findViewById(R.id.tv_cinemaAdress);
            cinemaPhone = (TextView) itemView.findViewById(R.id.tv_cinemaPhone);
        }
    }
}
