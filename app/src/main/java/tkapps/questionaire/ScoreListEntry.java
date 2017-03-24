package tkapps.questionaire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tkapps.questionaire.data.DataStore;

/**
 * Created by Timo on 20.03.2017.
 */

public class ScoreListEntry {

    private String name;
    private String email;
    private int score;
    private boolean agb;

    public ScoreListEntry(String name, String email, int score, boolean agb){
        this.name = name;
        this.email = email;
        this.score = score;
        this.agb = agb;
    }

    public String getName() {
        return name;
    }
    public String getEmail() { return email; }
    public int getScore() {
        return score;
    }
    public boolean getAGB() { return agb; }
}

