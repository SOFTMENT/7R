package in.softment.playrangers.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import in.softment.playrangers.API.CricketApi;
import in.softment.playrangers.Adapter.FreeFireUpComingAdapter;
import in.softment.playrangers.MainActivity;
import in.softment.playrangers.Model.FreeFireGameModel;
import in.softment.playrangers.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class FreeFireUpComingFragment extends Fragment {
    private RecyclerView recyclerView;
    private FreeFireUpComingAdapter upComingAdapter;

    private Context context;
    public FreeFireUpComingFragment(Context context) {
        this.context = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_free_fire_up_coming, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        upComingAdapter  = new FreeFireUpComingAdapter(context,FreeFireGameModel.freeFireGameModels);
        recyclerView.setAdapter(upComingAdapter);
        return view;
    }






    public void notifyFreeFreeUpcomongAdapter() {
        upComingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MainActivity)context).initializeFreeFireUpcomingFragment(this);
    }


}