package in.softment.playrangers.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;
import in.softment.playrangers.Adapter.MyHeaderPagerAdapter;
import in.softment.playrangers.MainActivity;
import in.softment.playrangers.Model.Poll;
import in.softment.playrangers.Model.User;
import in.softment.playrangers.R;
import in.softment.playrangers.SpinnerActivity;
import in.softment.playrangers.Utils.Constants;


public class HomeFragment extends Fragment {
    private final ImageView[] dots = new ImageView[5];
    private ViewPager headerviewpager;
    private int current_pos = 0;
    private LinearLayout dotlayout;
    private int dotcustomposi = 0;
    private final Context context;
    private Handler handler;
    private Runnable runnable;

    private TextView matchesPlayed;
    private TextView winningRatio;
    private TextView won;
    private TextView referralEarning;
    private LinearLayout wholePoll, pollQueView, pollResultView;
    private RadioButton rb1, rb2;
    private TextView polloption_a_text,polloption_b_text;
    private View pollView1, pollView2;
    private TextView polloption_a_score, polloption_b_score;
    private TextView pollQuestion,  pollCount;
    private FirebaseFirestore db;
    public HomeFragment(Context context) {
       this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        //Intialize Home Variables
        matchesPlayed = view.findViewById(R.id.matchesPlayed);
        winningRatio = view.findViewById(R.id.winningRatio);
        won = view.findViewById(R.id.won);
        referralEarning = view.findViewById(R.id.referralEarning);

        //DB INITIALIZE
        db = FirebaseFirestore.getInstance();


        //Slider & DotLayout
        dotlayout = view.findViewById(R.id.dotlayout);
        headerviewpager = view.findViewById(R.id.headerviewpager);
        int[] images = {R.drawable.preview, R.drawable.preview1,R.drawable.preview2,R.drawable.preview3,R.drawable.preview4};
        MyHeaderPagerAdapter myHeaderPagerAdapter = new MyHeaderPagerAdapter(context,images);
        headerviewpager.setAdapter(myHeaderPagerAdapter);
        for (int i = 0 ; i < 5;i++) {
            dots[i] = new ImageView(getContext());
        }
        preparedots(dotcustomposi++);
        headerviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (dotcustomposi > 4) {
                    dotcustomposi = 0;
                }

                preparedots(dotcustomposi++);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //POLL_SECTION

        wholePoll = view.findViewById(R.id.whole_pollview);
        pollQueView = view.findViewById(R.id.pollqueview);
        pollResultView = view.findViewById(R.id.pollresultview);
        RadioGroup pollRadioGroup = view.findViewById(R.id.pollgroup);
        rb1 = view.findViewById(R.id.rb1);
        rb2 = view.findViewById(R.id.rb2);

        pollView1 = view.findViewById(R.id.polloption_a_view);
        pollView2 = view.findViewById(R.id.polloption_b_view);

        polloption_a_text = view.findViewById(R.id.polloption_a_text);
        polloption_b_text = view.findViewById(R.id.polloption_b_text);

        polloption_a_score = view.findViewById(R.id.polloption_a_score);
        polloption_b_score = view.findViewById(R.id.polloption_b_score);

        pollQuestion = view.findViewById(R.id.question);
        pollCount = view.findViewById(R.id.count);





        pollRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                db.collection(Constants.DbPath.user).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("pollId", Poll.data.pollId);

                if (radioGroup.getCheckedRadioButtonId() == rb1.getId()) {
                    setPollResultView("a", Poll.data.getA() + 1, Poll.data.getB());

                    db.collection(Constants.DbPath.user).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("selectedOption", "a").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            incrementPoll("a");
                        }
                    });

                }
                else {
                    setPollResultView("b",Poll.data.getA(), Poll.data.getB() + 1);
                    incrementPoll("b");
                    db.collection(Constants.DbPath.user).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("selectedOption", "b").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            incrementPoll("b");
                        }
                    });
                }

            }
        });






        //PlayWinReward
        ImageView playwinreward = view.findViewById(R.id.playwinrewardimage);
        playwinreward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).switchToPlayPage();
            }
        });

        //Rewardbuttonclicked
        LinearLayout scratchbuttonclicked = view.findViewById(R.id.scratchbuttonclicked);
        scratchbuttonclicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view1 = getLayoutInflater().inflate(R.layout.scratchview,null);
                builder.setView(view1);

               AlertDialog alertDialog = builder.create();
               alertDialog.setCancelable(false);


            ScratchCardLayout scratchCardLayout = view1.findViewById(R.id.scratchCard);
            //Reveal full layout when some percent of the view is scratched
                scratchCardLayout.setRevealFullAtPercent(30);

                //Scratching enable/disable
                scratchCardLayout.setScratchEnabled(true);

                //Remove all scratch made till now
                scratchCardLayout.resetScratch();

                //Reveal scratch card (Shows the layout underneath the scratch)
                scratchCardLayout.revealScratch();
                scratchCardLayout.setScratchListener(new ScratchListener() {
                    @Override
                    public void onScratchStarted() {

                    }

                    @Override
                    public void onScratchProgress(@NotNull ScratchCardLayout scratchCardLayout, int i) {

                    }

                    @Override
                    public void onScratchComplete() {

                        alertDialog.setCancelable(true);

                    }
                });

            alertDialog.show();
            }
        });

        //SpinWheelButtonClicked
        LinearLayout spinWheel = view.findViewById(R.id.spinwheel);
        spinWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SpinnerActivity.class));
            }
        });

        return view;
    }
    private void preparedots(int dotcustomposi) {

        if (dotlayout.getChildCount() > 0) {
            dotlayout.removeAllViews();
        }


        for (int i = 0 ; i < 5;i++) {

            if (i == dotcustomposi) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(context,R.drawable.active_dot));
            }
            else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(context,R.drawable.inactive_dot));
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10,0,10,0);
            dotlayout.addView(dots[i],layoutParams);

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        createSlideShow();

    }

    @Override
    public void onStop() {
        super.onStop();
        removeSlideShow();
    }



    public void removeSlideShow() {
        handler.removeCallbacks(runnable);
    }

    public void createSlideShow() {
        handler  = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (current_pos == 500){
                    current_pos = 0;
                }
                headerviewpager.setCurrentItem(current_pos++, true);

                handler.postDelayed(this,2500);
            }
        };

        handler.postDelayed(runnable,2500);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MainActivity)context).initializeHomeFragment(this);

    }

    public void setHomeFragment() {
       User user = User.data;
       matchesPlayed.setText(String.valueOf(user.getMatchesPlayed()));
       won.setText(String.valueOf(user.getWon()));
       referralEarning.setText(String.valueOf(user.getEarnedByReferral()));

       try {
           int wr = (int) ((user.getWon() / (float)user.getMatchesPlayed()) * 100);
           winningRatio.setText(wr + "%");
       }

       catch (Exception er) {
           winningRatio.setText("0%");
       }

    }

    public void setPoll() {



        Poll poll = Poll.getData();
        if ( poll != null) {
            wholePoll.setVisibility(View.VISIBLE);
            int count = poll.getA() + poll.getB();
            if (count > 0) {
                pollCount.setText(count + " Votes");
            }
            else {
                pollCount.setText("0 Vote");
            }

            if (User.data.getPollId() != null && User.data.getPollId().equalsIgnoreCase(poll.getPollId())) {

                if (User.data.getSelectedOption() != null)
                    setPollResultView(User.data.getSelectedOption(), poll.getA(), poll.getB());
                else {
                    setPollResultView("a", poll.getA(), poll.getB());
                }
            }
            else {

                pollQueView.setVisibility(View.VISIBLE);
                pollResultView.setVisibility(View.GONE);

                pollQuestion.setText(poll.getQuestion());
                rb1.setText(poll.getOptions().get("a"));
                rb2.setText(poll.getOptions().get("b"));



            }

        }
        else {
            wholePoll.setVisibility(View.GONE);
        }
    }

    public void incrementPoll(String option) {

        db.collection("Poll").document(Poll.data.pollId).update(option, FieldValue.increment(1));
    }

    public void setPollResultView(String selectedOption, int a , int b) {



        pollQueView.setVisibility(View.GONE);
        pollResultView.setVisibility(View.VISIBLE);
        pollQuestion.setText(Poll.data.getQuestion());

        if (selectedOption.equalsIgnoreCase("a")) {

            pollView1.setBackgroundResource(R.drawable.pollprogressback);
            pollView2.setBackgroundResource(R.drawable.pollprogressback_trans);


        }
        else {

            pollView2.setBackgroundResource(R.drawable.pollprogressback);
            pollView1.setBackgroundResource(R.drawable.pollprogressback_trans);

        }


        LinearLayout.LayoutParams lay = (LinearLayout.LayoutParams) pollView1.getLayoutParams();

        try {
            lay.weight = (((float)a / (a + b)) * 100);
        }
        catch (Exception e) {

            lay.weight = 0;
        }
        polloption_a_text.setText(Poll.data.getOptions().get("a"));
        polloption_a_score.setText(((int) lay.weight)+"%");


        LinearLayout.LayoutParams lay2 = (LinearLayout.LayoutParams) pollView2.getLayoutParams();
        try {
            lay2.weight =  (((float)b / (a + b)) * 100);
        }
        catch (Exception e) {
            lay2.weight = 0;
        }
        polloption_b_text.setText(Poll.data.getOptions().get("b"));
        polloption_b_score.setText((100 - (int) lay.weight)+ "%");


    }




}