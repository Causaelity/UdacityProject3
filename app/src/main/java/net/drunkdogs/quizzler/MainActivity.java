package net.drunkdogs.quizzler;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    QuestionBank allQuestions = new QuestionBank();
    String pickedAnswer = "";
    int questionNumber = 0;
    int score = 0;
    final int numberOfQuestions = allQuestions.list.size();
    boolean noRadioSelected = false;

    // Create Views
    TextView questionLabel, scoreLabel, progressLabel;
    View progressBar;
    LinearLayout buttonLayout, checkBoxLayout;
    RadioGroup radioGroup;
    Button submitButton;
    EditText answerField;
    RadioButton radioButtonA, radioButtonB, radioButtonC, radioButtonD;

    final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup views
        questionLabel = findViewById(R.id.question_textview);
        scoreLabel = findViewById(R.id.score_label);
        progressLabel = findViewById(R.id.progress_label);
        progressBar = findViewById(R.id.progress_bar);
        buttonLayout = findViewById(R.id.button_layout);
        radioGroup = findViewById(R.id.radio_layout);
        checkBoxLayout = findViewById(R.id.checkbox_layout);
        submitButton = findViewById(R.id.submit_button);
        answerField = findViewById(R.id.answer_field);
        radioButtonA = findViewById(R.id.radiobutton_a);
        radioButtonB = findViewById(R.id.radiobutton_b);
        radioButtonC = findViewById(R.id.radiobutton_c);
        radioButtonD = findViewById(R.id.radiobutton_d);


        // Deal with Screen rotations
        if (savedInstanceState != null) {
            allQuestions.list = (ArrayList<Question>) savedInstanceState.getSerializable("questionList");
            score = savedInstanceState.getInt("score");
            questionNumber = savedInstanceState.getInt("questionNumber");
        } else {
            // This is first run, shuffle the questions
            // Shuffle the array to change the order of the questions
            Collections.shuffle(allQuestions.list);
        }
        // Hide all the input views
        hideAll();

        // Ask the first question
        nextQuestion();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("questionList", allQuestions.list);
        outState.putInt("score", score);
        outState.putInt("questionNumber", questionNumber);
    }

    // Get Screen Width for Progress Bar
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    // Hide all the Input Views
    private void hideAll() {

        buttonLayout.setVisibility(View.INVISIBLE);
        radioGroup.setVisibility(View.INVISIBLE);
        checkBoxLayout.setVisibility(View.INVISIBLE);
        submitButton.setVisibility(View.INVISIBLE);
        answerField.setVisibility(View.INVISIBLE);
    }

    // Display Button Input View
    private void showButtons() {
        buttonLayout.setVisibility(View.VISIBLE);
    }

    // Display Checkbox View and Submit Button
    private void showCheckBoxes() {
        checkBoxLayout.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);
    }

    // Display Radio Buttons
    private void showRadioButtons() {
        radioGroup.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);
    }

    // Display Text Entry and Submit Button
    private void showTextEntry() {
        answerField.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);

    }

    // Display next question and enable appropriate input mechanism. Else if game is over display alert
    private void nextQuestion() {

        if (questionNumber <= numberOfQuestions - 1) {

            // Build out Question
            String fullQuestion = allQuestions.list.get(questionNumber).questionSet.get("question").toString();
            fullQuestion += "\n\na) " + allQuestions.list.get(questionNumber).questionSet.get("a");
            fullQuestion += "\nb) " + allQuestions.list.get(questionNumber).questionSet.get("b");
            fullQuestion += "\nc) " + allQuestions.list.get(questionNumber).questionSet.get("c");
            fullQuestion += "\nd) " + allQuestions.list.get(questionNumber).questionSet.get("d");

            // Determine which Input mode to display
            QuestionType type = (QuestionType) allQuestions.list.get(questionNumber).questionSet.get("format");
            switch (type) {
                case BUTTON:
                    showButtons();
                    break;
                case RADIO:
                    showRadioButtons();
                    break;
                case CHECKBOX:
                    showCheckBoxes();
                    break;
                case TEXTENTRY:
                    showTextEntry();
                    // Rewrite question for this format
                    fullQuestion = allQuestions.list.get(questionNumber).questionSet.get("question").toString();
                    break;
            }

            questionLabel.setText(fullQuestion);
//            questionLabel.setTextColor(Color.RED);  // Test line to confirm how Color works.

            updateUI();

        } else {
            // No more questions, Quiz over! Display Alert.
            hideAll();
            scoreLabel.setText("Score: " + score);

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Awesome!");
            alertDialog.setMessage("You scored " + score * 100 / numberOfQuestions + "% Try again?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Restart",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            restart();
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    // Verify answer for all questions
    public void submitPressed(View view) {

        // check text edit or checkbox question
        String correctAnswer = allQuestions.list.get(questionNumber).questionSet.get("answer").toString();

        QuestionType type = (QuestionType) allQuestions.list.get(questionNumber).questionSet.get("format");

        if (type == QuestionType.TEXTENTRY) {
            EditText answerField = findViewById(R.id.answer_field);
            String proposedAnswer = answerField.getText().toString();

            pickedAnswer = proposedAnswer.toLowerCase();
            checkAnswer(correctAnswer.toLowerCase());

            // Reset answer field to nothing for future questions and or runs
            answerField.setText("");

            // For Checkbox, determine which combination was chosen and compare score.
            // After calculated, unselect checkbox
        } else if (type == QuestionType.CHECKBOX) {
            int checkBoxSum = 0;
            CheckBox buttonA = findViewById(R.id.checkbox_a);
            CheckBox buttonB = findViewById(R.id.checkbox_b);
            CheckBox buttonC = findViewById(R.id.checkbox_c);
            CheckBox buttonD = findViewById(R.id.checkbox_d);

            if (buttonA.isChecked()) {
                checkBoxSum += 1;
                buttonA.setChecked(false);
            }

            if (buttonB.isChecked()) {
                checkBoxSum += 2;
                buttonB.setChecked(false);
            }

            if (buttonC.isChecked()) {
                checkBoxSum += 4;
                buttonC.setChecked(false);
            }

            if (buttonD.isChecked()) {
                checkBoxSum += 8;
                buttonD.setChecked(false);
            }

            pickedAnswer = "" + checkBoxSum;
            checkAnswer(correctAnswer);

        } else if (type == QuestionType.RADIO) {
            checkRadioButtons();
            if (noRadioSelected) {
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle("Nothing Selected");
                dialog.setMessage("Please pick an answer!");
                dialog.show();
                noRadioSelected = false;
                return;
            } else {
                // check answer
                checkAnswer(correctAnswer);
            }
        } else if (type == QuestionType.BUTTON) {
            switch(view.getId()) {
                case R.id.button_a:
                    pickedAnswer = "a";
                    break;
                case R.id.button_b:
                    pickedAnswer = "b";
                    break;
                case R.id.button_c:
                    pickedAnswer = "c";
                    break;
                case R.id.button_d:
                    pickedAnswer = "d";
                    break;
                default:
                    break;
            }
            checkAnswer(correctAnswer);
        }

        hideAll();

        questionNumber += 1;

        // introduce delay
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextQuestion();
            }
        }, 2000);

        // In case you don't want the delay
//        nextQuestion();

    }

    private void checkRadioButtons() {
        if (radioButtonA.isChecked()) { pickedAnswer = "a"; }
        else if (radioButtonB.isChecked()) { pickedAnswer = "b"; }
        else if (radioButtonC.isChecked()) { pickedAnswer = "c"; }
        else if (radioButtonD.isChecked()) { pickedAnswer = "d"; }
        else { noRadioSelected = true; }

        radioGroup.clearCheck();
    }


    // Verify if Answer was correct and inform user and add score
    private void checkAnswer(String correctAnswer) {

        if (correctAnswer.equals(pickedAnswer)) {
            showToast(true);
            score += 1;
        } else {
            showToast(false);
        }
    }

    // Inform user is answer was correct or not
    private void showToast(boolean value) {
        if (value) {
            Toast toast = Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Wrong!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


    // Update scores and other UI Elements
    private void updateUI() {
        scoreLabel.setText("Score: " + score);
        progressLabel.setText(questionNumber + 1 + " / " + numberOfQuestions);
        int parentWidth = ((View) progressBar.getParent()).getMeasuredWidth();

        // On first run parentWidth is always 0 (not on reset), so get screenwidth for this run.
        if (parentWidth == 0) {
            parentWidth = getScreenWidth();
        }

        // Update progress Bar to show how far in the quiz we are (pre animation)
//        progressBar.getLayoutParams().width = parentWidth / numberOfQuestions * (questionNumber + 1);

        // Updated to use animation instead
        ValueAnimator anim = ValueAnimator.ofInt(progressBar.getMeasuredWidth(), parentWidth / numberOfQuestions * (questionNumber + 1));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();
                layoutParams.width = val;
                progressBar.setLayoutParams(layoutParams);
            }
        });

        anim.setDuration(500);
        anim.start();

    }

    // Restart the game for another attempt.
    private void restart() {
        questionNumber = 0;
        score = 0;
        Collections.shuffle(allQuestions.list);
        nextQuestion();

    }
}
