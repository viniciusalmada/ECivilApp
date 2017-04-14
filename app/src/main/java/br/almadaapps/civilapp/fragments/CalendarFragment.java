package br.almadaapps.civilapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.almadaapps.civilapp.R;
import br.almadaapps.civilapp.adapters.CalendarMonthAdapter;
import br.almadaapps.civilapp.domains.Calendar;
import br.almadaapps.civilapp.interfaces.GetterCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements GetterCalendar {
    public static final String TAG = "CalendarFragment";


    private View rootView;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Calendar.getCalendar(this);
    }

    private void initRecyclerView(List<Calendar> list) {
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_calendar);
        CalendarMonthAdapter adapter = new CalendarMonthAdapter(getActivity(), list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
    }

    @Override
    public void getCalendar(List<Calendar> list) {
        Log.d(TAG, "getCalendar: " + list.get(0));
        initRecyclerView(list);
    }
}
