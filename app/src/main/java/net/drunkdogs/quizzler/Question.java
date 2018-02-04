package net.drunkdogs.quizzler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tiberius on 1/24/18.
 */

// Define the question Types
enum QuestionType {
    BUTTON, RADIO, CHECKBOX, TEXTENTRY
}

// Question Class to hold various types of question formats
public class Question {
    public Map questionSet = new HashMap<>();

    public Question(String questionText, String answer1, String answer2, String answer3, String answer4, String correctAnswer, QuestionType type) {
        questionSet.put("question", questionText);
        questionSet.put("a", answer1);
        questionSet.put("b", answer2);
        questionSet.put("c", answer3);
        questionSet.put("d", answer4);
        questionSet.put("answer", correctAnswer);
        questionSet.put("format", type);

    }

}
