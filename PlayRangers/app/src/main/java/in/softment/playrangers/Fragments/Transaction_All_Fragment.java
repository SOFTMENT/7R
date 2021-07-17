package in.softment.playrangers.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import in.softment.playrangers.Adapter.FreeFireUpComingAdapter;
import in.softment.playrangers.Adapter.TransactionAdapter;
import in.softment.playrangers.MainActivity;
import in.softment.playrangers.Model.TransactionModel;
import in.softment.playrangers.R;


public class Transaction_All_Fragment extends Fragment {

    private TransactionAdapter transactionAdapter;

    private final Context context;
    public Transaction_All_Fragment(Context context) {
        this.context = context;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_transaction__all, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionAdapter  = new TransactionAdapter(context, TransactionModel.transactionModels);
        recyclerView.setAdapter(transactionAdapter);
        return view;
    }

    public void reloadTransactionAdapter() {
        transactionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MainActivity)context).initializeTransactionAllFragment(this);
    }
}