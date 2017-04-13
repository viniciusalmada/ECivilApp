package br.com.viniciusalmada.civilapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.viniciusalmada.civilapp.R;
import br.com.viniciusalmada.civilapp.domains.Syllabus;
import br.com.viniciusalmada.civilapp.utils.ExpanderCollapserView;
import br.com.viniciusalmada.civilapp.utils.GeneralMethods;

/**
 * A simple {@link Fragment} subclass.
 */
public class SyllabusFragment extends Fragment {
    public static final String TAG = "SyllabusFragment";


    private View rootView;
    private List<Syllabus> mSyllabusList;

    public SyllabusFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSyllabusList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_syllabus2, container, false);
        hideSyllabus();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initSyllabusList();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews() {
        TextView tvSyllabus = (TextView) rootView.findViewById(R.id.tv_syllabus_in);
        TextView tvObjectives = (TextView) rootView.findViewById(R.id.tv_objectives_in);
        TextView tvContent = (TextView) rootView.findViewById(R.id.tv_content_in);
        TextView tvRef = (TextView) rootView.findViewById(R.id.tv_ref_in);
        LinearLayout llPicker = (LinearLayout) rootView.findViewById(R.id.ll_picker);
        initCollapseViews(tvSyllabus, tvObjectives, tvContent, tvRef, llPicker);

        rootView.findViewById(R.id.bt_less_syllabus).setOnClickListener(expandView(tvSyllabus));
        rootView.findViewById(R.id.bt_less_objectives).setOnClickListener(expandView(tvObjectives));
        rootView.findViewById(R.id.bt_less_content).setOnClickListener(expandView(tvContent));
        rootView.findViewById(R.id.bt_less_ref).setOnClickListener(expandView(tvRef));
        rootView.findViewById(R.id.bt_pick_syllabus).setOnClickListener(expandView(llPicker));
    }

    private void initCollapseViews(View... views) {
        for (View v : views) {
            ExpanderCollapserView.collapseView(v);
        }
    }

    private void initSyllabusList() {
        DatabaseReference syllabusRef = FirebaseDatabase.getInstance().getReference().child(Syllabus.DR_SYLLABUS);
        syllabusRef.keepSynced(true);
        syllabusRef.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> it = dataSnapshot.getChildren();
                mSyllabusList = new ArrayList<>();
                for (DataSnapshot ds : it) {
                    mSyllabusList.add(ds.getValue(Syllabus.class));
                }
//                Toast.makeText(getActivity(), "Subjects: " + mSyllabusList.size(), Toast.LENGTH_SHORT).show();
                initViews();
                initSpinners();
                showSyllabus();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initSpinners() {
        Spinner spPeriods = (Spinner) rootView.findViewById(R.id.sp_periods);

        ArrayAdapter<CharSequence> adapterPeriods = ArrayAdapter.createFromResource(getActivity(), R.array.mock_periods, R.layout.item_spinner);
        adapterPeriods.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spPeriods.setAdapter(adapterPeriods);
        spPeriods.setOnItemSelectedListener(onSpinnerPeriodClick());

        confSubjectsSpinner(1);
    }

    private void confSubjectsSpinner(int period) {
        List<Syllabus> syllabuses = getmSyllabusListByPeriod(period);
        String[] namesSyllabus = GeneralMethods.getNameFromList(syllabuses);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, namesSyllabus);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        Spinner spSubjects = (Spinner) rootView.findViewById(R.id.sp_subjects);
        spSubjects.setAdapter(adapter);
        spSubjects.setOnItemSelectedListener(onSpinnerSubjectClick(syllabuses));

        setSyllabus(syllabuses.get(0));
    }

    private List<Syllabus> getmSyllabusListByPeriod(int period) {
        List<Syllabus> syllabuses = new ArrayList<>();
        for (Syllabus s : mSyllabusList) {
            if (s.getPeriod() == period) {
                syllabuses.add(s);
            }
        }
        return syllabuses;
    }

    private void setSyllabus(Syllabus syllabus) {
        TextView tvName = (TextView) rootView.findViewById(R.id.tv_name_in);
        TextView tvPeriod = (TextView) rootView.findViewById(R.id.tv_period_in);
        TextView tvCode = (TextView) rootView.findViewById(R.id.tv_code_in);
        TextView tvPrereq = (TextView) rootView.findViewById(R.id.tv_prereq_in);
        TextView tvSemesterLoad = (TextView) rootView.findViewById(R.id.tv_sload_in);
        TextView tvWeeklyLoad = (TextView) rootView.findViewById(R.id.tv_wload_in);
        TextView tvCredits = (TextView) rootView.findViewById(R.id.tv_credits_in);
        TextView tvSyllabus = (TextView) rootView.findViewById(R.id.tv_syllabus_in);
        TextView tvObjectives = (TextView) rootView.findViewById(R.id.tv_objectives_in);
        TextView tvContent = (TextView) rootView.findViewById(R.id.tv_content_in);
        TextView tvRef = (TextView) rootView.findViewById(R.id.tv_ref_in);

        animateTransition(tvName, tvPeriod, tvCode, tvPrereq, tvSemesterLoad, tvWeeklyLoad, tvCredits, tvSyllabus, tvObjectives, tvContent, tvRef);

        tvName.setText(syllabus.getName());
        tvPeriod.setText(String.format("%sº Período", String.valueOf(syllabus.getPeriod())));
        tvCode.setText(syllabus.getCode());
        tvPrereq.setText(Syllabus.getCodeFromList(mSyllabusList, syllabus.getPreReq()));
        tvSemesterLoad.setText(String.format("%s horas", String.valueOf(syllabus.getSemesterLoad())));
        tvWeeklyLoad.setText(String.format("%s horas", String.valueOf(syllabus.getWeeklyLoad())));
        tvCredits.setText(String.valueOf(syllabus.getCredits()));
        tvSyllabus.setText(syllabus.getSyllabus());
        tvObjectives.setText(syllabus.getObjective());
        tvContent.setText(syllabus.getContent());
        tvRef.setText(syllabus.getRefer());

    }

    private void animateTransition(TextView... tvs) {
        for (TextView tv : tvs) {
            YoYo.with(Techniques.Wave)
                    .duration(500)
                    .playOn(tv);
        }
    }

    private void showSyllabus() {
        rootView.findViewById(R.id.ll_container).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.ll_pb).setVisibility(View.GONE);
    }

    private void hideSyllabus() {
        rootView.findViewById(R.id.ll_container).setVisibility(View.GONE);
        rootView.findViewById(R.id.ll_pb).setVisibility(View.VISIBLE);
    }

    private View.OnClickListener collapseView(final View tView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View iButton) {
                ExpanderCollapserView.collapseView(tView);
                ((ImageButton) iButton).setImageResource(R.drawable.ic_expand_more);
                iButton.setOnClickListener(expandView(tView));
            }
        };
    }

    private View.OnClickListener expandView(final View tView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View iButton) {
                ExpanderCollapserView.expandView(tView);
                ((ImageButton) iButton).setImageResource(R.drawable.ic_expand_less);
                iButton.setOnClickListener(collapseView(tView));
            }
        };
    }

    private AdapterView.OnItemSelectedListener onSpinnerPeriodClick() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                confSubjectsSpinner(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private AdapterView.OnItemSelectedListener onSpinnerSubjectClick(final List<Syllabus> list) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSyllabus(list.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }
}
