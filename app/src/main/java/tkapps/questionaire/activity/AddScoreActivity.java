package tkapps.questionaire.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.jar.Attributes;

import tkapps.questionaire.R;
import tkapps.questionaire.data.DataStore;

public class AddScoreActivity extends AppCompatActivity {

    private DataStore dataStore;

    public SharedPreferences keepNameEmail;
    SharedPreferences.Editor edit;
    public static int Score;

    //Zum Abgleich mit den eingegebenen Daten der Felder Name und E-Mail
    String namePattern = "[\\sa-zA-Z0-9.-]+";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addscore);

        //Shared Data vorbereiten
        keepNameEmail = getSharedPreferences("Leaderboard", MODE_PRIVATE);
        edit = keepNameEmail.edit();

        //Eingabefelder initialisieren
        EditText name = (EditText) findViewById(R.id.editText_name);
        EditText email = (EditText) findViewById(R.id.editText_email);
        //Textfeld für Punkte initialisieren und füllen
        TextView textView_score = (TextView) findViewById(R.id.textView_scoreLeaderboard);
        textView_score.setText(Integer.toString(QuizActivity.scoreCounter));
        //Textfelder über Eingabefeldern initialisieren
        final TextView textView_name = (TextView) findViewById(R.id.textView_name);
        final TextView textView_email = (TextView) findViewById(R.id.textView_email);

        dataStore = DataStore.getInstance(getApplicationContext());

        //Daten aus Übersicht(Score)
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                Score = 0;
            } else {
                Score = extras.getInt("INT_I_NEED");
            }
        } else {
            Score = (int) savedInstanceState.getSerializable("INT_I_NEED");
        }

        //Daten nach ungültiger Eingabe + Absenden wiederherstellen (Name, Email)
        name.setText(keepNameEmail.getString("name", ""));
        email.setText(keepNameEmail.getString("email", ""));

        //Eingabe von Daten überspringen
        Button button_skip = (Button) findViewById(R.id.button_skip);
        button_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuizActivity.scoreCounter = 0;
                edit.putString("email", "");
                edit.putString("name", "");
                edit.commit();
                finish();
            }
        });

        //Daten Absenden, prüfen und in Datenbank aufnehmen, bzw. Fehler zurüchgeben (siehe sendData())
        Button button_send = (Button) findViewById(R.id.button_send);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });

        //Prüfen ob Namens-Eingabe gültig ist, sobald sie geändert wird
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                EditText name = (EditText) findViewById(R.id.editText_name);
                String validateName = name.getText().toString();

                if (validateName.matches(namePattern) && s.length() > 0) {
                    textView_name.setTextColor(0xff22B900);
                } else {
                    textView_name.setTextColor(0xffFF0000);
                }
            }
        });

        //Prüfen ob E-Mail-Eingabe gültig ist, sobald sie geändert wird
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                EditText email = (EditText) findViewById(R.id.editText_email);
                String validateEmail = email.getText().toString();

                if (validateEmail.matches(emailPattern) && s.length() > 0) {
                    textView_email.setTextColor(0xff22B900);
                } else {
                    textView_email.setTextColor(0xffFF0000);
                }
            }
        });
    }
    //Prüft die eingegebenen Daten und sendet sie, falls gültig
    public void sendData() {
        EditText editText_name = (EditText) findViewById(R.id.editText_name);
        EditText editText_email = (EditText) findViewById(R.id.editText_email);
        CheckBox checkBox_agb = (CheckBox) findViewById(R.id.AGB);

        //Eingabefeld Email
        String email = editText_email.getText().toString().trim();
        //Eingabefeld Name
        String name = editText_name.getText().toString();

        //Falls Eingabe ungültig, um Eingegebenes wiederherzustellen
        edit.putString("email", email);
        edit.putString("name", name);
        edit.commit();

        //Beide Felder wurden korrekt gefüllt
        if (email.matches(emailPattern) && name.matches(namePattern)) {
            boolean agb = checkBox_agb.isChecked();
            dataStore.addScore(name, email, QuizActivity.scoreCounter, agb);
            Toast.makeText(AddScoreActivity.this, R.string.dataSaved, Toast.LENGTH_SHORT).show();
            QuizActivity.scoreCounter = 0;
            edit.putString("email", "");
            edit.putString("name", "");
            edit.commit();
            finish();
        } else {
            //Eingegebene E-Mail ungültig
            if (!email.matches(emailPattern) && name.matches(namePattern)) {
                Toast.makeText(AddScoreActivity.this, R.string.badEmail, Toast.LENGTH_LONG).show();
            }
            //Eingegebener Name ungültig
            else if (!name.matches(namePattern) && email.matches(emailPattern)) {
                Toast.makeText(AddScoreActivity.this, R.string.badName, Toast.LENGTH_LONG).show();
            }
            //Eingegebene E-Mail und Name ungültig
            else {
                Toast.makeText(AddScoreActivity.this, R.string.badNameEmail, Toast.LENGTH_LONG).show();
            }
            finish();
            Intent intent = getIntent();
            startActivity(intent);
        }
    }
    //Zurück Taste überschreiben um Fehlfunktionen zu verhindern
    @Override
    public void onBackPressed() {
        QuizActivity.scoreCounter = 0;
        edit.putString("email", "");
        edit.putString("name", "");
        edit.commit();
        finish();
    }
}
