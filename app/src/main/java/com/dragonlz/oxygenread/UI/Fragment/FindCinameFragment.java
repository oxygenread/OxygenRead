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
import android.widget.ImageView;
import android.widget.TextView;

import com.dragonlz.oxygenread.R;
import com.dragonlz.oxygenread.UI.Model.Movie;
import com.dragonlz.oxygenread.UI.Utils.GetUrlUtil;
import com.dragonlz.oxygenread.UI.Utils.ParseUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by sdm on 2015/8/22.
 */
public class FindCinameFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.findciname_fragment,container,false);
        mMovieListView = (RecyclerView) view.findViewById(R.id.findcinameList);
        mButton = (Button) view.findViewById(R.id.bt_findciname);
        mEditText = (EditText) view.findViewById(R.id.et_findciname);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cinameName = mEditText.getText().toString();
                init(cinameName);
            }
        });
        setData();
        return view;
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
        mLayoutManager = new LinearLayoutManager(getActivity());
        mMovieListView.setLayoutManager(mLayoutManager);
        mMovieAdapter = new ItemAdapter(getActivity());
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
            View view = mInflater.inflate(R.layout.findciname_item, viewGroup, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
            Movie movie = (Movie) mDataSet.get(i);
            for (int a = 0; a < movie.getMovie().size() ; a++) {
                Movie.MovieContent content = (Movie.MovieContent) movie.getMovie().get(a);
                itemViewHolder.movieName.setText(content.getMovieName());
                itemViewHolder.movieArea.setText(content.getMovieProduceArea());
                itemViewHolder.movieStar.setText(content.getMovieStar());
                itemViewHolder.movieDirector.setText(content.getMovieDirector());
                itemViewHolder.movieDescrption.setText(content.getMovieDescrption());
                Picasso.with(getActivity()).load(content.getMoviePicture()).into(itemViewHolder.movieImage);
            }
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView movieName;
        TextView movieArea;
        TextView movieStar;
        TextView movieDirector;
        TextView movieDescrption;
        ImageView movieImage;


        public ItemViewHolder(View itemView) {
            super(itemView);
            movieName = (TextView) itemView.findViewById(R.id.tv_movieName);
            movieArea = (TextView) itemView.findViewById(R.id.tv_movieArea);
            movieStar = (TextView) itemView.findViewById(R.id.tv_movieStar);
            movieDirector = (TextView) itemView.findViewById(R.id.tv_movieStar);
            movieDescrption = (TextView) itemView.findViewById(R.id.tv_movieDescrption);
            movieImage = (ImageView) itemView.findViewById(R.id.iv_movieImage);
        }
    }
}
