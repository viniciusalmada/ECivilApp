package br.almadaapps.civilapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.almadaapps.civilapp.R;
import br.almadaapps.civilapp.domains.Monograph;
import br.almadaapps.civilapp.utils.AlertLinkExternal;

/**
 * Created by vinicius-almada on 07/04/17.
 */

public class MonographsAdapter extends RecyclerView.Adapter<MonographsAdapter.VH> {
    private Context context;
    private List<Monograph> monographList;

    public MonographsAdapter(Context context, List<Monograph> monographList, final boolean sortByTitle) {
        this.context = context;
        Collections.sort(monographList, new Comparator<Monograph>() {
            @Override
            public int compare(Monograph o1, Monograph o2) {
                return sortByTitle ? o1.getTitle().compareTo(o2.getTitle()) : o1.getAuthor().compareTo(o2.getAuthor());
            }
        });
        this.monographList = monographList;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_monograph, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.tvTitle.setText(monographList.get(position).getTitle());
        holder.tvAuthor.setText(monographList.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return monographList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        TextView tvAuthor;

        public VH(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AlertLinkExternal.openAlertDialog(monographList.get(getAdapterPosition()).getLink(), context, false);
        }
    }
}
