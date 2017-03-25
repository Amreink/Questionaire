package tkapps.questionaire.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import tkapps.questionaire.Answer;
import tkapps.questionaire.Interrogation;
import tkapps.questionaire.ScoreListEntry;

/**
 * Created by Karsten on 21.02.2017.
 */

public class DataStore {
    private static DataStore dataStore;
    private SQLiteDatabase db;

    //Singleton
    private DataStore(Context context){
        db = new DbHelper(context).getWritableDatabase();
    }

    public static DataStore getInstance(Context context){
        if(dataStore == null)
            dataStore = new DataStore(context);
        return dataStore;
    }

    //Zerlegt interrogation in values um diese in die Datenbank zu geben
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

    //Fügt Daten in die Tabelle ein
    public void addQuestion(Interrogation interrogation){
        ContentValues values = getContentValues(interrogation);
        db.insert(DbSchema.Table.NAME, null, values);
    }

    //Allgemeine SQL-Abfrage
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

    //Cursor liest Tabelle Zeile für Zeile aus
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

    //Entfernt aktuellen Fragekatalog
    public void removeQuestions(){
        db.execSQL("delete from "+ DbSchema.Table.NAME);
        db.execSQL("vacuum");
    }

    //Gibt Anzahl der Fragen aus
    public int getAmountQuestions(){
        Cursor c = db.rawQuery("select * from "+DbSchema.Table.NAME,null);
        return c.getCount();
    }

    //Abfragen für Score-Tabelle
    private static ContentValues getScoreValues(String name, String email, int score, boolean agb){
        ContentValues values = new ContentValues();
        values.put(DbSchema.ScoreTable.Columns.NAME, name);
        values.put(DbSchema.ScoreTable.Columns.EMAIL, email);
        values.put(DbSchema.ScoreTable.Columns.SCORE, score);
        values.put(DbSchema.ScoreTable.Columns.AGB, agb);
        return values;
    }
    //Fügt Daten in die Tabelle ein
    public void addScore(String name, String email, int score, boolean agb){
        ContentValues values = getScoreValues(name, email, score, agb);
        db.insert(DbSchema.ScoreTable.SCORENAME, null, values);
    }

    //Allgemeine SQL-Abfrage für Cursor
    private DbCursorWrapper queryScore(String whereClause, String[] whereArgs){
        Cursor cursor = db.query(
                DbSchema.ScoreTable.SCORENAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new DbCursorWrapper(cursor);
    }

    //Cursor liest Tabelle Zeile für Zeile aus
    public List<ScoreListEntry> getScoreListEntry(){
        List<ScoreListEntry> scorelist = new ArrayList<>();

        DbCursorWrapper cursorScore = queryScore(null, null);
        try{
            cursorScore.moveToFirst();
            while(!cursorScore.isAfterLast()) {
                scorelist.add(cursorScore.getScore());
                cursorScore.moveToNext();
            }
        } finally {cursorScore.close(); }
        return scorelist;
    }

    public void removeLeaderboard() {
    //Entfernt aktuelle Bestenliste
        db.execSQL("delete from " + DbSchema.ScoreTable.SCORENAME);
        db.execSQL("vacuum");
    }

    //Gibt Anzahl der Fragen aus
    public Cursor getScoreboard(){
        Cursor c = db.rawQuery("SELECT * FROM Score",null);;
        return c;
    }

}
