package br.almadaapps.civilapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.almadaapps.civilapp.R;
import br.almadaapps.civilapp.domains.Schedule;
import br.almadaapps.civilapp.interfaces.OnClickTimeTableListenerImpl;

/**
 * Created by vinicius-almada on 24/03/17.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.VH> {
    private Context context;
    private List<Schedule.TimeLine> timeLines;
    private OnClickTimeTableListenerImpl onClickTimeTableListener;

    public ScheduleAdapter(Context context, List<Schedule.TimeLine> timeLines, OnClickTimeTableListenerImpl onClickTimeTableListener) {
        this.context = context;
        Collections.sort(timeLines, new Comparator<Schedule.TimeLine>() {
            @Override
            public int compare(Schedule.TimeLine o1, Schedule.TimeLine o2) {
                return o1.getTimeinit().compareTo(o2.getTimeinit());
            }
        });
        this.timeLines = timeLines;
        this.onClickTimeTableListener = onClickTimeTableListener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_schedule, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (timeLines.size() != 0) {
            holder.tvSubject.setText(timeLines.get(position).getSubject());
            holder.tvProf.setText(timeLines.get(position).getProfessor());
            holder.tvTime.setText(timeLines.get(position).getTime());
            if (position % 2 != 0)
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            else
                holder.itemView.setBackgroundColor(Color.WHITE);

            YoYo.with(Techniques.FlipInX)
                    .duration(800)
                    .playOn(holder.itemView);
        }
       /* holder.tvSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTimeTableListener.onClickTimeTable(v);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return timeLines.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private TextView tvSubject;
        private TextView tvProf;
        private TextView tvTime;

        public VH(View itemView) {
            super(itemView);
            tvSubject = (TextView) itemView.findViewById(R.id.tv_subject);
            tvProf = (TextView) itemView.findViewById(R.id.tv_prof);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
