package in.softment.playrangers.Services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import in.softment.playrangers.MainActivity;
import in.softment.playrangers.Model.User;
import in.softment.playrangers.PhoneNumberVerification;
import in.softment.playrangers.ProgressHud;
import in.softment.playrangers.R;
import in.softment.playrangers.Utils.Constants;
import in.softment.playrangers.Interface.WalletHasUpdatedInterface;
import in.softment.playrangers.WelcomeScreen;

public class Service {

    public static void sendMatchDetails(final int entryFee,  int adapterPosition, WalletHasUpdatedInterface walletHasUpdatedInterface, Context context, int count) {

        ProgressHud.show(context,"Wait...");
        final DocumentReference sfDocRef = FirebaseFirestore.getInstance().collection(Constants.DbPath.user).document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> apply(Transaction transaction) throws FirebaseFirestoreException {

                DocumentSnapshot snapshot = transaction.get(sfDocRef);

                int oldWinningAmont = 0;
                int oldDepositAmount = 0;

                if (snapshot.contains("winningAmount")) {
                    oldWinningAmont = snapshot.get("winningAmount",Integer.class);

                }
                if (snapshot.contains("depositAmount")){
                    oldDepositAmount = snapshot.get("depositAmount",Integer.class);
                }

                Map<String, Integer> map = new HashMap<>();
                map.put("winningAmount",oldWinningAmont);
                map.put("depositAmount",oldDepositAmount);

                return map;
            }
        }).addOnSuccessListener(new OnSuccessListener<Map<String, Integer>>() {
            @Override
            public void onSuccess(Map<String, Integer> map) {
                int oldWinningAmont = 0;
                int oldDepositAmount = 0;
                int newWinningAmount = 0;
                int newDepositAmount = 0;
                oldWinningAmont = map.get("winningAmount");
                oldDepositAmount = map.get("depositAmount");

                if (entryFee * count <= oldDepositAmount) {
                    newDepositAmount = oldDepositAmount - (entryFee * count);
                    updateWallet(context,newDepositAmount, oldWinningAmont,oldWinningAmont,oldDepositAmount, adapterPosition,  walletHasUpdatedInterface);
                }
                else if (entryFee * count <= (oldDepositAmount + oldWinningAmont)) {

                    newWinningAmount = oldWinningAmont - (entryFee * count) + oldDepositAmount;
                    newDepositAmount = 0;
                    updateWallet(context,newDepositAmount, newWinningAmount, oldWinningAmont, oldDepositAmount, adapterPosition, walletHasUpdatedInterface);

                }
                else {
                    ProgressHud.dialog.dismiss();
                    Service.showErrorDialog(context,"Insufficient Coin","Please add coin into your wallet and try again.");
                    walletHasUpdatedInterface.walletHasUpdated(false,0,0,0);





                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TransactionFailed",e.getMessage());
                        ProgressHud.dialog.dismiss();
                        walletHasUpdatedInterface.walletHasUpdated(false,0,0,0);

                    }
                });
    }



    private static void updateWallet(Context context,int depositAmount, int winningAmount, int oldWinning, int oldDeposit, int position, WalletHasUpdatedInterface walletHasUpdatedInterface) {


        Map<String, Object> map = new HashMap<>();
        map.put("depositAmount", depositAmount);
        map.put("winningAmount",winningAmount);
        map.put("matchesPlayed", FieldValue.increment(1));
        FirebaseFirestore.getInstance().collection(Constants.DbPath.user).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                ProgressHud.dialog.dismiss();
                if (task.isSuccessful()) {
                    walletHasUpdatedInterface.walletHasUpdated(true,oldWinning,oldDeposit,position);


                }
                else {
                    walletHasUpdatedInterface.walletHasUpdated(false,oldWinning,oldDeposit,position);
                    Toast toast = Toast.makeText(context, "Error - "+task.getException().getMessage(), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        });

    }

    public static void sendOtpToMob(Context context, String sPhone) {

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Map<String, Object> data = new HashMap<>();
                data.put("isMobileVerified", true);
                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        try {
                            ProgressHud.dialog.dismiss();
                        }
                        catch (Exception ignored) {

                        }
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        try {
                            ProgressHud.dialog.dismiss();
                        }
                        catch (Exception ignored) {

                        }
                        Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                try {
                    ProgressHud.dialog.dismiss();
                }
                catch (Exception ignored) {

                }
                Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

            @Override
            public void onCodeSent(final String s, @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                try {
                    ProgressHud.dialog.dismiss();
                }
                catch (Exception ignored) {

                }
                Intent otpIntent = new Intent(context, PhoneNumberVerification.class);
                otpIntent.putExtra("AuthCredentials", s);
                otpIntent.putExtra("phoneNumber", sPhone);
                otpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(otpIntent);


            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber("+91"+sPhone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity((Activity) context)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    public static String generateOrderId() {
        return "7RINDIA"+ System.currentTimeMillis();
    }



    public static void refundAmount(int oldDeposit,int oldWinning) {
        Map<String, Object> map = new HashMap<>();
        map.put("depositAmount", oldDeposit);
        map.put("winningAmount",oldWinning);
        map.put("matchesPlayed", FieldValue.increment(-1));
        FirebaseFirestore.getInstance().collection(Constants.DbPath.user).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(map);
    }

    public static void addTransaction(String uid, int amt, String title, String type, String reason, String orderId, boolean isSuccessful) {

        DocumentReference documentReference =  FirebaseFirestore.getInstance().collection(Constants.DbPath.user).document(uid).collection(Constants.DbPath.transaction).document();
        String pushKey = documentReference.getId();
        Map<String, Object> map = new HashMap();
        map.put("tId",pushKey);
        map.put("uid",uid);
        map.put("title",title);
        map.put("date",FieldValue.serverTimestamp());
        map.put("amount", amt);
        map.put("type",type);
        map.put("reason",reason);
        map.put("orderId",orderId);
        map.put("isSuccessful",isSuccessful);
        documentReference.set(map);



    }


    public static void depositAmount(Context context,String uid, int amt, String title, String type, String reason, String orderId, boolean isSuccessful) {

        if (isSuccessful) {

            FirebaseFirestore.getInstance().collection(Constants.DbPath.user).document(uid).update("depositAmount", FieldValue.increment(amt)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    addTransaction(uid, amt,title, type, reason, orderId, isSuccessful);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Map<String, Object> map = new HashMap();
                    map.put("mobilenumber", User.data.getMobile());
                    map.put("amount", amt);
                    map.put("email",User.data.getMail());
                    FirebaseFirestore.getInstance().collection("Refund").document().set(map);

                    Service.showErrorDialog(context,"Error","Failed to deposit money in wallet, But don't worry we have received your request and we will update your wallet manually within 1 hour");

                }
            });
        }
        else {
            addTransaction(uid, amt,title, type, reason, orderId, isSuccessful);
        }
    }



    public static void showErrorDialog(Context context,String title,String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        Activity activity = (Activity) context;
        View view = activity.getLayoutInflater().inflate(R.layout.error_message_layout, null);
        TextView titleView = view.findViewById(R.id.title);
        TextView msg = view.findViewById(R.id.message);
        titleView.setText(title);
        msg.setText(message);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public static void logout(Context context) {
        FirebaseAuth.getInstance().signOut();
        context.getSharedPreferences("MyPref", Context.MODE_PRIVATE).edit().putBoolean("isVerified", false).apply();
        Intent intent = new Intent(context, WelcomeScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }


    public static  String convertDate(Date date) {
        date.setTime(date.getTime());
        String pattern = "dd-MMM-yyyy, hh:mm aa";
        DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return  df.format(date);
    }


}
