package tkapps.questionaire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tkapps.questionaire.data.*;

public class QuizActivity extends AppCompatActivity {

    public static int questionCounter = 0;
    public static int scoreCounter = 0;
    public static int amountQuestions = 0;

    private ArrayAdapter adapter;
    private DataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        /*if (questionCounter == amountQuestions && amountQuestions != 0) {
            goToEnd();
        }*/

        TextView textView_scoreCounter = (TextView) findViewById(R.id.textView_scoreCounter);
        TextView textView_questionCounter = (TextView) findViewById(R.id.textView_questionCounter);
        TextView textView_Question = (TextView) findViewById(R.id.textView_question);

        dataStore = DataStore.getInstance(getApplicationContext());
        List<Interrogation> interrogations = dataStore.getQuestions();

        amountQuestions = dataStore.getAmountQuestions();
        Interrogation fragestellung = interrogations.get(questionCounter);
        String frage = fragestellung.getQuestion();
        Answer[] antworten = fragestellung.getAnswers();

        //Arrayadapter für die Listview erstellt
        //Für die einzelnen Elemente wird das Layout aus der answer_itemxml gezogen
        ArrayAdapter<Answer> answerlistAdapter = new ArrayAdapter<Answer>(
                this,
                R.layout.answer_item,
                antworten);

        //Erstellen der ListView und Anbindung des Adapters
        final ListView list_answer = (ListView)findViewById(R.id.listView_answer);
        list_answer.setAdapter(answerlistAdapter);

        textView_Question.setText(frage);
        textView_questionCounter.setText(Integer.toString(questionCounter+1)+" / "+amountQuestions);
        textView_scoreCounter.setText(Integer.toString(scoreCounter));

        list_answer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Answer answer = (Answer) parent.getItemAtPosition(position);

                String meldung = "Antwort ist falsch!";
                if (answer.isCorrect()) {
                    meldung = "Antwort ist richtig!";
                    scoreCounter+=5;
                }

                Toast.makeText(QuizActivity.this, meldung, Toast.LENGTH_SHORT).show();
                questionCounter++;
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });

    }
    /*public void goToEnd(){
        setContentView(R.layout.activity_end);

        TextView textView_endScore = (TextView)findViewById(R.id.textView_endScore);
        Button button_goOn = (Button) findViewById(R.id.button_goOn);
        //OnClick Listener einfügen und auf Main verweisen

        textView_endScore.setText(scoreCounter);
    }*/
}
