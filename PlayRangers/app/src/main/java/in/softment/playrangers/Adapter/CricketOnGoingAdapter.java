package in.softment.playrangers.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


import in.softment.playrangers.Model.CricketMatchDetailsModel;
import in.softment.playrangers.R;
import in.softment.playrangers.Services.Service;

public class CricketOnGoingAdapter extends RecyclerView.Adapter<CricketOnGoingAdapter.MyViewHolder> {

    private Context context;
    private List<CricketMatchDetailsModel> matches;
    protected Animation blink_anim;



    public CricketOnGoingAdapter(Context context, List<CricketMatchDetailsModel> matches) {
        blink_anim = AnimationUtils.loadAnimation(context,
                R.anim.blink);
        this.context = context;
        this.matches = matches;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cricket_upcoming_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        CricketMatchDetailsModel match = matches.get(position);

        holder.macthname1.setText(match.getTeamNameHome());
        holder.macthname2.setText(match.getTeamNameAway());

        holder.matchimage1.setImageResource(getTeamImageResource(match.getTeamIdHome()));
        holder.matchimage2.setImageResource(getTeamImageResource(match.getTeamIdAway()));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        Date futuredate = new Date();
        try {
            futuredate = dateFormat.parse(match.getStartTime());
            futuredate.setTime(futuredate.getTime());

            //1800000

        } catch (ParseException e) {

            e.printStackTrace();
        }
        holder.date.setText(Service.convertDate(futuredate));

        holder.timer.startAnimation(blink_anim);
        holder.timer.setText("LIVE");
        holder.timer.setTextColor(Color.RED);

        //TEMPSCORE
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, TempScore.class);
//                intent.putExtra("matchid",match.matchId);
//                intent.putExtra("seriesid",match.tournamentId);
//                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {

            return matches.size();

    }

    public int getTeamImageResource(int teamID) {
        switch (teamID) {
            case 57 : return  R.drawable.rcblogo;
            case 62 : return  R.drawable.mumbailogo;
            case 64 : return R.drawable.rajashtanlogo;
            case 63 : return R.drawable.punjablogo;
            case 61 : return R.drawable.kkrlogo;
            case 60 : return R.drawable.delhilogo;
            case 244 : return R.drawable.sunriserslogo;
            case 58 : return R.drawable.csklogo;
            default: return  -1;
        }

    }

    public String getTeamName(int teamID) {
        switch (teamID) {
            case 57 : return  "RCB";
            case 62 : return  "MUMBAI";
            case 64 : return "RAJASTHAN";
            case 63 : return "KXIP";
            case 61 : return "KKR";
            case 60 : return "DELHI";
            case 244 : return "SRH";
            case 58 : return "CHENNAI";
            default: return "ERROR 404";


        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView matchimage2;
        ShapeableImageView matchimage1;
        private TextView timer, date, macthname1, macthname2;
        private View view;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            matchimage1 = itemView.findViewById(R.id.matchimg1);
            matchimage2 = itemView.findViewById(R.id.matchimg2);
            matchimage1.setShapeAppearanceModel(ShapeAppearanceModel.builder().setAllCorners(CornerFamily.ROUNDED,16).build());
            matchimage2.setShapeAppearanceModel(ShapeAppearanceModel.builder().setAllCorners(CornerFamily.ROUNDED,16).build());
            timer = itemView.findViewById(R.id.timer);
            date = itemView.findViewById(R.id.date);
            macthname1 = itemView.findViewById(R.id.matchname1);
            macthname2 = itemView.findViewById(R.id.matchname2);
        }
    }
}
