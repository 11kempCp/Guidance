package com.example.guidance.Util.Advice;

import java.util.Date;

/**
 * Created by Conor K on 29/03/2021.
 */
public class LocationAdvice {

    private static final String locationStatic = "You have been inside for several days in a row; you might want to go outside for a walk";
    private static final String locationMobile = "You have been outside for several days in a row; you might want to stay inside for a while";
    private static final String jusLocationStatic = "You have been inside for %s days in a row; you might want to go outside for a walk";
    private static final String jusLocationMobile = "You have been outside for %s days in a row; you might want to stay inside for a while";
    private static final String nameLocationStatic = "%s, you have been inside for several days in a row; you might want to go outside for a walk";
    private static final String nameLocationMobile = "%s, you have been outside for several days in a row; you might want to stay inside for a while";
    private static final String nameJusLocationStatic = "%s, you have been inside for %s days in a row; you might want to go outside for a walk";
    private static final String nameJusLocationMobile = "%s, you have been outside for %s days in a row; you might want to stay inside for a while";
    private static final String s_Weather = " The %s weather should be %s you may want to go for a walk on this date.";
    private static final String s_Temperature = "The temperature on %s should be %s. You may want to go for a walk on this date.";
    private static final String f_Temperature = "The temperature on %s should be %s.";
    private static final String s_Sun = "The sun rises at %s and sets at %s";

    /**
     * Provides location advice without justification and does not require the users name
     *
     * @param value refers to if the user was inactive (static) or too active (mobile)
     * @return locationAdvice
     */
    public static String getLocationAdvice(int value) {
        String locationAdvice = null;

        // inactive location change
        if (value == 0) {
            locationAdvice = locationStatic;
            // too active location change
        } else if (value == 1) {
            locationAdvice = locationMobile;

        }
        return locationAdvice;
    }

    /**
     * Provides location advice with justification and does not require the users name
     *
     * @param value value refers to if the user was inactive (static) or too active (mobile)
     * @param days  provides justification on the days where their location has been inactive (static) or too active (mobile)
     * @return locationAdvice
     */
    public static String getLocationAdvice(int value, int days) {
        String locationAdvice = null;

        // inactive location change
        if (value == 0) {
            locationAdvice = String.format(jusLocationStatic, days);
            // too active location change
        } else if (value == 1) {
            locationAdvice = String.format(jusLocationMobile, days);

        }
        return locationAdvice;
    }

    /**
     * Provides location advice without justification and requires the users name
     *
     * @param value value refers to if the user was inactive (static) or too active (mobile)
     * @param name  prefix to the advice talking directly to the owner of the phone
     * @return locationAdvice
     */
    public static String getLocationAdvice(int value, String name) {
        String locationAdvice = null;

        // inactive location change
        if (value == 0) {
            locationAdvice = String.format(nameLocationStatic, name);
            // too active location change
        } else if (value == 1) {
            locationAdvice = String.format(nameLocationMobile, name);
        }
        return locationAdvice;
    }

    /**
     * Provides location advice with justification and requires the users name
     *
     * @param value value refers to if the user was inactive (static) or too active (mobile)
     * @param days provides justification on the days where their location has been inactive (static) or too active (mobile)
     * @param name  prefix to the advice talking directly to the owner of the phone
     * @return locationAdvice
     */
    public static String getLocationAdvice(int value, int days, String name) {
        String locationAdvice = null;

        // inactive location change
        if (value == 0) {
            locationAdvice = String.format(nameJusLocationStatic, days, name);
            // too active location change
        } else if (value == 1) {
            locationAdvice = String.format(nameJusLocationMobile, days, name);

        }
        return locationAdvice;
    }

    /**
     * Provides location advice with justification and does not require the users name
     *
     * @param value value refers to if the user was inactive (static) or too active (mobile)
     * @param days  provides justification on the days where their location has been inactive (static) or too active (mobile)
     * @return locationAdvice
     */
    public static String getLocationAdvice(int value, int days, Date date, String weather, Float temperature, Date sunrise, Date sunset) {
        String locationAdvice = null;

        // inactive location change
        if (value == 0) {

            if (weather != null && temperature == null && sunrise == null && sunset == null) {
                locationAdvice = String.format(jusLocationStatic, days) + String.format(s_Weather, date, weather);
            } else if (weather == null && temperature != null) {
                locationAdvice = String.format(jusLocationStatic, days) + String.format(f_Temperature, date, temperature);

            } else if (weather != null && temperature != null && sunrise == null && sunset == null) {
                locationAdvice = String.format(jusLocationStatic, days) + String.format(s_Weather, date, weather) + String.format(s_Temperature, date, temperature);

            } else if (weather != null && temperature != null && sunrise != null && sunset != null) {
                locationAdvice = String.format(jusLocationStatic, days) + String.format(s_Weather, date, weather) + String.format(s_Temperature, date, temperature) + String.format(s_Sun, sunrise, sunset);
            }


            // too active location change
        } else if (value == 1) {
            locationAdvice = String.format(jusLocationMobile, days);

        }
        return locationAdvice;
    }

    /**
     * Provides location advice with justification and requires the users name
     *
     * @param value value refers to if the user was inactive (static) or too active (mobile)
     * @param days provides justification on the days where their location has been inactive (static) or too active (mobile)
     * @param name  prefix to the advice talking directly to the owner of the phone
     * @return locationAdvice
     */
    public static String getLocationAdvice(int value, int days, String name, Date date, String weather, Double temperature, Date sunrise, Date sunset) {
        String locationAdvice = null;

        // inactive location change
        if (value == 0) {

            if (weather != null && temperature == null && sunrise == null && sunset == null) {
                locationAdvice = String.format(nameJusLocationStatic, days, name) + String.format(s_Weather, date, weather);
            } else if (weather == null && temperature != null) {
                locationAdvice = String.format(nameJusLocationStatic, days, name) + String.format(f_Temperature, date, temperature);

            } else if (weather != null && temperature != null && sunrise == null && sunset == null) {
                locationAdvice = String.format(nameJusLocationStatic, days, name) + String.format(s_Weather, date, weather) + String.format(s_Temperature, date, temperature);

            } else if (weather != null && temperature != null && sunrise != null && sunset != null) {
                locationAdvice = String.format(nameJusLocationStatic, days, name) + String.format(s_Weather, date, weather) + String.format(s_Temperature, date, temperature) + String.format(s_Sun, sunrise, sunset);
            }


            // too active location change
        } else if (value == 1) {
            locationAdvice = String.format(nameJusLocationMobile, days, name);

        }
        return locationAdvice;
    }
}
