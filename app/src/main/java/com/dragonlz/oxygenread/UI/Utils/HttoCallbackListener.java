package com.dragonlz.oxygenread.UI.Utils;

import android.graphics.Bitmap;

/**
 * Created by ASUS on 2015/5/30.
 */
public interface HttoCallbackListener {

    void onFinish(String response);

    void onError(Exception e);

    void onBitmap(Bitmap bitmap);
}
