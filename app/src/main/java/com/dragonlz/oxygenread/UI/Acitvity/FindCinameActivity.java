package com.dragonlz.oxygenread.UI.Acitvity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
public class FindCinameActivity extends AppCompatActivity {

    private List mDataSet = new ArrayList();
    private ItemAdapter mMovieAdapter;
    private RecyclerView mMovieListView;
    private OkHttpClient client;
    private String readMovieData;
    private static final int UPDATA = 0;

    private Button mButton;
    private EditText mEditText;

    private LinearLayoutManager mLayoutManager;

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
        setContentView(R.layout.activity_findciname);

        pref = getSharedPreferences("read", Context.MODE_PRIVATE);
        mMovieListView = (RecyclerView) findViewById(R.id.findcinameList);
        mButton = (Button) findViewById(R.id.bt_findciname);
        mEditText = (EditText) findViewById(R.id.et_findciname);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cinameName = mEditText.getText().toString();
                init(cinameName);
            }
        });
        setData();
    }

    private void init(String cinemaname){
        GetUrlUtil getUrlUtil = new GetUrlUtil();
        String city = pref.getString("city","");
        if (city != null){
            String url = getUrlUtil.getCinemaUrl(cinemaname,city);
            initDataSet(url);
        }
    }

    private void setData(){
        mLayoutManager = new LinearLayoutManager(this);
        mMovieListView.setLayoutManager(mLayoutManager);
        mMovieAdapter = new ItemAdapter(this);
        mMovieListView.setAdapter(mMovieAdapter);
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
            final Movie movie = (Movie) mDataSet.get(i);
            itemViewHolder.CinemaName.setText(movie.getCinema());
            itemViewHolder.CinemaArea.setText(movie.getCinemaAddress());
            itemViewHolder.CinemaNumber.setText(movie.getCinemaPhone());
            itemViewHolder.myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List movieContent = movie.getMovie();
                    ShowMovieActivity.getMovieContent(movieContent);
                    Intent intent = new Intent(FindCinameActivity.this,ShowMovieActivity.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView CinemaName;
        TextView CinemaArea;
        TextView CinemaNumber;
        View myView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            CinemaName = (TextView) itemView.findViewById(R.id.tv_cinemaName);
            CinemaArea = (TextView) itemView.findViewById(R.id.tv_cinemaAdress);
            CinemaNumber = (TextView) itemView.findViewById(R.id.tv_cinemaPhone);
            myView = itemView;
        }
    }
}
