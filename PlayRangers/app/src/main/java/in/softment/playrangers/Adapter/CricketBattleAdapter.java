package in.softment.playrangers.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.softment.playrangers.Fragments.CricketShowBattleFragment;
import in.softment.playrangers.R;

public class CricketBattleAdapter extends RecyclerView.Adapter<CricketBattleAdapter.MyViewHolder> {
    protected Animation blink_anim;
    private final CricketShowBattleFragment cricketShowBattleFragment;
    public CricketBattleAdapter(Context context, CricketShowBattleFragment cricketShowBattleFragment){
        blink_anim = AnimationUtils.loadAnimation(context,
                R.anim.blink);
        this.cricketShowBattleFragment = cricketShowBattleFragment;
    }
    @NonNull
    @Override
    public CricketBattleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cricket_battle_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CricketBattleAdapter.MyViewHolder holder, int position) {
        holder.prize.startAnimation(blink_anim);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView prize;
        private View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            prize = itemView.findViewById(R.id.prize);
        }
    }
}
