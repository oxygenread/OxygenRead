package com.dragonlz.oxygenread.UI.Utils;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.dragonlz.oxygenread.UI.Model.AreaId;
import com.dragonlz.oxygenread.UI.Model.HealthyNotice;
import com.dragonlz.oxygenread.UI.Model.HistoryToday;
import com.dragonlz.oxygenread.UI.Model.Movie;
import com.dragonlz.oxygenread.UI.Model.Reflect;
import com.dragonlz.oxygenread.UI.Model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ParseUtil {

    private String parseJson(String jsonData) {
        String showapi_res_body = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            String showapi_res_code = jsonObject.getString("showapi_res_code");
            String showapi_res_error = jsonObject.getString("showapi_res_error");
            if (showapi_res_code != "0") {
                //请求失败
                return showapi_res_error;
            }
            showapi_res_body = jsonObject.getString("showapi_res_body");
            Log.d("showapi_res_body",showapi_res_body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return showapi_res_body;
    }

    /**
     *
     * 百思不解
     * 解析
     *
     */

        private Reflect reflect = new Reflect();

        public Reflect parseReflect(String Data) {
            parseReflectTwoJson(parseJson(Data));
            return reflect;
        }

        private void parseReflectTwoJson(String jsonData) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonData);
                String pagebean = jsonObject.getString("pagebean");//本页内容
                parseReflectThereJson(pagebean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void parseReflectThereJson(String jsonData) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonData);
                String allNum = jsonObject.getString("allNum");//所有数量
                String allPages = jsonObject.getString("allPages");//所有页数
                String contentlist = jsonObject.getString("contentlist");
                String currentPage = jsonObject.getString("currentPage");//当前页数
                String maxResult = jsonObject.getString("maxResult");//每页最多数量

                reflect.setAllNumber(allNum);
                reflect.setAllPages(allPages);
                reflect.setThisPage(currentPage);

                parseReflectArryJson(contentlist);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void parseReflectArryJson(String jsonData) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String type = jsonObject.getString("type");
                    int succeed = Integer.parseInt(type);
                    if (succeed == 10) {//图片
                        String image = jsonObject.getString("image0");
                        reflect.setContentImage(image);
                    } else if (succeed == 29) {//段子

                    } else if (succeed == 31) {//声音
                        String voicelength = jsonObject.getString("voicelength");//
                        String voicetime = jsonObject.getString("voicetime");//
                        String voiceuri = jsonObject.getString("voiceuri");//声音地址
                        reflect.setVoiceTime(voicetime);
                        reflect.setContentVoice(voiceuri);
                    } else if (succeed == 41) {//视屏
                        String videotime = jsonObject.getString("videotime");//
                        String videouri = jsonObject.getString("videouri");//视屏地址
                        reflect.setVideoTime(videotime);
                        reflect.setContentVideo(videouri);
                    }
                    String create_time = jsonObject.getString("create_time");//创建时间
                    String hate = jsonObject.getString("hate");//踩
                    String name = jsonObject.getString("name");//发布人
                    String love = jsonObject.getString("love");//点赞
                    String profile_image = jsonObject.getString("profile_image");//头像
                    String text = jsonObject.getString("text");//标题或文本的内容(段子)

                    reflect.setCreateTime(create_time);
                    reflect.setHate(hate);
                    reflect.setLove(love);
                    reflect.setUserName(name);
                    reflect.setUserHeader(profile_image);
                    reflect.setTextContent(text);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }


    /**
     *
     * 历史上的今天
     * 解析
     *
     */

        int year;
    List historyList = new ArrayList();

        public List parseHistory(String Data) {
            parseHistoryTwoJson(parseJson(Data));
            return historyList;
        }

        private void parseHistoryTwoJson(String jsonData) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonData);

                //获取当前年份
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);

                for (int i = 0; i < year; i++) {
                    HistoryToday historyToday = new HistoryToday();
                    if (!jsonObject.isNull(String.valueOf(i))) {//判断该节点是否为空
                        String thing = jsonObject.getString(String.valueOf(i));
                        historyToday.setTheYear( "(≧▽≦)年份：" + String.valueOf(i));
                        historyToday.setContent(thing);

                        historyList.add(historyToday);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }


    /**
     * 城市id、
     * 天气
     * 解析
     *
     */


        AreaId areaId = new AreaId();
        Weather weather = new Weather();

        public AreaId getAreaId(String Data) {
            parseAreaIdJson(parseJson(Data));
            return areaId;
        }

        public Weather getWeather(String Data){
            parseWeatherJson(parseJson(Data));
            return weather;
        }

    private void parseAreaIdJson(String jsonData){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            String list = jsonObject.getString("list");
            parseAreaIdArrayJson(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

        private void parseAreaIdArrayJson(String jsonData){
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(jsonData);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String area = jsonObject.getString("area");
                String areaid = jsonObject.getString("areaid");
                areaId.setCity(area);
                areaId.setCityId(areaid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void parseWeatherJson(String jsonData) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonData);
                String cityInfo = jsonObject.getString("cityInfo");//城市（国家、省、市...）
                String todayWeather = jsonObject.getString("f1");//时时天气
                parseWeatherTwoJson(todayWeather);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        private void parseWeatherTwoJson(String jsonData) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonData);
                String day = jsonObject.getString("day");
                String day_air_temperature = jsonObject.getString("day_air_temperature");//日间温度
                String night_air_temperature = jsonObject.getString("night_air_temperature");//晚间温度
                String day_weather = jsonObject.getString("day_weather");//日间天气
                String day_weather_pic = jsonObject.getString("day_weather_pic");//日间天气图像
                String night_weather = jsonObject.getString("night_weather");//晚间天气
                String night_weather_pic = jsonObject.getString("night_weather_pic");//晚间天气图像
                String day_wind_power = jsonObject.getString("day_wind_power");//日间风速
                String night_wind_power = jsonObject.getString("night_wind_power");//晚上间风速
                String weekday = jsonObject.getString("weekday");//日期
                switch (weekday){
                    case "1":
                        weekday = "星期一";
                        break;
                    case "2":
                        weekday = "星期二";
                        break;
                    case "3":
                        weekday = "星期三";
                        break;
                    case "4":
                        weekday = "星期四";
                        break;
                    case "5":
                        weekday = "星期五";
                        break;
                    case "6":
                        weekday = "星期六";
                        break;
                    case "7":
                        weekday = "星期日";
                        break;
                }
                weather.setDate(day);
                weather.setDayTemperature(day_air_temperature);
                weather.setDayweather(day_weather);
                weather.setDayWeatherImage(day_weather_pic);
                weather.setDayWindPower(day_wind_power);
                weather.setNightTemperature(night_air_temperature);
                weather.setNightweather(night_weather);
                weather.setNightWeatherImage(night_weather_pic);
                weather.setNightWindPower(night_wind_power);
                weather.setWeekday(weekday);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    /**
     *
     * 影视查询
     *
     */


        Movie movie = new Movie();

        public Movie parseMovie(String Data){
            parseMovieJson(Data);
            return movie;
        }

        private void parseMovieJson(String jsonData) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonData);
                String error = jsonObject.getString("error");
                String status = jsonObject.getString("status");
                String result = jsonObject.getString("result");
                parseMovieArrayJson(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void parseMovieArrayJson(String jsonData){
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(jsonData);
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    String telephone = jsonObject.getString("telephone");
                    String address = jsonObject.getString("address");
                    movie.setCinema(name);
                    movie.setCinemaPhone(telephone);
                    movie.setCinemaAddress(address);
                    if (!jsonObject.isNull("time_table")) {
                        String time_table = jsonObject.getString("time_table");
                        parseMoviePlayArrayJson(time_table);
                    }else {
                        String review = jsonObject.getString("review");
                        String movies = jsonObject.getString("movies");
                        parseMovieTwoArrayJson(movies);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void parseMovieTwoArrayJson(String jsonData){
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(jsonData);
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String movie_name = jsonObject.getString("movie_name");
                    String movie_type = jsonObject.getString("movie_type");
                    String movie_director = jsonObject.getString("movie_director");
                    String movie_starring = jsonObject.getString("movie_starring");
                    String movie_release_date = jsonObject.getString("movie_release_date");
                    String movie_picture = jsonObject.getString("movie_picture");
                    String movie_length = jsonObject.getString("movie_length");
                    String movie_description = jsonObject.getString("movie_description");
                    String movie_score = jsonObject.getString("movie_score");
                    String movie_message = jsonObject.getString("movie_message");
                    String movie_tags = jsonObject.getString("movie_tags");
                    String time_table = jsonObject.getString("time_table");

                    movie.setMovieName(movie_name);
                    movie.setMovieType(movie_type);
                    movie.setMovieDirector(movie_director);
                    movie.setMovieStar(movie_starring);
                    movie.setMovieFirstDate(movie_release_date);
                    movie.setMoviePicture(movie_picture);
                    movie.setMovieLength(movie_length);
                    movie.setMovieDescrption(movie_description);
                    movie.setMovieComment(movie_score);
                    movie.setMovieMessage(movie_message);
                    movie.setMovieStyle(movie_tags);

                    parseMoviePlayArrayJson(time_table);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void parseMoviePlayArrayJson(String jsonData){
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(jsonData);
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String time = jsonObject.getString("time");
                    String date = jsonObject.getString("date");

                    movie.setMovieStartTime(time);
                    movie.setMoviePlayDate(date);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    /**
     *
     * 养生知识
     *
     */

    List healthyList = new ArrayList<>();

        public List parseHealthy(String Data){
            parseHealthyJson(parseJson(Data));
            return healthyList;
        }

    private void parseHealthyJson(String jsonData) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            String pagebean = jsonObject.getString("pagebean");
            parseHealthyTwoJson(pagebean);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

        private void parseHealthyTwoJson(String jsonData) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonData);
                String allNum = jsonObject.getString("allNum");//共几条
                String allPages = jsonObject.getString("allPages");//共几页
                String contentlist = jsonObject.getString("contentlist");
                parseHealthyArrayJson(contentlist);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        private void parseHealthyArrayJson(String jsonData){
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(jsonData);
                for (int i = 0; i <jsonArray.length() ; i++) {
                    HealthyNotice healthyNotice = new HealthyNotice();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String creatTime = jsonObject.getString("ctime");
                    String title = jsonObject.getString("title");
                    String content = jsonObject.getString("intro");
                    String media_name = jsonObject.getString("media_name");//出处
                    String url = jsonObject.getString("media_name");//来源地址

                    healthyNotice.setCreatTime(creatTime);
                    healthyNotice.setTitle(title);
                    healthyNotice.setContent(content);
                    healthyNotice.setFrom(media_name);
                    healthyNotice.setFromUrl(url);
                    healthyList.add(healthyNotice);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
}
