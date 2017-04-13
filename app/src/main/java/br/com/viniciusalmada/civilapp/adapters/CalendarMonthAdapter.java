package br.com.viniciusalmada.civilapp.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.viniciusalmada.civilapp.R;
import br.com.viniciusalmada.civilapp.domains.Calendar;
import br.com.viniciusalmada.civilapp.utils.ExpanderCollapserView;

/**
 * Created by Vinicius on 13.4.17.
 */

public class CalendarMonthAdapter extends RecyclerView.Adapter<CalendarMonthAdapter.VH> {
    private Context context;
    private List<Calendar> calendarList;
    private List<Integer> monthsNumber;

    public CalendarMonthAdapter(Context context, List<Calendar> calendarList) {
        this.context = context;
        this.calendarList = calendarList;
        monthsNumber = getMonthNumber();
    }

    @Override
    public CalendarMonthAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_calendar_month, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(CalendarMonthAdapter.VH holder, int position) {
        String[] months = context.getResources().getStringArray(R.array.months);
        holder.tvMonth.setText(months[monthsNumber.get(position)]);

        CalendarDayAdapter adapter = new CalendarDayAdapter(context, getCalendarMonthList(monthsNumber.get(position)));
        LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.rvCalendar.setLayoutManager(llm);
        holder.rvCalendar.setAdapter(adapter);

        holder.ibExpand.setOnClickListener(collapseView(holder.rvCalendar));
    }

    @Override
    public int getItemCount() {
        return monthsNumber.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private List<Calendar> getCalendarMonthList(int month) {
        List<Calendar> list = new ArrayList<>();
        for (Calendar c : calendarList) {
            if (c.getMonth() == month)
                list.add(c);
        }
        Collections.sort(list, new Comparator<Calendar>() {
            @Override
            public int compare(Calendar o1, Calendar o2) {
                return o1.getInitPeriod().compareTo(o2.getInitPeriod());
            }
        });
        return list;
    }

    private List<Integer> getMonthNumber() {
        Set<Integer> set = new HashSet<>();
        for (Calendar c : calendarList) {
            set.add(c.getMonth());
        }
        List<Integer> list = new ArrayList<>(set);
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        return list;
    }

    private View.OnClickListener collapseView(final View rv) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View iButton) {
                ExpanderCollapserView.collapseView(rv);
                ((ImageButton) iButton).setImageResource(R.drawable.ic_expand_more);
                iButton.setOnClickListener(expandView(rv));
            }
        };
    }

    private View.OnClickListener expandView(final View rv) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View iButton) {
                ExpanderCollapserView.expandView(rv);
                ((ImageButton) iButton).setImageResource(R.drawable.ic_expand_less);
                iButton.setOnClickListener(collapseView(rv));
            }
        };
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView tvMonth;
        RecyclerView rvCalendar;
        ImageButton ibExpand;

        public VH(View itemView) {
            super(itemView);
            tvMonth = (TextView) itemView.findViewById(R.id.tv_month);
            rvCalendar = (RecyclerView) itemView.findViewById(R.id.rv_month_content);
            ibExpand = (ImageButton) itemView.findViewById(R.id.bt_expand);
        }
    }
}
