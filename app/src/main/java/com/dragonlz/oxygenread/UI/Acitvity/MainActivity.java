package com.dragonlz.oxygenread.UI.Acitvity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dragonlz.oxygenread.R;
import com.dragonlz.oxygenread.UI.Fragment.NewsFragment;
import com.dragonlz.oxygenread.UI.Model.Function;
import com.dragonlz.oxygenread.UI.Model.PrefConstants;
import com.dragonlz.oxygenread.UI.Utils.SAppUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private FragmentManager fragmentManager;
    private static final int NEWS_FRAGMENT= 0;

    private Toolbar mToorbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * 初始化需要的Fragment
     */

    private NewsFragment newsFragment;


    private ListView function_listview;
    private List<Function> functionList = new ArrayList<>();
    private FunctionAdapter functionAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkShowTutorial();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        fragmentManager = getFragmentManager();
        setFragmentSelect(NEWS_FRAGMENT);
    }

    private void initData(){
        /**
         * 进行findviewById 等一些操作
         */

        mToorbar = (Toolbar) findViewById(R.id.toolbar);
        mToorbar.setTitle("Oxygen阅读");
        setSupportActionBar(mToorbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToorbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        initList();
        function_listview= (ListView) findViewById(R.id.lv_function);
        functionAdapter = new FunctionAdapter(MainActivity.this,R.layout.function_item,functionList);
        function_listview.setAdapter(functionAdapter);

    }




    private void setFragmentSelect(int index){
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        hideFragment(transaction);

        switch(index){
            case NEWS_FRAGMENT:

                if ( newsFragment== null) {

                    newsFragment = new NewsFragment();
                    transaction.add(R.id.content, newsFragment);
                } else {

                    transaction.show(newsFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction){

         if ( newsFragment!= null) {
            transaction.hide(newsFragment);
           }

    }

    private void initList(){
        Function main = new Function("新闻",R.mipmap.news);
        functionList.add(main);
    }
    class FunctionAdapter extends ArrayAdapter<Function>{

        private int resourceId;
        private Context mContext;
        public FunctionAdapter(Context context, int resource, List<Function> objects) {
            super(context, resource, objects);
            resourceId =resource;
            mContext =context;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Function function =getItem(position);
            String name = function.getName();
            int image = function.getImageId();
            View view;
            ViewHolder viewHolder;

            if (convertView ==null){
                view = LayoutInflater.from(getContext()).inflate(resourceId,null);
                viewHolder = new ViewHolder();
                viewHolder.functionName = (TextView) view.findViewById(R.id.function_text);
                viewHolder.functionImage = (ImageView) view.findViewById(R.id.function_image);
                view.setTag(viewHolder);
            }else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.functionName.setText(name);
            viewHolder.functionImage.setImageResource(image);
            return view;
        }

        class ViewHolder{

            TextView functionName;
            ImageView functionImage;
        }
        }


    private void checkShowTutorial(){
        int oldVersionCode = PrefConstants.getAppPrefInt(this, "version_code");
        int currentVersionCode = SAppUtil.getAppVersionCode(this);
        if(currentVersionCode>oldVersionCode){
            startActivity(new Intent(MainActivity.this,WelcomeActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            PrefConstants.putAppPrefInt(this, "version_code", currentVersionCode);
        }
    }
    }



