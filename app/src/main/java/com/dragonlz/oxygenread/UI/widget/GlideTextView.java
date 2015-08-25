package com.dragonlz.oxygenread.UI.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sdm on 2015/8/24.
 */
public class GlideTextView extends TextView {

    public GlideTextView(Context context) {
        super(context);
    }

    public GlideTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GlideTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
