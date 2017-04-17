package br.almadaapps.civilapp.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.almadaapps.civilapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LinksFragment extends Fragment implements View.OnClickListener {

    private String linkIFMA = "http://portal.ifma.edu.br/";
    private String linkQAcad = "https://qacad.ifma.edu.br/";
    private String linkLibrary = "http://acervo.ifma.edu.br/sophia_web/";
    private String linkSUAP = "https://suap.ifma.edu.br/accounts/login/?next=/";
    private String linkDrive = "https://drive.google.com/drive/folders/0B2GZwIAg0MerckM2ZjRzZEtZd2s";
    private String linkSchedule = "https://firebasestorage.googleapis.com/v0/b/ecivil-app.appspot.com/o/Hor%C3%A1rios%20EC%202017.1.PDF?alt=media&token=c42747b8-ff67-43cd-af92-29e028b9d049";
    private String linkCalendar = "https://firebasestorage.googleapis.com/v0/b/ecivil-app.appspot.com/o/Calendario%20Acad%C3%AAmico%20Ensino%20Superior%202017.pdf?alt=media&token=056d0d37-0817-4d50-996d-e781690eb1d8";
    private String linkProject = "https://firebasestorage.googleapis.com/v0/b/ecivil-app.appspot.com/o/PROJETO_PEDAGOGICO_ENG_CIVIL.pdf?alt=media&token=fb17db31-8e40-4237-873b-92b7d7af0216";
    private String linkFluxogram = "https://firebasestorage.googleapis.com/v0/b/ecivil-app.appspot.com/o/fluxograma.png?alt=media&token=5c08ef0e-6fc8-46ce-a1db-51287bcad6ae";
    private View rootView;

    public LinksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_links, container, false);
        initViews();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("links")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        linkIFMA = dataSnapshot.child("ifma").getValue(String.class);
                        linkQAcad = dataSnapshot.child("qacad").getValue(String.class);
                        linkLibrary = dataSnapshot.child("library").getValue(String.class);
                        linkSUAP = dataSnapshot.child("suap").getValue(String.class);
                        linkDrive = dataSnapshot.child("drive").getValue(String.class);
                        linkSchedule = dataSnapshot.child("schedule").getValue(String.class);
                        linkCalendar = dataSnapshot.child("calendar").getValue(String.class);
                        linkProject = dataSnapshot.child("project").getValue(String.class);
                        linkFluxogram = dataSnapshot.child("fluxogram").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void initViews() {
        rootView.findViewById(R.id.bt_ifma).setOnClickListener(this);
        rootView.findViewById(R.id.bt_qacad).setOnClickListener(this);
        rootView.findViewById(R.id.bt_library).setOnClickListener(this);
        rootView.findViewById(R.id.bt_suap).setOnClickListener(this);
        rootView.findViewById(R.id.bt_drive).setOnClickListener(this);
        rootView.findViewById(R.id.bt_schedules).setOnClickListener(this);
        rootView.findViewById(R.id.bt_calendar).setOnClickListener(this);
        rootView.findViewById(R.id.bt_project).setOnClickListener(this);
        rootView.findViewById(R.id.bt_fluxogram).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String link = "www.google.com.br";
        switch (v.getId()) {
            case R.id.bt_ifma:
                link = linkIFMA;
                break;
            case R.id.bt_qacad:
                link = linkQAcad;
                break;
            case R.id.bt_library:
                link = linkLibrary;
                break;
            case R.id.bt_suap:
                link = linkSUAP;
                break;
            case R.id.bt_drive:
                link = linkDrive;
                break;
            case R.id.bt_schedules:
                link = linkSchedule;
                break;
            case R.id.bt_calendar:
                link = linkCalendar;
                break;
            case R.id.bt_project:
                link = linkProject;
                break;
            case R.id.bt_fluxogram:
                link = linkFluxogram;
                break;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        getActivity().startActivity(intent);
    }
}
