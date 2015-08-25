package com.dragonlz.oxygenread.UI.Acitvity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.dragonlz.oxygenread.R;
import com.dragonlz.oxygenread.UI.Fragment.EssayFragment;
import com.dragonlz.oxygenread.UI.Fragment.HealthyFragment;
import com.dragonlz.oxygenread.UI.Fragment.HistoryTodayFragment;
import com.dragonlz.oxygenread.UI.Fragment.MovieFragment;
import com.dragonlz.oxygenread.UI.Fragment.ReflectFragment;
import com.dragonlz.oxygenread.UI.Model.AreaId;
import com.dragonlz.oxygenread.UI.Model.Function;
import com.dragonlz.oxygenread.UI.Model.PrefConstants;
import com.dragonlz.oxygenread.UI.Model.Weather;
import com.dragonlz.oxygenread.UI.Utils.GetUrlUtil;
import com.dragonlz.oxygenread.UI.Utils.ParseUtil;
import com.dragonlz.oxygenread.UI.Utils.SAppUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private FragmentManager fragmentManager;

    private static final int REFLECT = 0;
    private static final int MOVIEW = 1;
    private static final int HEALTHY = 2;
    private static final int HISTORYTODAY = 3;
    private static final int ESSAY = 4;

    private Toolbar mToorbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private ImageView mImageView;
    private TextView weatherCity;
    private TextView theWeather;
    private TextView weatherTem;
    private TextView windSpeed;
    private TextView weekday;
    private AppCompatEditText et_city;
    private LinearLayout finish_layout;
    private LinearLayout about_layout;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    /**
     * 初始化需要的Fragment
     */

    private ReflectFragment reflectFragment;
    private HistoryTodayFragment historyFragment;
    private HealthyFragment healthyFragment;
    private MovieFragment movieFragment;
    private EssayFragment essayFragment;

    private ListView function_listview;
    private List<Function> functionList = new ArrayList<>();
    private FunctionAdapter functionAdapter;

    private OkHttpClient client = new OkHttpClient();
    private String readWeather;
    private static final int UPDATA_CITYID = -1;
    private static final int UPDATA_WEATHER = 0;
    private boolean isFirst;
    private ShareActionProvider mShareActionProvider;


    AreaId areaid = new AreaId();
    Weather weather = new Weather();
    GetUrlUtil getUrl = new GetUrlUtil();
    ParseUtil parse = new ParseUtil();


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_CITYID:
                    String data = readWeather;
                    areaid = parse.getAreaId(data);
                    String city = areaid.getCity();
                    String cityId = areaid.getCityId();
                    getWeatherData(getUrl.getWeatherUrl(city, cityId), false);
                    break;
                case UPDATA_WEATHER:
                    String weatherData = readWeather;
                    Log.d("weatherData", weatherData);
                    weather = parse.getWeather(weatherData);
                    if (weather.getDayWeatherImage() != null) {
                        getWeatherImage(weather.getDayWeatherImage());
                    }
                    weatherCity.setText(areaid.getCity());
                    if (weather.getDayTemperature()!=null) {
                        weatherTem.setText(weather.getDayTemperature() + "℃");
                    }
                    theWeather.setText(weather.getDayweather());
                    windSpeed.setText(weather.getDayWindPower());
                    weekday.setText(weather.getWeekday());
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkShowTutorial();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("read",MODE_PRIVATE);
        editor = pref.edit();
        isFirst = pref.getBoolean("isFirstUse", true);
        if(isFirst){
            DiaLog();
        }else {

        }
        editor.putBoolean("isFirstUse", false);
        editor.commit();
        initData();
        fragmentManager = getSupportFragmentManager();
        setFragmentSelect(REFLECT);
        String city = pref.getString("city","");
        getWeatherData(getUrl.getWeatherCityIdUrl(city), true);

    }

    private void initData() {
        /**
         * 进行findviewById 等一些操作
         */

        mToorbar = (Toolbar) findViewById(R.id.toolbar);
        mToorbar.setTitle("Oxygen阅读");
        setSupportActionBar(mToorbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToorbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.change_city:
                        DiaLog();
                        break;

                    case R.id.checkVision:
                        Toast.makeText(MainActivity.this,"没有更新",Toast.LENGTH_SHORT).show();

                        break;
                }
                return true;
            }

        });
        mImageView = (ImageView) findViewById(R.id.iv_weather);
        weatherCity = (TextView) findViewById(R.id.tv_city);
        weatherTem = (TextView) findViewById(R.id.tv_temperature);
        theWeather = (TextView) findViewById(R.id.tv_weather);
        windSpeed = (TextView) findViewById(R.id.tv_wind);
        weekday = (TextView) findViewById(R.id.tv_weekend);
        about_layout = (LinearLayout) findViewById(R.id.about_layout);
        about_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutus = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(aboutus);
            }
        });
        finish_layout = (LinearLayout) findViewById(R.id.finish_layout);
        finish_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToorbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        initList();
        function_listview = (ListView) findViewById(R.id.lv_function);
        functionAdapter = new FunctionAdapter(MainActivity.this, R.layout.function_item, functionList);
        function_listview.setAdapter(functionAdapter);
        function_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        setFragmentSelect(REFLECT);
                        mDrawerLayout.closeDrawers();
                        break;
                    case 1:
                        setFragmentSelect(MOVIEW);
                        mDrawerLayout.closeDrawers();

                        break;
                    case 2:
                        setFragmentSelect(HISTORYTODAY);
                        mDrawerLayout.closeDrawers();

                        break;
                    case 3:
                        setFragmentSelect(HEALTHY);
                        mDrawerLayout.closeDrawers();

                        break;
                    case 4:
                        setFragmentSelect(ESSAY);
                        mDrawerLayout.closeDrawers();
                        break;
                }
            }
        });
    }


    public void getWeatherData(String url, final boolean isCityId) {
        final Request request = new Request.Builder().url(url)
                .header("apikey", "a535145037f63f973c5aad9e7ba1331d")
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    readWeather = response.body().string();
                    Message message = new Message();
                    if (isCityId) {
                        message.what = UPDATA_CITYID;
                    } else {
                        message.what = UPDATA_WEATHER;
                    }
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getWeatherImage(String url) {
        RequestQueue mQues = Volley.newRequestQueue(this);
        ImageLoader loader = new ImageLoader(mQues, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return null;
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
            }
        });
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(mImageView, 0, 0);
        loader.get(url, listener);
    }


    private void setFragmentSelect(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        hideFragment(transaction);

        switch (index) {

            case REFLECT:

                mToorbar.setTitle("百思不解");
                if (reflectFragment == null) {
                    reflectFragment = new ReflectFragment();
                    transaction.add(R.id.content, reflectFragment);
                } else {
                    transaction.show(reflectFragment);
                }
                break;

            case MOVIEW:

                mToorbar.setTitle("电影专区");
                if (movieFragment == null) {
                    movieFragment = new MovieFragment();
                    transaction.add(R.id.content, movieFragment);
                } else {
                    transaction.show(movieFragment);
                }
                break;

            case HISTORYTODAY:

                mToorbar.setTitle("历史上的今天");

                if (historyFragment == null) {

                    historyFragment = new HistoryTodayFragment();
                    transaction.add(R.id.content, historyFragment);
                } else {
                    transaction.show(historyFragment);
                }
                break;

            case HEALTHY:

                mToorbar.setTitle("养生妙招");

                if (healthyFragment == null) {

                    healthyFragment = new HealthyFragment();
                    transaction.add(R.id.content, healthyFragment);
                } else {
                    transaction.show(healthyFragment);
                }
                break;
            case ESSAY:

                mToorbar.setTitle("趣味段子");

                if (essayFragment == null) {

                    essayFragment = new EssayFragment();
                    transaction.add(R.id.content, essayFragment);
                } else {
                    transaction.show(essayFragment);
                }
                break;

            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {

        if (reflectFragment != null) {
            transaction.hide(reflectFragment);
        }

        if (movieFragment != null) {
            transaction.hide(movieFragment);
        }

        if (healthyFragment != null) {
            transaction.hide(healthyFragment);
        }

        if (historyFragment != null) {
            transaction.hide(historyFragment);
        }
        if (essayFragment != null) {
            transaction.hide(essayFragment);
        }
    }

    private void initList() {
        Function ReflectMain = new Function("百思不解", R.mipmap.bsbdj);
        Function movieMain = new Function("电影专区", R.mipmap.movie);
        Function historyMain = new Function("历史上的今天", R.mipmap.history);
        Function healthyMain = new Function("养生妙招", R.mipmap.news);
        Function storeMain = new Function("趣味段子", R.mipmap.essay);
        functionList.add(ReflectMain);
        functionList.add(movieMain);
        functionList.add(historyMain);
        functionList.add(healthyMain);
        functionList.add(storeMain);
    }

    class FunctionAdapter extends ArrayAdapter<Function> {

        private int resourceId;
        private Context mContext;

        public FunctionAdapter(Context context, int resource, List<Function> objects) {
            super(context, resource, objects);
            resourceId = resource;
            mContext = context;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Function function = getItem(position);
            String name = function.getName();
            int image = function.getImageId();
            View view;
            ViewHolder viewHolder;

            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, null);
                viewHolder = new ViewHolder();
                viewHolder.functionName = (TextView) view.findViewById(R.id.function_text);
                viewHolder.functionImage = (ImageView) view.findViewById(R.id.function_image);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.functionName.setText(name);
            viewHolder.functionImage.setImageResource(image);
            return view;
        }

        class ViewHolder {

            TextView functionName;
            ImageView functionImage;
        }
    }


    private void checkShowTutorial() {
        int oldVersionCode = PrefConstants.getAppPrefInt(this, "version_code");
        int currentVersionCode = SAppUtil.getAppVersionCode(this);
        if (currentVersionCode > oldVersionCode) {
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            PrefConstants.putAppPrefInt(this, "version_code", currentVersionCode);
        }
    }

    private void DiaLog(){
        View ChangeCity = LayoutInflater.from(this).inflate(R.layout.change_city, null);
        et_city = (AppCompatEditText) ChangeCity.findViewById(R.id.et_city);

        new MaterialDialog.Builder(this)
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
                        editor.putString("city",city_et);
                        editor.commit();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {

                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        /* ShareActionProvider配置 */
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu
                .findItem(R.id.action_share));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        mShareActionProvider.setShareIntent(intent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

}



