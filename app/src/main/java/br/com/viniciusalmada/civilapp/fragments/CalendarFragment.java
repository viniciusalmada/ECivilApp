package br.com.viniciusalmada.civilapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.viniciusalmada.civilapp.R;
import br.com.viniciusalmada.civilapp.adapters.CalendarMonthAdapter;
import br.com.viniciusalmada.civilapp.domains.Calendar;
import br.com.viniciusalmada.civilapp.interfaces.GetterCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements GetterCalendar {


    private View rootView;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        Calendar.getCalendar(this);
        return rootView;
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
        initRecyclerView(list);
    }
}
