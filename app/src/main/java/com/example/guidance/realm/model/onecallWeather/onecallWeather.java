package com.example.guidance.realm.model.onecallWeather;

import android.util.Log;

import com.example.guidance.realm.model.currentWeather.weather;

import java.util.ArrayList;

/**
 * Created by Conor K on 05/03/2021.
 */
public class onecallWeather {

    String TAG = "onecallWeather";


    private final double lon;
    private final double lat;
    private final String timezone;
    private final int timezone_offset;
    private final current current;
    private final ArrayList<minutely> minutely;
    private final ArrayList<hourly> hourly;
    private final ArrayList<daily> daily;
    private final ArrayList<alerts> alerts;


    public onecallWeather(double lon, double lat, String timezone, int timezone_offset, com.example.guidance.realm.model.onecallWeather.current current, ArrayList<com.example.guidance.realm.model.onecallWeather.minutely> minutely, ArrayList<com.example.guidance.realm.model.onecallWeather.hourly> hourly, ArrayList<com.example.guidance.realm.model.onecallWeather.daily> daily, ArrayList<com.example.guidance.realm.model.onecallWeather.alerts> alerts) {
        this.lon = lon;
        this.lat = lat;
        this.timezone = timezone;
        this.timezone_offset = timezone_offset;
        this.current = current;
        this.minutely = minutely;
        this.hourly = hourly;
        this.daily = daily;
        this.alerts = alerts;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public String getTimezone() {
        return timezone;
    }

    public int getTimezone_offset() {
        return timezone_offset;
    }

    public current getCurrent() {
        return current;
    }

    public ArrayList<minutely> getMinutely() {
        return minutely;
    }

    public ArrayList<hourly> getHourly() {
        return hourly;
    }

    public ArrayList<daily> getDaily() {
        return daily;
    }

    public ArrayList<alerts> getAlerts() {
        return alerts;
    }


    public void printOneCallWeather(onecallWeather test) {

        Log.d(TAG, test.getLat() + " " + test.getLon() + " " + test.getTimezone() + " " + test.getTimezone_offset());

        if (test.getCurrent() != null) {
            Log.d(TAG, test.getCurrent().getDt() + " " + test.getCurrent().getSunrise() + " " + test.getCurrent().getSunset() + " " + test.getCurrent().getTemp() + " " + test.getCurrent().getFeels_like() + " " + test.getCurrent().getPressure() + " " + test.getCurrent().getHumidity() + " " + test.getCurrent().getDw_point() + " " + test.getCurrent().getUvi() + " " + test.getCurrent().getClouds() + " " + test.getCurrent().getVisibility() + " " + test.getCurrent().getWind_speed() + " " + test.getCurrent().getWind_deg());
            ArrayList<weather> weatherArrayList = test.getCurrent().getWeather();
            for (weather t : weatherArrayList) {
                Log.d(TAG, "current weather " + t.getId() + " " + t.getMain() + " " + t.getDescription() + " " + t.getIcon());

            }
        }

        if (test.getMinutely() != null) {
            ArrayList<minutely> minutelyArrayList = test.getMinutely();

            for (minutely t : minutelyArrayList) {

                Log.d(TAG, "minutely " + t.getDt() + " " + t.getPrecipitation());
            }
        }


        if (test.getDaily() != null) {
            ArrayList<daily> dailyArrayList = test.getDaily();

            for (daily t : dailyArrayList) {

                Log.d(TAG, "daily " + t.getDt() + " " + t.getSunrise() + " " + t.getSunset() + " " + t.getTemp().getDay() + " " + t.getTemp().getMin() + " " + t.getTemp().getMax() + " " + t.getTemp().getNight() + " " + t.getTemp().getEve() + " " + t.getTemp().getMorn() + " " + t.getFeels_like().getDay() + " " + t.getFeels_like().getNight() + " " + t.getFeels_like().getEve() + " " + t.getFeels_like().getMorn() + " " + t.getPressure() + " " + t.getHumidity() + " " + t.getDw_point() + " " + t.getWind_speed() + " " + t.getWind_deg());

                ArrayList<weather> weatherArrayList2 = t.getWeather();
                for (weather tt : weatherArrayList2) {
                    Log.d(TAG, "weather " + tt.getId() + " " + tt.getMain() + " " + tt.getDescription() + " " + tt.getIcon());

                }

                Log.d(TAG, t.getClouds() + " " + t.getPop() + " " + t.getUvi());
                Log.d(TAG, " ");
            }
        }

        if (test.getAlerts() != null) {
            ArrayList<alerts> alertsArrayList = test.getAlerts();
            for (alerts t : alertsArrayList) {
                Log.d(TAG, t.getSender_name() + " " + t.getEvent() + " " + t.getStart() + " " + t.getEnd() + " " + t.getDescription());
            }

        }


    }

}
