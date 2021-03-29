package com.example.guidance.Util;

/**
 * Created by Conor K on 28/03/2021.
 */
public class Advice {

    private static String stepCountLow = "Your step count was been low over the previous few days, increasing your step count might be beneficial to you";
    private static String stepCountHigh = "Your step count has been high over the previous few days, decreasing your step count might be beneficial to you";
    private static String jusStepCountLow = "Your step count was been low over the previous %s days, being %s, increasing your step count might be beneficial to you";
    private static String jusStepCountHigh = "Your step count has been high over the previous %s days, being %s, decreasing your step count might be beneficial to you";

    private static String nameStepCountLow = "%s, your step count was been low over the previous few days, increasing your step count might be beneficial to you";
    private static String nameStepCountHigh = "%s, your step count has been high over the previous few days, decreasing your step count might be beneficial to you";
    private static String nameJusStepCountLow = "%s, your step count was been low over the previous %s days, being %s, increasing your step count might be beneficial to you";
    private static String nameJusStepCountHigh = "%s, your step count has been high over the previous %s days, being %s, decreasing your step count might be beneficial to you";


    private static String locationStatic = "You have been inside for several days in a row; you might want to go outside for a walk";
    private static String locationMobile = "You have been outside for several days in a row; you might want to stay inside for a while";
    private static String jusLocationStatic = "You have been inside for %s days in a row; you might want to go outside for a walk";
    private static String jusLocationMobile = "You have been outside for %s days in a row; you might want to stay inside for a while";

    private static String nameLocationStatic = "%s, you have been inside for several days in a row; you might want to go outside for a walk";
    private static String nameLocationMobile = "%s, you have been outside for several days in a row; you might want to stay inside for a while";
    private static String nameJusLocationStatic = "%s, you have been inside for %s days in a row; you might want to go outside for a walk";
    private static String nameJusLocationMobile = "%s, you have been outside for %s days in a row; you might want to stay inside for a while";

    private static String screentimeHigh = "Your Screentime on %s was quite high yesterday, you might want to limit your Screentime for %s";
    private static String jusScreentimeHigh = "Your Screentime on %s was quite high yesterday, at %s minutes, you might want to limit your Screentime for %s";
    private static String nameScreentimeHigh = "%s, your Screentime on %s was quite high yesterday, you might want to limit your Screentime for %s";
    private static String nameJusScreentimeHigh = "%s, your Screentime on %s was quite high yesterday, at %s minutes, you might want to limit your Screentime for %s";

    //todo improve
    private static String lowSocialness = "Your socialness has been low over the last %s days, you may want to reach out to your friends";
    private static String jusLowSocialness = "Your socialness has been low over the last %s days, with an average of %s, you may want to reach out to your friends";
    private static String nameLowSocialness = "%s, your socialness has been low over the last %s days, you may want to reach out to your friends";
    private static String nameJusLowSocialness = "%s, your socialness has been low over the last %s days, with an average of %s, you may want to reach out to your friends";

    //todo improve
    private static String lowMood = "Your mood has been low over the last %s days, you may want to try talking to people";
    private static String jusLowMood = "Your mood has been low over the last %s days, with an average of %s, you may want to try talking to people";
    private static String nameLowMood = "%s, your mood has been low over the last %s days, you may want to try talking to people";
    private static String nameJusLowMood = "%s, your mood has been low over the last %s days, with an average of %s, you may want to try talking to people";


    /**
     * Provides Step advice without justification and does not require the users name
     * value refers to if the user was too low or high in their step count
     *
     * @param value
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
     * @param value
     * @param days
     * @param steps
     * @return stepAdvice
     */
    public static String getStepAdvice(int value, int days, float steps) {
        String stepAdvice = null;

        //low step count
        if (value == 0) {
            stepAdvice = String.format(jusStepCountLow, steps, days, days);
            //high step count
        } else if (value == 1) {
            stepAdvice = String.format(jusStepCountHigh, steps, days, days);

        }
        return stepAdvice;
    }

    /**
     * Provides Step advice without justification and requires the users name
     * value refers to if the user was too low or high in their step count
     *
     * @param value
     * @param name
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
     * Provides Step advice without justification and requires the users name
     * value refers to if the user was too low or high in their step count
     *
     * @param value
     * @param days
     * @param steps
     * @param name
     * @return stepAdvice
     */
    public static String getStepAdvice(int value, int days, float steps, String name) {
        String stepAdvice = null;

        //low step count
        if (value == 0) {
            stepAdvice = String.format(nameJusStepCountLow, name, steps, days, days);
            //high step count
        } else if (value == 1) {
            stepAdvice = String.format(nameJusStepCountHigh, name, steps, days, days);

        }
        return stepAdvice;
    }

    /**
     * Provides location advice without justification and does not require the users name
     * value refers to if the user was inactive (static) or too active (mobile)
     *
     * @param value
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
     * value refers to if the user was inactive (static) or too active (mobile)
     *
     * @param value
     * @param days
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
     * value refers to if the user was inactive (static) or too active (mobile)
     *
     * @param value
     * @param name
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
     * value refers to if the user was inactive (static) or too active (mobile)
     *
     * @param value
     * @param days
     * @param name
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
     * Provides screentime advice without justification and does not require the users name
     * value refers to if the user used the application too much or too little
     * @param value
     * @param applicationName
     * @return screentimeAdvice
     */
    public static String getScreentimeAdvice(int value, String applicationName) {
        String screentimeAdvice = null;

        //high usage of an application
        if (value == 1) {
            screentimeAdvice = String.format(screentimeHigh, applicationName, applicationName);
        }
        return screentimeAdvice;
    }

