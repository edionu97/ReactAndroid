package com.example.eduard.mobile.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.example.eduard.mobile.login.activities.AuthenticatedActivity;

import java.lang.ref.WeakReference;

public abstract class NetworkVolatile extends Fragment {

    public NetworkVolatile() {
        callback = new ConnectivityManager.NetworkCallback() {

            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);
                connectionAvailable();
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                connectionLost();
            }
        };
    }

    public void register(@NonNull Activity activity, String broadcastAction) {

        manager =
                (ConnectivityManager) activity
                        .getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        this.activity = new WeakReference<>(activity);

        // register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(broadcastAction);
        activity.registerReceiver(receiver, filter);

        ((AuthenticatedActivity)activity).addReceiverToUnregister(receiver);

        manager.registerDefaultNetworkCallback(callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.unregisterNetworkCallback(callback);
    }

    public abstract void connectionLost();

    public abstract void connectionAvailable();

    public abstract void broadcastReceived();


    private ConnectivityManager.NetworkCallback callback;
    private WeakReference<Activity> activity;
    private ConnectivityManager manager;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            broadcastReceived();
        }
    };
}
