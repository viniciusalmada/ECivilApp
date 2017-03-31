package br.com.viniciusalmada.civilapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import br.com.viniciusalmada.civilapp.interfaces.NetworkChangesImpl;

/**
 * Created by vinicius-almada on 26/03/17.
 */

public class NetworkStateReceiver extends BroadcastReceiver {
    public static final String TAG = "NSR";
    private NetworkChangesImpl networkChanges;

    public NetworkStateReceiver(NetworkChangesImpl networkChanges) {
        this.networkChanges = networkChanges;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        boolean isConn = ni != null && ni.isConnectedOrConnecting();
        networkChanges.setConnection(isConn);
//        setConnected(ni != null && ni.isConnectedOrConnecting());
        Log.d(TAG, "onReceive: " + isConn);
    }
}
