package br.com.viniciusalmada.civilapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by vinicius-almada on 16/03/17.
 */

public abstract class CommonActivity extends AppCompatActivity {
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        progressDialog = new ProgressDialog(this);
    }

    protected abstract void initViews();

    protected void showDialog(String text) {
        progressDialog.setMessage(text);
        progressDialog.show();
    }

    protected void showDialog(String text, boolean cancelable) {
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(text);
        progressDialog.show();
    }

    protected void hideDialog() {
        progressDialog.hide();
    }

    protected void showToast(String text, boolean isShort) {
        if (isShort)
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    protected void showToast(@StringRes int text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected void showSnackbar(String text, View view) {
        Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setActionTextColor(Color.CYAN)
                .show();
    }

    protected void showSnackbar(@StringRes int text, View view) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setActionTextColor(Color.CYAN)
                .show();
    }

    protected void showErrorConnectionSnackar(View v) {
        Snackbar.make(v, R.string.no_connection, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.to_conect, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent[] it = new Intent[1];
                        AlertDialog.Builder b = new AlertDialog.Builder(CommonActivity.this);
                        b.setTitle(R.string.active_data_wifi);
                        b.setPositiveButton(getString(R.string.mobile_data), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                it[0] = new Intent(Settings.ACTION_SETTINGS);
                                startActivity(it[0]);
                            }
                        });
                        b.setNegativeButton(getString(R.string.wifi), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                it[0] = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                startActivity(it[0]);
                            }
                        });
                        b.show();
                    }
                })
                .setActionTextColor(Color.CYAN)
                .show();
    }
}
