package com.example.guidance.Util;

import com.example.guidance.realm.model.Intelligent_Agent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Conor K on 08/03/2021.
 */
public class IA {

    //    Passcode MLNJXXHHSP
    public static final String MACHINE_LEARNING = "ML";
    public static final String TRADITIONAL_PROGRAMMING = "TP";

    public static final String NO_JUSTIFICATION = "NJ";
    public static final String WITH_JUSTIFICATION = "WJ";

    public static final String MALE = "XY";
    public static final String FEMALE = "XX";

    public static final String HIGH = "HH";
    public static final String LOW = "LL";

    public static final String SPEECH = "SP";
    public static final String TEXT = "TE";


    public static final String TEXT_MACHINE_LEARNING = "Machine Learning";
    public static final String TEXT_TRADITIONAL_PROGRAMMING = "Traditional Programming";
    public static final String TEXT_NO_JUSTIFICATION = "Without Justification";
    public static final String TEXT_WITH_JUSTIFICATION = "With Justification";
    public static final String TEXT_MALE = "Male";
    public static final String TEXT_FEMALE = "Female";
    public static final String TEXT_HIGH = "High";
    public static final String TEXT_LOW = "Low";
    public static final String TEXT_SPEECH = "Speech";
    public static final String TEXT_TEXT = "Text";

    public static final List<String> Analysis = Arrays.asList(MACHINE_LEARNING, TRADITIONAL_PROGRAMMING);
    public static final List<String> Advice = Arrays.asList(NO_JUSTIFICATION, WITH_JUSTIFICATION);
    public static final List<String> Gender = Arrays.asList(MALE, FEMALE);
    public static final List<String> Interaction = Arrays.asList(HIGH, LOW);
    public static final List<String> Output = Arrays.asList(SPEECH, TEXT);

//     All Valid Passcodes
//     [MLNJXYHHSP, MLNJXYHHTE, MLNJXYLLSP, MLNJXYLLTE, MLNJXXHHSP, MLNJXXHHTE, MLNJXXLLSP, MLNJXXLLTE,
//     MLWJXYHHSP, MLWJXYHHTE, MLWJXYLLSP, MLWJXYLLTE, MLWJXXHHSP, MLWJXXHHTE, MLWJXXLLSP, MLWJXXLLTE,
//     TPNJXYHHSP, TPNJXYHHTE, TPNJXYLLSP, TPNJXYLLTE, TPNJXXHHSP, TPNJXXHHTE, TPNJXXLLSP, TPNJXXLLTE,
//     TPWJXYHHSP, TPWJXYHHTE, TPWJXYLLSP, TPWJXYLLTE, TPWJXXHHSP, TPWJXXHHTE, TPWJXXLLSP, TPWJXXLLTE]


    public static boolean withJustification(Intelligent_Agent intelligent_agent) {
        return intelligent_agent.getAdvice().equals(WITH_JUSTIFICATION);
    }

    public static boolean noJustification(Intelligent_Agent intelligent_agent) {
        return intelligent_agent.getAdvice().equals(NO_JUSTIFICATION);
    }


}
