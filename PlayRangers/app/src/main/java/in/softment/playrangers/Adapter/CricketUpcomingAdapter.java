package in.softment.playrangers.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
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

import org.w3c.dom.Text;

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
import java.util.concurrent.TimeUnit;

import in.softment.playrangers.API.CricketApi;
import in.softment.playrangers.Fragments.CricketUpcomingFragment;
import in.softment.playrangers.Model.CricketMatchDetailsModel;
import in.softment.playrangers.R;
import in.softment.playrangers.Services.Service;

public class CricketUpcomingAdapter extends RecyclerView.Adapter<CricketUpcomingAdapter.MyViewHolder> {

    private Context context;
    private List<CricketMatchDetailsModel> matches;
    protected Animation blink_anim;
    private final List<MyViewHolder> lstHolders;
    private CricketUpcomingFragment cricketUpcomingFragment;
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


    public CricketUpcomingAdapter(Context context, List<CricketMatchDetailsModel> matches, CricketUpcomingFragment cricketUpcomingFragment) {
         blink_anim = AnimationUtils.loadAnimation(context,
                R.anim.blink);
        this.context = context;
        this.matches = matches;
        this.cricketUpcomingFragment = cricketUpcomingFragment;
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
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cricket_upcoming_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        CricketMatchDetailsModel match = matches.get(position);
        holder.setData(match);
        synchronized (lstHolders) {
            lstHolders.add(holder);
        }
        holder.updateTimeRemaining(System.currentTimeMillis());

        holder.macthname1.setText(match.getTeamNameHome());
        holder.macthname2.setText(match.getTeamNameAway());



        if (match.teamImageHome != null && !match.teamImageHome.isEmpty() && !match.teamImageHome.equalsIgnoreCase("https://cdn.sportmonks.com") ) {
            Glide.with(context).load(match.teamImageHome).into(holder.matchimage1);
        }
        else {
            holder.matchimage1.setImageResource(R.drawable.photonotavail);
        }

        if (match.teamImageAway != null && !match.teamImageAway.isEmpty() && !match.teamImageAway.equalsIgnoreCase("https://cdn.sportmonks.com") ) {
            Glide.with(context).load(match.teamImageAway).into(holder.matchimage2);
        }
        else {
            holder.matchimage2.setImageResource(R.drawable.photonotavail);
        }


        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date futuredate = new Date();
        try {
            futuredate = dateFormat.parse(match.getStartTime());

        } catch (ParseException e) {

            e.printStackTrace();
        }
        holder.date.setText(Service.convertDate(futuredate));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cricketUpcomingFragment.showAvailableMatches(match);

            }
        });

    }


    @Override
    public int getItemCount() {
       if (matches.size()>=3) {
           return 3;
       }
       else {
           return matches.size();
       }
    }





    public class MyViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView matchimage2;
        ShapeableImageView matchimage1;
        private TextView timer, date, macthname1, macthname2;
        private CricketMatchDetailsModel match;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",Locale.getDefault());
        private View view;
        public void setData(CricketMatchDetailsModel match) {
            this.match = match;
           // textViewName.setText(item.name);
            updateTimeRemaining(System.currentTimeMillis());
        }
        public void updateTimeRemaining(long currentTime) {
            Date futureDate = new Date();
            try {
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                futureDate = dateFormat.parse(match.getStartTime());
                futureDate.setTime(futureDate.getTime());
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
