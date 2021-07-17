package in.softment.playrangersadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import in.softment.playrangersadmin.Model.CricketMatchDetailsModel.Datum;


public class CricketChooseCombosScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cricket_choose_combos_screen);


        Datum datum = (Datum) getIntent().getSerializableExtra("datum");


        findViewById(R.id.squad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(CricketChooseCombosScreen.this, CricketShowAllCombos.class);
               intent.putExtra("comboType","squad");
               intent.putExtra("datum",datum);
               startActivity(intent);
            }
        });

        findViewById(R.id.duo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CricketChooseCombosScreen.this, CricketShowAllCombos.class);
                intent.putExtra("comboType","duo");
                intent.putExtra("datum",datum);
                startActivity(intent);
            }
        });
        findViewById(R.id.trio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CricketChooseCombosScreen.this, CricketShowAllCombos.class);
                intent.putExtra("comboType","trio");
                intent.putExtra("datum",datum);
                startActivity(intent);
            }
        });
    }





}