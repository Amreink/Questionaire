package tkapps.questionaire.data;

/**
 * Created by Karsten on 21.02.2017.
 */

public class DbSchema {
    //Fragenkatalog
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

    //Bestenliste
    public static final class ScoreTable {
        public static final String SCORENAME = "Score";

        public static final class Columns {
            public static final String NAME= "name";
            public static final String EMAIL = "email";
            public static final String SCORE = "score";
            public static final String AGB = "AGB";
        }
    }
}
