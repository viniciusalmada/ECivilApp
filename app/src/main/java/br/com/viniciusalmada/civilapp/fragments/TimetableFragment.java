package br.com.viniciusalmada.civilapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.viniciusalmada.civilapp.R;
import br.com.viniciusalmada.civilapp.adapters.TimeTableAdapter;
import br.com.viniciusalmada.civilapp.domains.TimeTable;
import co.ceryle.radiorealbutton.library.RadioRealButton;
import co.ceryle.radiorealbutton.library.RadioRealButtonGroup;

public class TimetableFragment extends Fragment implements RadioRealButtonGroup.OnClickedButtonListener {

    public static final String TAG = "TimetableFrag";


    private View rootView;
    private int mDay = 3;
    private int mPeriod = 7;

    public TimetableFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_timetable2, container, false);
        initViews();

        return rootView;
    }

    private void initViews() {
        RadioRealButton[] rbPeriods = new RadioRealButton[6];
        rbPeriods[0] = (RadioRealButton) rootView.findViewById(R.id.rb_p1);
        rbPeriods[1] = (RadioRealButton) rootView.findViewById(R.id.rb_p2);
        rbPeriods[2] = (RadioRealButton) rootView.findViewById(R.id.rb_p3);
        rbPeriods[3] = (RadioRealButton) rootView.findViewById(R.id.rb_p4);
        rbPeriods[4] = (RadioRealButton) rootView.findViewById(R.id.rb_p5);
        rbPeriods[5] = (RadioRealButton) rootView.findViewById(R.id.rb_p6);

        RadioRealButton[] rbDays = new RadioRealButton[6];
        rbDays[0] = (RadioRealButton) rootView.findViewById(R.id.rb_d1);
        rbDays[1] = (RadioRealButton) rootView.findViewById(R.id.rb_d2);
        rbDays[2] = (RadioRealButton) rootView.findViewById(R.id.rb_d3);
        rbDays[3] = (RadioRealButton) rootView.findViewById(R.id.rb_d4);
        rbDays[4] = (RadioRealButton) rootView.findViewById(R.id.rb_d5);
        rbDays[5] = (RadioRealButton) rootView.findViewById(R.id.rb_d6);

        RadioRealButtonGroup buttonGroupDays = (RadioRealButtonGroup) rootView.findViewById(R.id.rg_days);
        RadioRealButtonGroup buttonGroupPeriods = (RadioRealButtonGroup) rootView.findViewById(R.id.rg_per);

        buttonGroupDays.setOnClickedButtonListener(this);
        buttonGroupPeriods.setOnClickedButtonListener(this);

//        for (int i = 0; i < rbPeriods.length; i++) {
//            rbPeriods[i].setOnCheckedChangeListener(this);
//            rbDays[i].setOnCheckedChangeListener(this);
//
//            rbPeriods[i].setOnClickListener(this);
//            rbDays[i].setOnClickListener(this);
//        }

        initRVTimeTable();
    }

    private void initRVTimeTable() {
        final RecyclerView rvTimeTable = (RecyclerView) rootView.findViewById(R.id.rv);
        DatabaseReference timeRef = FirebaseDatabase.getInstance().getReference().child(TimeTable.DR_TIMETABLE).child("Y2017_1");
        timeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: P=" + mPeriod + " D=" + mDay + " size=" + dataSnapshot.getChildrenCount());
                GenericTypeIndicator<List<TimeTable>> t = new GenericTypeIndicator<List<TimeTable>>() {
                };
                List<TimeTable> listTimeTables = dataSnapshot.getValue(t);
                if (listTimeTables != null) {
                    List<TimeTable.TimeLine> timeLines = getLinesFromTimeTable(listTimeTables);
                    TimeTableAdapter adapter = new TimeTableAdapter(getActivity(), timeLines);
                    rvTimeTable.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    rvTimeTable.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private List<TimeTable.TimeLine> getLinesFromTimeTable(List<TimeTable> list) {
        List<TimeTable.TimeLine> timeLines = new ArrayList<>();
        for (TimeTable tt : list) {
            if (tt.getPeriod() == mPeriod) {
                List<Integer> days = tt.getDay();
                List<Integer> init = tt.getTimeinit();
                for (int i = 0; i < days.size(); i++) {
                    if (days.get(i) == mDay) {
                        String sub = tt.getName();
                        String prof = tt.getProf();
                        String time = TimeTable.getStringTimetable(init.get(i), 49);
                        String code = tt.getCode();
                        int timeinit = init.get(i);
                        timeLines.add(new TimeTable.TimeLine(sub, prof, time, code, timeinit));
                    }
                }
            }
        }

        return timeLines;
    }

    @Override
    public void onClickedButton(RadioRealButton button, int position) {
        switch (button.getId()) {
            case R.id.rb_d1:
                mDay = 0;
                break;
            case R.id.rb_d2:
                mDay = 1;
                break;
            case R.id.rb_d3:
                mDay = 2;
                break;
            case R.id.rb_d4:
                mDay = 3;
                break;
            case R.id.rb_d5:
                mDay = 4;
                break;
            case R.id.rb_d6:
                mDay = 5;
                break;
            case R.id.rb_p1:
                mPeriod = 1;
                break;
            case R.id.rb_p2:
                mPeriod = 3;
                break;
            case R.id.rb_p3:
                mPeriod = 5;
                break;
            case R.id.rb_p4:
                mPeriod = 7;
                break;
            case R.id.rb_p5:
                mPeriod = 9;
                break;
            case R.id.rb_p6:
                mPeriod = 9;
                break;
        }
        Log.d(TAG, "onClick: D=" + mDay + " P=" + mPeriod);
        initRVTimeTable();
    }
}
