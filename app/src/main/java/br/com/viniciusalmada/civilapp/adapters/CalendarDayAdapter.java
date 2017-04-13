package br.com.viniciusalmada.civilapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.viniciusalmada.civilapp.R;
import br.com.viniciusalmada.civilapp.domains.Calendar;

/**
 * Created by Vinicius on 13.4.17.
 */

public class CalendarDayAdapter extends RecyclerView.Adapter<CalendarDayAdapter.VH> {
    private Context context;
    private List<Calendar> calendarList;

    public CalendarDayAdapter(Context context, List<Calendar> calendarList) {
        this.context = context;
        this.calendarList = calendarList;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_calendar_day, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        int endPeriod = calendarList.get(position).getEndPeriod();
        int initPeriod = calendarList.get(position).getInitPeriod();

        String day = "-";

        if (endPeriod != 0 && endPeriod > initPeriod) {
            if (endPeriod - initPeriod > 1)
                day = initPeriod + " a " + endPeriod;
            else if (endPeriod - initPeriod == 1)
                day = initPeriod + " e " + endPeriod;
        } else if (endPeriod == 0)
            day = String.valueOf(initPeriod);

        holder.tvDay.setText(day);

        int format = calendarList.get(position).getFormat();
        int colorBG;
        int colorTxt;
        switch (format) {
            case Calendar.TEXT_NORMAL:
                colorBG = ContextCompat.getColor(context, android.R.color.transparent);
                colorTxt = Color.parseColor("#212121");
                break;
            case Calendar.TEXT_WARNING:
                colorBG = ContextCompat.getColor(context, android.R.color.transparent);
                colorTxt = Color.parseColor("#d50000");
                break;
            case Calendar.TEXT_EVENTS:
                colorBG = ContextCompat.getColor(context, android.R.color.transparent);
                colorTxt = Color.parseColor("#03a9f4");
                break;
            case Calendar.TEXT_HOLIDAY:
                colorBG = ContextCompat.getColor(context, android.R.color.transparent);
                colorTxt = Color.parseColor("#00c853");
                break;
            case Calendar.TEXT_GRIFFIN:
                colorBG = Color.parseColor("#ffea00");
                colorTxt = Color.parseColor("#212121");
                break;
            default:
                colorBG = ContextCompat.getColor(context, android.R.color.transparent);
                colorTxt = Color.parseColor("#212121");
                break;
        }
        holder.tvText.setTextColor(colorTxt);
        holder.tvText.setBackgroundColor(colorBG);
        holder.tvText.setText(calendarList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return calendarList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView tvDay;
        TextView tvText;

        public VH(View itemView) {
            super(itemView);
            tvDay = (TextView) itemView.findViewById(R.id.tv_calendar_day);
            tvText = (TextView) itemView.findViewById(R.id.tv_calendar_text);
        }
    }
}
