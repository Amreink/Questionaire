package tkapps.questionaire.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import tkapps.questionaire.R;
import tkapps.questionaire.data.DataStore;

public class AddScoreActivity extends AppCompatActivity {

    private DataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addscore);

        TextView textView_score = (TextView) findViewById(R.id.textView_scoreLeaderboard);
        textView_score.setText(Integer.toString(QuizActivity.scoreCounter));

        dataStore = DataStore.getInstance(getApplicationContext());

        Button button_skip = (Button) findViewById(R.id.button_skip);
        button_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuizActivity.scoreCounter = 0;
                finish();
            }
        });

        Button button_send = (Button) findViewById(R.id.button_send);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
    }

    public void sendData() {
        TextView textView_name = (TextView) findViewById(R.id.textView_name);
        EditText editText_name = (EditText) findViewById(R.id.editText_name);
        TextView textView_email = (TextView) findViewById(R.id.textView_email);
        EditText editText_email = (EditText) findViewById(R.id.editText_email);
        CheckBox checkBox_agb = (CheckBox) findViewById(R.id.AGB);

        textView_name.setTextColor(0xff0099cc);
        textView_email.setTextColor(0xff0099cc);

        //Eingabefeld + erlaubte Zeichen fuer Email
        String email = editText_email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        //Eingabefeld + erlaubte Zeichen fuer Name
        String name = editText_name.getText().toString();
        String namePattern = "[\\sa-zA-Z0-9.-]+";

        if (email.matches(emailPattern) && name.matches(namePattern)) {
            boolean agb = checkBox_agb.isChecked();
            dataStore.addScore(name, email, QuizActivity.scoreCounter, agb);
            Toast.makeText(AddScoreActivity.this, R.string.dataSaved, Toast.LENGTH_SHORT).show();
            QuizActivity.scoreCounter = 0;
            finish();

        } else if(email.matches(emailPattern) == false) {
            textView_email.setTextColor(0xffCC0000);
            Toast.makeText(AddScoreActivity.this, R.string.badEmail, Toast.LENGTH_LONG).show();
            Intent intent = getIntent();
            finish();
            startActivity(intent);

        } else if(name.matches(namePattern) == false) {
            textView_email.setTextColor(0xffCC0000);
            Toast.makeText(AddScoreActivity.this, R.string.badName, Toast.LENGTH_LONG).show();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}