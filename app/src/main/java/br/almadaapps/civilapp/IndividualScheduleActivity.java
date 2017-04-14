package br.almadaapps.civilapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.almadaapps.civilapp.adapters.IndividualScheduleAdapter;
import br.almadaapps.civilapp.domains.Schedule;
import br.almadaapps.civilapp.domains.User;

public class IndividualScheduleActivity extends CommonActivity implements View.OnClickListener {
    public static final String TAG = "IndividualTimetable";
    public static final int INDIVIDUAL_PERIOD = 11;
    private List<Schedule> listGen;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_schedule);
        listGen = new ArrayList<>();
    }

    @Override
    public void initViews() {
        findViewById(R.id.ib_close).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSubjects();
        Intent intent = getIntent();
        user = intent.getParcelableExtra(LoginActivity.KEY_USER_PARCELABLE);
        initRV(user.getUid());
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void hideSubjects() {
        findViewById(R.id.pb_individual).setVisibility(View.VISIBLE);
        findViewById(R.id.rv_sel_individual_subjects).setVisibility(View.GONE);
        findViewById(R.id.bt_save_individual).setEnabled(false);
        findViewById(R.id.ib_close).setEnabled(false);
    }

    private void showSubjects() {
        findViewById(R.id.pb_individual).setVisibility(View.GONE);
        findViewById(R.id.rv_sel_individual_subjects).setVisibility(View.VISIBLE);
        findViewById(R.id.bt_save_individual).setEnabled(true);
        findViewById(R.id.ib_close).setEnabled(true);
    }

    private void initRV(final String uid) {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_sel_individual_subjects);
        DatabaseReference timeRef = FirebaseDatabase.getInstance().getReference().child(Schedule.DR_SCHEDULE);
        timeRef.keepSynced(true);
        timeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Schedule>> t = new GenericTypeIndicator<List<Schedule>>() {
                };
                listGen = dataSnapshot.child("Y2017_1").getValue(t);
                List<Schedule> listUser;
                if (dataSnapshot.child(uid).hasChildren()) {
                    listUser = dataSnapshot.child(uid).getValue(t);
                } else {
                    listUser = new ArrayList<>();
                }

                boolean[] saved = getSavedSubjects(listGen, listUser);


                if (listGen != null && listUser != null) {
                    IndividualScheduleAdapter adapter = new IndividualScheduleAdapter(IndividualScheduleActivity.this, listGen, saved);
                    recyclerView.setLayoutManager(new LinearLayoutManager(IndividualScheduleActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(adapter);
                    showSubjects();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(IndividualScheduleActivity.this, "FirebaseError: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean[] getSavedSubjects(List<Schedule> listGen, List<Schedule> listUser) {
        if (listUser.size() == 0) {
            return new boolean[listGen.size()];
        } else {
            boolean[] saveds = new boolean[listGen.size()];
            for (Schedule tt : listUser) {
                for (int i = 0; i < saveds.length; i++) {
                    if (tt.getCode().equals(listGen.get(i).getCode()))
                        if (!saveds[i])
                            saveds[i] = tt.getCode().equals(listGen.get(i).getCode());
                }
            }
            return saveds;
        }
    }

    public void saveIndividualTimeTable(View v) {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_sel_individual_subjects);
        IndividualScheduleAdapter adapter = (IndividualScheduleAdapter) recyclerView.getAdapter();
        boolean[] savedDisciplines = adapter.getSubCheckeds();

        List<Schedule> listUser = new ArrayList<>();
        for (int i = 0; i < savedDisciplines.length; i++) {
            if (savedDisciplines[i]) {
                Schedule schedule = listGen.get(i);
                schedule.setPeriod(INDIVIDUAL_PERIOD);
                listUser.add(schedule);
            }
        }

        List<String> dayTimeList = new ArrayList<>();
        for (Schedule sc : listUser) {
            List<Schedule.DayTime> dt = Schedule.getDayTime(sc);
            for (Schedule.DayTime sdt : dt) {
                dayTimeList.add(sdt.getDay() + "-" + sdt.getTime());
            }
        }

        Set<String> dayTimeSet = new HashSet<>(dayTimeList);
        if (dayTimeList.size() == dayTimeSet.size()) {
            DatabaseReference individualRef = FirebaseDatabase.getInstance().getReference().child(Schedule.DR_SCHEDULE).child(user.getUid());
            individualRef.setValue(listUser);
            finish();
        } else {
            Toast.makeText(this, "Há disciplinas com horários conflitantes", Toast.LENGTH_SHORT).show();
        }
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sair sem salvar?");
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("NÃO", null);
        builder.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_close) {
            showExitDialog();
        }
    }
}
