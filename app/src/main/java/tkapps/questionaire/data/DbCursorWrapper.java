package tkapps.questionaire.data;

import android.database.Cursor;
import android.database.CursorWrapper;

import tkapps.questionaire.Questions;

/**
 * Created by Karsten on 21.02.2017.
 */

public class DbCursorWrapper extends CursorWrapper {
    public DbCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Questions getQuestion(){
        String question = getString(
                getColumnIndex(DbSchema.Table.Columns.QUESTION));
        String answer1 = getString(
                getColumnIndex(DbSchema.Table.Columns.ANSWER1));
        String answer2 = getString(
                getColumnIndex(DbSchema.Table.Columns.ANSWER2));
        String answer3 = getString(
                getColumnIndex(DbSchema.Table.Columns.ANSWER3));
        String answer4 = getString(
                getColumnIndex(DbSchema.Table.Columns.ANSWER4));
        String correct_answer = getString(
                getColumnIndex(DbSchema.Table.Columns.CORRECT_ANSWER));
        return new Questions(question, answer1, answer2, answer3, answer4, correct_answer);
    }
}
