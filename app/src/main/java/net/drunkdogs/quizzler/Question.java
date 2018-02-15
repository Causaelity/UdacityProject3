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


/**
 * Enum removed as apparently it has suboptimal performance and memory usage in Android
 */

// Define the question Types
//enum QuestionType {
//    BUTTON, RADIO, CHECKBOX, TEXTENTRY
//}

// Question Class to hold various types of question formats
public class Question {
    public Map questionSet = new HashMap<>();

    public Question(String questionText, String answer1, String answer2, String answer3, String answer4, String correctAnswer, @QuestionType int type) {
        questionSet.put("question", questionText);
        questionSet.put("a", answer1);
        questionSet.put("b", answer2);
        questionSet.put("c", answer3);
        questionSet.put("d", answer4);
        questionSet.put("answer", correctAnswer);
        questionSet.put("format", type);

    }

    /**
     * Create Enum replacement
     */

    // Constants
    public static final int BUTTON = 0;
    public static final int RADIO = 1;
    public static final int CHECKBOX = 2;
    public static final int TEXTENTRY = 3;

    // Declare the @IntDef for these constants
    @IntDef({BUTTON, RADIO, CHECKBOX, TEXTENTRY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface QuestionType {}

}

