package com.example.guidance.Util;

import com.example.guidance.realm.model.Intelligent_Agent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Conor K on 08/03/2021.
 */
public class IA {

    //    Passcode MLWJXXHHSP

    /**
     * The following refers to the possible attributes that the intelligent agent might possess
     * each attribute type is denoted by a two letter combination. These attributes types will
     * be stored in the relevant Intelligent_Agent attribute category realm of the application
     * */
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


    /**
     * The following is the list attributes that are being tested. Each attribute List contains the above
     * attributes types.
     * */
    public static final List<String> Analysis = Arrays.asList(MACHINE_LEARNING, TRADITIONAL_PROGRAMMING);
    public static final List<String> Advice = Arrays.asList(NO_JUSTIFICATION, WITH_JUSTIFICATION);
    public static final List<String> Gender = Arrays.asList(MALE, FEMALE);
    public static final List<String> Interaction = Arrays.asList(HIGH, LOW);
    public static final List<String> Output = Arrays.asList(SPEECH, TEXT);

    /**
     * The following refers to the full text version of the attributes that the intelligent agent possesses.
     * Each attribute has a full text version for when the attributes are displayed on the front end
     * */
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



//     All Valid Passcodes
//     [MLNJXYHHSP, MLNJXYHHTE, MLNJXYLLSP, MLNJXYLLTE, MLNJXXHHSP, MLNJXXHHTE, MLNJXXLLSP, MLNJXXLLTE,
//     MLWJXYHHSP, MLWJXYHHTE, MLWJXYLLSP, MLWJXYLLTE, MLWJXXHHSP, MLWJXXHHTE, MLWJXXLLSP, MLWJXXLLTE,
//     TPNJXYHHSP, TPNJXYHHTE, TPNJXYLLSP, TPNJXYLLTE, TPNJXXHHSP, TPNJXXHHTE, TPNJXXLLSP, TPNJXXLLTE,
//     TPWJXYHHSP, TPWJXYHHTE, TPWJXYLLSP, TPWJXYLLTE, TPWJXXHHSP, TPWJXXHHTE, TPWJXXLLSP, TPWJXXLLTE]


    /**
     * determines if the intelligent agent has the WITH_JUSTIFICATION attribute
     *
     * @param intelligent_agent the intelligent agent for the application
     * @return true if the intelligent agent's advice attribute equals WITH_JUSTIFICATION, false if the intelligent agent's advice attribute does not equal WITH_JUSTIFICATION
     * */
    public static boolean withJustification(Intelligent_Agent intelligent_agent) {
        return intelligent_agent.getAdvice().equals(WITH_JUSTIFICATION);
    }

    /**
     * determines if the intelligent agent has the NO_JUSTIFICATION attribute
     *
     * @param intelligent_agent the intelligent agent for the application
     * @return true if the intelligent agent's advice attribute equals NO_JUSTIFICATION, false if the intelligent agent's advice attribute does not equal NO_JUSTIFICATION
     * */
    public static boolean noJustification(Intelligent_Agent intelligent_agent) {
        return intelligent_agent.getAdvice().equals(NO_JUSTIFICATION);
    }


}
