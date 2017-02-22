package tkapps.questionaire.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Karsten on 21.02.2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "quiz.sqlite";
    public static final int VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DbSchema.Table.NAME + " (" +
        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        DbSchema.Table.Columns.QUESTION + " TEXT NOT NULL, "+
        DbSchema.Table.Columns.ANSWER1 + " TEXT NOT NULL, "+
        DbSchema.Table.Columns.ANSWER2 + " TEXT NOT NULL, "+
        DbSchema.Table.Columns.ANSWER3 + " TEXT NOT NULL, "+
        DbSchema.Table.Columns.ANSWER4 + " TEXT NOT NULL, "+
        DbSchema.Table.Columns.CORRECT_ANSWER + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
