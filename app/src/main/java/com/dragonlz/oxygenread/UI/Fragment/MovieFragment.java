package com.dragonlz.oxygenread.UI.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dragonlz.oxygenread.R;
import com.dragonlz.oxygenread.UI.widget.PagerSlidingTabStrip;

/**
 * Created by sdm on 2015/8/20.
 */
public class MovieFragment extends Fragment {

    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;

    private FindMovieFragment findmovie ;
    private FindCinameFragment findCiname ;

    private String[] titles = {"搜影院","搜电影"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_fragment,container,false);


        mPagerSlidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);

        mViewPager.setAdapter(new MyAdapter(getActivity().getSupportFragmentManager(),titles));

        mPagerSlidingTabStrip.setViewPager(mViewPager);

        initTabsValue();

        return view;
    }


    /**
     *
     * pager
     *
     */


    private void initTabsValue() {

        // 底部游标颜色

        mPagerSlidingTabStrip.setIndicatorColor(getResources().getColor(R.color.light_orange));

        // tab的分割线颜色

        mPagerSlidingTabStrip.setDividerColor(getResources().getColor(R.color.blue_sky));

        // tab背景

        mPagerSlidingTabStrip.setBackgroundColor(getResources().getColor(R.color.blue_sky));

        // tab底线高度

        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,

                1, getResources().getDisplayMetrics()));

        // 游标高度

        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,

                2, getResources().getDisplayMetrics()));



        //字体大小

        mPagerSlidingTabStrip.setTextSize(40);



        // 选中的文字颜色

        mPagerSlidingTabStrip.setSelectedTextColor(getResources().getColor(R.color.gray));

        // 正常文字颜色

        mPagerSlidingTabStrip.setTextColor(getResources().getColor(R.color.white));

    }



    public class MyAdapter extends FragmentPagerAdapter {

        String[] _titles;


        public MyAdapter(FragmentManager fragmentManager, String[] titles) {
            super(fragmentManager);
            _titles=titles;
        }


        @Override

        public CharSequence getPageTitle(int position) {

            return _titles[position];

        }



        @Override

        public int getCount() {

            return _titles.length;

        }



        @Override

        public android.support.v4.app.Fragment getItem(int position) {

            switch (position) {

                case 0:

                    if (findCiname == null) {

                        findCiname = new FindCinameFragment();

                    }

                    return findCiname;

                case 1:

                    if (findmovie == null) {

                        findmovie = new FindMovieFragment();

                    }

                    return findmovie;

                default:

                    return null;

            }

        }

    }

}
