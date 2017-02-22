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

    private static ContentValues getContentValues(Questions questions){
        ContentValues values = new ContentValues();
        values.put(DbSchema.Table.Columns.QUESTION, questions.getQuestion());
        values.put(DbSchema.Table.Columns.ANSWER1, questions.getAnswer1());
        values.put(DbSchema.Table.Columns.ANSWER2, questions.getAnswer2());
        values.put(DbSchema.Table.Columns.ANSWER3, questions.getAnswer3());
        values.put(DbSchema.Table.Columns.ANSWER4, questions.getAnswer4());
        values.put(DbSchema.Table.Columns.CORRECT_ANSWER, questions.getCorrect_answer());
        return values;
    }

    public void addQuestion(Questions questions){
        ContentValues values = getContentValues(questions);
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
