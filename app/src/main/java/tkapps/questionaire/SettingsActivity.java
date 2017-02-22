package tkapps.questionaire;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button button_import = (Button)findViewById(R.id.button_import);
        Button button_edit = (Button)findViewById(R.id.button_edit);
        Button button_export = (Button)findViewById(R.id.button_export);

        //Datenbankanbindung
        dataStore = DataStore.getInstance(getApplicationContext());
        //Xml einspielen

        //Button button_import soll eine XML importieren können
        button_import.setOnClickListener(new View.OnClickListener(){
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

                } catch (Exception e) {e.printStackTrace();}

            }

            private String getValue(String tag, Element element) {
                NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
                Node node = nodeList.item(0);
                return node.getNodeValue();
            }

            });

        //Button button_edit soll Fragen edtieren können
        button_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Tu Dinge!
            }
        });

        //Button button_export soll eine XML exportieren können
        button_export.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Tu Dinge!
            }
        });
        //Bestenliste zurücksetzen






    }
}
