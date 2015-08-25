package com.dragonlz.oxygenread.UI.Fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dragonlz.oxygenread.R;
import com.dragonlz.oxygenread.UI.Acitvity.SampleItemAnimator;
import com.dragonlz.oxygenread.UI.Model.Reflect;
import com.dragonlz.oxygenread.UI.Utils.GetUrlUtil;
import com.dragonlz.oxygenread.UI.Utils.ParseUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.race604.flyrefresh.FlyRefreshLayout;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import org.apache.http.Header;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class ReflectFragment extends Fragment implements FlyRefreshLayout.OnPullRefreshListener {


    private FlyRefreshLayout mFlylayout;
    private RecyclerView mListView;
    private LinearLayoutManager mLayoutManager;
    private List mDataSet = new ArrayList<>();
    private List theAllDataList = new ArrayList();
    private ItemAdapter mAdapter;
    private Handler mHandler = new Handler();
    private OkHttpClient client = new OkHttpClient();
    private String readReflectData;
    private MediaPlayer mediaPlayer;
    private int page=1;

    private int theStart;
    private int theEnd;
    private int contentNumber;
    private int thisNumber;

    private AsyncHttpClient asyncHttpClient;

    private static final int UPDATA = -1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATA:
                    ParseUtil reflect = new ParseUtil();
                    String data = readReflectData;
                    Log.d("data", data);
                    theAllDataList = reflect.parseReflect(data);
                    contentNumber = theAllDataList.size();
                    setListData();
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View newsView = inflater.inflate(R.layout.reflect_fragment, container, false);
        init(page);
        initData(newsView);
        return newsView;
    }

    public void initDataSet(String url){
        final Request request = new Request.Builder().url(url)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    readReflectData = response.body().string();
                    Message message = new Message();
                    message.what = UPDATA;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setListData() {
        for (int a = theStart; a < theEnd && a< contentNumber ; a++) {
            mDataSet.add(theAllDataList.get(a));
            thisNumber++;
        }
        theStart += 5;
        theEnd += 5;
        if (mDataSet != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void init(int page){
        theStart = 0;
        theEnd = 5;
        thisNumber = 0;
        contentNumber = 0;
        GetUrlUtil getUrlUtil = new GetUrlUtil();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");//时间显示格式
        Date curDate = new Date(System.currentTimeMillis());
        String Time = formatter.format(curDate);

        String url = getUrlUtil.getReflectUrl(String.valueOf(page), Time);//这里填加载第几页
        initDataSet(url);
    }

    private void initData(View v) {

        mFlylayout = (FlyRefreshLayout) v.findViewById(R.id.fly_layout);

        mFlylayout.setOnPullRefreshListener(this);

        mListView = (RecyclerView) v.findViewById(R.id.reflectList);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mListView.setLayoutManager(mLayoutManager);
        mAdapter = new ItemAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setItemAnimator(new SampleItemAnimator());

        mListView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                    if (mLayoutManager.findLastCompletelyVisibleItemPosition() == (contentNumber-1)) {
                        Toast.makeText(getActivity(),"正在加载下一页",Toast.LENGTH_SHORT).show();
                        mDataSet.clear();
                        page++;
                        init(page);
                    } else {
                        if (mLayoutManager.findLastCompletelyVisibleItemPosition() == (thisNumber-1)) {
                            setListData();
                        }
                    }
                }
        });
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
        mDataSet.clear();
        page = 1;
        init(page);
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
            View view = mInflater.inflate(R.layout.reflect_item, viewGroup, false);
            return new ItemViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }

        @Override
        public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
            final Reflect theReflect = (Reflect) mDataSet.get(i);

            if (theReflect.getUserHeader().equals("")) {
                itemViewHolder.userHead.setImageResource(R.mipmap.eroo);
            }else {
                Picasso.with(getActivity()).load(theReflect.getUserHeader()).placeholder(R.mipmap.wait).error(R.mipmap.eroo).into(itemViewHolder.userHead);
            }
            itemViewHolder.userName.setText(theReflect.getUserName());
            itemViewHolder.creatTime.setText(theReflect.getCreateTime());
            itemViewHolder.Love.setText(theReflect.getLove());
            itemViewHolder.Hate.setText(theReflect.getHate());
            itemViewHolder.textContent.setText(theReflect.getTextContent());
            if (theReflect.getType() == 29){
                itemViewHolder.jpgImageView.setVisibility(View.GONE);
                itemViewHolder.gifImageView.setVisibility(View.GONE);
            } else if(theReflect.getType() != 29) {
                if (theReflect.getType() == 10) {
                    String imageurl = theReflect.getContentImage();
                    if (imageurl != null) {
                        if (imageurl.indexOf(".jpg") != -1) {
                            Log.d(" setJPG", theReflect.getContentImage());
                            Picasso.with(getActivity()).load(imageurl).into(itemViewHolder.jpgImageView);
                            itemViewHolder.gifImageView.setVisibility(View.GONE);
                        } else if (imageurl.indexOf(".gif") != -1) {
                            Log.d(" setGifImage", theReflect.getContentImage());
                            itemViewHolder.jpgImageView.setVisibility(View.GONE);
                            setGifImage(imageurl, itemViewHolder.gifImageView);
                        }
                    }
                } else if (theReflect.getType() == 31) {
                    itemViewHolder.jpgImageView.setImageResource(R.mipmap.ic_launcher);
                    itemViewHolder.gifImageView.setVisibility(View.GONE);
                    itemViewHolder.jpgImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            playMusic(theReflect.getContentVoice());
                        }
                    });
                }
            }

        }

    }

        private void setGifImage(String url, final GifImageView gifImageView) {
            asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient
                    .get(url,
                            new AsyncHttpResponseHandler() {

                                @Override
                                public void onSuccess(int arg0, Header[] arg1,
                                                      byte[] arg2) {

                                    GifDrawable drawable = null;
                                    try {
                                        drawable = new GifDrawable(arg2);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    gifImageView.setBackgroundDrawable(drawable);
                                }

                                @Override
                                public void onFailure(int arg0, Header[] arg1,
                                                      byte[] arg2, Throwable arg3) {

                                    Toast.makeText(getActivity(),
                                            "加载GIF图片出错", Toast.LENGTH_SHORT).show();
                                }
                            });
        }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView creatTime;
        TextView textContent;
        ImageView userHead;
        ImageView jpgImageView;
        GifImageView gifImageView;
        TextView Love;
        TextView Hate;

        public ItemViewHolder(View itemView) {
            super(itemView);
            userHead = (ImageView) itemView.findViewById(R.id.iv_useHead);
            userName = (TextView) itemView.findViewById(R.id.tv_userName);
            creatTime = (TextView) itemView.findViewById(R.id.tv_creatTime);
            textContent = (TextView) itemView.findViewById(R.id.tv_reflectcontent);
            jpgImageView = (ImageView) itemView.findViewById(R.id.iv_reflectImage);
            gifImageView = (GifImageView) itemView.findViewById(R.id.gifView);
            Love = (TextView) itemView.findViewById(R.id.tv_reflectLove);
            Hate = (TextView) itemView.findViewById(R.id.tv_reflectHate);
        }
    }


    /**
     *
     * 播音乐
     *
     */
    private void playMusic(String musicUrl) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(musicUrl);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
