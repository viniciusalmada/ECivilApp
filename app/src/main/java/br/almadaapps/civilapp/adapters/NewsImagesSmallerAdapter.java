package br.almadaapps.civilapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.almadaapps.civilapp.R;
import br.almadaapps.civilapp.domains.News;
import br.almadaapps.civilapp.utils.AlertLinkExternal;

/**
 * Created by vinicius-almada on 21/03/17.
 */

public class NewsImagesSmallerAdapter extends RecyclerView.Adapter<NewsImagesSmallerAdapter.VH> {
    private List<News.NSmallImages> smallImagesList;
    private Context context;

    public NewsImagesSmallerAdapter(List<News.NSmallImages> smallImagesList, Context context) {
        this.smallImagesList = smallImagesList;
        this.context = context;
    }

    @Override
    public NewsImagesSmallerAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news_image_smaller, parent, false);
        return new NewsImagesSmallerAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(NewsImagesSmallerAdapter.VH holder, int position) {
        holder.tvTitle.setText(smallImagesList.get(position).getText());
        Picasso.with(context).load(smallImagesList.get(position).getLinkImg()).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return smallImagesList.size();
    }

    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        ImageView ivImage;

        public VH(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AlertLinkExternal.openAlertDialog(smallImagesList.get(getAdapterPosition()).getLink(), context, true);
        }
    }
}
