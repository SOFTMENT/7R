package in.softment.playrangers.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.softment.playrangers.MainActivity;
import in.softment.playrangers.Model.FreeFireGameModel;
import in.softment.playrangers.Model.User;
import in.softment.playrangers.ProgressHud;
import in.softment.playrangers.R;
import in.softment.playrangers.Services.Service;
import in.softment.playrangers.Utils.Constants;
import in.softment.playrangers.Interface.WalletHasUpdatedInterface;


public class FreeFireUpComingAdapter extends RecyclerView.Adapter<FreeFireUpComingAdapter.MyViewHolder> implements WalletHasUpdatedInterface {

    private int count = 1;
    private ArrayList<FreeFireGameModel> freeFireGameModels;
    private Context context;
    private AlertDialog alertDialog4;
    private ArrayList<String> namess;

    public FreeFireUpComingAdapter(Context context, ArrayList<FreeFireGameModel> freeFireGameModels) {
        this.context = context;
        this.freeFireGameModels = freeFireGameModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.freefire_upcoming_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        FreeFireGameModel ffModel = freeFireGameModels.get(position);
        holder.title.setText(ffModel.getTitle());
        holder.time.setText("Time : "+Service.convertDate(ffModel.date));
        holder.winprize.setText("₹"+ffModel.getWinPrize());
        holder.perkill.setText("₹"+ffModel.getPerKill());
        holder.entryfee.setText("₹"+ffModel.getEntryFee());
        holder.type.setText(ffModel.getType());
        holder.version.setText(ffModel.getVersion());
        holder.map.setText(ffModel.getMap());
        holder.totalSpotLeft.setText("Only "+(ffModel.totalSpots - ffModel.totalJoined)+" spots left");
        holder.total.setText(ffModel.getTotalJoined()+"/"+ffModel.getTotalSpots());
        holder.progressBar.setProgress(ffModel.getTotalJoined());
        holder.progressBar.setMax(ffModel.getTotalSpots());

        if (ffModel.totalJoined >= ffModel.totalSpots) {
            holder.totalSpotLeft.setText("No spot is left!");
            holder.totalSpotLeft.setTextColor(context.getResources().getColor(R.color.red));
            holder.join.setEnabled(false);
            holder.join.setTextSize(10);
            holder.join.setBackgroundResource(R.drawable.matchfullbutton);
            holder.join.setText("Match Full");

        }

        for (String s : ffModel.getJoinedUsers()) {

            if (User.data.getFreeFireUsername() != null && User.data.getFreeFireUsername().equals(s)) {
                holder.totalSpotLeft.setText("Congratulation!");
                holder.totalSpotLeft.setTextColor(context.getResources().getColor(R.color.red));
                holder.join.setEnabled(false);
                holder.join.setTextSize(11);
                holder.join.setBackgroundResource(R.drawable.matchfullbutton);
                holder.join.setText("Joined");
                break;
            }
        }

        if (ffModel.isExpired) {

                holder.join.setEnabled(true);
               holder.join.setText("Live Match");
                holder.join.setTextColor(Color.WHITE);
                holder.join.setBackgroundResource(R.drawable.livematchbutton);
                holder.join.setTextSize(10);

        }

        holder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (User.data.getFreeFireUsername() != null && !User.data.getFreeFireUsername().equals("")) {
                        joinMatch(FreeFireGameModel.freeFireGameModels.get(position),holder.join.getText().toString(),position);
                    }
                    else {
                        // Set Free Fire Name
                    }

            }
        });

    }

    public void disableJoinBtn(MyViewHolder viewHolder) {

    }


    public void joinMatch(FreeFireGameModel playModel, String join, int position) {


        if (!join.equals("Live Match")) {




            if (playModel.getType().equalsIgnoreCase("squad")) {
                AlertDialog.Builder builder4 = new AlertDialog.Builder(context,R.style.myalerttheme);
                View view4 = ((MainActivity)context).getLayoutInflater().inflate(R.layout.squad, null);
                TextView one = view4.findViewById(R.id.one);
                final EditText two = view4.findViewById(R.id.two);
                final EditText three = view4.findViewById(R.id.three);
                final EditText four = view4.findViewById(R.id.four);

                builder4.setView(view4);

                alertDialog4 = builder4.create();
                one.setText("1.  " + User.data.getFreeFireUsername());

                view4.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog4.dismiss();
                    }
                });
                view4.findViewById(R.id.join).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sTwo = two.getText().toString().trim();
                        String sThree = three.getText().toString().trim();
                        String sFour = four.getText().toString().trim();

                        namess = new ArrayList<>();
                        count = 1;
                        namess.add(User.data.freeFireUsername);
                        if (!sTwo.isEmpty()) {
                            count++;
                            namess.add(sTwo);
                        }
                        if (!sThree.isEmpty()) {
                            count++;
                            namess.add(sThree);
                        }
                        if (!sFour.isEmpty()) {
                            count++;
                            namess.add(sFour);
                        }
                        alertDialog4.cancel();

                        new Service().sendMatchDetails(playModel.getEntryFee(),position,FreeFireUpComingAdapter.this,context,count);

                    }
                });


                alertDialog4.show();


            } else if (playModel.getType().equalsIgnoreCase("duo")) {
                AlertDialog.Builder builder5 = new AlertDialog.Builder(context,R.style.myalerttheme);
                View view5 = ((MainActivity)context).getLayoutInflater().inflate(R.layout.duo, null);
                TextView one = view5.findViewById(R.id.one);
                final EditText two = view5.findViewById(R.id.two);


                builder5.setView(view5);
                AlertDialog alertDialog5 = builder5.create();
                one.setText("1.  " + User.data.getFreeFireUsername());

                view5.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog5.dismiss();
                    }
                });
                view5.findViewById(R.id.join).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sTwo = two.getText().toString().trim();


                         namess = new ArrayList<>();
                         count = 1;
                        namess.add(User.data.freeFireUsername);
                        if (!sTwo.isEmpty()) {
                            count++;
                            namess.add(sTwo);
                        }

                        alertDialog5.cancel();

                        new Service().sendMatchDetails(playModel.getEntryFee(),position,FreeFireUpComingAdapter.this,context,count);


                    }
                });


                alertDialog5.show();


            } else {
                  count = 1;
                  namess.add(User.data.freeFireUsername);
                  new Service().sendMatchDetails(playModel.getEntryFee(),position,FreeFireUpComingAdapter.this,context,count);
            }


        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.youtube.com/channel/UC_L1f0ftFFrnfnLU0PFTLLA"));
            context.startActivity(intent);
        }
    }



    private void updateMatch(int position, int oldWinning, int oldDeposit) {
        final DocumentReference sfDocRef = FirebaseFirestore.getInstance().collection(Constants.DbPath.freeFireEvent).document(FreeFireGameModel.freeFireGameModels.get(position).gameID);

        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Map<String, Object>>() {
            @Override
            public Map<String, Object> apply(Transaction transaction) throws FirebaseFirestoreException {

                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                int totalJoined = 0;
                int totalSpots = 48;
                boolean isExpired = false;
                ArrayList<String> joinedUsers = new ArrayList<>();

                if (snapshot.contains("totalJoined")) {
                    totalJoined = snapshot.get("totalJoined",Integer.class);

                }
                if (snapshot.contains("totalSpots")) {
                    totalSpots = snapshot.get("totalSpots",Integer.class);
                }
                if (snapshot.contains("isExpired")) {
                    isExpired = snapshot.getBoolean("isExpired");
                }


                if (snapshot.contains("joinedUsers")) {
                    joinedUsers = (ArrayList<String>) snapshot.get("joinedUsers");
                }

                Map<String, Object> map = new HashMap<>();
                map.put("totalJoined",totalJoined);
                map.put("totalSpots",totalSpots);
                map.put("isExpired",isExpired);
                map.put("joinedUsers",joinedUsers);

                return map;
            }
        }).addOnSuccessListener(new OnSuccessListener<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> map) {
                int totalJoined = 0;
                int totalSpots = 48;
                boolean isExpired = false;
                ArrayList<String> joinedUsers;

                totalJoined = (int) map.get("totalJoined");
                totalSpots = (int) map.get("totalSpots");
                isExpired = (boolean) map.get("isExpired");
                joinedUsers = (ArrayList<String>) map.get("joinedUsers");

                if (!isExpired) {
                    if (totalJoined < totalSpots) {
                        //Increment Joined user + Add Joined User name
                        if (namess.size() > 0)
                            joinedUsers.addAll(namess);

                        Map<String, Object> map1 = new HashMap();
                        map1.put("joinedUsers",joinedUsers);
                        map1.put("totalJoined",(totalJoined+count));

                        sfDocRef.update(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                 ProgressHud.dialog.dismiss();
                                if (task.isSuccessful()) {
                                    Service.addTransaction(FirebaseAuth.getInstance().getCurrentUser().getUid(),FreeFireGameModel.freeFireGameModels.get(position).getEntryFee(),"Free Fire Match Joined","debit","FreeFire",Service.generateOrderId(),true);
                                }
                                else {
                                    Service.refundAmount(oldDeposit,oldWinning);
                                   Service.showErrorDialog(context,"Error",task.getException().getMessage());
                                }


                            }
                        });
                    }
                    else {
                        ProgressHud.dialog.dismiss();
                        Service.refundAmount(oldDeposit,oldWinning);
                        Service.showErrorDialog(context,"Oh No!","Sorry! No space left. Please join upcoming matches.");
                    }

                }
                else {
                    ProgressHud.dialog.dismiss();
                    Service.refundAmount(oldDeposit,oldWinning);
                    Service.showErrorDialog(context,"Oh No!","Sorry! The Match has started.. Please join upcoming matches.");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ProgressHud.dialog.dismiss();
                        Service.refundAmount(oldDeposit,oldWinning);
                        Service.showErrorDialog(context,"Oh No!",e.getMessage());

                    }
        });

    }





    @Override
    public int getItemCount() {
        return freeFireGameModels.size();
    }


    @Override
    public void walletHasUpdated(boolean isUpdated, int oldWinning, int oldDeposit, int position) {
        if (isUpdated) {
            updateMatch(position,oldWinning,oldDeposit);
        }

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, time, winprize, perkill, entryfee, type, version, map, totalSpotLeft,total;
        ImageView seeJoinedList;
        AppCompatButton join;
        private ProgressBar progressBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            winprize = itemView.findViewById(R.id.winprizetext);
            perkill = itemView.findViewById(R.id.perkilltext);
            entryfee = itemView.findViewById(R.id.entryfeestext);
            type = itemView.findViewById(R.id.typetext);
            version = itemView.findViewById(R.id.versiontext);
            map = itemView.findViewById(R.id.maptext);
            seeJoinedList = itemView.findViewById(R.id.see);
            progressBar = itemView.findViewById(R.id.progressbars);
            totalSpotLeft = itemView.findViewById(R.id.totallefttext);
            total = itemView.findViewById(R.id.total);
            join = itemView.findViewById(R.id.joinbutton);

        }
    }
}
