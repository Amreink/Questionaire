package tkapps.questionaire;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import tkapps.questionaire.data.DataStore;

public class SettingsActivity extends AppCompatActivity {

    private DataStore dataStore;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static final String TAG = "FileChooserActivity";
    private static final int REQUEST_CODE = 6384; // onActivityResult request
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
                case REQUEST_CODE:
                    // If the file selection was successful
                    if (resultCode == SettingsActivity.RESULT_OK) {
                        if (data != null) {
                            // Get the URI of the selected file
                            final Uri uri = data.getData();
                            Log.i(TAG, "Uri = " + uri.toString());
                            try {
                                // Get the file path from the URI
                                final String path = tkapps.questionaire.afilechooser.utils.FileUtils.getPath(this, uri);
                                Toast.makeText(SettingsActivity.this,
                                        "File Selected: " + path, Toast.LENGTH_LONG).show();
                                try {
                                    //Lese die XML ein
                                    File fXmlFile = new File(path);
                                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                                    Document doc = dBuilder.parse(fXmlFile);

                                    //XML Normalisieren (Keine parallelen Knoten etc.)
                                    Element element=doc.getDocumentElement();
                                    element.normalize();

                                    NodeList nList = doc.getElementsByTagName("FrageUndAntwort");
                                    //String[] values = {"Frage", "Antwort1","Anwort2","Antwort3","Antwort4","Korrekte_Antwort"};

                                    for (int i=0; i<nList.getLength(); i++) {

                                        Node node = nList.item(i);
                                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                                            Element item = (Element) node;

                                            //Werte auslesen
                                            String question = getValue("Frage", item);
                                            String correct_answer =  getValue("Korrekte_Antwort", item);

                                            String[] dbAnswers = {
                                                    getValue("Antwort1", item),
                                                    getValue("Antwort2", item),
                                                    getValue("Antwort3", item),
                                                    getValue("Antwort4", item)
                                            };

                                            Answer[] answers = new Answer[4];

                                            //Jeder Antwort mitgeben, ob sie korrekt ist
                                            for (int counter = 0; counter < 4; counter++) {
                                                boolean correctAnswer = false;
                                                if (Integer.parseInt(correct_answer) == counter) {
                                                    correctAnswer = true;
                                                }
                                                answers[counter] = new Answer(dbAnswers[counter], correctAnswer);
                                            }

                                            Interrogation interrogation = new Interrogation(
                                                    question, answers[0], answers[1], answers[2], answers[3]);
                                            //Datensatz abspeichern
                                            dataStore.addQuestion(interrogation);
                                        }
                                    }
                                    Toast.makeText(SettingsActivity.this, "Xml wurde erfolgreich importiert.", Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {e.printStackTrace();}

                            } catch (Exception e) {
                                Log.e("FileSelectorActivity", "File select error", e);
                            }
                        } else
                            Toast.makeText(this, "Datei ist leer", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

        //Hier wird das Passwort gesetzt, falls noch keines vorhanden war
        public void setPassword(){

            editor.putString("password", "");
            editor.commit();

            setContentView(R.layout.activity_setpassword);

            Button confirmButton = (Button) findViewById(R.id.button_confirmPassword);
            confirmButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    EditText password = (EditText) findViewById(R.id.password);

                    editor.putString("password", password.getText().toString());
                    editor.commit();
                    Toast.makeText(SettingsActivity.this,"Passwort erfolgreich geändert.",Toast.LENGTH_LONG).show();
                    showSettings();
                }
            });

        }
        //Aufforderung zur Passworteingabe falls bereits ein Passwort erstellt wurde
        public void enterPassword() {

            setContentView(R.layout.activity_password);

            Button loginButton = (Button) findViewById(R.id.button_enterPassword);
            loginButton.setOnClickListener(new View.OnClickListener(){
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

            Button button_import = (Button) findViewById(R.id.button_importXML);
            Button button_edit = (Button) findViewById(R.id.button_editXML);
            Button button_export = (Button) findViewById(R.id.button_exportXML);
            Button button_changePassword = (Button)findViewById(R.id.button_changePassword);

            //Datenbankanbindung
            dataStore = DataStore.getInstance(getApplicationContext());

            //Button button_import soll eine XML importieren können
            button_import.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Datenbank leeren
                    //Nötig, damit eine XML nicht mehrmals eingelesen wird
                    dataStore.removeQuestions();
                    //Dateibrowser öffnen
                    showChooser();
                }
            });

            //Button button_edit soll Fragen edtieren können
            button_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Tu Dinge!
                }
            });

            //Button button_export soll eine XML exportieren können
            button_export.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Tu Dinge!
                }
            });
            //Bestenliste zurücksetzen

            //Passwort ändern
            button_changePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setPassword();
                }
            });

        }

    //Zeigt den Dateibrowser an
    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = tkapps.questionaire.afilechooser.utils.FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }

    //Methode um die Knoten aus der XML auszulesen
    private String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
}

