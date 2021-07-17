package in.softment.playrangers.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import in.softment.playrangers.Adapter.CricketBattleAdapter;
import in.softment.playrangers.CricketPointCalculation;
import in.softment.playrangers.Model.CombosModel;
import in.softment.playrangers.Model.CricketMatchDetailsModel;
import in.softment.playrangers.R;
import in.softment.playrangers.Services.Service;
import in.softment.playrangers.Utils.Constants;

public class CricketShowBattleFragment extends Fragment {
    protected Animation blink_anim;
    ShapeableImageView matchimage2;
    private AppCompatButton totalCollection;
    ShapeableImageView matchimage1;
    private TextView timer, date, macthname1, macthname2;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",Locale.getDefault());
    private Context context;
    private CricketMatchDetailsModel match;
    private LinearLayout duo, trio, squad;
    private int prizePool = 0;
    private final Handler mHandler = new Handler();

    private TextView activeMatchDuo, totalSpotLeftDuo, totalJoinedDuo,prizeDuo;
    private ProgressBar progressBarDuo;

    private TextView activeMatchTrio, totalSpotLeftTrio, totalJoinedTrio,prizeTrio;
    private ProgressBar progressBarTrio;

    private TextView activeMatchSquad, totalSpotLeftSquad, totalJoinedSquad,prizeSquad;
    private ProgressBar progressBarSquad;

    private List<CombosModel> combosModelsDuo, combosModelsTrio, combosModelsSquad;
    private int oldDuoPrize, oldTrioPrize , oldSquadPrize;

    private CricketSelectComboFragment cricketSelectComboFragment;

