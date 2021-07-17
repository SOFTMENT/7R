package in.softment.playrangersadmin.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;


import in.softment.playrangersadmin.Model.CombosModel;
import in.softment.playrangersadmin.R;


public class CricketComboAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Animation blink_anim;
    private Context context;
    private int totalPlayersincombo = 2;
    private ArrayList<CombosModel> combosModels;
    public CricketComboAdapter(Context context,ArrayList<CombosModel> combosModels,int  totalPlayerincombo) {
        this.context = context;
        this.combosModels = combosModels;
        this.totalPlayersincombo = totalPlayerincombo;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (totalPlayersincombo == 2) {
            return new MyDuoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_duo_combo_layout, parent, false));

        }
        else if (totalPlayersincombo == 3) {
            return new MyTrioViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_trio_combo_layout, parent, false));

        }
        else if (totalPlayersincombo == 4) {
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
            myDuoViewHolder.investedText.setText("Invested By: "+combosModel.getTotalInvestors()+" Players");
            int i = 0;
            for (Map<String,String> map : combosModel.getPlayersInfos()) {
                i++;
                if (map.containsKey("playerImage")) {
                    String img = map.get("playerImage");
                    if (img != null && !img.isEmpty()) {
                        if (i == 1) {
                            Glide.with(context).load(img).into(myDuoViewHolder.playerImg1);
                        }
                        else{
                            Glide.with(context).load(img).into(myDuoViewHolder.playerImg2);
                        }


                    }
                }

                if (map.containsKey("playerName")) {
                    if (i == 1) {
                        myDuoViewHolder.playerName1.setText(map.get("playerName"));
                    }
                    else {
                        myDuoViewHolder.playerName2.setText(map.get("playerName"));
                    }


                }


            }

        }
        else if (totalPlayersincombo == 3) {

            MyTrioViewHolder myTrioViewHolder = (MyTrioViewHolder) holder;
            myTrioViewHolder.investedText.setText("Invested By: "+combosModel.getTotalInvestors()+" Players");
            int i = 0;
            for (Map<String,String> map : combosModel.getPlayersInfos()) {
                i++;
                if (map.containsKey("playerImage")) {
                    String img = map.get("playerImage");
                    if (img != null && !img.isEmpty()) {
                        if (i == 1) {
                            Glide.with(context).load(img).into(myTrioViewHolder.playerImg1);
                        }
                        else if (i == 2){
                            Glide.with(context).load(img).into(myTrioViewHolder.playerImg2);
                        }
                        else {
                            Glide.with(context).load(img).into(myTrioViewHolder.playerImg3);
                        }

                    }
                }

                if (map.containsKey("playerName")) {
                    if (i == 1) {
                        myTrioViewHolder.playerName1.setText(map.get("playerName"));
                    }
                    else if (i == 2){
                        myTrioViewHolder.playerName2.setText(map.get("playerName"));
                    }
                    else  {
                        myTrioViewHolder.playerName3.setText(map.get("playerName"));
                    }

                }


            }

        }
        else if (totalPlayersincombo == 4){
            MySquadViewHolder mySquadViewHolder = (MySquadViewHolder) holder;
            mySquadViewHolder.investedText.setText("Invested By: "+combosModel.getTotalInvestors()+" Players");
            int i = 0;
            for (Map<String,String> map : combosModel.getPlayersInfos()) {
                i++;
                if (map.containsKey("playerImage")) {
                    String img = map.get("playerImage");
                    if (img != null && !img.isEmpty()) {
                        if (i == 1) {
                            Glide.with(context).load(img).into(mySquadViewHolder.playerImg1);
                        }
                        else if (i == 2){
                            Glide.with(context).load(img).into(mySquadViewHolder.playerImg2);
                        }
                        else if (i == 3) {
                            Glide.with(context).load(img).into(mySquadViewHolder.playerImg3);
                        }
                        else {
                            Glide.with(context).load(img).into(mySquadViewHolder.playerImg4);
                        }
                    }
                }

                if (map.containsKey("playerName")) {
                    if (i == 1) {
                        mySquadViewHolder.playerName1.setText(map.get("playerName"));
                    }
                    else if (i == 2){
                        mySquadViewHolder.playerName2.setText(map.get("playerName"));
                    }
                    else if (i == 3) {
                        mySquadViewHolder.playerName3.setText(map.get("playerName"));
                    }
                    else {
                        mySquadViewHolder.playerName4.setText(map.get("playerName"));
                    }
                }


            }

        }

    }

    @Override
    public int getItemCount() {

        return combosModels.size();
    }



    public static class MyDuoViewHolder extends RecyclerView.ViewHolder{
        private ImageView playerImg1, playerImg2;
        private TextView playerName1, playerName2;
        private TextView investedText;
        private AppCompatButton investBtn;
        public MyDuoViewHolder(@NonNull View itemView) {
            super(itemView);
            playerImg1 = itemView.findViewById(R.id.player1);
            playerImg2 = itemView.findViewById(R.id.player2);

            playerName1 = itemView.findViewById(R.id.playerName1);
            playerName2 = itemView.findViewById(R.id.playerName2);

            investedText = itemView.findViewById(R.id.investedText);


        }
    }

    public static class MyTrioViewHolder extends RecyclerView.ViewHolder{
        private ImageView playerImg1, playerImg2, playerImg3;
        private TextView playerName1, playerName2, playerName3;
        private TextView investedText;
        private AppCompatButton investBtn;
        public MyTrioViewHolder(@NonNull View itemView) {
            super(itemView);
            playerImg1 = itemView.findViewById(R.id.player1);
            playerImg2 = itemView.findViewById(R.id.player2);
            playerImg3 = itemView.findViewById(R.id.player3);

            playerName1 = itemView.findViewById(R.id.playerName1);
            playerName2 = itemView.findViewById(R.id.playerName2);
            playerName3 = itemView.findViewById(R.id.playerName3);

            investedText = itemView.findViewById(R.id.investedText);


        }
    }
    public static class MySquadViewHolder extends RecyclerView.ViewHolder{
        private ImageView playerImg1, playerImg2, playerImg3, playerImg4;
        private TextView playerName1, playerName2, playerName3, playerName4;
        private TextView investedText;
        private AppCompatButton investBtn;
        public MySquadViewHolder(@NonNull View itemView) {
            super(itemView);
            playerImg1 = itemView.findViewById(R.id.player1);
            playerImg2 = itemView.findViewById(R.id.player2);
            playerImg3 = itemView.findViewById(R.id.player3);
            playerImg4 = itemView.findViewById(R.id.player4);

            playerName1 = itemView.findViewById(R.id.playerName1);
            playerName2 = itemView.findViewById(R.id.playerName2);
            playerName3 = itemView.findViewById(R.id.playerName3);
            playerName4 = itemView.findViewById(R.id.playerName4);

            investedText = itemView.findViewById(R.id.investedText);


        }
    }
}
