package net.drunkdogs.quizzler;

import java.util.ArrayList;

/**
 * Created by Tiberius on 1/24/18.
 */

// An array of questions for the quiz.  Just add more questions to make the quiz longer.
public class QuestionBank {

    ArrayList<Question> list = new ArrayList<>();

    public QuestionBank() {
        list.add(new Question("Under the constitution, some powers belong to the states, what is one power of the states?",
                "coin or print money",
                "make treaties",
                "provide schooling and education",
                "create an army",
                "c",
                QuestionType.BUTTON));
        list.add(new Question("Who was the President during the Great Depression and World War II?",
                "Calvin Coolidge",
                "Harry Truman",
                "Herbert Hoover",
                "Franklin Roosevelt",
                "d",
                QuestionType.RADIO));
        list.add(new Question("What do we show loyalty to when we say the Pledge of Allegiance?",
                "Congress",
                "the President",
                "the United States",
                "the state where you live",
                "c",
                QuestionType.BUTTON));
        list.add(new Question("There are four amendments to the Constitution about who can vote. Describe one of them.",
                "Only citizens with a job can vote.",
                "Citizens seventeen (17) and older can vote",
                "Citizens by birth only can vote",
                "Citizens eighteen (18) and older can vote.",
                "d",
                QuestionType.BUTTON));
        list.add(new Question("We elect a US Senator for how many years?",
                "six (6)",
                "ten (10)",
                "two (2)",
                "four (4)",
                "a",
                QuestionType.RADIO));
        list.add(new Question("What is the forty-fifth (45th) President\'s last name?", "", "", "", "",
                "Trump",
                QuestionType.TEXTENTRY));
        list.add(new Question("Which of these States touch the Pacific Ocean?",
                "California",
                "Washington",
                "Colorado",
                "Texas",
                "6",
                QuestionType.CHECKBOX));

    }


}
