package br.almadaapps.civilapp.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.almadaapps.civilapp.R;
import br.almadaapps.civilapp.domains.Minicurso;
import br.almadaapps.civilapp.domains.Palestra;
import br.almadaapps.civilapp.interfaces.GetterMinicurso;
import br.almadaapps.civilapp.interfaces.GetterPalestra;
import br.almadaapps.civilapp.utils.AlertLinkExternal;
import br.almadaapps.civilapp.utils.CustomTypefaceSpan;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimecFragment extends Fragment implements GetterPalestra, GetterMinicurso, View.OnClickListener {


    private View rootView;

    public SimecFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_simec, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Palestra.getPalestras(this);
        Minicurso.getMinicursos(this);
        (rootView.findViewById(R.id.bt_sign_up)).setOnClickListener(this);
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
}