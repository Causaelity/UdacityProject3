package net.drunkdogs.quizzler;

import android.provider.MediaStore;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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
class Question {
    Map questionSet = new HashMap<>();

    Question(String questionText, String answer1, String answer2, String answer3, String answer4, String correctAnswer, QuestionType type) {
        questionSet.put("question", questionText);
        questionSet.put("a", answer1);
        questionSet.put("b", answer2);
        questionSet.put("c", answer3);
        questionSet.put("d", answer4);
        questionSet.put("answer", correctAnswer);
        questionSet.put("format", type);

    }

}

