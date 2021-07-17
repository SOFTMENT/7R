package in.softment.playrangersadmin.Adapter;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


import in.softment.playrangersadmin.CricketChooseCombosScreen;
import in.softment.playrangersadmin.Model.CricketMatchDetailsModel.Datum;
import in.softment.playrangersadmin.Model.CricketMatchSquad.CricketMatchSquadModel;
import in.softment.playrangersadmin.R;
import in.softment.playrangersadmin.Service.Service;


public class CricketShowAllMatchsAdapter extends RecyclerView.Adapter<CricketShowAllMatchsAdapter.MyViewHolder> {

    private Context context;
    private List<Datum> matches;
    protected Animation blink_anim;
    private final List<MyViewHolder> lstHolders;
    private final Handler mHandler = new Handler();

    private final Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (lstHolders) {
                long currentTime = System.currentTimeMillis();
                for (MyViewHolder holder : lstHolders) {

                    holder.updateTimeRemaining(currentTime);
                }
            }
        }
    };


    public CricketShowAllMatchsAdapter(Context context, List<Datum> matches) {
        blink_anim = AnimationUtils.loadAnimation(context, R.anim.blink);
        this.context = context;
        this.matches = matches;
        lstHolders = new ArrayList<>();
        startUpdateTimer();
    }

    private void startUpdateTimer() {
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cricket_show_match_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        Map<String, CricketMatchSquadModel> matchSquadModelMap = CricketMatchSquadModel.cricketMatchSquadModelMap;

        Datum match = matches.get(position);
        holder.setData(match);
        synchronized (lstHolders) {
            lstHolders.add(holder);
        }
        holder.updateTimeRemaining(System.currentTimeMillis());

        holder.macthname1.setText(matchSquadModelMap.get(String.valueOf(match.getLocalteamId())).getData().getCode());
        holder.macthname2.setText(matchSquadModelMap.get(String.valueOf(match.getVisitorteamId())).getData().getCode());

        Glide.with(context).load(matchSquadModelMap.get(String.valueOf(match.getLocalteamId())).getData().getImagePath()).into(holder.matchimage1);
        Glide.with(context).load(matchSquadModelMap.get(String.valueOf(match.getVisitorteamId())).getData().getImagePath()).into(holder.matchimage2);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
        Date futuredate = new Date();
        try {
            futuredate = dateFormat.parse(match.getStartingAt());

        } catch (ParseException e) {

            e.printStackTrace();
        }
        holder.date.setText(Service.convertDate(futuredate));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (match.getStatus().equalsIgnoreCase("LIVE")) {


                }
                else {
                    if (match != null) {

                        Intent intent = new Intent(context, CricketChooseCombosScreen.class);
                        intent.putExtra("datum",match);
                        context.startActivity(intent);
                    }


                }




            }
        });

    }


    @Override
    public int getItemCount() {
        if (matches.size()>=6) {

            return 6;
        }
        else {
            return matches.size();
        }
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView matchimage2;
        ShapeableImageView matchimage1;
        private TextView timer, date, macthname1, macthname2;
        private Datum match;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

        private View view;
        public void setData(Datum match) {
            this.match = match;
            // textViewName.setText(item.name);
            updateTimeRemaining(System.currentTimeMillis());
        }
        public void updateTimeRemaining(long currentTime) {
            dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
            Date futureDate = new Date();
            try {
                futureDate = dateFormat.parse(match.getStartingAt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long timeDiff = futureDate.getTime() - currentTime;

            if (timeDiff > 0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 1000);

                timer.setText("START IN: "+hours + ":" + minutes + ":" + seconds);

            } else {

                timer.setText("LIVE");
                timer.setTextColor(Color.RED);
                timer.startAnimation(blink_anim);
            }
        }

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
