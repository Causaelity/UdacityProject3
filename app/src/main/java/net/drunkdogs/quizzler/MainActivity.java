package net.drunkdogs.quizzler;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    QuestionBank allQuestions = new QuestionBank();
    String pickedAnswer = "";
    int questionNumber = 0;
    int score = 0;
    final int numberOfQuestions = allQuestions.list.size();
    TextView questionLabel;
    TextView scoreLabel;
    TextView progressLabel;
    View progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionLabel = (TextView) findViewById(R.id.question_textview);
        scoreLabel = findViewById(R.id.score_label);
        progressLabel = findViewById(R.id.progress_label);
        progressBar = findViewById(R.id.progress_bar);

        // Hide all the input views
        hideAll();

        // Shuffle the array to change the order of the questions
        Collections.shuffle(allQuestions.list);

        // Ask the first question
        nextQuestion();

    }

    // Get Screen Width for Progress Bar
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    // Hide all the Input Views
    private void hideAll() {
        LinearLayout buttonLayout = findViewById(R.id.button_layout);
        buttonLayout.setVisibility(View.INVISIBLE);

        RadioGroup radioLayout = findViewById(R.id.radio_layout);
        radioLayout.setVisibility(View.INVISIBLE);

        LinearLayout checkBoxLayout = findViewById(R.id.checkbox_layout);
        checkBoxLayout.setVisibility(View.INVISIBLE);

        Button button = findViewById(R.id.submit_button);
        button.setVisibility(View.INVISIBLE);

        EditText answerField = findViewById(R.id.answer_field);
        answerField.setVisibility(View.INVISIBLE);
    }

    // Display Button Input View
    private void showButtons() {
        LinearLayout buttonLayout = findViewById(R.id.button_layout);
        buttonLayout.setVisibility(View.VISIBLE);
    }

    // Display Checkbox View and Submit Button
    private void showCheckBoxes() {
        LinearLayout checkBoxLayout = findViewById(R.id.checkbox_layout);
        checkBoxLayout.setVisibility(View.VISIBLE);

        Button button = findViewById(R.id.submit_button);
        button.setVisibility(View.VISIBLE);
    }

    // Display Radio Buttons
    private void showRadioButtons() {
        RadioGroup radioLayout = findViewById(R.id.radio_layout);
        radioLayout.setVisibility(View.VISIBLE);
    }

    // Display Text Entry and Submit Button
    private void showTextEntry() {

        EditText answerField = findViewById(R.id.answer_field);
        answerField.setVisibility(View.VISIBLE);

        Button button = findViewById(R.id.submit_button);
        button.setVisibility(View.VISIBLE);

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

            updateUI();

        } else {
            // No more questions, display Alert.
            hideAll();
            scoreLabel.setText("Score: " + score);

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Awesome!");
            alertDialog.setMessage("You scored " + score * 100 / numberOfQuestions + "% Try again?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Restart",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            restart();
                            dialogInterface.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    // Verify answer for questions that require the submit button
    public void submitPressed(View view) {

        // check text edit or checkbox question
        String correctAnswer = allQuestions.list.get(questionNumber).questionSet.get("answer").toString();

        QuestionType type = (QuestionType) allQuestions.list.get(questionNumber).questionSet.get("format");

        if (type == QuestionType.TEXTENTRY) {
            EditText answerField = findViewById(R.id.answer_field);
            String proposedAnswer = answerField.getText().toString();

            if (correctAnswer.toLowerCase().equals(proposedAnswer.toLowerCase())) {
                showToast(true);
                score += 1;
            } else {
                showToast(false);
            }
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
                checkBoxSum += 2;
                buttonA.setChecked(false);
            }

            if (buttonB.isChecked()) {
                checkBoxSum += 4;
                buttonB.setChecked(false);
            }

            if (buttonC.isChecked()) {
                checkBoxSum += 8;
                buttonC.setChecked(false);
            }

            if (buttonD.isChecked()) {
                checkBoxSum += 16;
                buttonD.setChecked(false);
            }

            String checkBoxAnswer = "" + checkBoxSum;
            if (checkBoxAnswer.equals(correctAnswer)) {
                showToast(true);
                score += 1;
            } else {
                showToast(false);
            }
        }

        hideAll();

        questionNumber += 1;

        nextQuestion();

    }

    // Button or Radio Group question.  Determine if answer is correct and uncheck (if applicable)
    public void answerPressed(View view) {
        RadioButton radioButton;
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
            case R.id.radiobutton_a:
                pickedAnswer = "a";
                radioButton = findViewById(R.id.radiobutton_a);
                radioButton.setChecked(false);
                break;
            case R.id.radiobutton_b:
                pickedAnswer = "b";
                radioButton = findViewById(R.id.radiobutton_b);
                radioButton.setChecked(false);
                break;
            case R.id.radiobutton_c:
                pickedAnswer = "c";
                radioButton = findViewById(R.id.radiobutton_c);
                radioButton.setChecked(false);
                break;
            case R.id.radiobutton_d:
                pickedAnswer = "d";
                radioButton = findViewById(R.id.radiobutton_d);
                radioButton.setChecked(false);
                break;
            default:
        }

        checkAnswer();

        questionNumber += 1;

        nextQuestion();

    }

    // Verify if Answer was correct and inform user and add score
    private void checkAnswer() {
        String correctAnswer = allQuestions.list.get(questionNumber).questionSet.get("answer").toString();

        if (correctAnswer.equals(pickedAnswer)) {
            showToast(true);
            score += 1;
        } else {
            showToast(false);
        }

        hideAll();
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
        // Update progress Bar to show how far in the quiz we are
        progressBar.getLayoutParams().width = parentWidth / numberOfQuestions * (questionNumber + 1);
//        progressBar.requestLayout();
    }

    // Restart the game for another attempt.
    private void restart() {
        questionNumber = 0;
        score = 0;
        Collections.shuffle(allQuestions.list);
        nextQuestion();

    }
}
