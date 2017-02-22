package tkapps.questionaire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Button button_import = (Button)findViewById(R.id.button_import);
        Button button_edit = (Button)findViewById(R.id.button_edit);
        Button button_export = (Button)findViewById(R.id.button_export);

        //Xml einspielen

        //Button button_import soll eine XML importieren können
        button_import.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Tu Dinge!
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
