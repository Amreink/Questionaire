package tkapps.questionaire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import tkapps.questionaire.data.DataStore;

public class SettingsActivity extends AppCompatActivity {

    private DataStore dataStore;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;

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
            // TODO Auto-generated method stub
            if (requestCode == 1) {
                if(resultCode == this.RESULT_OK) {
                    String newDir = data.getStringExtra(
                            FileBrowserActivity.returnDirectoryParameter);
                    Toast.makeText(
                            this,
                            "Received path from file browser:"+newDir,
                            Toast.LENGTH_LONG
                    ).show();
                } else {//if(resultCode == this.RESULT_OK) {
                    Toast.makeText(
                            this,
                            "Received NO result from file browser",
                            Toast.LENGTH_LONG)
                            .show();
                }//END } else {//if(resultCode == this.RESULT_OK) {
            }//if (requestCode == REQUEST_CODE_PICK_FILE_TO_SAVE_INTERNAL) {
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
        public void showSettings() {

            setContentView(R.layout.activity_settings);

            TextView textView_path = (TextView) findViewById(R.id.textView_path);
            Button button_import = (Button) findViewById(R.id.button_importXML);
            Button button_edit = (Button) findViewById(R.id.button_editXML);
            Button button_export = (Button) findViewById(R.id.button_exportXML);
            Button button_changePassword = (Button)findViewById(R.id.button_changePassword);

            //XML Pfad anzeigen


            //Datenbankanbindung
            dataStore = DataStore.getInstance(getApplicationContext());

            //ImportPfad auswählen
            textView_path.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent fileExploreIntent = new Intent(
                        FileBrowserActivity.INTENT_ACTION_SELECT_DIR,
                        null,
                        SettingsActivity.this,
                        FileBrowserActivity.class
                    );
//  fileExploreIntent.putExtra(
//      ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.startDirectoryParameter,
//      "/sdcard"
//  );//Here you can add optional start directory parameter, and file browser will start from that directory.
                    startActivityForResult(
                        fileExploreIntent,
                        1
                    );
                }
            });


            //Xml einspielen

            //Button button_import soll eine XML importieren können
            button_import.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                try {
                    InputStream is = getAssets().open("xml_questionnaire.xml");

                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(is);

                    Element element=doc.getDocumentElement();
                    element.normalize();

                    NodeList nList = doc.getElementsByTagName("FrageUndAntwort");
                    //String[] values = {"Frage", "Antwort1","Anwort2","Antwort3","Antwort4","Korrekte_Antwort"};

                    for (int i=0; i<nList.getLength(); i++) {

                        Node node = nList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element item = (Element) node;

                            String question = getValue("Frage", item);
                            String correct_answer =  getValue("Korrekte_Antwort", item);

                            String[] dbAnswers = {
                                    getValue("Antwort1", item),
                                    getValue("Antwort2", item),
                                    getValue("Antwort3", item),
                                    getValue("Antwort4", item)
                            };

                            Answer[] answers = new Answer[4];

                            for (int counter = 0; counter < 4; counter++) {
                                boolean correctAnswer = false;
                                if (Integer.parseInt(correct_answer) == counter) {
                                    correctAnswer = true;
                                }
                                answers[counter] = new Answer(dbAnswers[counter], correctAnswer);
                            }

                            Interrogation interrogation = new Interrogation(
                                    question, answers[0], answers[1], answers[2], answers[3]);
                            dataStore.addQuestion(interrogation);
                        }
                    }

                } catch (Exception e) {e.printStackTrace();}

                }

                private String getValue(String tag, Element element) {
                    NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
                    Node node = nodeList.item(0);
                    return node.getNodeValue();
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

}