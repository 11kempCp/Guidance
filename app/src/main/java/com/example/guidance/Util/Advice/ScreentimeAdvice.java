package com.example.guidance.Util.Advice;

/**
 * Created by Conor K on 29/03/2021.
 */
public class ScreentimeAdvice extends AdviceText{

    private static final String screentimeHigh = "Your Screentime on %s was quite high yesterday, you might want to limit your Screentime for %s. ";
    private static final String jusScreentimeHigh = "Your Screentime on %s was quite high yesterday, at %s minutes, you might want to limit your Screentime for %s. ";
    private static final String nameScreentimeHigh = "%s, your Screentime on %s was quite high yesterday, you might want to limit your Screentime for %s. ";
    private static final String nameJusScreentimeHigh = "%s, your Screentime on %s was quite high yesterday, at %s minutes, you might want to limit your Screentime for %s. ";



    /**
     * Provides screentime advice without justification and does not require the users name
     * value refers to if the user used the application too much or too little
     *
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
        return screentimeAdvice + full_Disclaimer;
    }

    /**
     * Provides screentime advice with justification and does not require the users name
     * value refers to if the user used the application too much or too little
     *
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
        return screentimeAdvice + full_Disclaimer;
    }

    /**
     * Provides screentime advice without justification and requires the users name
     * value refers to if the user used the application too much or too little
     *
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
        return screentimeAdvice + full_Disclaimer;
    }

    /**
     * Provides screentime advice with justification and requires the users name
     * value refers to if the user used the application too much or too little
     *
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
        return screentimeAdvice + full_Disclaimer;
    }
}
