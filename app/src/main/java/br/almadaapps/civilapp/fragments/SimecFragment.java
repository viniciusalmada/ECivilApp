package br.almadaapps.civilapp.fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.almadaapps.civilapp.R;
import br.almadaapps.civilapp.adapters.ContactsSheetAdapter;
import br.almadaapps.civilapp.domains.Minicurso;
import br.almadaapps.civilapp.domains.Palestra;
import br.almadaapps.civilapp.interfaces.GetterMinicurso;
import br.almadaapps.civilapp.interfaces.GetterPalestra;
import br.almadaapps.civilapp.utils.AlertLinkExternal;
import br.almadaapps.civilapp.utils.CustomTypefaceSpan;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimecFragment extends Fragment implements GetterPalestra, GetterMinicurso, View.OnClickListener, AdapterView.OnItemClickListener {


    private View rootView;
    private String description = "(Informações não puderam ser carregadas por um erro de conexão)";
    private BottomSheetBehavior sheetBehavior;
    private String email = "caec.montecastelo@ifma.edu.br";
    private String telephone = "tel:98982664942";
    private String facebook = "http://www.facebook.com/simecma";
    private String instagram = "http://instagram.com/_u/simec_ifma";

    public SimecFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_simec, container, false);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Palestra.getPalestras(this);
        Minicurso.getMinicursos(this);
        initBottonSheet();
        (rootView.findViewById(R.id.bt_sign_up)).setOnClickListener(this);
        DatabaseReference simecRef = FirebaseDatabase.getInstance().getReference().child("SIMEC/description");
        simecRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                description = dataSnapshot.child("info").getValue(String.class);
                email = dataSnapshot.child("email").getValue(String.class);
                telephone = "tel:" + dataSnapshot.child("tel").getValue();
                facebook = dataSnapshot.child("facebook").getValue(String.class);
                instagram = dataSnapshot.child("instagram").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_simec, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            AlertLinkExternal.openAlertDialog(description, getActivity());
            return true;

        } else if (item.getItemId() == R.id.action_contact) {
//            Toast.makeText(getActivity(), "Contatos", Toast.LENGTH_SHORT).show();
//            AlertLinkExternal.openContactDialog(getActivity(), this);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPalestras(List<Palestra> list) {
        Collections.sort(list, new Comparator<Palestra>() {
            @Override
            public int compare(Palestra o1, Palestra o2) {
                int res = o1.getDay().compareTo(o2.getDay());
                if (res == 0)
                    res = o1.getInit().compareTo(o2.getInit());
                return res;
            }
        });
        SpannableStringBuilder[] stringBuilders = new SpannableStringBuilder[5];
        for (int i = 0; i < stringBuilders.length; i++)
            stringBuilders[i] = new SpannableStringBuilder();


        for (Palestra p : list) {
            stringBuilders[p.getDay()].append(getSpannablePalestraToTextView(p));
            stringBuilders[p.getDay()].append("\n\n");
        }

        ((TextView) rootView.findViewById(R.id.tv_pal_1_day)).setText(stringBuilders[0], TextView.BufferType.SPANNABLE);
        ((TextView) rootView.findViewById(R.id.tv_pal_2_day)).setText(stringBuilders[1], TextView.BufferType.SPANNABLE);
        ((TextView) rootView.findViewById(R.id.tv_pal_3_day)).setText(stringBuilders[2], TextView.BufferType.SPANNABLE);
        ((TextView) rootView.findViewById(R.id.tv_pal_4_day)).setText(stringBuilders[3], TextView.BufferType.SPANNABLE);
        ((TextView) rootView.findViewById(R.id.tv_pal_5_day)).setText(stringBuilders[4], TextView.BufferType.SPANNABLE);
    }

    private SpannableStringBuilder getSpannablePalestraToTextView(Palestra palestra) {
        SpannableString time = new SpannableString(palestra.getTime());

        SpannableString title;
        if (palestra.getTitle().equals("[Não informado]"))
            title = new SpannableString("Palestra ainda sem título");
        else
            title = new SpannableString("\"" + palestra.getTitle() + "\"");

        SpannableString author = new SpannableString(palestra.getPalestrante());

        Typeface tfCondReg = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        Typeface tfCondBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoCondensed-Bold.ttf");

        time.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.colorTimePalestra)),
                0, time.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        );
        time.setSpan(
                new CustomTypefaceSpan("", tfCondReg),
                0, time.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        );
        title.setSpan(
                new CustomTypefaceSpan("", tfCondBold),
                0, title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        );
        title.setSpan(
                new RelativeSizeSpan(1.2f),
                0, title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        );

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(time);
        builder.append("\n");
        builder.append(title);
        builder.append("\n");
        builder.append(author);

        return builder;
    }

    private void initMinicursos(List<Minicurso> list) {
        Collections.sort(list, new Comparator<Minicurso>() {
            @Override
            public int compare(Minicurso o1, Minicurso o2) {
                int res = o1.getDayinit().compareTo(o2.getDayinit());
                if (res == 0)
                    res = o1.getTimeinit().compareTo(o2.getTimeinit());
                return res;
            }
        });
        SpannableStringBuilder[] stringBuilders = new SpannableStringBuilder[5];
        for (int i = 0; i < stringBuilders.length; i++)
            stringBuilders[i] = new SpannableStringBuilder();


        for (int i = 0; i < stringBuilders.length; i++) {
            stringBuilders[i].append(getSpannableMinicursoToTextView(list.get(i)));
            stringBuilders[i].append("\n\n");
        }

        ((TextView) rootView.findViewById(R.id.tv_mc_1)).setText(stringBuilders[0], TextView.BufferType.SPANNABLE);
        ((TextView) rootView.findViewById(R.id.tv_mc_2)).setText(stringBuilders[1], TextView.BufferType.SPANNABLE);
        ((TextView) rootView.findViewById(R.id.tv_mc_3)).setText(stringBuilders[2], TextView.BufferType.SPANNABLE);
        ((TextView) rootView.findViewById(R.id.tv_mc_4)).setText(stringBuilders[3], TextView.BufferType.SPANNABLE);
        ((TextView) rootView.findViewById(R.id.tv_mc_5)).setText(stringBuilders[4], TextView.BufferType.SPANNABLE);
    }

    private SpannableStringBuilder getSpannableMinicursoToTextView(Minicurso minicurso) {
        int dayInit = minicurso.getDayinit();
        int dayEnd = minicurso.getDayend();
        int timeInit = minicurso.getTimeinit();
        int timeEnd = minicurso.getEndtime();

        Typeface tfCondReg = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        Typeface tfCondBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoCondensed-Bold.ttf");

        //parte única
        if (dayInit == dayEnd && timeEnd == timeInit) {
            SpannableString title = new SpannableString("\"" + minicurso.getTitle() + "\"");
            SpannableString time = new SpannableString(minicurso.getTime(timeInit, dayInit));
            SpannableString author = new SpannableString(minicurso.getAuthor());

            title.setSpan(
                    new CustomTypefaceSpan("", tfCondBold),
                    0, title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            );
            title.setSpan(
                    new RelativeSizeSpan(1.2f),
                    0, title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            );
            time.setSpan(
                    new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.colorTimePalestra)),
                    0, time.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            );
            time.setSpan(
                    new CustomTypefaceSpan("", tfCondReg),
                    0, time.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            );

            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(time);
            builder.append("\n");
            builder.append(title);
            builder.append("\n");
            builder.append(author);

            return builder;
        } else {
            SpannableString title = new SpannableString("\"" + minicurso.getTitle() + "\"");
            SpannableString time1 = new SpannableString("Parte 1 - " + minicurso.getTime(timeInit, dayInit));
            SpannableString time2 = new SpannableString("Parte 2 - " + minicurso.getTime(timeEnd, dayEnd));
            SpannableString author = new SpannableString(minicurso.getAuthor());
            title.setSpan(
                    new CustomTypefaceSpan("", tfCondBold),
                    0, title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            );
            title.setSpan(
                    new RelativeSizeSpan(1.2f),
                    0, title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            );
            time1.setSpan(
                    new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.colorTimePalestra)),
                    0, time1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            );
            time1.setSpan(
                    new CustomTypefaceSpan("", tfCondReg),
                    0, time1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            );
            time2.setSpan(
                    new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.colorTimePalestra)),
                    0, time2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            );
            time2.setSpan(
                    new CustomTypefaceSpan("", tfCondReg),
                    0, time2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            );
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(time1);
            builder.append("\n");
            builder.append(time2);
            builder.append("\n");
            builder.append(title);
            builder.append("\n");
            builder.append(author);

            return builder;
        }
    }

    private void initBottonSheet() {
        View bottomSheet = getActivity().findViewById(R.id.view_botton_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_SETTLING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_HIDDEN");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        ListView listView = (ListView) getActivity().findViewById(R.id.lv_contact);
//        ArrayAdapter<CharSequence> a = ArrayAdapter.createFromResource(getActivity(),R.array.months, android.R.layout.simple_list_item_1);
        ContactsSheetAdapter adapter = new ContactsSheetAdapter(getActivity());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void getPalestra(List<Palestra> list) {
        initPalestras(list);
    }

    @Override
    public void getMinicursos(List<Minicurso> list) {
        initMinicursos(list);
    }

    @Override
    public void onClick(View v) {
        AlertLinkExternal.openAlertDialog("https://www.simecifma.com/", getActivity(), true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                break;
            case 1:
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(telephone));
                break;
            case 2:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(facebook));
                break;
            case 3:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(instagram));
                break;
        }
        try {
            if (intent != null)
                startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(instagram)));
        }
    }
}