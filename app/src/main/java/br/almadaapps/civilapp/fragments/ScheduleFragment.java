package br.almadaapps.civilapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import br.almadaapps.civilapp.HomeActivity;
import br.almadaapps.civilapp.IndividualScheduleActivity;
import br.almadaapps.civilapp.LoginActivity;
import br.almadaapps.civilapp.R;
import br.almadaapps.civilapp.adapters.ScheduleAdapter;
import br.almadaapps.civilapp.domains.Schedule;
import br.almadaapps.civilapp.domains.User;
import br.almadaapps.civilapp.interfaces.OnClickTimeTableListenerImpl;
import br.almadaapps.civilapp.utils.AlertLinkExternal;
import co.ceryle.radiorealbutton.library.RadioRealButton;
import co.ceryle.radiorealbutton.library.RadioRealButtonGroup;

public class ScheduleFragment extends Fragment implements RadioRealButtonGroup.OnClickedButtonListener, OnClickTimeTableListenerImpl {

    public static final String TAG = "TimetableFrag";
    public static final int DURATION_TIMETABLE = 49;
    public static final String LINK_SCHEDULES = "https://firebasestorage.googleapis.com/v0/b/ecivil-app.appspot.com/o/Hor%C3%A1rios%20EC%202017.1.PDF?alt=media&token=c42747b8-ff67-43cd-af92-29e028b9d049";

    private View rootView;
    private int mDay = 0;
    private int mPeriod = 1;
    private RadioRealButtonGroup mButtonGroupDays;
    private RadioRealButtonGroup mButtonGroupPeriods;
    private List<Schedule> listComplete;

    public ScheduleFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        setHasOptionsMenu(true);
        initDay();
        initPeriod();

        initViews();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_schedules, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_individual_schedules) {
            Intent intent = new Intent(getActivity(), IndividualScheduleActivity.class);
            intent.putExtra(LoginActivity.KEY_USER_PARCELABLE, ((HomeActivity) getActivity()).getUserLogged());
            getActivity().startActivity(intent);
        } else if (item.getItemId() == R.id.action_individual_schedules_dowload) {
            AlertLinkExternal.openAlertDialog(LINK_SCHEDULES, getActivity(), false);
        }
        return super.onOptionsItemSelected(item);
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

        DatabaseReference timeRef = FirebaseDatabase.getInstance().getReference().child(Schedule.DR_SCHEDULE);
        timeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Schedule>> t = new GenericTypeIndicator<List<Schedule>>() {
                };
                User uLogged = ((HomeActivity) getActivity()).getUserLogged();
                List<Schedule> listTimeTablesGen = dataSnapshot.child("Y2017_1").getValue(t);
                List<Schedule> listTimeTablesUser;

                if (dataSnapshot.child(uLogged.getUid()).hasChildren())
                    listTimeTablesUser = dataSnapshot.child(uLogged.getUid()).getValue(t);
                else
                    listTimeTablesUser = new ArrayList<>();

                listComplete = addLists(listTimeTablesGen, listTimeTablesUser);
                if (listComplete != null) {
                    List<Schedule.TimeLine> timeLines = getLinesFromTimeTable(listComplete);
                    if (timeLines.size() != 0) {
                        if (!rvIsVisible()) showRecyclerView();
                        ScheduleAdapter adapter = new ScheduleAdapter(getActivity(), timeLines, ScheduleFragment.this);
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

    private void setRVSchedule() {
        final RecyclerView rvTimeTable = (RecyclerView) rootView.findViewById(R.id.rv);
        if (listComplete != null) {
            List<Schedule.TimeLine> timeLines = getLinesFromTimeTable(listComplete);
            if (timeLines.size() != 0) {
                if (!rvIsVisible()) showRecyclerView();
                ScheduleAdapter adapter = new ScheduleAdapter(getActivity(), timeLines, ScheduleFragment.this);
                rvTimeTable.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                rvTimeTable.setAdapter(adapter);
            } else {
                showEmptyView();
            }
        } else {
            showEmptyView();
        }
    }

    @SafeVarargs
    private final List<Schedule> addLists(List<Schedule>... lists) {
        List<Schedule> listComplete = new ArrayList<>();
        for (List<Schedule> l : lists) {
            for (Schedule tt : l) {
                listComplete.add(tt);
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

    private List<Schedule.TimeLine> getLinesFromTimeTable(List<Schedule> list) {
        List<Schedule.TimeLine> timeLines = new ArrayList<>();
        for (Schedule tt : list) {
            if (tt.getPeriod() == mPeriod) {
                List<Integer> days = tt.getDay();
                List<Integer> init = tt.getTimeinit();
                for (int i = 0; i < days.size(); i++) {
                    if (days.get(i) == mDay) {
                        String sub = tt.getName();
                        String prof = tt.getProf();
                        String time = Schedule.getStringTimetable(init.get(i), DURATION_TIMETABLE);
                        String code = tt.getCode();
                        int period = tt.getPeriod();
                        int timeinit = init.get(i);
                        timeLines.add(new Schedule.TimeLine(sub, prof, time, code, period, timeinit));
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
        setRVSchedule();
    }

    @Override
    public void onClickTimeTable(View view) {
        Toast.makeText(getActivity(), "click: " + ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
    }
}
