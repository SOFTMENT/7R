package in.softment.playrangers.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import in.softment.playrangers.Adapter.CricketOnGoingAdapter;
import in.softment.playrangers.Adapter.CricketUpcomingAdapter;

import in.softment.playrangers.Model.CricketMatchDetailsModel;
import in.softment.playrangers.R;

public class CricketOnGoingFragment extends Fragment {

    private RecyclerView recyclerView;
    private Context context;
    private CricketFragment cricketFragment;
    public CricketOnGoingFragment(Context context, CricketFragment cricketFragment) {
        // Required empty public constructor
        this.context = context;
        this.cricketFragment = cricketFragment;
    }
    private CricketOnGoingAdapter cricketOnGoingAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cricket_on_going, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    public void notifyAdapter() {
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
        f.setTimeZone(TimeZone.getTimeZone("GMT"));


        List<CricketMatchDetailsModel> matches = new ArrayList<>(CricketMatchDetailsModel.cricketMatchDetailsModels);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            matches.removeIf(s -> {
                try {
                    return (((f.parse(s.startTime)).getTime()) >= System.currentTimeMillis());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return true;
                }

            });
        }

        cricketOnGoingAdapter = new CricketOnGoingAdapter(context,matches);
        recyclerView.setAdapter(cricketOnGoingAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        cricketFragment.initializeOnGoingFragment(this);
    }


}