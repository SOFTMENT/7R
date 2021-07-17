package in.softment.playrangers.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import in.softment.playrangers.Adapter.CricketOnGoingAdapter;
import in.softment.playrangers.Model.CricketMatchDetailsModel;
import in.softment.playrangers.R;
import in.softment.playrangers.Services.Service;
import in.softment.playrangers.Utils.Constants;

public class CricketFragment extends Fragment {

    private Context context;
    private AppCompatButton ongoing, upcoming, results;
    private CricketUpcomingFragment cricketUpcomingFragment;
    private CricketOnGoingFragment cricketOnGoingFragment;

    public CricketFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cricket, container, false);

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

        //getAllMatchsData
        getAllMatchesData();


        return view;
    }




    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new CricketOnGoingFragment(context,this));
        adapter.addFrag(new CricketUpcomingFragment(context,this));
        adapter.addFrag(new CricketResultFragment());
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

    public void getAllMatchesData() {
        FirebaseFirestore.getInstance().collection(Constants.DbPath.cricket).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    CricketMatchDetailsModel.cricketMatchDetailsModels.clear();
                    for (QueryDocumentSnapshot querySnapshot : task.getResult()) {
                      CricketMatchDetailsModel cricketMatchDetailsModel = querySnapshot.toObject(CricketMatchDetailsModel.class);
                      CricketMatchDetailsModel.cricketMatchDetailsModels.add(cricketMatchDetailsModel);

                  }

                    cricketUpcomingFragment.notifyAdapter();
                    //cricketOnGoingFragment.notifyAdapter();


                }
                else {
                    Service.showErrorDialog(context,"Error",task.getException().getMessage());
                }
            }
        });
    }

    public void initializeUpcomingFragment(CricketUpcomingFragment cricketUpcomingFragment) {
        this.cricketUpcomingFragment = cricketUpcomingFragment;

    }

    public void initializeOnGoingFragment(CricketOnGoingFragment cricketOnGoingFragment) {
        this.cricketOnGoingFragment = cricketOnGoingFragment;
    }


}

