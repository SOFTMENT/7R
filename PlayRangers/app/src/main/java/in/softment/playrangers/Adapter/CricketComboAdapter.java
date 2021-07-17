package in.softment.playrangers.Adapter;

import android.content.Context;
import android.media.Image;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.List;
import java.util.function.LongFunction;

import de.hdodenhof.circleimageview.CircleImageView;
import in.softment.playrangers.Fragments.CricketSelectComboFragment;
import in.softment.playrangers.MainActivity;
import in.softment.playrangers.Model.CombosModel;
import in.softment.playrangers.Model.CricketInvestedAmountModel;
import in.softment.playrangers.Model.User;
import in.softment.playrangers.R;
import in.softment.playrangers.Services.Service;
import in.softment.playrangers.Utils.Constants;

public class CricketComboAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Animation blink_anim;
    private Context context;
    private int totalPlayersincombo = 2;
    private List<CombosModel> combosModels;
    private String type = "duo";
    private CricketSelectComboFragment cricketSelectComboFragment;

    public CricketComboAdapter(Context context, List<CombosModel> combosModels, int count, CricketSelectComboFragment cricketSelectComboFragment) {
        this.combosModels = combosModels;
        this.context = context;
        this.totalPlayersincombo = count;
        this.cricketSelectComboFragment = cricketSelectComboFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (totalPlayersincombo == 2) {
            type = "duo";
            return new MyDuoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_duo_combo_layout, parent, false));

        }
        else if (totalPlayersincombo == 3) {
            type = "trio";
            return new MyTrioViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_trio_combo_layout, parent, false));

        }
        else if (totalPlayersincombo == 4) {
            type = "squad";
            return new MySquadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_sqaud_combo_layout, parent, false));

        }
      return new MyDuoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_duo_combo_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        CombosModel combosModel = combosModels.get(position);
        if (totalPlayersincombo == 2) {


            MyDuoViewHolder myDuoViewHolder = (MyDuoViewHolder)holder;

            myDuoViewHolder.invest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            myDuoViewHolder.invested.setText("Invested By : "+combosModel.totalInvestors+" Players");


            myDuoViewHolder.playerName1.setText(combosModel.playersInfos.get(0).get("playerName"));
            myDuoViewHolder.playerName2.setText(combosModel.playersInfos.get(1).get("playerName"));

            if (combosModel.playersInfos.get(0).get("playerImage") != null && !combosModel.playersInfos.get(0).get("playerImage").isEmpty()) {
                Glide.with(context).load(combosModel.playersInfos.get(0).get("playerImage")).into(myDuoViewHolder.playerImg1);
            }

            if (combosModel.playersInfos.get(1).get("playerImage") != null && !combosModel.playersInfos.get(1).get("playerImage").isEmpty()) {
                Glide.with(context).load(combosModel.playersInfos.get(1).get("playerImage")).into(myDuoViewHolder.playerImg2);
            }

        }
        else if (totalPlayersincombo == 3) {
            MyTrioViewHolder myTrioViewHolder = (MyTrioViewHolder) holder;
            myTrioViewHolder.invested.setText("Invested By : "+combosModel.totalInvestors+" Players");

            myTrioViewHolder.invest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            myTrioViewHolder.playerName1.setText(combosModel.playersInfos.get(0).get("playerName"));
            myTrioViewHolder.playerName2.setText(combosModel.playersInfos.get(1).get("playerName"));
            myTrioViewHolder.playerName3.setText(combosModel.playersInfos.get(2).get("playerName"));

            if (combosModel.playersInfos.get(0).get("playerImage") != null && !combosModel.playersInfos.get(0).get("playerImage").isEmpty()) {
                Glide.with(context).load(combosModel.playersInfos.get(0).get("playerImage")).into(myTrioViewHolder.playerImg1);
            }

            if (combosModel.playersInfos.get(1).get("playerImage") != null && !combosModel.playersInfos.get(1).get("playerImage").isEmpty()) {
                Glide.with(context).load(combosModel.playersInfos.get(1).get("playerImage")).into(myTrioViewHolder.playerImg2);
            }

            if (combosModel.playersInfos.get(2).get("playerImage") != null && !combosModel.playersInfos.get(2).get("playerImage").isEmpty()) {
                Glide.with(context).load(combosModel.playersInfos.get(2).get("playerImage")).into(myTrioViewHolder.playerImg3);
            }
        }
        else {



            MySquadViewHolder mySquadViewHolder = (MySquadViewHolder) holder;
            mySquadViewHolder.invested.setText("Invested By : "+combosModel.totalInvestors+" Players");


            mySquadViewHolder.invest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cricketSelectComboFragment.showInvestPopUp(String.valueOf(combosModel.matchId),type,combosModel.comboId,combosModel.totalPrizePool);

                }
            });

            DocumentReference df = FirebaseFirestore.getInstance().collection(Constants.DbPath.cricket).document(String.valueOf(combosModel.getMatchId())).collection(type).document(combosModel.comboId);
            df.collection(Constants.DbPath.joinedUsers).document(User.data.uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            if (value !=null && value.exists()) {
                                CricketInvestedAmountModel cricketInvestedAmountModel = null;

                                    cricketInvestedAmountModel = value.toObject(CricketInvestedAmountModel.class);
                                    Log.d("VIJAYAMOUNT",cricketInvestedAmountModel.amount+"");
                                    mySquadViewHolder.estimatedWinning.setText("Estimated Earning: ₹"+cricketSelectComboFragment.getEstimatedWinning(cricketInvestedAmountModel.amount,combosModel.totalPrizePool,false)+"");
                                    mySquadViewHolder.invsetedAmount.setText("Invested Amount: ₹"+cricketInvestedAmountModel.amount);
                                    mySquadViewHolder.estimatedandamontLL.setVisibility(View.VISIBLE);


                            }
                            else {
                                mySquadViewHolder.estimatedandamontLL.setVisibility(View.GONE);
                            }
                        }
                }
            });




            mySquadViewHolder.playerName1.setText(combosModel.playersInfos.get(0).get("playerName"));
            mySquadViewHolder.playerName2.setText(combosModel.playersInfos.get(1).get("playerName"));
            mySquadViewHolder.playerName3.setText(combosModel.playersInfos.get(2).get("playerName"));
            mySquadViewHolder.playerName4.setText(combosModel.playersInfos.get(3).get("playerName"));

            if (combosModel.playersInfos.get(0).get("playerImage") != null && !combosModel.playersInfos.get(0).get("playerImage").isEmpty()) {
                Glide.with(context).load(combosModel.playersInfos.get(0).get("playerImage")).into(mySquadViewHolder.playerImg1);
            }

            if (combosModel.playersInfos.get(1).get("playerImage") != null && !combosModel.playersInfos.get(1).get("playerImage").isEmpty()) {
                Glide.with(context).load(combosModel.playersInfos.get(1).get("playerImage")).into(mySquadViewHolder.playerImg2);
            }

            if (combosModel.playersInfos.get(2).get("playerImage") != null && !combosModel.playersInfos.get(2).get("playerImage").isEmpty()) {
                Glide.with(context).load(combosModel.playersInfos.get(2).get("playerImage")).into(mySquadViewHolder.playerImg3);
            }

            if (combosModel.playersInfos.get(3).get("playerImage") != null && !combosModel.playersInfos.get(3).get("playerImage").isEmpty()) {
                Glide.with(context).load(combosModel.playersInfos.get(3).get("playerImage")).into(mySquadViewHolder.playerImg4);
            }


            mySquadViewHolder.estimatedWinningInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Service.showErrorDialog(context,"INFO","Estimated winning will be vary according to number of participants before the match starts.");
                }
            });

            mySquadViewHolder.investedAmountInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Service.showErrorDialog(context,"INFO","Your invested amount on this combo.");
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return combosModels.size();
    }





    public static class MyDuoViewHolder extends RecyclerView.ViewHolder{
        private ImageView playerImg1, playerImg2;
        private TextView playerName1, playerName2;
        private AppCompatButton invest;
        private TextView invested;
        public MyDuoViewHolder(@NonNull View itemView) {
            super(itemView);
            playerImg1 = itemView.findViewById(R.id.playerImage1);
            playerImg2 = itemView.findViewById(R.id.playerImage2);

            playerName1 =itemView.findViewById(R.id.playerName1);
            playerName2 =itemView.findViewById(R.id.playerName2);

            invest = itemView.findViewById(R.id.investBtn);
            invested = itemView.findViewById(R.id.invested);

        }
    }

    public static class MyTrioViewHolder extends RecyclerView.ViewHolder{
        private ImageView playerImg1, playerImg2, playerImg3;
        private TextView playerName1, playerName2, playerName3;
        private AppCompatButton invest;
        private TextView invested;
        public MyTrioViewHolder(@NonNull View itemView) {
            super(itemView);
            playerImg1 = itemView.findViewById(R.id.playerImage1);
            playerImg2 = itemView.findViewById(R.id.playerImage2);
            playerImg3 = itemView.findViewById(R.id.playerImage3);


            playerName1 =itemView.findViewById(R.id.playerName1);
            playerName2 =itemView.findViewById(R.id.playerName2);
            playerName3 =itemView.findViewById(R.id.playerName3);

            invest = itemView.findViewById(R.id.investBtn);
            invested = itemView.findViewById(R.id.invested);

        }
    }


    public static class MySquadViewHolder extends RecyclerView.ViewHolder{
        private ImageView playerImg1, playerImg2, playerImg3, playerImg4;
        private TextView playerName1, playerName2, playerName3, playerName4;
        private AppCompatButton invest;
        private TextView invested;

        private LinearLayout estimatedandamontLL;
        private TextView estimatedWinning, invsetedAmount;
        private ImageView estimatedWinningInfo, investedAmountInfo;
        public MySquadViewHolder(@NonNull View itemView) {
            super(itemView);
            playerImg1 = itemView.findViewById(R.id.playerImage1);
            playerImg2 = itemView.findViewById(R.id.playerImage2);
            playerImg3 = itemView.findViewById(R.id.playerImage3);
            playerImg4 = itemView.findViewById(R.id.playerImage4);

            playerName1 =itemView.findViewById(R.id.playerName1);
            playerName2 =itemView.findViewById(R.id.playerName2);
            playerName3 =itemView.findViewById(R.id.playerName3);
            playerName4 =itemView.findViewById(R.id.playerName4);

            estimatedandamontLL = itemView.findViewById(R.id.estimatedandinvestedLL);

            invest = itemView.findViewById(R.id.investBtn);
            invested = itemView.findViewById(R.id.invested);

            estimatedWinning = itemView.findViewById(R.id.estimatedWinning);
            invsetedAmount = itemView.findViewById(R.id.investedAmount);

            estimatedWinningInfo = itemView.findViewById(R.id.estimatedWinningInfo);
            investedAmountInfo = itemView.findViewById(R.id.investedAmountInfo);

        }
    }
}
