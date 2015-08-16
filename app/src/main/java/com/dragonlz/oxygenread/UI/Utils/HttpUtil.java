package com.dragonlz.oxygenread.UI.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ASUS on 2015/5/30.
 */
public class HttpUtil {

    public static void sendHttpRequest(final boolean needApikey,final String address,final HttoCallbackListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    if (needApikey)
                        connection.setRequestProperty("apikey", "a535145037f63f973c5aad9e7ba1331d");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    // 下面对获取到的输入流进行读取
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener !=null){

                           listener.onFinish(response.toString());
                    }

                }catch (Exception e){

                    if (listener != null){
                        listener.onError(e);
                    }
                }finally {
                    if (connection != null) {
                        connection.disconnect();
                     }
                   }
                }

                }).start();
            }

    public static void sendHttpRequestForPicfinal(final String address,final HttoCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL Url = new URL(address);
                    connection = (HttpURLConnection) Url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream inputStream = connection.getInputStream();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    options.inSampleSize = 3;
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    if (listener != null) {
                        listener.onBitmap(bitmap);
                    }
                    inputStream.close();
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    connection.disconnect();
                }
            }
        }).start();
  }


    public static void sendHttpRequestForBigPic(final String address, final HttoCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL Url = new URL(address);
                    connection = (HttpURLConnection) Url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream inputStream = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (listener != null) {
                            listener.onBitmap(bitmap);
                        }
                    inputStream.close();
                    }catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    connection.disconnect();
                }
            }
        }).start();
    }
}


