package in.softment.playrangers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import in.softment.playrangers.Services.Service;
import in.softment.playrangers.Utils.Constants;

public class SignUpActivity extends AppCompatActivity {

    private EditText fullName, phone,email, referralCode, password;
    private CheckBox checkBox;
    private TextView terms;
    private FirebaseFirestore db;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String sPhone = "9999999999";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);




        //INITIALIZE FIREBASE DATABASE
        db = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.fullName);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        referralCode = findViewById(R.id.referralCode);
        password = findViewById(R.id.password);
        checkBox = findViewById(R.id.checkbox);
        terms = findViewById(R.id.terms);

        findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.signUpCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sFullName = fullName.getText().toString().trim();
                sPhone = phone.getText().toString();
                String sEmail = email.getText().toString().trim();
                String sReferralCode = referralCode.getText().toString().trim();
                String sPassword = password.getText().toString().trim();

                if (!sFullName.isEmpty()) {
                    if (!sPhone.isEmpty()) {
                        if (!sEmail.isEmpty()) {
                            if (!sPassword.isEmpty()) {


                                if (checkBox.isChecked()) {

                                    ProgressHud.show(SignUpActivity.this, "Creating Account...");
                                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if (task.isSuccessful()) {
                                                uploadData(sFullName, sEmail, sPhone);

                                            } else {
                                                ProgressHud.dialog.dismiss();
                                                Toast toast = Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();
                                            }
                                        }
                                    });

                                } else {
                                    Toast toast = Toast.makeText(SignUpActivity.this, "Accept Terms And Conditions", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            } else {
                                password.setError("Empty");
                                password.requestFocus();
                            }
                        } else {
                            email.setError("Empty");
                            email.requestFocus();
                        }
                    } else {
                        phone.setError("Empty");
                        phone.requestFocus();
                    }
                } else {
                    fullName.setError("Empty");
                    fullName.requestFocus();
                }
            }

        });

    }



    private void uploadData(String fullName, String sEmail, String sPhone) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("name",fullName);
        hashMap.put("mail",sEmail);
        hashMap.put("mobile",sPhone);
        hashMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("time",new Date());



        db.collection(Constants.DbPath.user).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Service.sendOtpToMob(SignUpActivity.this, sPhone);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ProgressHud.dialog.dismiss();
                Toast toast = Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });






    }





}