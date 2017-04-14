package br.almadaapps.civilapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.almadaapps.civilapp.R;
import br.almadaapps.civilapp.domains.News;
import br.almadaapps.civilapp.utils.AlertLinkExternal;

/**
 * Created by vinicius-almada on 21/03/17.
 */

public class NewsNonImageAdapter extends RecyclerView.Adapter<NewsNonImageAdapter.VH> {
    private List<News.NNonImage> nonImageList;
    private Context context;

    public NewsNonImageAdapter(List<News.NNonImage> nonImageList, Context context) {
        this.nonImageList = nonImageList;
        this.context = context;
    }

    @Override
    public NewsNonImageAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news_non_image, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(NewsNonImageAdapter.VH holder, int position) {
        holder.tvTitle.setText(nonImageList.get(position).getText());
        holder.tvComment.setText(nonImageList.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return nonImageList.size();
    }

    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        TextView tvComment;

        public VH(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvComment = (TextView) itemView.findViewById(R.id.tv_comment);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AlertLinkExternal.openAlertDialog(nonImageList.get(getAdapterPosition()).getLink(), context);
        }
    }
}
