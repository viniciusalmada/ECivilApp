package br.com.viniciusalmada.civilapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.viniciusalmada.civilapp.adapters.MonographsAdapter;
import br.com.viniciusalmada.civilapp.domains.Monograph;
import br.com.viniciusalmada.civilapp.interfaces.GetterMonographs;

public class MonographsFragment extends Fragment implements GetterMonographs {

    private View rootView;
    private List<Monograph> monographList;
    private String title = "TÍTULO";
    private String author = "AUTOR";
    private boolean sortByTitle = true;
    private Menu menu;

    public MonographsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.fragment_monographs, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Monograph.getMonographs(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_monographs, menu);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*if (item.getItemId() == R.id.action_sort_title) {

            return true;
        } else if (item.getItemId() == R.id.action_sort_author) {
            initRV(monographList, false);
            return true;
        }*/
        if (item.getItemId() == R.id.action_sort) {
            sortByTitle = !sortByTitle;
            updateMenuTitles(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRV(List<Monograph> monographs, boolean sortByTitle) {
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv_monographs);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        MonographsAdapter adapter = new MonographsAdapter(getActivity(), monographs, sortByTitle);
        rv.setAdapter(adapter);
        rv.setLayoutManager(llm);
    }

    private void updateMenuTitles(MenuItem item) {
        if (sortByTitle) {
            item.setTitle(title);
            initRV(monographList, sortByTitle);
        } else {
            item.setTitle(author);
            initRV(monographList, sortByTitle);
        }
    }

    @Override
    public void getMonographs(List<Monograph> monographs) {
        monographList = monographs;
        initRV(monographs, true);
    }
}
