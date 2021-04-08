package com.example.guidance.Util.Advice;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Conor K on 08/04/2021.
 */
public class AdviceText {

    public static final SimpleDateFormat standardDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
    public static final SimpleDateFormat hourMinuteDateFormat = new SimpleDateFormat("hh:mm", Locale.ENGLISH);

    public static final String new_line = "\n";
    public static final String disclaimer_covid = "You should ensure that you follow the relevant government guidelines and restrictions with regards to the Coronavirus (COVID-19) pandemic. ";
    public static final String disclaimer_medical_advice = "The advice given here is not based on medical advice and should therefore not be used as a substitute for a professional medical opinion. If in doubt please contact your GP. ";
    public static final String full_Disclaimer = new_line + disclaimer_covid + disclaimer_medical_advice;

    public static final String s_Weather = " The weather on %s should be %s you may want to go for a walk on this date. ";
    public static final String s_Temperature = "The temperature on %s should be %sc. You may want to go for a walk on this date. ";
    public static final String f_Temperature = "The temperature on %s should be %sc. ";
    public static final String s_Sun = "The sun rises at %s and sets at %s. ";
}
