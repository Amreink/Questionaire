package tkapps.questionaire.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tkapps.questionaire.Answer;
import tkapps.questionaire.Interrogation;

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

    private static ContentValues getContentValues(Interrogation interrogation){
        ContentValues values = new ContentValues();
        values.put(DbSchema.Table.Columns.QUESTION, interrogation.getQuestion());
        values.put(DbSchema.Table.Columns.ANSWER1, interrogation.getAnswer(0).toString());
        values.put(DbSchema.Table.Columns.ANSWER2, interrogation.getAnswer(1).toString());
        values.put(DbSchema.Table.Columns.ANSWER3, interrogation.getAnswer(2).toString());
        values.put(DbSchema.Table.Columns.ANSWER4, interrogation.getAnswer(3).toString());

        Answer[] answers = interrogation.getAnswers();
        for (int i = 0; i < answers.length; i++) {
            if(answers[i].isCorrect()){
                values.put(DbSchema.Table.Columns.CORRECT_ANSWER, Integer.toString(i));
            }
        }

        return values;
    }

    public void addQuestion(Interrogation interrogation){
        ContentValues values = getContentValues(interrogation);
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

    public List<Interrogation> getQuestions(){
        List<Interrogation> questions = new ArrayList<>();

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

    //TODO: Truncate (als wüsstest du noch was das gerade bedeutet... noob)
    public void removeLecture(Interrogation interrogation){
        String question = interrogation.getQuestion();
        db.delete(DbSchema.Table.NAME, DbSchema.Table.Columns.QUESTION + " = ?", new String[]{question});
    }

    public int getAmountQuestions(){
        Cursor c = db.rawQuery("select * from "+DbSchema.Table.NAME,null);
        return c.getCount();
    }
}
