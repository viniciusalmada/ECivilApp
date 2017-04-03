package br.com.viniciusalmada.civilapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.viniciusalmada.civilapp.adapters.IndividualTimetableAdapter;
import br.com.viniciusalmada.civilapp.domains.TimeTable;
import br.com.viniciusalmada.civilapp.domains.User;

public class IndividualTimetableActivity extends CommonActivity {
    public static final String TAG = "IndividualTimetable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_timetable);
    }

    @Override
    public void initViews() {
        Toolbar tb = (Toolbar) findViewById(R.id.tb_individual);
        setSupportActionBar(tb);
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
        User user = intent.getParcelableExtra(LoginActivity.KEY_USER_PARCELABLE);
        initRV(user.getUid());
    }

    private void hideSubjects() {
        findViewById(R.id.pb_individual).setVisibility(View.VISIBLE);
        findViewById(R.id.rv_sel_individual_subjects).setVisibility(View.GONE);
        findViewById(R.id.bt_save_individual).setEnabled(false);
    }

    private void showSubjects() {
        findViewById(R.id.pb_individual).setVisibility(View.GONE);
        findViewById(R.id.rv_sel_individual_subjects).setVisibility(View.VISIBLE);
        findViewById(R.id.bt_save_individual).setEnabled(true);
    }

    private void initRV(final String uid) {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_sel_individual_subjects);
        DatabaseReference timeRef = FirebaseDatabase.getInstance().getReference().child(TimeTable.DR_TIMETABLE);
        timeRef.keepSynced(true);
        timeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<TimeTable>> t = new GenericTypeIndicator<List<TimeTable>>() {
                };
                List<TimeTable> listGen = dataSnapshot.child("Y2017_1").getValue(t);
                List<TimeTable> listUser;
                if (dataSnapshot.child(uid).hasChildren()) {
                    listUser = dataSnapshot.child(uid).getValue(t);
                } else {
                    listUser = new ArrayList<>();
                }

                boolean[] saved = getSavedSubjects(listGen, listUser);


                if (listGen != null && listUser != null) {
                    IndividualTimetableAdapter adapter = new IndividualTimetableAdapter(IndividualTimetableActivity.this, listGen, saved);
                    recyclerView.setLayoutManager(new LinearLayoutManager(IndividualTimetableActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(adapter);
                    showSubjects();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(IndividualTimetableActivity.this, "FirebaseError: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean[] getSavedSubjects(List<TimeTable> listGen, List<TimeTable> listUser) {
        if (listUser.size() == 0) {
            return new boolean[listGen.size()];
        } else {
            boolean[] saveds = new boolean[listGen.size()];
            for (TimeTable tt : listUser) {
                for (int i = 0; i < saveds.length; i++) {
                    if (tt.getCode().equals(listGen.get(i).getCode()))
                        if (!saveds[i])
                            saveds[i] = tt.getCode().equals(listGen.get(i).getCode());
                }
            }
            Log.d(TAG, "getSavedSubjects: " + Arrays.toString(saveds));
            return saveds;
        }
    }

    public void saveIndividualTimeTable(View v) {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_sel_individual_subjects);
        IndividualTimetableAdapter adapter = (IndividualTimetableAdapter) recyclerView.getAdapter();
        Log.d(TAG, "saveIndividualTimeTable: " + Arrays.toString(adapter.getSubCheckeds()));
    }
}
