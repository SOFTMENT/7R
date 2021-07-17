package in.softment.playrangersadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.softment.playrangersadmin.Adapter.CricketPlayersAdapter;
import in.softment.playrangersadmin.Adapter.CricketShowAllMatchsAdapter;
import in.softment.playrangersadmin.Api.CrickApi;


import in.softment.playrangersadmin.Model.CricketMatchDetailsModel.Datum;
import in.softment.playrangersadmin.Model.CricketMatchSquad.CricketMatchSquadModel;
import in.softment.playrangersadmin.Model.CricketMatchSquad.Squad;

import in.softment.playrangersadmin.Service.ProgressHud;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Cricket_Create_Combos extends AppCompatActivity {
    String comboType;
    Datum datum;


    private CricketPlayersAdapter cricketPlayersAdapter;
    private TextView team1Name,team2Name;
    private  ArrayList<Map<String,String>> playersInfos;
    private  int total = 2;

    private List<Squad> homeTeamSquad, awayTeamSquad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cricket__create__combos);



        comboType = getIntent().getStringExtra("comboType");
        datum = (Datum) getIntent().getSerializableExtra("datum");



        playersInfos = new ArrayList<>();

        team1Name = findViewById(R.id.team1Name);
        team2Name = findViewById(R.id.team2Name);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


       homeTeamSquad =  CricketMatchSquadModel.cricketMatchSquadModelMap.get(String.valueOf(datum.getLocalteamId())).getData().getSquad();
       awayTeamSquad = CricketMatchSquadModel.cricketMatchSquadModelMap.get(String.valueOf(datum.getVisitorteamId())).getData().getSquad();

       team1Name.setText(CricketMatchSquadModel.cricketMatchSquadModelMap.get(String.valueOf(datum.getLocalteamId())).getData().getCode());
       team2Name.setText(CricketMatchSquadModel.cricketMatchSquadModelMap.get(String.valueOf(datum.getVisitorteamId())).getData().getCode());


       Log.d("VIJAYT",datum.getLocalteamId() +"  "+datum.getVisitorteamId());

        if (comboType.equalsIgnoreCase("duo")) {
            total = 2;
        }
        else  if (comboType.equalsIgnoreCase("trio")) {
            total = 3;
        }
        else  if (comboType.equalsIgnoreCase("squad")) {
            total = 4;
        }
        cricketPlayersAdapter = new CricketPlayersAdapter(this,homeTeamSquad,awayTeamSquad,total);
        recyclerView.setAdapter(cricketPlayersAdapter);

        findViewById(R.id.createComboBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playersInfos.size() == total) {
                    ProgressHud.show(Cricket_Create_Combos.this, "Wait...");


                    FirebaseFirestore.getInstance().collection("Cricket").document(String.valueOf(datum.getId())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (!document.exists()) {

                                    Map<String, Object> map1 = new HashMap<>();
                                    map1.put("tournamentId",datum.getSeasonId());
                                    map1.put("matchId",datum.getId());
                                    map1.put("teamIdHome",datum.getLocalteamId());
                                    map1.put("teamIdAway",datum.getVisitorteamId());
                                    map1.put("teamNameHome",CricketMatchSquadModel.cricketMatchSquadModelMap.get(String.valueOf(datum.getLocalteamId())).getData().getCode());
                                    map1.put("teamNameAway",CricketMatchSquadModel.cricketMatchSquadModelMap.get(String.valueOf(datum.getVisitorteamId())).getData().getCode());
                                    map1.put("teamImageHome",CricketMatchSquadModel.cricketMatchSquadModelMap.get(String.valueOf(datum.getLocalteamId())).getData().getImagePath());
                                    map1.put("teamImageAway",CricketMatchSquadModel.cricketMatchSquadModelMap.get(String.valueOf(datum.getVisitorteamId())).getData().getImagePath());

                                    map1.put("startTime",datum.getStartingAt());
                                    FirebaseFirestore.getInstance().collection("Cricket").document(String.valueOf(datum.getId())).set(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                addCombo();
                                            }
                                            else {
                                                ProgressHud.dialog.dismiss();
                                                Toast.makeText(Cricket_Create_Combos.this, "INTERNAL ERROR 905", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                                else {
                                    addCombo();
                                }
                            } else {

                                ProgressHud.dialog.dismiss();
                                Toast.makeText(Cricket_Create_Combos.this, "INTERNAL ERROR 906", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }
                else {
                    String m = "Please add "+ (total - playersInfos.size()) +" more players";
                    Toast.makeText(Cricket_Create_Combos.this, m, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void addCombo() {


        CollectionReference collectionReference =  FirebaseFirestore.getInstance().collection("Cricket").document(String.valueOf(datum.getId())).collection(comboType);
        String comboID = collectionReference.document().getId();
        Map<String, Object> map = new HashMap();
        map.put("comboId",comboID);
        map.put("matchId",datum.getId());
        map.put("playersInfos",playersInfos);
        map.put("totalPrizePool",0);
        map.put("title","Cricket Battle - "+comboType.toUpperCase());
        map.put("totalSpots",500);
        map.put("eachComboPlayers",total);
        map.put("totalInvestors",0);

        collectionReference.document(comboID).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ProgressHud.dialog.dismiss();
                Toast.makeText(Cricket_Create_Combos.this, "COMBO SUCCESSFULLY ADDED", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ProgressHud.dialog.dismiss();
                Toast.makeText(Cricket_Create_Combos.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void addPlayers(String playerName, String playerId, String playerImage) {
       // Toast.makeText(this, ""+playerName, Toast.LENGTH_SHORT).show();
        Map<String, String> map = new HashMap();
        map.put("playerName",playerName);
        map.put("playerId",playerId);
        if (playerImage == null) {
            map.put("playerImage","");
        }
        else {
            map.put("playerImage",playerImage);
        }



        playersInfos.add(map);
    }

    public void removePlayer(String playerId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            playersInfos.removeIf(s -> s.get("playerId").equalsIgnoreCase(playerId));
        }
    }



}



