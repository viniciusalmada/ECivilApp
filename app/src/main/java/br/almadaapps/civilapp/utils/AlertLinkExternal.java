package br.almadaapps.civilapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

/**
 * Created by vinicius-almada on 21/03/17.
 */

public class AlertLinkExternal {
    public static void openAlertDialog(final String url, final Context context, boolean showLink) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alert = builder.create();
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
                alert.dismiss();
            }
        });
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
            }
        });
        alert.setTitle("Abrir link");
        if (showLink)
            alert.setMessage("Quer abrir o link \"" + url + "\" no browser externo?");
        else
            alert.setMessage("Quer abrir o link no browser externo?");

        alert.show();
    }

    public static void openAlertDialog(final String text, final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog alert = builder.create();
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
            }
        });
        alert.setTitle("III SIMPÓSIO MARANHENSE DE ENGENHARIA CIVIL");
        alert.setMessage(text);

        alert.show();
    }

    /*public static void openContactDialog(final Context context, final OnContactClickListener onContactClickListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_contact_list, null, false);
        LinearLayout email = (LinearLayout) view.findViewById(R.id.ll_email);
        LinearLayout call = (LinearLayout) view.findViewById(R.id.ll_call);
        LinearLayout facebook = (LinearLayout) view.findViewById(R.id.ll_facebook);
        LinearLayout instagram = (LinearLayout) view.findViewById(R.id.ll_instagram);
        ImageView iv = (ImageView) view.findViewById(R.id.ic_email);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "id:" + v.getId(), Toast.LENGTH_SHORT).show();
                onContactClickListener.onContactClick(v.getId());
            }
        };

        email.setOnClickListener(onClickListener);
        call.setOnClickListener(onClickListener);
        facebook.setOnClickListener(onClickListener);
        instagram.setOnClickListener(onClickListener);
        iv.setOnClickListener(onClickListener);

        builder.setView(R.layout.item_contact_list);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "hueueh", Toast.LENGTH_SHORT).show();
                builder.create().dismiss();
            }
        });
        builder.setTitle("CONTATO");
        builder.show();
        *//*final AlertDialog alert = builder.create();
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
            }
        });
        alert.setTitle("CONTATO");
//        alert.setContentView();
        alert.show();*//*
    }*/
}
