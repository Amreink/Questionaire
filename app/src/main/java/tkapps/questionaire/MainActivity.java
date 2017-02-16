package tkapps.questionaire;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        String [] answerlistArray = {
                "Antwort 1",
                "Antwort 2",
                "Antwort 3",
                "Antwort 4",
                "Antwort 5"
        };

        ArrayAdapter <String> answerlistAdapter = new ArrayAdapter<String>(
                this,
                R.layout.answer_items,
                answerlistArray);

        ListView answer_list = (ListView)findViewById(R.id.answer_list);
        answer_list.setAdapter(answerlistAdapter);

    }
}
