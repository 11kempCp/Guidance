package com.example.guidance.Util.Advice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Conor K on 29/03/2021.
 */
public class StepAdvice {

    private static final String stepCountLow = "Your step count was been low over the previous few days, increasing your step count might be beneficial to you.";
    private static final String stepCountHigh = "Your step count has been high over the previous few days, decreasing your step count might be beneficial to you.";
    private static final String jusStepCountLow = "Your step count was been low over the previous %s days, being %s, increasing your step count might be beneficial to you.";
    private static final String jusStepCountHigh = "Your step count has been high over the previous %s days, being %s, decreasing your step count might be beneficial to you.";
    private static final String nameStepCountLow = "%s, your step count was been low over the previous few days, increasing your step count might be beneficial to you.";
    private static final String nameStepCountHigh = "%s, your step count has been high over the previous few days, decreasing your step count might be beneficial to you.";
    private static final String nameJusStepCountLow = "%s, your step count was been low over the previous %s days, being %s, increasing your step count might be beneficial to you.";
    private static final String nameJusStepCountHigh = "%s, your step count has been high over the previous %s days, being %s, decreasing your step count might be beneficial to you.";

    private static final String s_Weather = " The weather on %s should be %s you may want to go for a walk on this date.";
    private static final String s_Temperature = "The temperature on %s should be %sc. You may want to go for a walk on this date.";
    private static final String f_Temperature = "The temperature on %s should be %sc.";
    private static final String s_Sun = "The sun rises at %s and sets at %s.";

    private static final SimpleDateFormat standardDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
    private static final SimpleDateFormat hourMinuteDateFormat = new SimpleDateFormat("hh:mm", Locale.ENGLISH);


    /**
     * Provides Step advice without justification and does not require the users name
     * @param value refers to if the user was too low or high in their step count
     * @return stepAdvice
     */
    public static String getStepAdvice(int value) {
        String stepAdvice = null;
        //low step count
        if (value == 0) {
            stepAdvice = stepCountLow;
            //high step count
        } else if (value == 1) {
            stepAdvice = stepCountHigh;
        }
        return stepAdvice;
    }

    /**
     * Provides Step advice with justification and does not require the users name
     * value refers to if the user was too low or high in their step count
     *
     * @param value refers to if the user was too low or high in their step count
     * @param days provides justification on the days where their step count has been high/low
     * @param steps provides justification on their step count over x amount of days
     * @return stepAdvice
     */
    public static String getStepAdvice(int value, int days, float steps) {
        String stepAdvice = null;

        //low step count
        if (value == 0) {
            stepAdvice = String.format(jusStepCountLow, days, steps);
            //high step count
        } else if (value == 1) {
            stepAdvice = String.format(jusStepCountHigh, days, steps);

        }
        return stepAdvice;
    }

    /**
     * Provides Step advice without justification and requires the users name
     * value refers to if the user was too low or high in their step count
     *
     * @param value refers to if the user was too low or high in their step count
     * @param name prefix to the advice talking directly to the owner of the phone
     * @return stepAdvice
     */
    public static String getStepAdvice(int value, String name) {
        String stepAdvice = null;
        //low step count
        if (value == 0) {
            stepAdvice = String.format(nameStepCountLow, name);

            //high step count
        } else if (value == 1) {
            stepAdvice = String.format(nameStepCountHigh, name);

        }
        return stepAdvice;
    }

    /**
     * Provides Step advice with justification and requires the users name
     * value refers to if the user was too low or high in their step count
     *
     * @param value refers to if the user was too low or high in their step count
     * @param days provides justification on the days where their step count has been high/low
     * @param steps provides justification on their step count over x amount of days
     * @param name prefix to the advice talking directly to the owner of the phone
     * @return stepAdvice
     */
    public static String getStepAdvice(int value, int days, float steps, String name) {
        String stepAdvice = null;

        //low step count
        if (value == 0) {
            stepAdvice = String.format(nameJusStepCountLow, name, days, steps);
            //high step count
        } else if (value == 1) {
            stepAdvice = String.format(nameJusStepCountHigh, name, days, steps);

        }
        return stepAdvice;
    }


