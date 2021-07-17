package in.softment.playrangers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firestore.v1.DocumentTransform;
import com.google.gson.Gson;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.tenclouds.fluidbottomnavigation.FluidBottomNavigation;
import com.tenclouds.fluidbottomnavigation.FluidBottomNavigationItem;
import com.tenclouds.fluidbottomnavigation.listener.OnTabSelectedListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.Callable;

import in.softment.playrangers.API.CricketApi;
import in.softment.playrangers.Adapter.MyHeaderPagerAdapter;
import in.softment.playrangers.Fragments.CricketFragment;
import in.softment.playrangers.Fragments.DiscoverFragment;
import in.softment.playrangers.Fragments.FreeFireFragment;
import in.softment.playrangers.Fragments.FreeFireUpComingFragment;
import in.softment.playrangers.Fragments.GamesFragment;
import in.softment.playrangers.Fragments.HomeFragment;
import in.softment.playrangers.Fragments.LeaderboardFragment;
import in.softment.playrangers.Fragments.Transaction_All_Fragment;
import in.softment.playrangers.Fragments.Transaction_Credit_Fragment;
import in.softment.playrangers.Fragments.Transaction_Debit_Fragment;
import in.softment.playrangers.Fragments.WalletFragment;
import in.softment.playrangers.Model.FreeFireGameModel;
import in.softment.playrangers.Model.Poll;
import in.softment.playrangers.Model.Token_Res;
import in.softment.playrangers.Model.TransactionModel;
import in.softment.playrangers.Model.User;
import in.softment.playrangers.Services.Service;
import in.softment.playrangers.Services.ServiceWrapper;
import in.softment.playrangers.Utils.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static in.softment.playrangers.Model.TransactionModel.transactionModels;
import static in.softment.playrangers.Model.TransactionModel.transactionModelsDebit;
import static in.softment.playrangers.Model.TransactionModel.transactionModelsCredit;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;
    private ViewPager viewPager;
    FluidBottomNavigation fluidBottomNavigation;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private HomeFragment homeFragment;
    private Transaction_All_Fragment transaction_all_fragment;
    private Transaction_Debit_Fragment transaction_debit_fragment;
    private Transaction_Credit_Fragment transaction_credit_fragment;
    private FreeFireUpComingFragment freeFireUpComingFragment;
    private WalletFragment walletFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        boolean isVerified = sharedpreferences.getBoolean("isVerified",false);
        if (!isVerified) {
            sharedpreferences.edit().putBoolean("isVerified", true).apply();
        }





        //TEMP
       // createFreeFireEvent("Free Fire Duo Match - #1",FieldValue.serverTimestamp(),100,5,10,"Duo","Tpp","Erangle",48,false);



        //getUserData
        getUserData();





        //viewpager
        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        //BottomNavigationView
        List<FluidBottomNavigationItem> fluidBottomNavigationItems = new ArrayList<>();
        fluidBottomNavigationItems.add(new FluidBottomNavigationItem(
                getString(R.string.Home),
                ContextCompat.getDrawable(this, R.drawable.icons8_home_512)));
        fluidBottomNavigationItems.add(new FluidBottomNavigationItem(
                getString(R.string.Discover),
                ContextCompat.getDrawable(this, R.drawable.compass)));
        fluidBottomNavigationItems.add(new FluidBottomNavigationItem(
                getString(R.string.Play),
                ContextCompat.getDrawable(this, R.drawable.vd)));
        fluidBottomNavigationItems.add(new FluidBottomNavigationItem(
                getString(R.string.LiveWinning),
                ContextCompat.getDrawable(this, R.drawable.podium)));
        fluidBottomNavigationItems.add(new FluidBottomNavigationItem(
                getString(R.string.Wallet),
                ContextCompat.getDrawable(this, R.drawable.wallet)));

        fluidBottomNavigation = findViewById(R.id.fluidBottomNavigation);
        fluidBottomNavigation.setItems(fluidBottomNavigationItems);



        viewPager.setOffscreenPageLimit(5);


        fluidBottomNavigation.setOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                viewPager.setCurrentItem(position);
            }
        });

        fluidBottomNavigation.selectTab(2);
        viewPager.setCurrentItem(2);
    }




    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HomeFragment(this));
        adapter.addFrag(new DiscoverFragment(this));
        adapter.addFrag(new GamesFragment(this));
        adapter.addFrag(new LeaderboardFragment(this));
        adapter.addFrag(new WalletFragment(this));



        viewPager.setAdapter(adapter);
    }

    public void switchToPlayPage() {
        fluidBottomNavigation.selectTab(2);
        viewPager.setCurrentItem(2);
    }



    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

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

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    //GetFreeFireEventDetails
    public void getFreeFireEventDetails() {
        FirebaseFirestore.getInstance().collection(Constants.DbPath.freeFireEvent).addSnapshotListener(MetadataChanges.INCLUDE,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error == null) {
                    if (value!=null && !value.getMetadata().hasPendingWrites()) {
                        FreeFireGameModel.freeFireGameModels.clear();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            FreeFireGameModel freeFireGameModel = documentSnapshot.toObject(FreeFireGameModel.class);
                            FreeFireGameModel.freeFireGameModels.add(freeFireGameModel);
                        }
                        freeFireUpComingFragment.notifyFreeFreeUpcomongAdapter();

                    }
                }
                else {
                    Toast toast =  Toast.makeText(MainActivity.this, "Something went wrong! "+Constants.MyError.error_in_freefire_data_load, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        });
    }

    public void getTransactionAllData() {
        if (mAuth.getCurrentUser() != null) {
            db.collection(Constants.DbPath.user).document(mAuth.getCurrentUser().getUid()).collection(Constants.DbPath.transaction).limitToLast(20).orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null) {
                        if (value != null && !value.getMetadata().hasPendingWrites()) {
                            transactionModels.clear();
                            for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                               transactionModels.add(documentSnapshot.toObject(TransactionModel.class));
                            }
                            transaction_all_fragment.reloadTransactionAdapter();


                        }
                    }
                }
            });
        }
        else {
            Service.logout(MainActivity.this);
        }

    }

    public void getTransactionDebitData() {
        if (mAuth.getCurrentUser() != null) {
            db.collection(Constants.DbPath.user).document(mAuth.getCurrentUser().getUid()).collection(Constants.DbPath.transaction).limitToLast(20).orderBy("date", Query.Direction.DESCENDING).whereEqualTo("type","debit").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null) {
                        if (value != null && !value.getMetadata().hasPendingWrites()) {
                            transactionModelsDebit.clear();
                            for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                                transactionModelsDebit.add(documentSnapshot.toObject(TransactionModel.class));
                            }
                            transaction_debit_fragment.reloadTransactionAdapter();
                        }
                    }
                }
            });
        }
        else {
            Service.logout(MainActivity.this);
        }

    }


    private void getTransactionCreditData() {

        if (mAuth.getCurrentUser() != null) {
            db.collection(Constants.DbPath.user).document(mAuth.getCurrentUser().getUid()).collection(Constants.DbPath.transaction).limitToLast(20).orderBy("date", Query.Direction.DESCENDING).whereEqualTo("type","credit").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null) {
                        if (value != null && !value.getMetadata().hasPendingWrites()) {
                            transactionModelsCredit.clear();
                            for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                                transactionModelsCredit.add(documentSnapshot.toObject(TransactionModel.class));
                            }
                            transaction_credit_fragment.reloadTransactionAdapter();
                        }
                    }
                }
            });
        }
        else {
            Service.logout(MainActivity.this);
        }

    }

    public void getPollData() {
        db.collection(Constants.DbPath.poll).orderBy("date").limitToLast(1).addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                    if (error == null) {
                        if (value != null && value.getDocuments().size() > 0) {

                            if (!value.getMetadata().hasPendingWrites()) {

                                value.getDocuments().get(0).toObject(Poll.class);
                                homeFragment.setPoll();
                            }
                        } else {
                            Poll.data = null;
                            homeFragment.setPoll();
                        }
                    } else {
                        Poll.data = null;
                        homeFragment.setPoll();
                        MyAlertDialog.show(MainActivity.this, "Poll Error", error.getMessage(), new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                return null;
                            }
                        });
                    }

            }
        });
    }

    public void getUserData() {


        ProgressHud.show(this, "Loading...");
        if (mAuth.getCurrentUser() != null) {
            db.collection(Constants.DbPath.user).document(mAuth.getCurrentUser().getUid()).addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                    if (value != null && value.exists()) {
                        if (!value.getMetadata().hasPendingWrites()) {
                            ProgressHud.dialog.dismiss();
                            value.toObject(User.class);
                            homeFragment.setHomeFragment();

                            //getPollData
                            getPollData();

                            //UpdateWalletBalance
                            walletFragment.updateWalletBalance();

                        }

                    }
                    else {
                        ProgressHud.dialog.dismiss();
                        MyAlertDialog.show(MainActivity.this,"Record Not Found","Your Record has been deleted or Not Loading. Please Contact Us.",new Callable<Void>() {
                                    public Void call() {
                                        Service.logout(MainActivity.this);
                                        return null;
                                    }
                                }
                        );

                    }
                }
            });
        }
        else {
            //LogOut
            Service.logout(MainActivity.this);
        }


    }




    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 2) {
            viewPager.setCurrentItem(2);
            fluidBottomNavigation.selectTab(2);
        }
        else {
            super.onBackPressed();
        }
    }


    public void startPaytmPayment (String token, String midString, String orderIdString, String amt){

        // for test mode use it
        // String host = "https://securegw-stage.paytm.in/";
        // for production mode use it

        //Log.e(TAG, "order details "+ orderDetails);
        String callBackUrl = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+orderIdString;


        PaytmOrder paytmOrder = new PaytmOrder(orderIdString, midString, token, amt, callBackUrl);
        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback(){
            int amt = 0;
            @Override
            public void onTransactionResponse(Bundle bundle) {

                if (bundle != null) {

                    try {

                        amt = (int) Double.parseDouble(bundle.getString("TXNAMOUNT"));


                    }
                    catch (Exception ignored) {
                        Toast.makeText(MainActivity.this, Constants.MyError.numberFormatError, Toast.LENGTH_LONG).show();
                    }

                    if (bundle.getString("STATUS").equalsIgnoreCase("TXN_SUCCESS")) {


                        if (amt > 0) {
                            if (mAuth.getCurrentUser() != null) {
                                Service.depositAmount(MainActivity.this, mAuth.getCurrentUser().getUid(),amt,"Coins Added","credit","Deposit",bundle.getString("ORDERID"),true);
                            }
                            else {
                                Service.logout(MainActivity.this);
                            }

                        }

                        walletFragment.startAnimation();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                walletFragment.stopAnimation();
                            }
                        },3000);


                    }
                    else {
                        Service.depositAmount(MainActivity.this,mAuth.getCurrentUser().getUid(),amt,"Coins Added","credit","Deposit",bundle.getString("ORDERID"),false);
                       Toast toast =  Toast.makeText(MainActivity.this, "Transaction Failed", Toast.LENGTH_LONG);
                       toast.setGravity(Gravity.CENTER,0,0);
                       toast.show();
                    }
                }
            }
            @Override
            public void networkNotAvailable() {
                //    Log.e(TAG, "network not available ");
            }
            @Override
            public void onErrorProceed(String s) {
                //     Log.e(TAG, " onErrorProcess "+s.toString());
            }
            @Override
            public void clientAuthenticationFailed(String s) {
                //   Log.e(TAG, "Clientauth "+s);
            }
            @Override
            public void someUIErrorOccurred(String s) {

            }
            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {

            }
            @Override
            public void onBackPressedCancelTransaction() {


            }
            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                Service.depositAmount(MainActivity.this,mAuth.getCurrentUser().getUid(),amt,"Coins Added","credit","Deposit",bundle.getString("ORDERID"),false);
                Toast.makeText(MainActivity.this, "Transaction Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        transactionManager.setAppInvokeEnabled(false);
        transactionManager.setEnableAssist(true);
        //transactionManager.setShowPaymentUrl("https://securegw.paytm.in/theia/api/v1/showPaymentPage");
        transactionManager.startTransaction(this, 10);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {

                }
            }

        }else{
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }





    public void initializeFreeFireUpcomingFragment(FreeFireUpComingFragment freeFireUpComingFragment) {
        this.freeFireUpComingFragment =  freeFireUpComingFragment;
    }

    public void initializeTransactionAllFragment(Transaction_All_Fragment transaction_all_fragment) {
        this.transaction_all_fragment = transaction_all_fragment;
        getTransactionAllData();
    }
    public void initializeTransactionDebitFragment(Transaction_Debit_Fragment transaction_debit_fragment) {
        this.transaction_debit_fragment= transaction_debit_fragment;
        getTransactionDebitData();

    }

    public void initializeTransactionCreditFragment(Transaction_Credit_Fragment transaction_credit_fragment) {
        this.transaction_credit_fragment= transaction_credit_fragment;
        getTransactionCreditData();
    }

    public void initializeWalletFragment(WalletFragment walletFragment) {
        this.walletFragment = walletFragment;

    }

    public void initializeHomeFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

