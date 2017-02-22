package tkapps.questionaire.data;

/**
 * Created by Karsten on 21.02.2017.
 */

public class DbSchema {
    public static final class Table {
        public static final String NAME = "quiz";

        public static final class Columns {
            public static final String QUESTION = "question";
            public static final String ANSWER1 = "answer1";
            public static final String ANSWER2 = "answer2";
            public static final String ANSWER3 = "answer3";
            public static final String ANSWER4 = "answer4";
            public static final String CORRECT_ANSWER = "correct_answer";
        }
    }
}
