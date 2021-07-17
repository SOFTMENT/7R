package in.softment.playrangers.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import in.softment.playrangers.MainActivity;
import in.softment.playrangers.Model.FreeFireGameModel;
import in.softment.playrangers.R;
import in.softment.playrangers.Services.Service;
import in.softment.playrangers.Utils.Constants;

public class FreeFireFragment extends Fragment {

    private Context context;
    private AppCompatButton ongoing, upcoming, results;

    public FreeFireFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_free_fire, container, false);

        ViewPager viewPager = view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        ongoing = view.findViewById(R.id.ongoing);
        upcoming = view.findViewById(R.id.upcoming);
        results = view.findViewById(R.id.results);

        ongoing.setTag("unselected");
        upcoming.setTag("selected");
        results.setTag("unselected");

        viewPager.setCurrentItem(1);

        ongoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ongoing.getTag().toString().equals("unselected")) {
                    ongoing.setTag("selected");
                    upcoming.setTag("unselected");
                    results.setTag("unselected");
                    ongoing.setBackgroundResource(R.drawable.btnbackred);
                    upcoming.setBackgroundResource(R.drawable.btnback);
                    results.setBackgroundResource(R.drawable.btnback);
                    viewPager.setCurrentItem(0);
                }

            }
        });

        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (upcoming.getTag().toString().equals("unselected")) {
                    ongoing.setTag("unselected");
                    upcoming.setTag("selected");
                    results.setTag("unselected");
                    ongoing.setBackgroundResource(R.drawable.btnback);
                    upcoming.setBackgroundResource(R.drawable.btnbackred);
                    results.setBackgroundResource(R.drawable.btnback);
                    viewPager.setCurrentItem(1);
                }

            }
        });

        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (results.getTag().toString().equals("unselected")) {
                    ongoing.setTag("unselected");
                    upcoming.setTag("unselected");
                    results.setTag("selected");
                    ongoing.setBackgroundResource(R.drawable.btnback);
                    upcoming.setBackgroundResource(R.drawable.btnback);
                    results.setBackgroundResource(R.drawable.btnbackred);
                    viewPager.setCurrentItem(2);
                }

            }
        });

       ((MainActivity)context).getFreeFireEventDetails();

        return view;
    }




    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new FreeFireOnGoingFragment());
        adapter.addFrag(new FreeFireUpComingFragment(context));
        adapter.addFrag(new FreeFireResults());
        viewPager.setAdapter(adapter);
    }


    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        final List<Fragment> mFragmentList = new ArrayList<>();
        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {

            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);

        }


    }
}


