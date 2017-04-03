package br.com.viniciusalmada.civilapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import br.com.viniciusalmada.civilapp.LoginActivity;
import br.com.viniciusalmada.civilapp.domains.Syllabus;

/**
 * Created by vinicius-almada on 25/03/17.
 */

public abstract class GeneralMethods {
    public static void signOutFinish(Context context, Class<? extends AppCompatActivity> destiny) {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences.Editor editor = context.getSharedPreferences(LoginActivity.PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putBoolean(LoginActivity.KEY_BOOLEAN_IS_LOGGED, false).apply();
        Intent i = new Intent(context, destiny);
        context.startActivity(i);
    }

    public static boolean isConnected(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null && ni.isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    public static String[] getNameFromList(List<Syllabus> list) {
        String[] strings = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strings[i] = (list.get(i).getName());
        }
        return strings;
    }
}
