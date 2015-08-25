package com.dragonlz.oxygenread.UI.Acitvity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dragonlz.oxygenread.R;

/**
 * Created by Dragon丶Lz on 2015/8/23.
 */
public class AboutActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("关于我们");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
