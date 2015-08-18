package com.dragonlz.oxygenread.UI.Utils;

import com.dragonlz.oxygenread.UI.confilg.API;

import javax.net.ssl.SSLContext;

/**
 * Created by sdm on 2015/8/17.
 */
public class GetUrlUtil {

    private static final String Page = "page=";
    private static final String title = "title=";
    private static final String type = "type=";

    private static final String area = "area=";
    private static final String areaid = "areaid=";
    private static final String needMoreDay = "needMoreDay=0";
    private static final String needIndex = "needIndex=0";

    private static final String cinemaName_or_movieName = "wd=";
    private static final String theCity = "location=";
    private static final String everyPage_messageNumber = "rn=15";
    private static final String output = "output=json";
    private static final String coord_type = "coord_type=bd09ll";
    private static final String out_coord_type = "out_coord_type=bd09ll";
    private static final String pageNumber = "pn=";

    private static final String showapi_appid = "showapi_appid=6066";
    private static final String showapi_timestamp = "showapi_timestamp=";
    private static final String showapi_sign = "showapi_sign=d1dee7b168d0477495c27fb73d912ba4";

    private String getHistoryUrl(String time){
        String historyUrl = API.getHistoryApi() + "?" + showapi_appid + "&" + showapi_timestamp + time + "&" + showapi_sign;
        return historyUrl;
    }

    private String getReflectUrl(String page, String time){
        String reflectUrl = API.getReflectApi() + "?" + Page + "&" + showapi_appid +
                "&" + showapi_timestamp + time + "&" + title + "&" + type + "&" + showapi_sign;
        return reflectUrl;
    }

    private String getWeatherCityIdUrl(String city){
        String cityIdUrl = API.getAreaidApi() + "?" + area + city;
        return cityIdUrl;
    }
    private String getWeatherUrl(String city,String cityId){
        String weatherUrl = API.getWeatherApi() + "?" + area + city + "&" + areaid
                + cityId + "&" + needMoreDay + "&" + needIndex;
                ;
        return weatherUrl;
    }

    private String getCinemaUrl(String cinemaName,String city){
        String CinemaUrl = API.getCinemaApi() + "?" + cinemaName_or_movieName
                + cinemaName + "&" + theCity + city + "&" + everyPage_messageNumber
                + "&" + output + "&" + coord_type + "&" + out_coord_type;
        return CinemaUrl;
    }

    private String getMovieUrl(String movieName,String city,String page){
        String MovieUrl = API.getMoviewApi() + "?" + cinemaName_or_movieName
                + movieName + "&" + theCity + city + "&" + pageNumber + page + "&" + everyPage_messageNumber
                + "&" + output + "&" + coord_type + "&" + out_coord_type;
        return MovieUrl;
    }
}