    private final Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {

                long currentTime = System.currentTimeMillis();
                updateTimeRemaining(currentTime);

        }
    };
    public CricketShowBattleFragment(Context context, CricketMatchDetailsModel match) {
        // Required empty public constructor
        this.context = context;
        this.match = match;
        startUpdateTimer();
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
    }
    private void startUpdateTimer() {
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cricket_join, container, false);

        matchimage1 = view.findViewById(R.id.matchimg1);
        matchimage2 = view.findViewById(R.id.matchimg2);
        matchimage1.setShapeAppearanceModel(ShapeAppearanceModel.builder().setAllCorners(CornerFamily.ROUNDED,16).build());
        matchimage2.setShapeAppearanceModel(ShapeAppearanceModel.builder().setAllCorners(CornerFamily.ROUNDED,16).build());
        timer = view.findViewById(R.id.timer);
        date = view.findViewById(R.id.date);
        macthname1 = view.findViewById(R.id.matchname1);
        macthname2 = view.findViewById(R.id.matchname2);


        totalCollection = view.findViewById(R.id.totalCollection);
        totalCollection.setText("Total Prize Pool: ₹0");


       updateTimeRemaining(System.currentTimeMillis());

        macthname1.setText(match.getTeamNameHome());
        macthname2.setText(match.getTeamNameAway());

        if (match.teamImageHome != null && !match.teamImageHome.isEmpty() && !match.teamImageHome.equalsIgnoreCase("https://cdn.sportmonks.com") ) {
            Glide.with(context).load(match.teamImageHome).into(matchimage1);
        }
        else {
           matchimage1.setImageResource(R.drawable.photonotavail);
        }

        if (match.teamImageAway != null && !match.teamImageAway.isEmpty() && !match.teamImageAway.equalsIgnoreCase("https://cdn.sportmonks.com") ) {
            Glide.with(context).load(match.teamImageAway).into(matchimage2);
        }
        else {
            matchimage2.setImageResource(R.drawable.photonotavail);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date futuredate = new Date();
        try {
            futuredate = dateFormat.parse(match.getStartTime());

        } catch (ParseException e) {

            e.printStackTrace();
        }
        date.setText(Service.convertDate(futuredate));


        //Points Calculation
        view.findViewById(R.id.pointCalculation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, CricketPointCalculation.class));
            }
        });

        //Linear layout initialize
        duo = view.findViewById(R.id.llduo);
        trio = view.findViewById(R.id.lltrio);
        squad = view.findViewById(R.id.llsquad);

        //CombosModel
        combosModelsDuo = new ArrayList<>();
        combosModelsTrio = new ArrayList<>();
        combosModelsSquad = new ArrayList<>();

        //CLICK EVENT
        duo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAvailableCombos(2);
            }
        });

        trio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAvailableCombos(3);
            }
        });

        squad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAvailableCombos(4);
            }
        });

        //Duo
        activeMatchDuo = view.findViewById(R.id.availablematchduo);
        progressBarDuo = view.findViewById(R.id.progressbarsduo);
        totalSpotLeftDuo = view.findViewById(R.id.totallefttextduo);
        prizeDuo = view.findViewById(R.id.prizeduo);
        totalJoinedDuo = view.findViewById(R.id.totalduo);

        activeMatchDuo.setText("Total Combos "+0);
        progressBarDuo.setProgress(0);
        totalSpotLeftDuo.setText("");
        prizeDuo.setText("0");
        totalJoinedDuo.setText("");


        //Trio
        activeMatchTrio = view.findViewById(R.id.availablematchtrio);
        progressBarTrio = view.findViewById(R.id.progressbarstrio);
        totalSpotLeftTrio = view.findViewById(R.id.totallefttexttrio);
        prizeTrio = view.findViewById(R.id.prizetrio);
        totalJoinedTrio = view.findViewById(R.id.totaltrio);

        activeMatchTrio.setText("Total Combos "+0);
        progressBarTrio.setProgress(0);
        totalSpotLeftTrio.setText("");
        prizeTrio.setText("0");
        totalJoinedTrio.setText("");




        //Squad
        activeMatchSquad = view.findViewById(R.id.availablematchsquad);
        progressBarSquad = view.findViewById(R.id.progressbarssquad);
        totalSpotLeftSquad = view.findViewById(R.id.totallefttextSquad);
        prizeSquad = view.findViewById(R.id.prizesquad);
        totalJoinedSquad = view.findViewById(R.id.totalSquad);

        activeMatchSquad.setText("Total Combos "+0);
        progressBarSquad.setProgress(0);
        totalSpotLeftSquad.setText("");
        prizeSquad.setText("0");
        totalJoinedSquad.setText("");





        //GETDATA
        getCombos();


        return view;
    }

    private void getCombos() {

        FirebaseFirestore.getInstance().collection(Constants.DbPath.cricket).document(String.valueOf(match.matchId)).collection("duo").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {

                    if (value != null && value.size()>0) {
                        int totalJoined = 0;
                        int totalPrize = 0;
                        int totalSpots = 0;
                        prizePool -= oldDuoPrize;

                        activeMatchDuo.setText("Total Combos "+ value.size());
                        combosModelsDuo.clear();
                        for (QueryDocumentSnapshot documentSnapshot : value) {

                            CombosModel combosModel = documentSnapshot.toObject(CombosModel.class);
                            totalJoined += combosModel.totalInvestors;
                            totalPrize += combosModel.totalPrizePool;
                            totalSpots = combosModel.totalSpots;
                            combosModelsDuo.add(combosModel);
                        }

                        if (cricketSelectComboFragment !=null) {
                            cricketSelectComboFragment.notifyComboAdapter();
                        }


                        prizePool += totalPrize;
                        oldDuoPrize = totalPrize;
                        totalCollection.setText("Total Prize Pool : ₹"+prizePool);

                        totalJoinedDuo.setText(totalJoined+"/"+totalSpots);
                        prizeDuo.setText("₹"+totalPrize);
                        progressBarDuo.setMax(totalSpots);
                        progressBarDuo.setProgress(totalJoined);
                        if (totalJoined >= totalSpots) {
                            totalSpotLeftDuo.setText("Sorry! No spot left");
                        }
                        else {
                            totalSpotLeftDuo.setText("Only "+ (totalSpots - totalJoined) +" spots left");
                        }
                        duo.setVisibility(View.VISIBLE);


                    }
                    else{
                        duo.setVisibility(View.GONE);
                    }
                }

            }
        });




        FirebaseFirestore.getInstance().collection(Constants.DbPath.cricket).document(String.valueOf(match.matchId)).collection("trio").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    if (value != null && value.size()>0) {
                        int totalJoined = 0;
                        int totalPrize = 0;
                        int totalSpots = 0;
                        prizePool -= oldTrioPrize;
                        activeMatchTrio.setText("Total Combos "+ value.size());
                        combosModelsTrio.clear();
                        for (QueryDocumentSnapshot documentSnapshot : value) {

                            CombosModel combosModel = documentSnapshot.toObject(CombosModel.class);
                            totalJoined += combosModel.totalInvestors;
                            totalPrize += combosModel.totalPrizePool;
                            totalSpots = combosModel.totalSpots;
                            combosModelsTrio.add(combosModel);
                        }
                        if (cricketSelectComboFragment !=null) {
                            cricketSelectComboFragment.notifyComboAdapter();
                        }


                        prizePool += totalPrize;
                        oldTrioPrize = totalPrize;
                        totalCollection.setText("Total Prize Pool : ₹"+prizePool);

                        totalJoinedTrio.setText(totalJoined+"/"+totalSpots);
                        prizeTrio.setText("₹"+totalPrize);
                        progressBarTrio.setMax(totalSpots);
                        progressBarTrio.setProgress(totalJoined);
                        if (totalJoined >= totalSpots) {
                            totalSpotLeftTrio.setText("Sorry! No spot left");
                        }
                        else {
                            totalSpotLeftTrio.setText("Only "+ (totalSpots - totalJoined) +" spots left");
                        }
                        trio.setVisibility(View.VISIBLE);


                    }
                    else{
                        trio.setVisibility(View.GONE);
                    }
                }

            }
        });

        FirebaseFirestore.getInstance().collection(Constants.DbPath.cricket).document(String.valueOf(match.matchId)).collection("squad").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    if (value != null && value.size()>0) {
                        int totalJoined = 0;
                        int totalPrize = 0;
                        int totalSpots = 0;

                        prizePool -= oldSquadPrize;

                        activeMatchSquad.setText("Total Combos "+ value.size());

                        combosModelsSquad.clear();

                        Log.d("CHANGED","YES");
                        for (QueryDocumentSnapshot documentSnapshot : value) {

                            CombosModel combosModel = documentSnapshot.toObject(CombosModel.class);
                            totalJoined += combosModel.totalInvestors;
                            totalPrize += combosModel.totalPrizePool;
                            totalSpots = combosModel.totalSpots;
                            combosModelsSquad.add(combosModel);
                        }
                        if (cricketSelectComboFragment !=null) {
                            cricketSelectComboFragment.notifyComboAdapter();
                        }


                        prizePool += totalPrize;
                        oldSquadPrize = totalPrize;
                        totalCollection.setText("Total Prize Pool : ₹"+prizePool);

                        totalJoinedSquad.setText(totalJoined+"/"+totalSpots);
                        prizeSquad.setText("₹"+totalPrize);
                        progressBarSquad.setMax(totalSpots);
                        progressBarSquad.setProgress(totalJoined);
                        if (totalJoined >= totalSpots) {
                            totalSpotLeftSquad.setText("Sorry! No spot left");
                        }
                        else {
                            totalSpotLeftSquad.setText("Only "+ (totalSpots - totalJoined) +" spots left");
                        }
                        squad.setVisibility(View.VISIBLE);


                    }
                    else{
                        squad.setVisibility(View.GONE);
                    }
                }

            }
        });


    }

    public void initalizSelectComboFragment(CricketSelectComboFragment cricketSelectComboFragment) {
        this.cricketSelectComboFragment = cricketSelectComboFragment;
    }


    public void updateTimeRemaining(long currentTime) {
        Date futureDate = new Date();
        try {
            futureDate = dateFormat.parse(match.getStartTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeDiff = futureDate.getTime() - currentTime;


        if (timeDiff > 0) {
            int seconds = (int) (timeDiff / 1000) % 60;
            int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
            int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 1000);

            timer.setText("START IN: "+hours + ":" + minutes + ":" + seconds);

        } else {

            timer.setText("LIVE");
            timer.setTextColor(Color.RED);
            timer.startAnimation(blink_anim);
        }
    }


    public void showAvailableCombos(int i) {
        FragmentTransaction ft = null;
        if (i == 2) {
           ft = getActivity().getSupportFragmentManager().beginTransaction().add(R.id.main_container,new CricketSelectComboFragment(context,combosModelsDuo,this));
        }
        else if (i == 3) {
            ft = getActivity().getSupportFragmentManager().beginTransaction().add(R.id.main_container,new CricketSelectComboFragment(context,combosModelsTrio,this));
        }
        else {
            ft = getActivity().getSupportFragmentManager().beginTransaction().add(R.id.main_container,new CricketSelectComboFragment(context,combosModelsSquad,this));
        }

        ft.addToBackStack(null);
        ft.commit();

    }
}