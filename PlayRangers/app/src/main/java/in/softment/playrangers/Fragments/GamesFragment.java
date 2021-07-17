package in.softment.playrangers.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import in.softment.playrangers.Adapter.MyHeaderPagerAdapter;
import in.softment.playrangers.MainActivity;
import in.softment.playrangers.R;


public class GamesFragment extends Fragment {

    private ViewPager headerviewpager;
    private int current_pos = 0;
    private LinearLayout dotlayout;
    private int dotcustomposi = 0;
    private final Context context;
    private final ImageView[] dots = new ImageView[5];
   // private PlayFragment playFragment;

    public GamesFragment(Context context) {

        this.context = context;
        //this.playFragment = playFragment;

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_games, container, false);

        //Slider & DotLayout
        dotlayout = view.findViewById(R.id.dotlayout);
        headerviewpager = view.findViewById(R.id.headerviewpager);
        int[] images = {R.drawable.preview, R.drawable.preview1,R.drawable.preview2,R.drawable.preview3,R.drawable.preview4};
        MyHeaderPagerAdapter myHeaderPagerAdapter = new MyHeaderPagerAdapter(context,images);
        headerviewpager.setAdapter(myHeaderPagerAdapter);

        for (int i = 0 ; i < 5;i++) {
            dots[i] = new ImageView(getContext());
        }
        createSlideShow();
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

//        //FreeFireViewSelected
//        view.findViewById(R.id.freefire_rl).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                playFragment.replaceFragment("freefire");
//            }
//        });


        //CricketViewSelected
        view.findViewById(R.id.cricket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment("cricket");

            }
        });
        return view;
    }


    public void replaceFragment(String game) {
        if (game.equals("cricket")) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().add(R.id.main_container,new CricketFragment(context));
            ft.addToBackStack(null);
            ft.commit();

        }
        else if (game.equals("ludo")) {

        }
        else if (game.equals("freefire"))  {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().add(R.id.main_container,new FreeFireFragment(context));
            ft.addToBackStack(null);
            ft.commit();

        }


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

    public void createSlideShow() {
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
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




}