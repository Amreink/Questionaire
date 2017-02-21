package tkapps.questionaire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        //Stringarray mit Beispieldaten für die ListView erstellt
        String [] answerlistArray = {
                "Antwort 1",
                "Antwort 2",
                "Antwort 3",
                "Antwort 4",
                "Antwort 5"
        };

        //Arrayadapter für die Listview erstellt
        //Für die einzelnen Elemente wird das Layout aus der answer_items.xml gezogen
        ArrayAdapter<String> answerlistAdapter = new ArrayAdapter<String>(
                this,
                R.layout.answer_items,
                answerlistArray);

        //Erstellen der ListView und Anbindung des Adapters
        ListView list_answer = (ListView)findViewById(R.id.listView_answer);
        list_answer.setAdapter(answerlistAdapter);
    }
}
