package tkapps.questionaire.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tkapps.questionaire.Questions;

/**
 * Created by Karsten on 21.02.2017.
 */

public class DataStore {
    private static DataStore dataStore;
    private SQLiteDatabase db;

    private DataStore(Context context){
        db = new DbHelper(context).getWritableDatabase();
    }

    public static DataStore getInstance(Context context){
        if(dataStore == null)
            dataStore = new DataStore(context);
        return dataStore;
    }

    private static ContentValues getContentValues(String question, String answer1, String answer2, String answer3, String answer4, String correct_answer){
        ContentValues values = new ContentValues();
        values.put(DbSchema.Table.Columns.QUESTION, question);
        values.put(DbSchema.Table.Columns.ANSWER1, answer1);
        values.put(DbSchema.Table.Columns.ANSWER2, answer2);
        values.put(DbSchema.Table.Columns.ANSWER3, answer3);
        values.put(DbSchema.Table.Columns.ANSWER4, answer4);
        values.put(DbSchema.Table.Columns.CORRECT_ANSWER, correct_answer);
        return values;
    }

    public void addQuestion(String question, String answer1, String answer2, String answer3, String answer4, String correct_answer){
        ContentValues values = getContentValues(question, answer1, answer2, answer3, answer4, correct_answer);
        db.insert(DbSchema.Table.NAME, null, values);
    }
    private DbCursorWrapper queryQuestions(String whereClause, String[] whereArgs){
        Cursor cursor = db.query(
            DbSchema.Table.NAME,
                    null,
                    whereClause,
                    whereArgs,
                    null,
                    null,
                    null
        );
        return new DbCursorWrapper(cursor);
    }

    public List<Questions> getQuestions(){
        List<Questions> questions = new ArrayList<>();

        DbCursorWrapper cursor = queryQuestions(null, null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                questions.add(cursor.getQuestion());
                cursor.moveToNext();
            }
        } finally {cursor.close(); }
        return questions;
    }

    public void removeLecture(Questions question){
        String title = question.getQuestion();
        db.delete(DbSchema.Table.NAME, DbSchema.Table.Columns.QUESTION + " = ?", new String[]{title});
    }
}
