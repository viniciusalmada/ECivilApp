package br.com.viniciusalmada.civilapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.viniciusalmada.civilapp.R;
import br.com.viniciusalmada.civilapp.domains.Syllabus;

/**
 * Created by vinicius-almada on 30/03/17.
 */

public class PickerSyllabusDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private View rootView;
    private List<Syllabus> mSyllabusList;
    private Spinner mSpSubjects;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSyllabusList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("Escolha a disciplina");
//        rootView = inflater.inflate(R.layout.dialog_pick_syllabus, container, false);
        initSyllabusList();

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

    private void initSyllabusList() {
        DatabaseReference syllabusRef = FirebaseDatabase.getInstance().getReference().child(Syllabus.DR_SYLLABUS);
        syllabusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> it = dataSnapshot.getChildren();
                for (DataSnapshot ds : it) {
                    mSyllabusList.add(ds.getValue(Syllabus.class));
                }
                Toast.makeText(getActivity(), "Subjects: " + mSyllabusList.size(), Toast.LENGTH_SHORT).show();
                initSpinners();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initSpinners() {
        Spinner spPeriods = (Spinner) rootView.findViewById(R.id.sp_periods);
        mSpSubjects = (Spinner) rootView.findViewById(R.id.sp_subjects);

        ArrayAdapter<CharSequence> adapterPeriods = ArrayAdapter.createFromResource(getActivity(), R.array.mock_periods, R.layout.item_spinner);
        adapterPeriods.setDropDownViewResource(R.layout.item_spinner_dropdown);
        spPeriods.setAdapter(adapterPeriods);

        confSubjects(0);

        spPeriods.setOnItemSelectedListener(this);
    }

    private List<Syllabus> getmSyllabusListByPeriod(int period) {
        List<Syllabus> syllabuses = new ArrayList<>();
        for (Syllabus s : mSyllabusList) {
            if (s.getPeriod() == period + 1) {
                syllabuses.add(s);
            }
        }
        return syllabuses;
    }

    private List<String> getNames(List<Syllabus> syllabuses) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < syllabuses.size(); i++) {
            strings.add(syllabuses.get(i).getName());
        }
        return strings;
    }

    private void confSubjects(int position) {
        List<Syllabus> syllabuses = getmSyllabusListByPeriod(position);
        List<String> names = getNames(syllabuses);
        SubjectsAdapter adapter = new SubjectsAdapter(getActivity(), 0, names);
        mSpSubjects.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.sp_periods) {
            confSubjects(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class SubjectsAdapter extends ArrayAdapter<String> {
        private List<String> objects;
        private Context context;

        public SubjectsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
            super(context, 0, objects);
            this.context = context;
            this.objects = objects;
            setDropDownViewResource(R.layout.item_spinner_dropdown);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            VH holder;
            if (convertView == null) {
                holder = new VH();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner, null, false);
                holder.textView = (TextView) convertView.findViewById(R.id.tv_spinner);
                convertView.setTag(holder);
            } else {
                holder = (VH) convertView.getTag();
            }

            holder.textView.setText(objects.get(position));

            return convertView;
        }

        private class VH {
            TextView textView;
        }
    }
}
