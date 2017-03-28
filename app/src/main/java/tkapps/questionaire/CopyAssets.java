package tkapps.questionaire;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Karsten on 28.03.2017.
 */

public class CopyAssets {
    //Methode um Dateien des Assets-Ordners in das App-Verzeichnis zu kopieren
    //Wird benötigt, damit der Nutzer sich die XML aus dem Verzeichnis kopieren kann

    private static Context mCtx; //<-- declare a Context reference
    public static void copyAssets(Context context, File path) {
        mCtx = context;
        AssetManager assetManager = mCtx.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for(String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {

                in = assetManager.open(filename);
                File outFile = new File(path, filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;

            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
        }
    }
    //Hilfsmethode für copyAssets()
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
