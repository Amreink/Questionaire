package tkapps.questionaire.data;

import android.database.Cursor;
import android.database.CursorWrapper;

import tkapps.questionaire.Answer;
import tkapps.questionaire.Interrogation;
import tkapps.questionaire.ScoreListEntry;

/**
 * Created by Karsten on 21.02.2017.
 */

public class DbCursorWrapper extends CursorWrapper {
    public DbCursorWrapper(Cursor cursor){
        super(cursor);
    }

    //Lese Fragespalte aus
    public Interrogation getQuestion(){
        String question = getString(
                getColumnIndex(DbSchema.Table.Columns.QUESTION));

        //Lese korrekte Antwort aus
        String correct_answer = getString(
                getColumnIndex(DbSchema.Table.Columns.CORRECT_ANSWER));

        //Lese Antworten in Array aus
        String[] dbAnswers = {
                getString(getColumnIndex(DbSchema.Table.Columns.ANSWER1)),
                getString(getColumnIndex(DbSchema.Table.Columns.ANSWER2)),
                getString(getColumnIndex(DbSchema.Table.Columns.ANSWER3)),
                getString(getColumnIndex(DbSchema.Table.Columns.ANSWER4))
        };

        //Gebe jeder Antwort mit, ob diese richtig oder falsch ist
        Answer[] answers = new Answer[4];

        for (int i = 0; i < 4; i++) {
            boolean correctAnswer = false;
            if (Integer.parseInt(correct_answer) == i) {
                correctAnswer = true;
            }
            answers[i] = new Answer(dbAnswers[i], correctAnswer);
        }

        return new Interrogation(question, answers[0], answers[1], answers[2], answers[3]);
    }

    public ScoreListEntry getScore(){
        String name = getString(getColumnIndex(DbSchema.ScoreTable.Columns.NAME));
        String email = getString(getColumnIndex(DbSchema.ScoreTable.Columns.EMAIL));
        int score = getColumnIndex(DbSchema.ScoreTable.Columns.SCORE);
        Boolean agb = getColumnIndex(DbSchema.ScoreTable.Columns.AGB) > 0;
        return new ScoreListEntry(name, email, score, agb);
    }
}
