package com.example.eduard.mobile.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;

import com.example.eduard.mobile.charts.ChartFragment;
import com.example.eduard.mobile.contacts.AddContact;
import com.example.eduard.mobile.contacts.Contacts;
import com.example.eduard.mobile.contacts.DeleteContact;
import com.example.eduard.mobile.contacts.UpdateContact;
import com.example.eduard.mobile.repository.Repository;
import com.example.eduard.mobile.repository.RepositoryProvider;
import com.example.eduard.mobile.repository.offline.LocalRepository;
import com.example.eduard.mobile.repository.online.NetworkRepository;
import com.example.eduard.mobile.repository.room.entity.Contact;
import com.example.eduard.mobile.repository.room.entity.Fetch;
import com.example.eduard.mobile.repository.room.entity.utils.OperationType;

import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ForkJoinPool;

import androidx.annotation.Nullable;

public class SyncService extends Service {

    private static  String[] fragments = {
            Contacts.BROADCAST,
            ChartFragment.BROADCAST,
            AddContact.BROADCAST,
            DeleteContact.BROADCAST,
            UpdateContact.BROADCAST
    };

    @Override
    public void onCreate() {

        super.onCreate();

        if (timer == null) {
            timer = new Timer();
        } else {
            timer.cancel();
        }

        timer.scheduleAtFixedRate(new SyncTask(), 0, SYNC_INTERVAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        username = intent.getStringExtra("username");
        jwt = intent.getStringExtra("jwt");

        localRepository = RepositoryProvider.getInstance().getLocalRepo(
                this,
                username
        );

        netRepository = RepositoryProvider.getInstance().getNetRepo(
                this,
                jwt
        );

        return super.onStartCommand(intent, flags, startId);
    }


    class SyncTask extends TimerTask {

        @Override
        public void run() {

            if (netRepository == null || localRepository == null || !isInternet()) {
                return;
            }

            syncDataFromServerToLocal(
                    netRepository.getUnfetchedData()
            );

            syncDataFromLocalToServer(
                    localRepository.getUnfetchedData()
            );
        }


        private void syncDataFromServerToLocal(List<Fetch> fetches) {

            if (fetches.isEmpty()) {
                return;
            }

            for (Fetch fetch : fetches) {

                String tableName = fetch.getTableName();

                switch (tableName.toUpperCase()) {

                    case "CONTACT": {
                        executeOperationsOnContactServer(
                                createJSON(fetch.getData()),
                                fetch.getType()
                        );
                        break;
                    }
                }
            }

            sendBroadcastToAll();
        }

        private void syncDataFromLocalToServer(List<Fetch> fetches) {

            if(fetches.isEmpty()){
                return;
            }

            for (Fetch fetch : fetches) {
                String tableName = fetch.getTableName();

                switch (tableName.toUpperCase()) {
                    case "CONTACT": {
                        executeOperationsOnContactLocal(
                                createJSON(fetch.getData()),
                                fetch.getType()
                        );
                        break;
                    }
                }

            }

            sendBroadcastToAll();
        }

        private void executeOperationsOnContactServer(JSONObject object, int type) {

            try {

                OperationType operationType = OperationType.getFromInt(type);
                if (operationType == null) {
                    return;
                }

                switch (operationType) {

                    case INSERT: {

                        String first = object.getString("first");
                        String last = object.getString("last");
                        String phone = object.getString("phone");

                        localRepository.syncContactAdd(
                                first, last, phone, null
                        );

                        netRepository.fetch(
                                "contact", object.toString(), type
                        );

                        break;
                    }

                    case UPDATE: {

                        String first = object.getString("first");
                        String last = object.getString("last");
                        String phone = object.getString("phone");

                        localRepository.syncContactUpdate(
                                first, last, phone, null
                        );

                        netRepository.fetch(
                                "contact", object.toString(), type
                        );

                        break;
                    }


                    case DELETE: {

                        String first = object.getString("first");

                        localRepository.syncContactDelete(
                                first, null
                        );

                        netRepository.fetch(
                                "contact", object.toString(), type
                        );

                        break;
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void executeOperationsOnContactLocal(JSONObject object, int type) {

            try {

                OperationType operationType = OperationType.getFromInt(type);
                if (operationType == null) {
                    return;
                }

                switch (operationType) {

                    case INSERT: {

                        String first = object.getString("first");
                        String last = object.getString("last");
                        String phone = object.getString("phone");

                        netRepository.syncContactAdd(
                                first, last, phone,
                                () -> AsyncTask.execute(
                                        () -> {
                                            localRepository.fetch("contact", object.toString(), type);
                                        }
                                )
                        );

                        break;
                    }

                    case UPDATE: {

                        String first = object.getString("first");
                        String last = object.getString("last");
                        String phone = object.getString("phone");

                        System.out.println("UPDATE");

                        netRepository.syncContactUpdate(
                                first, last, phone,
                                () -> AsyncTask.execute(
                                        () -> {
                                            localRepository.fetch("contact", object.toString(), type);
                                        }
                                )
                        );

                        break;
                    }


                    case DELETE: {

                        String first = object.getString("first");

                        netRepository.syncContactDelete(
                                first,
                                () -> AsyncTask.execute(
                                        () -> {
                                            localRepository.fetch("contact", object.toString(), type);
                                        }
                                )
                        );

                        break;
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private JSONObject createJSON(String string) {

            try {
                return new JSONObject(string);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new JSONObject();
        }

        private boolean isInternet() {

            ConnectivityManager manager = (ConnectivityManager)
                    SyncService.this
                            .getApplicationContext()
                            .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo nInfo = manager.getActiveNetworkInfo();

            return nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        }

    }


    private void sendBroadcastToAll(){
        for(String action: fragments){
            sendBroadcast(new Intent(action));
        }
    }

    private String jwt;
    private String username;

    private Timer timer;

    private Repository localRepository;
    private Repository netRepository;

    private final static int SYNC_INTERVAL = 4500;

}
