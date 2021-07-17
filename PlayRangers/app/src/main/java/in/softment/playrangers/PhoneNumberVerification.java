package in.softment.playrangers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import in.softment.playrangers.Services.Service;
import in.softment.playrangers.Utils.Constants;

public class PhoneNumberVerification extends AppCompatActivity {


        private FirebaseAuth mAuth;
        private FirebaseUser mCurrentUser;
        private String mAuthVerificationId;
        private EditText mOtpText;
        private CardView mVerifyBtn;
        private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
        private TextView resend;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_phone_number_verification);

            mAuth = FirebaseAuth.getInstance();
            mCurrentUser = mAuth.getCurrentUser();

            mAuthVerificationId = getIntent().getStringExtra("AuthCredentials");

            startCountDown();
            resend = findViewById(R.id.resend);

            resend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (resend.getText().toString().equalsIgnoreCase("resend")) {
                        ProgressHud.show(PhoneNumberVerification.this, "Resending...");
                        Service.sendOtpToMob(PhoneNumberVerification.this,getIntent().getStringExtra("phoneNumber"));
                    }

                }
            });

            TextView otpDesc = findViewById(R.id.otp_desc);

            String sOtpDesc = "We have sent you an OTP for phone\nnumber verification on "+getIntent().getStringExtra("phoneNumber");
            otpDesc.setText(sOtpDesc);
            mOtpText = findViewById(R.id.otp_text_view);

            mVerifyBtn = findViewById(R.id.verify_btn);

            mVerifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String otp = mOtpText.getText().toString();

                    if(otp.isEmpty()){
                        mOtpText.setError("Empty");

                    } else {

                        ProgressHud.show(PhoneNumberVerification.this, "Verifying...");


                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthVerificationId, otp);
                        signInWithPhoneAuthCredential(credential);
                    }

                }
            });

            //MobileNumberChange
            findViewById(R.id.clickhere).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder mobChangeBuilder = new AlertDialog.Builder(PhoneNumberVerification.this);
                    AlertDialog mobChangeAlert = mobChangeBuilder.create();
                    View view1 = getLayoutInflater().inflate(R.layout.mobilenumberchangelayout,null);
                    CardView updateNumber = view1.findViewById(R.id.updateNumber);
                    EditText mobNumberEditText = view1.findViewById(R.id.phone);
                   mobNumberEditText.setText(getIntent().getStringExtra("phoneNumber"));
                    updateNumber.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String sNewPhone = mobNumberEditText.getText().toString();

                            if(!sNewPhone.isEmpty()) {

                                if (!sNewPhone.equalsIgnoreCase(getIntent().getStringExtra("phoneNumber"))) {
                                    mobChangeAlert.hide();
                                    ProgressHud.show(PhoneNumberVerification.this,"Updating...");
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("mobile", sNewPhone);
                                    FirebaseFirestore.getInstance().collection(Constants.DbPath.user).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Service.sendOtpToMob(PhoneNumberVerification.this, sNewPhone);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            ProgressHud.dialog.dismiss();
                                            Toast toast =  Toast.makeText(PhoneNumberVerification.this, e.getMessage(), Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER, 0,0);
                                            toast.show();
                                        }
                                    });
                                }
                            }
                            else {
                                mobNumberEditText.setError("Empty");
                                mobNumberEditText.requestFocus();
                            }
                        }
                    });
                    mobChangeAlert.setView(view1);
                    mobChangeAlert.show();
                }
            });

        }

        private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
            if (mAuth.getCurrentUser() != null) {
                mAuth.getCurrentUser().linkWithCredential(credential)
                        .addOnCompleteListener(PhoneNumberVerification.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    sendUserToHome();

                                } else {
                                    ProgressHud.dialog.dismiss();
                                    // The verification code entered was invalid
                                    Toast toast =  Toast.makeText(PhoneNumberVerification.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0,0);
                                    toast.show();
                                }

                            }
                        });
            }
            else {

                Toast toast =  Toast.makeText(PhoneNumberVerification.this, "Please clear Data of app", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0,0);
                toast.show();
                finish();
            }

        }




        public void sendUserToHome() {
            Map<String, Object> data = new HashMap<>();
            data.put("isMobileVerified", true);
            FirebaseFirestore.getInstance().collection(Constants.DbPath.user).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent intent = new Intent(PhoneNumberVerification.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ProgressHud.dialog.dismiss();
                    Toast toast =  Toast.makeText(PhoneNumberVerification.this, e.getMessage(), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0,0);
                    toast.show();
                }
            });

        }

        public void startCountDown() {
            new CountDownTimer(10000, 1000) {

                public void onTick(long millisUntilFinished) {
                    resend.setText("Resend Code After "+ millisUntilFinished / 1000+" Seconds");
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    resend.setText("Resend");
                }

            }.start();
        }
    }
