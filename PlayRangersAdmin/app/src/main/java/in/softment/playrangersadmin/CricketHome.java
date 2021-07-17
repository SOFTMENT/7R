package in.softment.playrangersadmin;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import in.softment.playrangersadmin.Adapter.CricketShowAllMatchsAdapter;
import in.softment.playrangersadmin.Api.CrickApi;
import in.softment.playrangersadmin.Model.CricketMatchDetailsModel.CricketMatchModel;

import in.softment.playrangersadmin.Model.CricketMatchDetailsModel.Datum;
import in.softment.playrangersadmin.Model.CricketMatchSquad.CricketMatchSquadModel;
import in.softment.playrangersadmin.Service.ProgressHud;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CricketHome extends AppCompatActivity {
    List<Datum> datum;
    private RecyclerView recyclerView;
    private CricketShowAllMatchsAdapter cricketShowAllMatchsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cricket_home);

        //getAllMatchesDetails()
        getAllIPLMatchesDetails();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datum = new ArrayList<>();
        cricketShowAllMatchsAdapter = new CricketShowAllMatchsAdapter(CricketHome.this,datum);
        recyclerView.setAdapter(cricketShowAllMatchsAdapter);


    }

    public void getAllIPLMatchesDetails() {

        ProgressHud.show(this,"Fetching Details...");

        new CrickApi().getUpcomingMatches().enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProgressHud.dialog.dismiss();

                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Gson gson = new Gson();
                ResponseBody responseBody = response.body();
                gson.fromJson(responseBody.string(), CricketMatchModel.class);
                Log.d("VIJAY",CricketMatchModel.cricketMatchModel.getData().size()+"WOW");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      //  ProgressHud.dialog.dismiss();
                        updateAllMatch();
                    }
                });
            }
        });




    }

    public void updateAllMatch() {
        if (datum.size() > 0) {
            datum.clear();
        }

        DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.getDefault());
        f.setTimeZone(TimeZone.getTimeZone("IST"));




        datum.addAll(CricketMatchModel.cricketMatchModel.getData());
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            datum.removeIf(s -> {
//                try {
//
//                    return ((s.getStatus().equalsIgnoreCase("Finished")) ||  (f.parse(s.getStartingAt()).getTime()  > System.currentTimeMillis()));
//                } catch (ParseException e) {
//
//                    e.printStackTrace();
//                    return true;
//                }
//            });
//        }

        CricketMatchSquadModel.cricketMatchSquadModelMap.clear();
        for (Datum datum : datum) {
            getSquadByTeamAndSeasonId(String.valueOf(datum.getLocalteamId()),String.valueOf(datum.getSeasonId()));
            getSquadByTeamAndSeasonId(String.valueOf(datum.getVisitorteamId()),String.valueOf(datum.getSeasonId()));
        }

        ProgressHud.dialog.dismiss();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cricketShowAllMatchsAdapter.notifyDataSetChanged();
            }
        },6000);





    }

    public void getSquadByTeamAndSeasonId(String teamId, String seasonId) {




        new CrickApi().getSquadByTeamAndSeasonId(teamId,seasonId).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProgressHud.dialog.dismiss();

                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Gson gson = new Gson();
                ResponseBody responseBody = response.body();


                CricketMatchSquadModel cricketMatchSquadModel = gson.fromJson(responseBody.string(), CricketMatchSquadModel.class);


                Log.d("VIJAY",cricketMatchSquadModel.getData().getName());
                  CricketMatchSquadModel.cricketMatchSquadModelMap.put(teamId,cricketMatchSquadModel);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });

    }









}