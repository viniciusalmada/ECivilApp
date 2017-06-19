package br.almadaapps.civilapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.almadaapps.civilapp.R;
import br.almadaapps.civilapp.domains.Schedule;
import br.almadaapps.civilapp.interfaces.OnSchedulesLoaded;
import br.almadaapps.civilapp.providers.SchedulesWidget;

/**
 * Created by vinicius on 16/06/17.
 */

public class SchedulesWidgetFactoryAdapter implements RemoteViewsService.RemoteViewsFactory, OnSchedulesLoaded {
    public static final String TAG = "SchedulesWidget";
    private Context context;
    private List<Schedule> listComplete = new ArrayList<>();
    private List<Schedule.TimeLine> timeLineList = new ArrayList<>();

    public SchedulesWidgetFactoryAdapter(Context context, Intent intent) {
        this.context = context;
//        initListTimeTable();
    }

    public List<Schedule.TimeLine> getTimeLineList() {
        return timeLineList;
    }

    public void setTimeLineList(List<Schedule.TimeLine> timeLineList) {
        this.timeLineList = timeLineList;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged: called");
        setTimeLineList(SchedulesWidget.timeLineList);
        Collections.sort(timeLineList, new Comparator<Schedule.TimeLine>() {
            @Override
            public int compare(Schedule.TimeLine o1, Schedule.TimeLine o2) {
                return o1.getTimeinit().compareTo(o2.getTimeinit());
            }
        });
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.schedules_widget);
    }

    /*private String getPeriod() {
        if (getTimeLineList().size() != 0) {
            int period = getTimeLineList().get(0).getPeriod();
            if (period != 11)
                return String.valueOf(period).concat("º Período");
            else
                return "Horário Individual";
        }
        return "[Período]";
    }

    private String getDay() {
        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_WEEK);
        return DAYS[today - 1];
    }
*/
    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount: called");
        return timeLineList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(TAG, "getViewAt: " + position);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_schedule_widget);
        views.setTextViewText(R.id.tv_time, timeLineList.get(position).getTime());
        views.setTextViewText(R.id.tv_subject, timeLineList.get(position).getSubject());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onSchedulesLoaded(ArrayList<Schedule.TimeLine> timeLines) {
        setTimeLineList(timeLines);
    }
}
