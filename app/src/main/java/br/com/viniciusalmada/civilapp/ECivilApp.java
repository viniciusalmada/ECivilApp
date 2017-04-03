package br.com.viniciusalmada.civilapp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by vinicius-almada on 02/04/17.
 */

public class ECivilApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
