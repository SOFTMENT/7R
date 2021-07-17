package in.softment.playrangersadmin.Adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.softment.playrangersadmin.Cricket_Create_Combos;

import in.softment.playrangersadmin.Model.CricketMatchSquad.Squad;
import in.softment.playrangersadmin.R;

public class CricketPlayersAdapter extends RecyclerView.Adapter<CricketPlayersAdapter.ViewHolder> {

    private Context context;
    private List<Squad> players;
    private List<Squad> player__1s;
    private int total = 0;
    Squad player__1 = null;
    SparseBooleanArray checkedItems1 = new SparseBooleanArray();
    SparseBooleanArray checkedItems2 = new SparseBooleanArray();
    private Cricket_Create_Combos cricket_create_combos;
    private int count = 0;
    public CricketPlayersAdapter(Context context, List<Squad> players, List<Squad> player__1s, int total)  {
        this.context = context;
        this.players = players;
        this.player__1s = player__1s;
        this.total = total;
        cricket_create_combos = (Cricket_Create_Combos)context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cricket_player_view,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Squad player = players.get(position);


        try {
            player__1 = player__1s.get(position);


            holder.playerName2.setText(player__1.getFullname());

            if (player__1.getImagePath() != null && !player__1.getImagePath().isEmpty() && !player.getImagePath().equalsIgnoreCase("https://cdn.sportmonks.com")) {
                Glide.with(context).load(player__1.getImagePath()).into(holder.playerImage2);

            } else {
                holder.playerImage2.setImageResource(R.drawable.photonotavail);
            }
        }
        catch (Exception e) {
            holder.secondplayerview.setVisibility(View.GONE);
        }






        holder.playerName1.setText(player.getFullname());





        holder.checkBox1.setChecked(checkedItems1.get(position));
        holder.checkBox2.setChecked(checkedItems2.get(position));

        holder.checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
             
                if (b) {
                    if (count < total) {
                        count++;
                        checkedItems1.put(position, b);
                        cricket_create_combos.addPlayers(player.getFullname(), String.valueOf(player.getId()), player.getImagePath());
                    }
                    else {
                        holder.checkBox1.setChecked(false);
                    }
                }
                else {

                    count--;
                    cricket_create_combos.removePlayer(String.valueOf(player.getId()));
                }
            }
        });

        holder.checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                
                if (b) {
                    if (count < total) {
                        count++;
                        checkedItems2.put(position, b);

                        cricket_create_combos.addPlayers(player__1s.get(position).getFullname(), String.valueOf(player__1s.get(position).getId()), player__1s.get(position).getImagePath());
                    }
                    else {
                        holder.checkBox2.setChecked(false);
                    }
                }
                else {
                    count--;
                    cricket_create_combos.removePlayer(String.valueOf(player__1.getId()));
                }
            }
        });



        if (player.getImagePath() != null && !player.getImagePath().isEmpty() && !player.getImagePath().equalsIgnoreCase("https://cdn.sportmonks.com") ) {
            Glide.with(context).load(player.getImagePath()).into(holder.playerImage1);

        }
        else {
            holder.playerImage1.setImageResource(R.drawable.photonotavail);
        }





    }

    @Override
    public int getItemCount() {

       return players.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView playerName1, playerName2;
        private CheckBox checkBox1, checkBox2;
        private ImageView playerImage1, playerImage2;
        private LinearLayout secondplayerview, firstplayerview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerName1 = itemView.findViewById(R.id.playerName1);
            playerName2 = itemView.findViewById(R.id.playerName2);
            checkBox1 = itemView.findViewById(R.id.playCheck1);
            checkBox2 = itemView.findViewById(R.id.playerCheck2);
            playerImage1 = itemView.findViewById(R.id.player1);
            playerImage2 = itemView.findViewById(R.id.player2);
            secondplayerview = itemView.findViewById(R.id.secondplayerview);
            firstplayerview = itemView.findViewById(R.id.firstplayerview);

        }
    }
}
