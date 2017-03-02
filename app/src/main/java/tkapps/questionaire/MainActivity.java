package tkapps.questionaire;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    public static boolean toastShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Beantragen der Berechtigung
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            // Nicht nötig da Berechtigung angefragt wird
            if (!toastShown) {
                Toast.makeText(this, "Die App muss einen Ordner anlegen.", Toast.LENGTH_SHORT).show();
                toastShown = true;
            }
            //App um 2 Sekunden verzögern, damit Nutzer Zeit hat den Toast zu lesen
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    //Hier wird die Berechtigung beim Nutzer angefragt (Mit 2 Sek Verzögerung)
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        Toast.makeText(MainActivity.this, "Die App muss nur einen Ordner für die XML anlegen. Es geschieht nichts mit Ihren Daten.", Toast.LENGTH_SHORT).show();
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            public void run() {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                            }
                        }, 4000);

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }
            }, 2000);

        } else{
            permGranted();
        }
    }

    //requestPermissions gibt einen Rükgabewert zurück mit welchem wir mit der App fortfahren oder sie beenden müssen
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Kopiere die Dateien aus Assets in den Ordner
                    copyAssets();
                    //Berechtigung wurde erteilt, fahre mit App fort
                    permGranted();

                } else {

                    //ToDo: Permission erneut beantragen oder App sauber beenden
                    finish();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //Verhalten der Activity wenn die Berechtigungen erteilt wurden
    private void permGranted(){



        //Button initialisieren
        Button button_start = (Button) findViewById(R.id.button_start);
        Button button_settings = (Button) findViewById(R.id.button_settings);
        Button button_leaderboard = (Button) findViewById(R.id.button_leaderboard);

        //Button button_start ruft die QuizActivity auf
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });

        //Button button_settings ruft die Einstellungen auf
        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        //Button button_settings ruft die Bestenliste auf
        button_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });

    }

    //Methode um Dateien des Assets-Ordners in das App-Verzeichnis zu kopieren
    //Wird benötigt, damit der Nutzer sich die XML aus dem Verzeichnis kopieren kann
    private void copyAssets() {
        AssetManager assetManager = getAssets();
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
                if (filename.equals("xml_questionnaire.xml")) {
                    Toast.makeText(this, filename, Toast.LENGTH_SHORT).show();
                    in = assetManager.open(filename);
                    File outFile = new File(getExternalFilesDir(null), filename);
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                }
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
        }
    }
    //Hilfsmethode für copyAssets()
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

}
