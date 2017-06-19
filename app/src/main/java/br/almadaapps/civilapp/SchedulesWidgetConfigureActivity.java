package br.almadaapps.civilapp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import br.almadaapps.civilapp.providers.SchedulesWidget;

/**
 * Created by vinicius on 18/06/17.
 */

public class SchedulesWidgetConfigureActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    public static final String TAG = "SchedulesActivity";
    private static final String PREFS_NAME = "br.almadaapps.civilapp.SchedulesWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_period";
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private int period = 11;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Context context = SchedulesWidgetConfigureActivity.this;

            savePeriod(context, mAppWidgetId, period);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            SchedulesWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public SchedulesWidgetConfigureActivity() {
        super();
    }

    public static int loadPeriod(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int period = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, 11);
        if (period != 0) {
            return period;
        } else {
            return 11;
        }
    }

    public static void savePeriod(Context context, int appWidgetId, int period) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, period);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_schedules_widget);
        RadioButton[] rbPeriods = new RadioButton[11];
        rbPeriods[0] = (RadioButton) findViewById(R.id.rb_p1);
        rbPeriods[1] = (RadioButton) findViewById(R.id.rb_p2);
        rbPeriods[2] = (RadioButton) findViewById(R.id.rb_p3);
        rbPeriods[3] = (RadioButton) findViewById(R.id.rb_p4);
        rbPeriods[4] = (RadioButton) findViewById(R.id.rb_p5);
        rbPeriods[5] = (RadioButton) findViewById(R.id.rb_p6);
        rbPeriods[6] = (RadioButton) findViewById(R.id.rb_p7);
        rbPeriods[7] = (RadioButton) findViewById(R.id.rb_p8);
        rbPeriods[8] = (RadioButton) findViewById(R.id.rb_p9);
        rbPeriods[9] = (RadioButton) findViewById(R.id.rb_p10);
        rbPeriods[10] = (RadioButton) findViewById(R.id.rb_p11);

        for (RadioButton rb : rbPeriods)
            rb.setOnCheckedChangeListener(this);

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        int savedPeriod = loadPeriod(this, mAppWidgetId);
        for (RadioButton rb : rbPeriods)
            rb.setChecked(false);
        rbPeriods[savedPeriod - 1].setChecked(true);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.rb_p1:
                    period = 1;
                    break;
                case R.id.rb_p2:
                    period = 2;
                    break;
                case R.id.rb_p3:
                    period = 3;
                    break;
                case R.id.rb_p4:
                    period = 4;
                    break;
                case R.id.rb_p5:
                    period = 5;
                    break;
                case R.id.rb_p6:
                    period = 6;
                    break;
                case R.id.rb_p7:
                    period = 7;
                    break;
                case R.id.rb_p8:
                    period = 8;
                    break;
                case R.id.rb_p9:
                    period = 9;
                    break;
                case R.id.rb_p10:
                    period = 10;
                    break;
                case R.id.rb_p11:
                    period = 11;
                    break;
            }
        }
        Log.d(TAG, "onCheckedChanged: " + period);
        savePeriod(this, mAppWidgetId, period);
    }
}
