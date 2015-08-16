package com.dragonlz.oxygenread.UI.confilg;

/**
 * Created by sdm on 2015/8/10.
 */
public class API {
    /**
     *      新闻频道
     */
    private static final String NEWS_API = "http://apis.baidu.com/showapi_open_bus/channel_news/channel_news";

    /**
     *      历史上的今天
     */
    private static final String HISTORY_API = "http://route.showapi.com/148-1";

    /**
     *      百思不解
     */
    private static final String REFLECT_API = "https://route.showapi.com/255-1";

    /**
     *      天气
     */
        //城市名字对应的id   API
    private static final String AREAID_API = " http://apis.baidu.com/showapi_open_bus/weather_showapi/areaid";
        //天气API
    private static final String WEATHER_API = "http://apis.baidu.com/showapi_open_bus/weather_showapi/address";

    /**
     *      影院查询
     *      电影查询
     */
    private static final String CINEMA_API = "http://apis.baidu.com/apistore/movie/cinema";
    private static final String MOVIEW_API = "http://apis.baidu.com/apistore/movie/film";

    public static String getNewsApi() {
        return NEWS_API;
    }

    public static String getHistoryApi() {
        return HISTORY_API;
    }

    public static String getReflectApi() {
        return REFLECT_API;
    }

    public static String getAreaidApi() {
        return AREAID_API;
    }

    public static String getWeatherApi() {
        return WEATHER_API;
    }

    public static String getCinemaApi() {
        return CINEMA_API;
    }

    public static String getMoviewApi() {
        return MOVIEW_API;
    }
}
