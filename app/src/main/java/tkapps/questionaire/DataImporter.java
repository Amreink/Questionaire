package tkapps.questionaire;

import android.content.Context;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import tkapps.questionaire.data.DataStore;

/**
 * Created by Karsten on 30.03.2017.
 */

public class DataImporter {

    private Context context;
    private File file;
    private DataStore dataStore;

    public DataImporter(Context context, File file) {

        this.context = context;
        this.file = file;
    }


        public void execute() {
            dataStore = DataStore.getInstance(context);
            try{
            //Do something
            //Lese die XML ein
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

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
                    String question =  getValue("Frage", item);
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
                    Toast.makeText(context, "XML erfolgreich exportiert", Toast.LENGTH_SHORT).show();
                }
            }
            } catch (Exception e) {e.printStackTrace();}
        }

    //Methode um die Knoten aus der XML auszulesen
    private String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
    }

