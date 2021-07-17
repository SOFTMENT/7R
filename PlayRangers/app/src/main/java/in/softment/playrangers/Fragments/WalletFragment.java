package in.softment.playrangers.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.firestore.FieldValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import in.softment.playrangers.MainActivity;
import in.softment.playrangers.Model.Token_Res;
import in.softment.playrangers.Model.User;
import in.softment.playrangers.ProgressHud;
import in.softment.playrangers.R;
import in.softment.playrangers.Services.Service;
import in.softment.playrangers.Services.ServiceWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WalletFragment extends Fragment  {

    private String midString = "GqkvzC02497137104593";
    private Context context;
    private TextView depositCoins,earnedCoins;
    private LottieAnimationView lottieAnimationView;

    public WalletFragment(Context context) {
        this.context = context;
    }
    private AppCompatButton all, debit, credit;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_wallet, container, false);
        lottieAnimationView = view.findViewById(R.id.animation_view);


        ViewPager viewPager = view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);


        //Deposite and Earned Coins
        depositCoins = view.findViewById(R.id.deposit_coin);
        earnedCoins = view.findViewById(R.id.earned_coin);


        //ADDCOIN BUTTOB
        LinearLayout addCoint = view.findViewById(R.id.addcoin);
        addCoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.myalerttheme);
                View view1 = getLayoutInflater().inflate(R.layout.buy_coins_layout, null);
                AlertDialog alertDialog = builder.create();
                alertDialog.setView(view1);
                EditText addcoinET = view1.findViewById(R.id.addcoinsedittext);
                TextView message = view1.findViewById(R.id.message);
                CardView proceed = view1.findViewById(R.id.proceed);
                CardView cancel = view1.findViewById(R.id.cancel);
                TextView proceedtext = view1.findViewById(R.id.proceedText);
                addcoinET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        int coin = 0;
                        if (!String.valueOf(charSequence).isEmpty()) {
                            coin =  Integer.parseInt(String.valueOf(charSequence));
                            proceedtext.setText("Proceed To Pay ₹"+coin);
                            message.setVisibility(View.VISIBLE);
                            message.setText(coin+" Coins x ₹1 = ₹"+coin);
                        }
                        else {
                            message.setVisibility(View.GONE);
                            proceedtext.setText("Proceed To Pay");
                        }


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });



                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                proceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String amt = addcoinET.getText().toString().trim();
                        if (!amt.isEmpty()) {
                            int amtInt = Integer.parseInt(amt);
                            if (amtInt >= 10) {
                                alertDialog.dismiss();
                                getToken(amt);
                            }
                            else {
                                Toast toast = Toast.makeText(context, "Please Buy minimum 10 Coins", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }
                        }
                        else {
                            addcoinET.setError("Empty");
                            addcoinET.requestFocus();
                        }
                    }
                });

                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });
        all = view.findViewById(R.id.all);
        debit = view.findViewById(R.id.debit);
        credit = view.findViewById(R.id.credit);

        all.setTag("selected");
        debit.setTag("unselected");
        credit.setTag("unselected");

        viewPager.setCurrentItem(0);

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (all.getTag().toString().equals("unselected")) {
                    all.setTag("selected");
                    debit.setTag("unselected");
                    credit.setTag("unselected");
                    all.setBackgroundResource(R.drawable.btnbackred);
                    debit.setBackgroundResource(R.drawable.btnback);
                    credit.setBackgroundResource(R.drawable.btnback);
                    viewPager.setCurrentItem(0);
                }

            }
        });

        debit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (debit.getTag().toString().equals("unselected")) {
                    all.setTag("unselected");
                    debit.setTag("selected");
                    credit.setTag("unselected");
                    all.setBackgroundResource(R.drawable.btnback);
                    debit.setBackgroundResource(R.drawable.btnbackred);
                    credit.setBackgroundResource(R.drawable.btnback);
                    viewPager.setCurrentItem(1);
                }

            }
        });

        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (credit.getTag().toString().equals("unselected")) {
                    all.setTag("unselected");
                    debit.setTag("unselected");
                    credit.setTag("selected");
                    all.setBackgroundResource(R.drawable.btnback);
                    debit.setBackgroundResource(R.drawable.btnback);
                    credit.setBackgroundResource(R.drawable.btnbackred);
                    viewPager.setCurrentItem(2);
                }

            }
        });



        return view;
    }

    public void updateWalletBalance() {
        depositCoins.setText(String.valueOf(User.data.depositAmount));
        earnedCoins.setText(String.valueOf(User.data.winningAmount));
    }

    public void getToken(String amt) {
        ProgressHud.show(context, "Wait...");
        String orderIdString = Service.generateOrderId();
        ServiceWrapper serviceWrapper = new ServiceWrapper(null);
        Call<Token_Res> call = serviceWrapper.getTokenCall( midString, orderIdString, amt);
        call.enqueue(new Callback<Token_Res>() {
            @Override
            public void onResponse(Call<Token_Res> call, Response<Token_Res> response) {
                try {
                    if (response.isSuccessful() && response.body()!=null){

                        if (!response.body().getBody().getTxnToken().equals("")) {
                            ProgressHud.dialog.dismiss();
                            ((MainActivity)context).startPaytmPayment(response.body().getBody().getTxnToken(),midString,orderIdString, amt);

                        }else {

                        }
                    }
                }catch (Exception e){

                }
            }
            @Override
            public void onFailure(Call<Token_Res> call, Throwable t) {


            }
        });
    }
    private void setupViewPager(ViewPager viewPager) {
        FreeFireFragment.ViewPagerAdapter adapter = new FreeFireFragment.ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new Transaction_All_Fragment(context));
        adapter.addFrag(new Transaction_Debit_Fragment(context));
        adapter.addFrag(new Transaction_Credit_Fragment(context));
        viewPager.setAdapter(adapter);
    }






    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        final List<Fragment> mFragmentList = new ArrayList<>();

        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {

            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);

        }


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MainActivity)context).initializeWalletFragment(this);
    }


    public void startAnimation() {
        lottieAnimationView.setVisibility(View.VISIBLE);
    }

    public void stopAnimation() {
        lottieAnimationView.setVisibility(View.GONE);
    }
}