    /**
     * Provides screentime advice with justification and does not require the users name
     * value refers to if the user used the application too much or too little
     * @param value
     * @param applicationName
     * @param minutes
     * @return screentimeAdvice
     */
    public static String getScreentimeAdvice(int value, String applicationName, Long minutes) {
        String screentimeAdvice = null;

        //high usage of an application
        if (value == 1) {
            screentimeAdvice = String.format(jusScreentimeHigh, applicationName, minutes, applicationName);
        }
        return screentimeAdvice;
    }

    /**
     * Provides screentime advice without justification and requires the users name
     * value refers to if the user used the application too much or too little
     * @param value
     * @param applicationName
     * @param name
     * @return screentimeAdvice
     */
    public static String getScreentimeAdvice(int value, String applicationName, String name) {
        String screentimeAdvice = null;

        //high usage of an application
        if (value == 1) {
            screentimeAdvice = String.format(nameScreentimeHigh, name, applicationName, applicationName);
        }
        return screentimeAdvice;
    }

    /**
     * Provides screentime advice with justification and requires the users name
     * value refers to if the user used the application too much or too little
     * @param value
     * @param applicationName
     * @param minutes
     * @param name
     * @return screentimeAdvice
     */
    public static String getScreentimeAdvice(int value, String applicationName, Long minutes, String name) {
        String screentimeAdvice = null;

        //high usage of an application
        if (value == 1) {
            screentimeAdvice = String.format(nameJusScreentimeHigh, name, applicationName, minutes, applicationName);
        }
        return screentimeAdvice;
    }

    /**
     * Provides socialness advice without justification and does not require the users name
     * value refers to if the users socialness was too low or too high
     * @param value
     * @param days
     * @return socialnessAdvice
     */
    public static String getSocialnessAdvice(int value, int days) {
        String socialnessAdvice = null;

        //low socialness
        if (value == 0) {
            socialnessAdvice = String.format(lowSocialness, days);

        }
        return socialnessAdvice;
    }

    /**
     * Provides socialness advice with justification and does not require the users name
     * value refers to if the user used the application too much or too little
     * @param value
     * @param days
     * @param averageSocialness
     * @return socialnessAdvice
     */
    public static String getSocialnessAdvice(int value, int days, float averageSocialness) {
        String socialnessAdvice = null;

        //low socialness
        if (value == 0) {
            socialnessAdvice = String.format(jusLowSocialness, days, averageSocialness);

        }
        return socialnessAdvice;
    }

    /**
     * Provides socialness advice without justification and requires the users name
     * value refers to if the users socialness was too low or too high
     * @param value
     * @param days
     * @param name
     * @return socialnessAdvice
     */
    public static String getSocialnessAdvice(int value, int days, String name) {
        String socialnessAdvice = null;

        //low socialness
        if (value == 0) {
            socialnessAdvice = String.format(nameLowSocialness, name, days);

        }
        return socialnessAdvice;
    }

    /**
     * Provides socialness advice with justification and requires the users name
     * value refers to if the users socialness was too low or too high
     * @param value
     * @param days
     * @param averageSocialness
     * @param name
     * @return socialnessAdvice
     */
    public static String getSocialnessAdvice(int value, int days, float averageSocialness, String name) {
        String socialnessAdvice = null;

        //low socialness
        if (value == 0) {
            socialnessAdvice = String.format(nameJusLowSocialness, name, days, averageSocialness);

        }
        return socialnessAdvice;
    }

    /**
     * Provides mood advice without justification and does not require the users name
     * value refers to if the users mood was too low or too high
     * @param value
     * @param days
     * @return moodAdvice
     */
    public static String getMoodAdvice(int value, int days) {
        String moodAdvice = null;

        //low mood
        if (value == 0) {
            moodAdvice = String.format(lowMood, days);
        }


        return moodAdvice;
    }

    /**
     * Provides mood advice with justification and does not require the users name
     * value refers to if the users mood was too low or too high
     * @param value
     * @param days
     * @param averageMood
     * @return moodAdvice
     */
    public static String getMoodAdvice(int value, int days, float averageMood) {
        String moodAdvice = null;

        //low mood
        if (value == 0) {
            moodAdvice = String.format(jusLowMood, days, averageMood);
        }


        return moodAdvice;
    }

    /**
     * Provides mood advice without justification and requires the users name
     * value refers to if the users mood was too low or too high
     * @param value
     * @param days
     * @param name
     * @return moodAdvice
     */
    public static String getMoodAdvice(int value, int days, String name) {
        String moodAdvice = null;

        //low mood
        if (value == 0) {
            moodAdvice = String.format(nameLowMood, name, days);
        }


        return moodAdvice;
    }

    /**
     * Provides mood advice with justification and requires the users name
     * value refers to if the users mood was too low or too high
     * @param value
     * @param days
     * @param averageMood
     * @param name
     * @return moodAdvice
     */
    public static String getMoodAdvice(int value, int days, float averageMood, String name) {
        String moodAdvice = null;

        //low mood
        if (value == 0) {
            moodAdvice = String.format(nameJusLowMood, name, days, averageMood);
        }


        return moodAdvice;
    }


}
