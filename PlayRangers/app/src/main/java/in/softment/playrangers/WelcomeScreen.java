package in.softment.playrangers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import in.softment.playrangers.Model.Token_Res;
import in.softment.playrangers.Model.User;
import in.softment.playrangers.Services.Service;
import in.softment.playrangers.Services.ServiceWrapper;
import in.softment.playrangers.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeScreen extends AppCompatActivity  {





    private  SharedPreferences sharedpreferences;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String sPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();






        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        boolean isVerified = sharedpreferences.getBoolean("isVerified",false);
        if (isVerified) {


            Intent intent = new Intent(WelcomeScreen.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        else if (mAuth.getCurrentUser() != null) {
            db.collection(Constants.DbPath.user).document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);

                        if (user != null) {
                            if (user.isMobileVerified()) {
                                Intent intent = new Intent(WelcomeScreen.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else {
                                sPhone = user.getMobile();
                                Service.sendOtpToMob(WelcomeScreen.this, sPhone);
                            }

                        }
                        else  {
                            Toast toast =  Toast.makeText(WelcomeScreen.this, "ErrorCode 51, Please contact Us.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0,0);
                            toast.show();
                        }
                    }
                    else {
                        Toast toast =  Toast.makeText(WelcomeScreen.this, "Your record has been deleted.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0,0);
                        toast.show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                   Toast toast =  Toast.makeText(WelcomeScreen.this, e.getMessage(), Toast.LENGTH_LONG);
                   toast.setGravity(Gravity.CENTER, 0,0);
                   toast.show();

                }
            });

        }
        else {
            new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeScreen.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        },2000);
        }
        }


}