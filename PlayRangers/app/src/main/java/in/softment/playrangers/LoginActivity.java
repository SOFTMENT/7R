package in.softment.playrangers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import in.softment.playrangers.Model.User;
import in.softment.playrangers.Services.Service;
import in.softment.playrangers.Utils.Constants;


public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private FirebaseAuth firebaseAuth;
    private ProgressHud progressHud;
    private static final int STORAGE_PERMISSION_CODE = 4655;
    private String sPhone;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NewCode.assistActivity(this);
        onWindowFocusChanged(true);
        firebaseAuth = FirebaseAuth.getInstance();

       // requestStoragePermission();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        //FORGET PASSWORD

        findViewById(R.id.forget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = email.getText().toString().trim();
                if (!sEmail.isEmpty()) {
                    ProgressHud.show(LoginActivity.this,"Wait...");
                    FirebaseAuth.getInstance().sendPasswordResetEmail(sEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ProgressHud.dialog.dismiss();
                            Toast toast = Toast.makeText(LoginActivity.this, "Password Reset Link Has Been Sent On Your Email Address.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ProgressHud.dialog.dismiss();
                            Toast toast = Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    });
                }
                else {
                    email.setError("Empty");
                    email.requestFocus();
                }

            }
        });

        findViewById(R.id.signInCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = email.getText().toString().trim();
                String sPassword = password.getText().toString().trim();

                if (!sEmail.isEmpty()) {
                    if (!sPassword.isEmpty()) {
                        ProgressHud.show(LoginActivity.this,"Sign In...");
                        firebaseAuth.signInWithEmailAndPassword(sEmail,sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseFirestore.getInstance().collection(Constants.DbPath.user).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                                User user = documentSnapshot.toObject(User.class);

                                                if (user != null) {
                                                    if (user.isMobileVerified()) {
                                                        ProgressHud.dialog.dismiss();
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                    }
                                                    else {
                                                        sPhone = user.getMobile();
                                                        Service.sendOtpToMob(LoginActivity.this, sPhone);
                                                        
                                                    }

                                                }
                                                else  {
                                                    ProgressHud.dialog.dismiss();
                                                    Toast toast =  Toast.makeText(LoginActivity.this, "ErrorCode 51, Please contact Us.", Toast.LENGTH_LONG);
                                                    toast.setGravity(Gravity.CENTER, 0,0);
                                                    toast.show();
                                                }
                                            }
                                            else {
                                                Toast toast =  Toast.makeText(LoginActivity.this, "Your record has been deleted.", Toast.LENGTH_LONG);
                                                toast.setGravity(Gravity.CENTER, 0,0);
                                                toast.show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast toast =  Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER, 0,0);
                                            toast.show();

                                        }
                                    });


                                }
                                else {
                                    ProgressHud.dialog.dismiss();
                                    Toast toast = Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            }
                        });
                    }
                    else {
                        password.requestFocus();
                        password.setError("Empty");
                    }
                }
                else{
                    email.requestFocus();
                    email.setError("Empty");
                }

            }
        });

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }


    private void requestStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

}
