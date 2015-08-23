package com.dragonlz.oxygenread.UI.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
public class FindMovieFragment extends Fragment implements View.OnClickListener {

    private List mDataSet = new ArrayList();
    private ItemAdapter mMovieAdapter;
    private RecyclerView mMovieListView;
    private OkHttpClient client;
    private String readMovieData;
    private static final int UPDATA = 0;

    private LinearLayoutManager mLayoutManager;
    private EditText writeMovie;
    private Button mButton;
    private TextView historyFind1;
    private TextView historyFind2;
    private TextView historyFind3;
    private TextView historyFind4;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private List historyMessage = new ArrayList();

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.findmovie_fragment,container,false);
        mMovieListView = (RecyclerView) view.findViewById(R.id.findmovieList);
        pref = getActivity().getSharedPreferences("read",Context.MODE_PRIVATE) ;
        editor = pref.edit();
        setHisotryData(view);
        setData();
        return view;
    }

    private void setHisotryData(View view) {
        writeMovie = (EditText) view.findViewById(R.id.et_findmovie);
        mButton = (Button) view.findViewById(R.id.bt_findmovie);

        historyFind1 = (TextView) view.findViewById(R.id.tv_his1);
        historyFind2 = (TextView) view.findViewById(R.id.tv_his2);
        historyFind3 = (TextView) view.findViewById(R.id.tv_his3);
        historyFind4 = (TextView) view.findViewById(R.id.tv_his4);

        getTheHistory();

        mButton.setOnClickListener(this);
        historyFind1.setOnClickListener(this);
        historyFind2.setOnClickListener(this);
        historyFind3.setOnClickListener(this);
        historyFind4.setOnClickListener(this);
    }

    private void setData(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        mMovieListView.setLayoutManager(mLayoutManager);
        mMovieAdapter = new ItemAdapter(getActivity());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_findmovie:
                String themovie = writeMovie.getText().toString();
                int historyNumber = pref.getInt("historyNumber", 0);
                if ( historyNumber< 4) {
                    if ((themovie != pref.getString("findmovie1", "")) && (themovie != pref.getString("findmovie2", ""))
                            && (themovie != pref.getString("findmovie3", "")) && (themovie != pref.getString("findmovie4", ""))) {
                        historyMessage.add(themovie);
                        for (int i = historyNumber; i < historyMessage.size(); i++) {
                            editor.putString("findmovie" + String.valueOf(i), (String) historyMessage.get(i));
                            editor.putInt("historyNumber", i);
                        }
                    }
                }else {
                    editor.putString("findmovie1",themovie);
                    editor.putString("findmovie2", (String) historyMessage.get(0));
                    editor.putString("findmovie3", (String) historyMessage.get(1));
                    editor.putString("findmovie4", (String) historyMessage.get(2));
                }
                editor.commit();
                mMovieListView.setVisibility(View.VISIBLE);
                init(themovie);
                break;
            case R.id.tv_his1:
                if (historyFind1.getText() != null) {
                    String gethistorymovie1 = historyFind1.getText().toString();
                    writeMovie.setText(gethistorymovie1);
                }
                break;
            case R.id.tv_his2:
                if (historyFind1.getText() != null) {
                    String gethistorymovie2 = historyFind2.getText().toString();
                    writeMovie.setText(gethistorymovie2);
                }
                break;
            case R.id.tv_his3:
                if (historyFind3.getText() != null) {
                    String gethistorymovie3 = historyFind3.getText().toString();
                    writeMovie.setText(gethistorymovie3);
                }
                break;
            case R.id.tv_his4:
                if (historyFind4.getText() != null) {
                    String gethistorymovie4 = historyFind4.getText().toString();
                    writeMovie.setText(gethistorymovie4);
                }
                break;

        }
    }

    public void getTheHistory() {
        historyFind1.setText(pref.getString("findmovie1",""));
        historyFind2.setText(pref.getString("findmovie2",""));
        historyFind3.setText(pref.getString("findmovie3",""));
        historyFind4.setText(pref.getString("findmovie4",""));
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
//            itemViewHolder.moviePlayDate.setText(movie.getMoviePlayDate());
//            itemViewHolder.moviePlayTime.setText(movie.getMoviePlayTime());
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
