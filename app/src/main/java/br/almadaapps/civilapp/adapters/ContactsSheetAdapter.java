package br.almadaapps.civilapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.almadaapps.civilapp.R;

/**
 * Created by Vinicius on 15.4.17.
 */

public class ContactsSheetAdapter extends BaseAdapter {

    private Context context;
    private int[] icons = new int[]{R.drawable.ic_email_grey600_24dp, R.drawable.ic_phone_grey600_24dp, R.drawable.ic_facebook_box_grey600_24dp, R.drawable.ic_instagram_grey600_24dp};
    private String[] contactTypes = new String[]{"Email", "Ligação", "Facebook", "Instagram"};

    public ContactsSheetAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return icons.length;
    }

    @Override
    public Object getItem(int position) {
        return icons[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH holder;
        if (convertView == null) {
            holder = new VH();
            convertView = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_contact_list, null, false);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_sheet_icon);
            holder.tvContact = (TextView) convertView.findViewById(R.id.tv_sheet);

            convertView.setTag(holder);
        } else {
            holder = (VH) convertView.getTag();
        }

        holder.ivIcon.setImageResource(icons[position]);
        holder.tvContact.setText(contactTypes[position]);

        return convertView;
    }

    private class VH {
        ImageView ivIcon;
        TextView tvContact;
    }
}
