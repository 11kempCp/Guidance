package com.example.guidance.Util.Advice;

/**
 * Created by Conor K on 29/03/2021.
 */
public class SocialnessAdvice extends AdviceText{

    //todo improve
    private static final String lowSocialness = "Your socialness has been low over the last %s days, you may want to reach out to your friends. ";
    private static final String jusLowSocialness = "Your socialness has been low over the last %s days, with an average of %s, you may want to reach out to your friends. ";
    private static final String nameLowSocialness = "%s, your socialness has been low over the last %s days, you may want to reach out to your friends. ";
    private static final String nameJusLowSocialness = "%s, your socialness has been low over the last %s days, with an average of %s, you may want to reach out to your friends. ";



    /**
     * Provides socialness advice without justification and does not require the users name
     * value refers to if the users socialness was too low or too high
     *
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
        return socialnessAdvice + full_Disclaimer;
    }

    /**
     * Provides socialness advice with justification and does not require the users name
     * value refers to if the user used the application too much or too little
     *
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
        return socialnessAdvice + full_Disclaimer;
    }

    /**
     * Provides socialness advice without justification and requires the users name
     * value refers to if the users socialness was too low or too high
     *
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
        return socialnessAdvice + full_Disclaimer;
    }

    /**
     * Provides socialness advice with justification and requires the users name
     * value refers to if the users socialness was too low or too high
     *
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
        return socialnessAdvice + full_Disclaimer;
    }
}
