/*
package tkapps.questionaire.csv;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import tkapps.questionaire.activity.SettingsActivity;
import tkapps.questionaire.data.DbSchema;

*/
/**
 * Created by Timo on 25.03.2017.
 *//*


public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {
    private final ProgressDialog dialog = new ProgressDialog();

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Exporting database...");
        this.dialog.show();
    }

    protected Boolean doInBackground(final String... args) {
        String currentDBPath = "/data/"+ "tkapps.questionaire" +"/data/abc.db";
        File dbFile = getDatabasePath(""+currentDBPath);
        System.out.println(dbFile);  // displays the data base path in your logcat
        File exportDir = new File(Environment.getExternalStorageDirectory(), "/your Folder Name/");

        if (!exportDir.exists()) { exportDir.mkdirs(); }

        File file = new File(exportDir, "myfile.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            Cursor curCSV = DbSchema.ScoreTable.SCORENAME.rawQuery("select * from ",null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext()) {
                String arrStr[]=null;
                String[] mySecondStringArray = new String[curCSV.getColumnNames().length];
                for(int i=0;i<curCSV.getColumnNames().length;i++)
                {
                    mySecondStringArray[i] =curCSV.getString(i);
                }
                csvWrite.writeNext(mySecondStringArray);
            }
            csvWrite.close();
            curCSV.close();
            return true;
        } catch (IOException e) {
            Log.e("MainActivity", e.getMessage(), e);
            return false;
        }
    }

    protected void onPostExecute(final Boolean success) {
        if (this.dialog.isShowing()) { this.dialog.dismiss(); }
        if (success) {
            Toast.makeText(SettingsActivity, "Export successful!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SettingsActivity, "Export failed", Toast.LENGTH_SHORT).show();
        }
    }
}*/
