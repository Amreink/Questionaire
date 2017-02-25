package tkapps.questionaire;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tkapps.questionaire.data.DataStore;

public class SettingsActivity extends AppCompatActivity {

    private DataStore dataStore;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

           /* //shared data vorbereiten
            pref = getSharedPreferences("Questionaire", MODE_PRIVATE);
            editor = pref.edit();

            //prüfen ob Passwort gesetzt wurde
            if (pref.getString("password", "").equals("")) {
                setPassword();
            } else {
                enterPassword();
            }
        }
        //Hier wird das Passwort gesetzt, falls noch keines vorhanden war
        public void setPassword(){

            setContentView(R.layout.activity_setpassword);

            Button confirmButton = (Button) findViewById(R.id.button_confirmPIN);
            confirmButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    EditText password = (EditText) findViewById(R.id.password);

                    editor.putString("password", password.getText().toString());
                    editor.commit();
                }
            });
            showSettings();
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
                        Toast.makeText(SettingsActivity.this, "Passwort stimmt nicht.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        public void showSettings() {*/

            setContentView(R.layout.activity_settings);

            Button button_import = (Button) findViewById(R.id.button_importXML);
            Button button_edit = (Button) findViewById(R.id.button_editXML);
            Button button_export = (Button) findViewById(R.id.button_exportXML);

            //Datenbankanbindung
            dataStore = DataStore.getInstance(getApplicationContext());
            //Xml einspielen

            //Button button_import soll eine XML importieren können
            button_import.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SettingsActivity.this, ListFilesActivity.class);
                    startActivity(intent);
                /*try {
                    InputStream is = getAssets().open("xml_questionnaire.xml");

                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(is);

                    Element element=doc.getDocumentElement();
                    element.normalize();

                    NodeList nList = doc.getElementsByTagName("FrageUndAntwort");

                    for (int i=0; i<nList.getLength(); i++) {

                        Node node = nList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element2 = (Element) node;
                            String question = getValue("Frage", element2);
                            String answer1 = getValue("Antwort1", element2);
                            String answer2 = getValue("Antwort2", element2);
                            String answer3 = getValue("Antwort3", element2);
                            String answer4 = getValue("Antwort4", element2);
                            String correct_answer =  getValue("Korrekte_Antwort", element2);
                            Questions questions = new Questions(question, answer1, answer2, answer3, answer4, correct_answer);
                            dataStore.addQuestion(questions);
                        }
                    }

                } catch (Exception e) {e.printStackTrace();}*/

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


        }

}