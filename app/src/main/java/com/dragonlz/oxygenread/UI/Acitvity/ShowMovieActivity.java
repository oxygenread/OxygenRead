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
 * Created by sdm on 2015/8/24.
 */
public class ShowMovieActivity extends AppCompatActivity {

    private static List mDataSet = new ArrayList();
    private ItemAdapter mMovieAdapter;
    private RecyclerView mMovieListView;

    private LinearLayoutManager mLayoutManager;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showmovie);
        mMovieListView = (RecyclerView) findViewById(R.id.showmovieList);
        setData();
    }

    public static void getMovieContent(List list){
        mDataSet = list;
    }

    private void setData(){
        mLayoutManager = new LinearLayoutManager(this);
        mMovieListView.setLayoutManager(mLayoutManager);
        mMovieAdapter = new ItemAdapter(this);
        mMovieListView.setAdapter(mMovieAdapter);
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
            View view = mInflater.inflate(R.layout.showmovie_item, viewGroup, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
            Movie.MovieContent content = (Movie.MovieContent) mDataSet.get(i);
                itemViewHolder.movieName.setText(content.getMovieName());
                itemViewHolder.movieArea.setText(content.getMovieProduceArea());
                itemViewHolder.movieStar.setText(content.getMovieStar());
                itemViewHolder.movieDirector.setText(content.getMovieDirector());
                itemViewHolder.movieDescrption.setText(content.getMovieDescrption());
                Picasso.with(ShowMovieActivity.this).load(content.getMoviePicture()).into(itemViewHolder.movieImage);
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
            movieDirector = (TextView) itemView.findViewById(R.id.tv_moviedir);
            movieDescrption = (TextView) itemView.findViewById(R.id.tv_movieDescrption);
            movieImage = (ImageView) itemView.findViewById(R.id.iv_movieImage);
        }
    }
}