    /**
     * Provides Step advice with justification and does not require the users name
     * provides a date and the weather on the specified date
     * provides a date and temperature on the specified date
     * provides the sunset and sunrise time
     * @param value refers to if the user was too low or high in their step count
     * @param days provides justification on the days where their step count has been high/low
     * @param steps provides justification on their step count over x amount of days
     * @param dateFor a date in which the user is being advised to go for a walk on
     * @param weather the weather on the specified date
     * @param temperature the temperature on the specified date
     * @param sunrise the sunrise on the specified date
     * @param sunset the sunset on the specified date
     * @return stepAdvice
     */
    public static String getStepAdvice(int value, int days, float steps, Date dateFor, String weather, Float temperature, Date sunrise, Date sunset) {
        String stepAdvice = null;

        String date = standardDateFormat.format(dateFor);


        //low step count
        if (value == 0) {
            if (weather != null && temperature == null && sunrise == null && sunset == null) {
                stepAdvice = String.format(jusStepCountLow, days, steps) + String.format(s_Weather, date, weather);
            } else if (weather == null && temperature != null) {
                stepAdvice = String.format(jusStepCountLow, days, steps) + String.format(f_Temperature, date, temperature);

            } else if (weather != null && temperature != null && sunrise == null && sunset == null) {
                stepAdvice = String.format(jusStepCountLow, days, steps) + String.format(s_Weather, date, weather) + String.format(s_Temperature, date, temperature);

            } else if (weather != null && temperature != null && sunrise != null && sunset != null) {
                stepAdvice = String.format(jusStepCountLow, days, steps) + String.format(s_Weather, date, weather) + String.format(s_Temperature, date, temperature) + String.format(s_Sun, sunrise, sunset);
            }

            //high step count
        } else if (value == 1) {
            stepAdvice = String.format(jusStepCountHigh, days, steps);

        }
        return stepAdvice;
    }

    /**
     * Provides Step advice with justification and requires the users name
     * value refers to if the user was too low or high in their step count
     *
     * @param value refers to if the user was too low or high in their step count
     * @param days provides justification on the days where their step count has been high/low
     * @param steps provides justification on their step count over x amount of days
     * @param name prefix to the advice talking directly to the owner of the phone
     * @param dateFor a date in which the user is being advised to go for a walk on
     * @param weather the weather on the specified date
     * @param temperature the temperature on the specified date
     * @param sunrise the sunrise on the specified date
     * @param sunset the sunset on the specified date
     * @return stepAdvice
     */
    public static String getStepAdvice(int value, int days, float steps, String name, Date dateFor, String weather, Double temperature, Date sunrise, Date sunset) {
        String stepAdvice = null;
        String date = standardDateFormat.format(dateFor);

        //low step count
        if (value == 0) {

            if (weather != null && temperature == null && sunrise == null && sunset == null) {
                stepAdvice = String.format(nameJusStepCountLow, name ,days, steps) + String.format(s_Weather, date, weather);
            } else if (weather == null && temperature != null) {
                stepAdvice = String.format(nameJusStepCountLow, name ,days, steps) + String.format(f_Temperature, date, temperature);

            } else if (weather != null && temperature != null && sunrise == null && sunset == null) {
                stepAdvice = String.format(nameJusStepCountLow, name ,days, steps) + String.format(s_Weather, date, weather) + String.format(s_Temperature, date, temperature);

            } else if (weather != null && temperature != null && sunrise != null && sunset != null) {
                stepAdvice = String.format(nameJusStepCountLow, name ,days, steps) + String.format(s_Weather, date, weather) + String.format(s_Temperature, date, temperature) + String.format(s_Sun, sunrise, sunset);
            }
            //high step count
        } else if (value == 1) {
            stepAdvice = String.format(nameJusStepCountHigh, name, steps, days);

        }
        return stepAdvice;
    }


}
