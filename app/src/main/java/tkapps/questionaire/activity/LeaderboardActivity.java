package tkapps.questionaire.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import tkapps.questionaire.R;
import tkapps.questionaire.ScoreListEntry;
import tkapps.questionaire.data.DataStore;

public class LeaderboardActivity extends AppCompatActivity {

    private DataStore dataStore;

    public class LeaderboardAdapter extends ArrayAdapter {
        public LeaderboardAdapter(List items) {
            super(LeaderboardActivity.this, 0, items);
        }

        //Befüllen der einzelnen Teile der ListView Items
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.leaderboard_item, null);
            }
            final ScoreListEntry lb = (ScoreListEntry) getItem(position);
            TextView textView_name = (TextView) convertView.findViewById(R.id.textView_nameLb);
            textView_name.setText(lb.getName());
            TextView textView_score = (TextView) convertView.findViewById(R.id.textView_scoreLb);
            textView_score.setText(Integer.toString(lb.getScore()));

            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        dataStore = DataStore.getInstance(getApplicationContext());

        //Eigener Adapter für Leaderboard erstellt
        LeaderboardAdapter leaderboardlistAdapter = new LeaderboardAdapter(dataStore.getScoreListEntry());

        ListView listView_leaderboard = (ListView) findViewById(R.id.listView_leaderboard);
        listView_leaderboard.setAdapter(leaderboardlistAdapter);
    }

}
