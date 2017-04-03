package br.com.viniciusalmada.civilapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import br.com.viniciusalmada.civilapp.HomeActivity;
import br.com.viniciusalmada.civilapp.R;
import br.com.viniciusalmada.civilapp.adapters.TimeTableAdapter;
import br.com.viniciusalmada.civilapp.domains.TimeTable;
import br.com.viniciusalmada.civilapp.domains.User;
import br.com.viniciusalmada.civilapp.interfaces.OnClickTimeTableListenerImpl;
import co.ceryle.radiorealbutton.library.RadioRealButton;
import co.ceryle.radiorealbutton.library.RadioRealButtonGroup;

public class TimetableFragment extends Fragment implements RadioRealButtonGroup.OnClickedButtonListener, OnClickTimeTableListenerImpl {

    public static final String TAG = "TimetableFrag";

    private View rootView;
    private int mDay = 0;
    private int mPeriod = 1;
    private RadioRealButtonGroup mButtonGroupDays;
    private RadioRealButtonGroup mButtonGroupPeriods;

    public TimetableFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        rootView = inflater.inflate(R.layout.fragment_timetable2, container, false);
        initDay();
        initPeriod();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        initViews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        User uLogged = ((HomeActivity) getActivity()).getUserLogged();
        DatabaseReference periodRef = FirebaseDatabase.getInstance().getReference().child(User.DR_USERS);
        Map<String, Object> map = uLogged.toMap();
        map.put("period", mPeriod);
        periodRef.child(uLogged.getUid()).updateChildren(map);
        uLogged.setPeriod(mPeriod);
        ((HomeActivity) getActivity()).setUserLogged(uLogged);
    }

    private void initDay() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        mDay = day == 1 ? 0 : day - 2;
    }

    private void initPeriod() {
        User uLogged = ((HomeActivity) getActivity()).getUserLogged();
        if (uLogged.getPeriod() != 0) {
            mPeriod = uLogged.getPeriod();
        } else {
            mPeriod = 1;
        }
    }

    private void initViews() {

        mButtonGroupDays = (RadioRealButtonGroup) rootView.findViewById(R.id.rg_days);
        mButtonGroupDays.setPosition(mDay, true);

        mButtonGroupPeriods = (RadioRealButtonGroup) rootView.findViewById(R.id.rg_per);
        mButtonGroupPeriods.setPosition((mPeriod - 1) / 2, true);

        mButtonGroupDays.setOnClickedButtonListener(this);
        mButtonGroupPeriods.setOnClickedButtonListener(this);

        initRVTimeTable();
    }

    private void initRVTimeTable() {
        final RecyclerView rvTimeTable = (RecyclerView) rootView.findViewById(R.id.rv);

        DatabaseReference timeRef = FirebaseDatabase.getInstance().getReference().child(TimeTable.DR_TIMETABLE);
        timeRef.keepSynced(true);
        timeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<TimeTable>> t = new GenericTypeIndicator<List<TimeTable>>() {
                };
                User uLogged = ((HomeActivity) getActivity()).getUserLogged();
                List<TimeTable> listTimeTablesGen = dataSnapshot.child("Y2017_1").getValue(t);
                List<TimeTable> listTimeTablesUser;

                if (dataSnapshot.child(uLogged.getUid()).hasChildren())
                    listTimeTablesUser = dataSnapshot.child(uLogged.getUid()).getValue(t);
                else
                    listTimeTablesUser = new ArrayList<>();

                List<TimeTable> listComplete = addLists(listTimeTablesGen, listTimeTablesUser);
                if (listComplete != null) {
                    List<TimeTable.TimeLine> timeLines = getLinesFromTimeTable(listComplete);
                    if (timeLines.size() != 0) {
                        if (!rvIsVisible()) showRecyclerView();
                        TimeTableAdapter adapter = new TimeTableAdapter(getActivity(), timeLines, TimetableFragment.this);
                        rvTimeTable.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        rvTimeTable.setAdapter(adapter);
                    } else {
                        showEmptyView();
                    }
                } else {
                    showEmptyView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @SafeVarargs
    private final List<TimeTable> addLists(List<TimeTable>... lists) {
        List<TimeTable> listComplete = new ArrayList<>();
        for (List<TimeTable> l : lists) {
            for (TimeTable tt : l) {
                listComplete.add(tt);
                Log.d(TAG, "addLists: tt" + tt.getName());
            }
        }
        return listComplete;
    }

    private void showEmptyView() {
        rootView.findViewById(R.id.rv).setVisibility(View.GONE);
        rootView.findViewById(R.id.tv_empty).setVisibility(View.VISIBLE);
        YoYo.with(Techniques.BounceInLeft)
                .duration(800)
                .playOn(rootView.findViewById(R.id.tv_empty));
    }

    private void showRecyclerView() {
        rootView.findViewById(R.id.tv_empty).setVisibility(View.GONE);
        rootView.findViewById(R.id.rv).setVisibility(View.VISIBLE);
        YoYo.with(Techniques.BounceInRight)
                .duration(1000)
                .playOn(rootView.findViewById(R.id.rv));
    }

    private boolean rvIsVisible() {
        return rootView.findViewById(R.id.rv).getVisibility() == View.VISIBLE;
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
                        int period = tt.getPeriod();
                        int timeinit = init.get(i);
                        timeLines.add(new TimeTable.TimeLine(sub, prof, time, code, period, timeinit));
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
            case R.id.rb_hi:
                mPeriod = 11;
                break;
        }
        Log.d(TAG, "onClick: D=" + mDay + " P=" + mPeriod);
        initRVTimeTable();
    }

    @Override
    public void onClickTimeTable(View view) {
        Toast.makeText(getActivity(), "click: " + ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
    }
}
