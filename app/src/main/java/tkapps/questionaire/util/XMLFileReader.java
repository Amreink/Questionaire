package tkapps.questionaire.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;

import tkapps.questionaire.DataImporter;
import tkapps.questionaire.interfaces.OnFileSelectedListener;


/**
 * Created by pascal.euhus on 02.02.2016.
 * Changed by karsten.amrein on 30.03.2017.
 */
public class XMLFileReader {

    public static void openDialogToReadXML(final Context context) {
        File mPath = getRootDirectoryForFileDialog(context);
        FileDialog fileDialog = new FileDialog(context, mPath);
        fileDialog.setFileEndsWith(".xml");
        fileDialog.addFileListener(new OnFileSelectedListener() {
            @Override
            public void onFileSelected(File file) {
                new DataImporter(context, file).execute();
            }
        });
        fileDialog.showDialog();
    }

    private static File getRootDirectoryForFileDialog(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            return new File(sharedPreferences.getString("fout.data.path",Environment.getExternalStorageDirectory() + "//DIR//"));
    }
}
