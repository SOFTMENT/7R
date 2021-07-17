package in.softment.playrangersadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import in.softment.playrangersadmin.Adapter.CricketComboAdapter;
import in.softment.playrangersadmin.Model.CombosModel;
import in.softment.playrangersadmin.Model.CricketMatchDetailsModel.Datum;
import in.softment.playrangersadmin.Utils.Constants;

public class CricketShowAllCombos extends AppCompatActivity {

    String comboType;
    Datum datum;
    private RecyclerView recyclerView;
    private CricketComboAdapter cricketComboAdapter;
    private ArrayList<CombosModel> combosModels;
    private TextView prizePoolTv, totalInvestorTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cricket_show_all_combos);

        comboType = getIntent().getStringExtra("comboType");
        datum = (Datum) getIntent().getSerializableExtra("datum");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        combosModels = new ArrayList<>();

        int total = 2;
        if (comboType.equalsIgnoreCase("duo")) {
            total = 2;
        }
        else  if (comboType.equalsIgnoreCase("trio")) {
            total = 3;
        }
        else  if (comboType.equalsIgnoreCase("squad")) {
            total = 4;
        }

        cricketComboAdapter = new CricketComboAdapter(this,combosModels,total);
        recyclerView.setAdapter(cricketComboAdapter);

        //getMatchAllCombos
        getMatchAllCombos();

        findViewById(R.id.addCombos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CricketShowAllCombos.this, Cricket_Create_Combos.class);
                intent.putExtra("comboType",comboType);
                intent.putExtra("datum",datum);
                startActivity(intent);
            }
        });


    }

    public void getMatchAllCombos() {
        FirebaseFirestore.getInstance().collection(Constants.DbPath.cricket).document(String.valueOf(datum.getId())).collection(comboType).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    if (value != null && !value.isEmpty()) {
                        combosModels.clear();

                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            CombosModel combosModel = documentSnapshot.toObject(CombosModel.class);
                            if (combosModel != null) {
                                combosModels.add(combosModel);
                            }

                        }

                        cricketComboAdapter.notifyDataSetChanged();
                    }

                }
                else {
                    Toast.makeText(CricketShowAllCombos.this, ""+error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }




}