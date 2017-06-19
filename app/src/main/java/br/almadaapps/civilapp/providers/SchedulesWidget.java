package br.almadaapps.civilapp.providers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.almadaapps.civilapp.R;
import br.almadaapps.civilapp.SchedulesWidgetConfigureActivity;
import br.almadaapps.civilapp.domains.Schedule;
import br.almadaapps.civilapp.services.SchedulesWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class SchedulesWidget extends AppWidgetProvider {
    public static final String SETTINGS_SCHEDULES_ACTIVITY = "SETTINGS_SCHEDULES_ACTIVITY";
    private static final String[] DAYS = {"SEG", "SEG", "TER", "QUA", "QUI", "SEX", "SAB"};
    private static final String TAG = "ScheduleService";
    private static final String UPDATE_SCHEDULES = "UPDATE_SCHEDULES";
    public static List<Schedule.TimeLine> timeLineList = new ArrayList<>();
    private static int mPeriod = 11;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        mPeriod = SchedulesWidgetConfigureActivity.loadPeriod(context, appWidgetId);
        Log.d(TAG, "updateAppWidget: " + mPeriod);

        Intent itService = new Intent(context, SchedulesWidgetService.class);
        itService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.schedules_widget);
        remoteViews.setRemoteAdapter(R.id.lv_schedules, itService);
        remoteViews.setEmptyView(R.id.lv_schedules, R.id.tv_empty);
        remoteViews.setTextViewText(R.id.appwidget_text_sub, getDay() + " - " + getCurrPeriod());
        initListTimeTable(appWidgetManager, appWidgetId);

        Intent itUpdate = new Intent(context, SchedulesWidget.class);
        itUpdate.setAction(UPDATE_SCHEDULES);
        itUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent piUpdate = PendingIntent.getBroadcast(context, 0, itUpdate, 0);
        remoteViews.setOnClickPendingIntent(R.id.ib_refresh, piUpdate);

        Intent itConf = new Intent(context, SchedulesWidgetConfigureActivity.class);
        itConf.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent piConf = PendingIntent.getActivity(context, 0, itConf, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ib_settings, piConf);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private static String getCurrPeriod() {
        if (mPeriod == 11)
            return "Horário Individual";
        else
            return String.valueOf(mPeriod).concat("º Período");
    }

    private static void initListTimeTable(final AppWidgetManager appWidgetManager, final int appWidgetId) {
        Log.d(TAG, "initListTimeTable: ");
        DatabaseReference timeRef = FirebaseDatabase.getInstance().getReference().child(Schedule.DR_SCHEDULE);
        timeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Schedule>> t = new GenericTypeIndicator<List<Schedule>>() {
                };
                FirebaseUser uLogged = getUserLogged();
                List<Schedule> listTimeTablesGen = dataSnapshot.child("Y2017_1").getValue(t);
                List<Schedule> listTimeTablesUser;

                if (dataSnapshot.child(uLogged.getUid()).hasChildren())
                    listTimeTablesUser = dataSnapshot.child(uLogged.getUid()).getValue(t);
                else
                    listTimeTablesUser = new ArrayList<>();

                ArrayList<Schedule> listComplete = new ArrayList<>(listTimeTablesGen);
                listComplete.addAll(listTimeTablesUser);
                Log.d(TAG, "onDataChange: " + dataSnapshot.toString());
                timeLineList = Schedule.TimeLine.getLinesFromTimeTable(listComplete, mPeriod, getCurrDay());
                if (timeLineList.size() == 0)
                    timeLineList.add(new Schedule.TimeLine(
                            "SEM AULAS",
                            "",
                            "--:--",
                            "",
                            mPeriod,
                            0
                    ));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_schedules);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private static int getCurrDay() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        return day == 1 ? 0 : day - 2;
    }

    private static String getDay() {
        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_WEEK);
        return DAYS[today - 1];
    }

    private static FirebaseUser getUserLogged() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        if (intent != null) {
            if (intent.getAction().equalsIgnoreCase(UPDATE_SCHEDULES)) {
                Toast.makeText(context, "Atualizando Horários...", Toast.LENGTH_SHORT).show();
                int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                onUpdate(context, appWidgetManager, new int[]{appWidgetId});
            }
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}