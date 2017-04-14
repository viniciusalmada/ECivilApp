package br.almadaapps.civilapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.almadaapps.civilapp.R;
import br.almadaapps.civilapp.domains.Schedule;

/**
 * Created by vinicius-almada on 02/04/17.
 */

public class IndividualScheduleAdapter extends RecyclerView.Adapter<IndividualScheduleAdapter.VH> {
    private Context context;
    private List<Schedule> listGen;
    private boolean[] saveds;

    public IndividualScheduleAdapter(Context context, List<Schedule> list, boolean[] booleen) {
        this.context = context;
        Collections.sort(list, new Comparator<Schedule>() {
            @Override
            public int compare(Schedule o1, Schedule o2) {
                return o1.getPeriod().compareTo(o2.getPeriod());
            }
        });
        this.listGen = list;
        saveds = booleen;
    }

    @Override
    public IndividualScheduleAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_individual_schedule, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(final IndividualScheduleAdapter.VH holder, int position) {
        holder.aSwitch.setText(listGen.get(position).getName());
        holder.aSwitch.setChecked(saveds[position]);

        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                subCheckeds[holder.getAdapterPosition()] = isChecked;
                saveds[holder.getAdapterPosition()] = isChecked;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listGen.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public boolean[] getSubCheckeds() {
        return saveds;
    }

    public class VH extends RecyclerView.ViewHolder {
        private Switch aSwitch;

        public VH(View itemView) {
            super(itemView);
            aSwitch = (Switch) itemView.findViewById(R.id.sw_sujects);
        }
    }
}
