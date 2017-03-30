package tkapps.questionaire.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;

import tkapps.questionaire.DataImporter;
import tkapps.questionaire.interfaces.OnFileSelectedListener;

/**
 * Created by Karsten on 30.03.2017.
 */

public class PNGFileReader {
    public static void openDialogToReadPNG(final Context context) {
        File mPath = getRootDirectoryForFileDialog(context);
        FileDialog fileDialog = new FileDialog(context, mPath);
        fileDialog.setFileEndsWith(".png");
        fileDialog.addFileListener(new OnFileSelectedListener() {
            @Override
            public void onFileSelected(File file) {
                new DataImporter(context, file).savePath();
            }
        });
        fileDialog.showDialog();
    }

    private static File getRootDirectoryForFileDialog(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new File(sharedPreferences.getString("fout.data.path", Environment.getExternalStorageDirectory() + "//DIR//"));
    }
}
