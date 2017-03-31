package tkapps.questionaire.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import tkapps.questionaire.Answer;
import tkapps.questionaire.Interrogation;
import tkapps.questionaire.R;
import tkapps.questionaire.data.*;

public class QuizActivity extends AppCompatActivity {
    //Liefert Nummer der aktuellen Frage
    public static int questionCounter = 0;
    //Zählt den Punktestand
    public static int scoreCounter = 0;
    //Gesamtmenge der Fragen im aktuellen Katalog
    public static int amountQuestions = 0;

    private DataStore dataStore;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //Fängt Methode nach letzter Frage ab und springt zur Punkteübersicht
        if (questionCounter == amountQuestions && amountQuestions != 0) {
            questionCounter = 0;
            amountQuestions = 0;
            goToEnd();
        } else {

            TextView textView_scoreCounter = (TextView) findViewById(R.id.textView_scoreCounter);
            TextView textView_questionCounter = (TextView) findViewById(R.id.textView_questionCounter);
            TextView textView_Question = (TextView) findViewById(R.id.textView_question);
            //ImageView initialisieren
            ImageView imageView_Quiz = (ImageView) findViewById(R.id.imageView_Quiz);

            //Logowechsel
            pref = getSharedPreferences("Questionaire", MODE_PRIVATE);
            if (!pref.getString("Logo", "").isEmpty()) {
                File imgFile = new File(pref.getString("Logo", ""));

                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageView_Quiz.setImageBitmap(myBitmap);
                } else {
                    Toast.makeText(this, getString(R.string.logoMovedOrDeleted), Toast.LENGTH_SHORT).show();
                }
            }

            dataStore = DataStore.getInstance(getApplicationContext());

            //Holt die Fragestellungen (Interrogation) aus der DB
            List<Interrogation> interrogations = dataStore.getQuestions();
            //Gesamtzahl der Fragen
            amountQuestions = dataStore.getAmountQuestions();

            //Aktuelle Frage und Antworten vorbereiten
            Interrogation fragestellung = interrogations.get(questionCounter);
            String frage = fragestellung.getQuestion();
            Answer[] antworten = fragestellung.getAnswers();

            //Arrayadapter für die Listview erstellt
            //Für die einzelnen Elemente wird das Layout aus der answer_item.xml gezogen
            ArrayAdapter<Answer> answerlistAdapter = new ArrayAdapter<>(
                    this,
                    R.layout.answer_item,
                    antworten);

            //Erstellen der ListView und Anbindung des Adapters
            final ListView list_answer = (ListView) findViewById(R.id.listView_answer);
            list_answer.setAdapter(answerlistAdapter);

            textView_Question.setText(frage);
            textView_questionCounter.setText(Integer.toString(questionCounter + 1) + " / " + amountQuestions);
            textView_scoreCounter.setText(Integer.toString(scoreCounter));

            //Taste um die aktuelle Frage zu überspringen
            Button button_Skip = (Button) findViewById(R.id.button_skip);
            button_Skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    questionCounter++;
                    scoreCounter -= 1;
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    Toast.makeText(QuizActivity.this, getString(R.string.questionSkipped), Toast.LENGTH_SHORT).show();
                }
            });

            //Ermöglicht die auswahl einer Antwort + Kontrolle ob sie korrekt ist
            list_answer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Answer answer = (Answer) parent.getItemAtPosition(position);
                    //Prüfen ob Antwort korrekt ist
                    String meldung = getString(R.string.answerWrong);
                    if (answer.isCorrect()) {
                        meldung = getString(R.string.answerRight);
                        scoreCounter += 5;
                    } else {
                        scoreCounter -= 3;
                    }

                    Toast.makeText(QuizActivity.this, meldung, Toast.LENGTH_SHORT).show();
                    questionCounter++;
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
        }
    }

    //Punkteübersicht nach dem Quiz
    public void goToEnd() {
        setContentView(R.layout.activity_overview);

        TextView textView_endScore = (TextView) findViewById(R.id.textView_endScore);
        textView_endScore.setText(Integer.toString(scoreCounter));

        Button button_goOn = (Button) findViewById(R.id.button_goOn);
        button_goOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(QuizActivity.this, AddScoreActivity.class);
                intent.putExtra("INT_I_NEED", scoreCounter);
                startActivity(intent);
            }
        });
    }
}
