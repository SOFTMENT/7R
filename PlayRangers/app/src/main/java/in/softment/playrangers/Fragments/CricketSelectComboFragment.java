package in.softment.playrangers.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.protobuf.Any;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.softment.playrangers.Adapter.CricketBattleAdapter;
import in.softment.playrangers.Adapter.CricketComboAdapter;

import in.softment.playrangers.CricketPointCalculation;
import in.softment.playrangers.Interface.WalletHasUpdatedInterface;
import in.softment.playrangers.MainActivity;
import in.softment.playrangers.Model.CombosModel;
import in.softment.playrangers.Model.User;
import in.softment.playrangers.ProgressHud;
import in.softment.playrangers.R;
import in.softment.playrangers.Services.Service;
import in.softment.playrangers.Utils.Constants;


public class CricketSelectComboFragment extends Fragment  {

    private AppCompatButton prizePool, totalInvestor;
    private RecyclerView recyclerView;
    private CricketComboAdapter cricketComboAdapter;
    private Context context;
    protected Animation blink_anim;
    private TextView selectComboText;
    private List<CombosModel> combosModels;
    private CricketShowBattleFragment cricketShowBattleFragment;
    private int tPrize = 0;
    private int tInvestor = 0;
    public CricketSelectComboFragment(Context context, List<CombosModel> combosModels, CricketShowBattleFragment cricketShowBattleFragment) {

        this.context = context;
        this.combosModels = combosModels;
        blink_anim = AnimationUtils.loadAnimation(context, R.anim.blink);
        this.cricketShowBattleFragment = cricketShowBattleFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crciket_select_combo, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cricketComboAdapter = new CricketComboAdapter(context,combosModels,combosModels.get(0).eachComboPlayers, this);
        recyclerView.setAdapter(cricketComboAdapter);
        selectComboText = view.findViewById(R.id.selectComboText);
        selectComboText.startAnimation(blink_anim);

        prizePool = view.findViewById(R.id.prizePool);
        totalInvestor = view.findViewById(R.id.totalInvestor);

         tPrize =  0;
         tInvestor = 0;
        for (CombosModel combosModel : combosModels) {
            tPrize += combosModel.totalPrizePool;
            tInvestor += combosModel.totalInvestors;

        }
        prizePool.setText("PRIZE POOL : "+tPrize);
        totalInvestor.setText("TOTAL INVESTOR : "+tInvestor);

        //PointCalculation
        view.findViewById(R.id.pointCalculation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, CricketPointCalculation.class));
            }
        });

        //HowToPlay
        view.findViewById(R.id.howtoplay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //HOW TO PLAY
                //OK
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        cricketShowBattleFragment.initalizSelectComboFragment(this);
    }

    public void showInvestPopUp(String macthId, String type,String comboId, int comboPrizePool) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = ((MainActivity)context).getLayoutInflater().inflate(R.layout.enter_amt_to_invest_layout,null);

        TextView deposit_coins = view.findViewById(R.id.deposit_coin);
        TextView earned_coins = view.findViewById(R.id.earned_coin);
        EditText coinsEdittext = view.findViewById(R.id.addcoinsedittext);
        ImageView estimatedWinningInfo = view.findViewById(R.id.estimatedWinningImg);
        TextView estimatedWinning = view.findViewById(R.id.estimatedWinning);
        CardView confirm = view.findViewById(R.id.confirm);
        CardView cancel = view.findViewById(R.id.cancel);

        deposit_coins.setText(User.data.getDepositAmount()+"");
        earned_coins.setText(User.data.getWon()+"");

        coinsEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("VIJAY",charSequence.toString());
                try {
                   int estimatedWin = getEstimatedWinning(Integer.parseInt(charSequence.toString()),comboPrizePool,true);
                   estimatedWinning.setText(estimatedWin+"");
                  
                }
                catch (Exception e) {
                    estimatedWinning.setText("0");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        builder.setView(view);

        AlertDialog alertDialog = builder.create();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amt = coinsEdittext.getText().toString().trim();
                if (!amt.isEmpty()) {
                    alertDialog.dismiss();
                    try {
                        int coins = Integer.parseInt(amt);

                        if (tInvestor < combosModels.get(0).totalSpots) {
                            invest(macthId,comboId,type,coins,User.data.name, User.data.uid);
                        }
                        else {
                            Service.showErrorDialog(context,"Oh No!","Sorry! No space left. Please invest on other matches.");
                        }
                       
                    }
                    catch (Exception e) {
                        Service.showErrorDialog(context,"Error",""+e.getMessage());
                    }
                }
                
            }
        });
        
        alertDialog.show();


    }


    public int getEstimatedWinning(int amt , int comboPrizePool, boolean areYouJoining) {
        if (areYouJoining) {
            int eWinning = (int) ((((((((tPrize + amt) / 100.0) * 90.0)) / (comboPrizePool + amt))) * amt));
            return eWinning;
        }
        else {
            int eWinning = (int) ((((((((tPrize) / 100.0) * 90.0)) / (comboPrizePool ))) * amt));
            return eWinning;
        }

    }

    public void invest(String matchid, String comboId, String type, int price, String userName,String uID) {

        new Service().sendMatchDetails(price, 0, new WalletHasUpdatedInterface() {
            @Override
            public void walletHasUpdated(boolean isUpdated, int oldWinning, int oldDeposit, int position) {
                if (isUpdated) {
                    ProgressHud.show(context,"Wait...");
                    Map<String, Object> map = new HashMap<>();
//
                    map.put("username",userName);
                    map.put("uid",uID);
                    map.put("amount",FieldValue.increment(price));

                    DocumentReference df = FirebaseFirestore.getInstance().collection(Constants.DbPath.cricket).document(matchid).collection(type).document(comboId);
                    df.collection(Constants.DbPath.joinedUsers).document(uID).set(map, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ProgressHud.dialog.dismiss();
                            if (task.isSuccessful()) {
                                Service.showErrorDialog(context,"INVESTED","You have successfully invested on this combo.\nInvest more to earn more.");
                                Map<String, Object> map1 = new HashMap<>();
                                map1.put("totalInvestors", FieldValue.increment(1));
                                map1.put("totalPrizePool",FieldValue.increment(price));
                                df.update(map1);
                                Service.addTransaction(User.data.uid,price,"Cricket Mactch Joined","debit","Cricket",Service.generateOrderId(),true);
                            }
                            else {
                                Service.refundAmount(oldDeposit,oldWinning);
                                Service.showErrorDialog(context,"ERROR",task.getException().getLocalizedMessage());
                            }



                        }
                    });
                }
            }
        }, context, 1);



    }

    public void notifyComboAdapter() {
        int tPrize =  0;
        int tInvestor = 0;
        for (CombosModel combosModel : combosModels) {
            tPrize += combosModel.totalPrizePool;
            tInvestor += combosModel.totalInvestors;


        }
        Log.d("VIJAYPRIZE",tPrize+"");
        prizePool.setText("PRIZE POOL : "+tPrize);
        totalInvestor.setText("TOTAL INVESTOR : "+tInvestor);
        cricketComboAdapter.notifyDataSetChanged();
    }


}