package com.example.guidance.Util.Advice;

/**
 * Created by Conor K on 28/03/2021.
 */
public class MoodAdvice extends AdviceText{


    //todo improve
    private static final String lowMood = "Your mood has been low over the last %s days, you may want to try talking to people. ";
    private static final String jusLowMood = "Your mood has been low over the last %s days, with an average of %s, you may want to try talking to people. ";
    private static final String nameLowMood = "%s, your mood has been low over the last %s days, you may want to try talking to people. ";
    private static final String nameJusLowMood = "%s, your mood has been low over the last %s days, with an average of %s, you may want to try talking to people. ";




    /**
     * Provides mood advice without justification and does not require the users name
     * value refers to if the users mood was too low or too high
     *
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


        return moodAdvice + full_Disclaimer;
    }

    /**
     * Provides mood advice with justification and does not require the users name
     * value refers to if the users mood was too low or too high
     *
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


        return moodAdvice + full_Disclaimer;
    }

    /**
     * Provides mood advice without justification and requires the users name
     * value refers to if the users mood was too low or too high
     *
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
     *
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


        return moodAdvice + full_Disclaimer;
    }


}
