package br.almadaapps.civilapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.almadaapps.civilapp.R;
import br.almadaapps.civilapp.adapters.CalendarMonthAdapter;
import br.almadaapps.civilapp.domains.Calendar;
import br.almadaapps.civilapp.interfaces.GetterCalendar;
import br.almadaapps.civilapp.utils.AlertLinkExternal;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements GetterCalendar {
    public static final String TAG = "CalendarFragment";
    private static final String LINK_CALENDAR = "https://firebasestorage.googleapis.com/v0/b/ecivil-app.appspot.com/o/Calendario%20Acad%C3%AAmico%20Ensino%20Superior%202017.pdf?alt=media&token=056d0d37-0817-4d50-996d-e781690eb1d8";


    private View rootView;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Calendar.getCalendar(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calendar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_calendar_dowload) {
            AlertLinkExternal.openAlertDialog(LINK_CALENDAR, getActivity(), false);
        }
        return super.onOptionsItemSelected(item);
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
