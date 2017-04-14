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
    public static void openAlertDialog(final String url, final Context context) {
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
        alert.setTitle("Abrir link externo");
        alert.setMessage("Quer abrir o link \"" + url + "\" no browser externo?");

        alert.show();
    }
}