//    public void createFreeFireEvent(String title, FieldValue timestamp, int winPrize, int perkill, int entryfee, String type, String version,
//                                    String map, int totalSpots, boolean isExpired) {
//
//        DocumentReference documentReference =  db.collection(Constants.DbPath.freeFireEvent).document();
//        String gameId = documentReference.getId();
//        Map<String, Object> freeFireMap = new HashMap<>();
//        freeFireMap.put("gameID", gameId);
//        freeFireMap.put("title",title);
//        freeFireMap.put("date",timestamp);
//        freeFireMap.put("winprize",winPrize);
//        freeFireMap.put("perKill",perkill);
//        freeFireMap.put("entryFee",entryfee);
//        freeFireMap.put("type",type);
//        freeFireMap.put("version",version);
//        freeFireMap.put("map",map);
//        freeFireMap.put("totalSpots",totalSpots);
//        freeFireMap.put("isExpired",isExpired);
//
//        documentReference.set(freeFireMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
//                }
//                else {
//
//                }
//            }
//        });
//        Map<String, Object> map1 = new HashMap<>();
//        ArrayList<String> strings = new ArrayList<>();
//        strings.add("Budweiser");
//        strings.add("MorTal");
//
//        map1.put("joinedUsers",strings);
//        db.collection(Constants.DbPath.freeFireEvent).document("wNRhcbCbAxPFTeLQLykm").update(map1);
//
//    }


}
