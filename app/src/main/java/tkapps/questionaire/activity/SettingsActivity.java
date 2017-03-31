package tkapps.questionaire.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

import java.io.File;
import java.io.FileOutputStream;

import tkapps.questionaire.CopyAssets;
import tkapps.questionaire.R;
import tkapps.questionaire.data.DataStore;
import tkapps.questionaire.util.PNGFileReader;
import tkapps.questionaire.util.XMLFileReader;

public class SettingsActivity extends AppCompatActivity {

    private DataStore dataStore;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static final int REQUEST_DIRECTORY = 6385; // on ActivityResult request
    // code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //shared data vorbereiten
        pref = getSharedPreferences("Questionaire", MODE_PRIVATE);
        editor = pref.edit();

        //prüfen ob Passwort gesetzt wurde
        if (pref.getString("password", "").equals("")) {
            setPassword();
        } else {
            enterPassword();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_DIRECTORY:
                //Export der XML-Datei in ein beliebiges Directory

                if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                    File file = new File(data
                            .getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR));
                    CopyAssets.copyAssets(getApplicationContext(), file);

                } else {
                    // Nothing selected
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Hier wird das Passwort gesetzt, initial und durch Einstellungen -> Passwort ändern
    public void setPassword() {

        editor.putString("password", "");
        editor.commit();

        setContentView(R.layout.activity_password);

        TextView textView_password = (TextView) findViewById(R.id.textView_password);
        textView_password.setText(R.string.setup_password);

        Button confirmButton = (Button) findViewById(R.id.button_enterPassword);
        confirmButton.setText(R.string.confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText password = (EditText) findViewById(R.id.password);

                editor.putString("password", password.getText().toString());
                editor.commit();
                Toast.makeText(SettingsActivity.this, "Passwort erfolgreich geändert.", Toast.LENGTH_LONG).show();

                showSettings();
            }
        });
    }

    //Aufforderung zur Passworteingabe falls bereits ein Passwort erstellt wurde
    public void enterPassword() {

        setContentView(R.layout.activity_password);

        TextView textView_password = (TextView) findViewById(R.id.textView_password);
        textView_password.setText(R.string.enter_password);

        Button loginButton = (Button) findViewById(R.id.button_enterPassword);
        loginButton.setText(R.string.sign_in);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText password = (EditText) findViewById(R.id.password);

                if (pref.getString("password", "").equals(password.getText().toString())) {
                    showSettings();
                } else {
                    Toast.makeText(SettingsActivity.this, "Passwort falsch.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Anzeigen der SettingsActivity und initialisieren der Buttons
    //Dies sind die Einstellungen
    public void showSettings() {

        setContentView(R.layout.activity_settings);
        //Buttons initialisieren
        Button button_import = (Button) findViewById(R.id.button_importXML);
        ImageButton button_helpXML = (ImageButton) findViewById(R.id.button_helpXML);
        Button button_export = (Button) findViewById(R.id.button_exportXML);
        Button button_changePassword = (Button) findViewById(R.id.button_changePassword);
        Button button_changeLogo = (Button) findViewById(R.id.button_changeLogo);
        Button button_resetLeaderboard = (Button) findViewById(R.id.button_leaderboardReset);
        Button button_exportLeaderboard = (Button) findViewById(R.id.button_settingsLeaderboardExport);
        ImageButton button_helpLB = (ImageButton) findViewById(R.id.button_helpLB);

        //Datenbankanbindung
        dataStore = DataStore.getInstance(getApplicationContext());

        //Button button_import soll eine XML importieren können
        button_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Datenbank leeren
                //Nötig, damit eine XML nicht mehrmals eingelesen wird
                dataStore.removeQuestions();
                //Dialogfenster Auswahl XML-Datei öffnen
                XMLFileReader.openDialogToReadXML(SettingsActivity.this);
            }
        });
        //Button button_help soll Hilfe für die Erstellung eigener Fragenkataloge anzeigen
        button_helpXML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelp(1);
            }
        });

        //Button button_export soll eine XML exportieren können
        button_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooserIntent = new Intent(SettingsActivity.this, DirectoryChooserActivity.class);

                DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                        .newDirectoryName("DirChooserSample")
                        .allowReadOnlyDirectory(true)
                        .allowNewDirectoryNameModification(true)
                        .build();

                chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);

                // REQUEST_DIRECTORY is a constant integer to identify the request, e.g. 0
                startActivityForResult(chooserIntent, REQUEST_DIRECTORY);
            }
        });

        //Passwort ändern
        button_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPassword();
            }
        });

        //Firmenlogo ändern
        button_changeLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PNGFileReader.openDialogToReadPNG(SettingsActivity.this);
            }
        });
        //Bestenliste zurücksetzen
        button_resetLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataStore.removeLeaderboard();
                Toast.makeText(SettingsActivity.this, R.string.resetLeaderboard, Toast.LENGTH_LONG).show();
            }
        });
        //Bestenliste exportieren
        button_exportLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportLeaderboard();
            }
        });
        //Hilfe für Bestenliste anzeigen
        button_helpLB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelp(2);
            }
        });
    }

    //Steuerung im Hilfe-Fenster
    public void showHelp(int index) {
        setContentView(R.layout.activity_help);

        TextView textView_help = (TextView) findViewById(R.id.textView_help);
        ImageView imageView_help = (ImageView) findViewById(R.id.imageView_help);

        if (index == 1) {
            textView_help.setText(R.string.help_xml);
            imageView_help.setImageResource(R.drawable.interrogation_help);
        } else if (index == 2) {
            textView_help.setText(R.string.help_lb);
        }
        Button button_exitHelp = (Button) findViewById(R.id.button_exitHelp);
        button_exitHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettings();
            }
        });
    }

    //Die Bestenliste als .csv exportieren
    public void exportLeaderboard() {

        //Cursor
        Cursor cursor = dataStore.getScoreboard();
        FileOutputStream outputStream;

        if (!cursor.moveToFirst()) {
            Toast.makeText(this, getString(R.string.csvNoEntry), Toast.LENGTH_SHORT).show();
        }

        File file = new File(getExternalFilesDir(null), "Bestenliste.csv");
        try {
            outputStream = new FileOutputStream(file);

            do {
                // if any of the columns have commas in their values, you will have to do more involved
                // checking here to ensure they are escaped properly in the csv

                // the numbes are column indexes. if you care about the order of the columns in your
                // csv, you may want to move them around

                outputStream.write(cursor.getString(0).getBytes());
                outputStream.write(",".getBytes());
                outputStream.write(cursor.getString(1).getBytes());
                outputStream.write(",".getBytes());
                outputStream.write(cursor.getString(2).getBytes());
                outputStream.write(",".getBytes());
                outputStream.write(cursor.getString(3).getBytes());
                outputStream.write("\n".getBytes());

            } while (cursor.moveToNext());
            outputStream.flush();
            outputStream.close();
            Toast.makeText(this, getString(R.string.csvExportSuccessful), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

            e.printStackTrace();
        }
        cursor.close();
    }
}


