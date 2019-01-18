package com.example.eduard.mobile.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.eduard.mobile.repository.offline.LocalRepository;
import com.example.eduard.mobile.repository.online.NetworkRepository;

import java.util.HashMap;
import java.util.HashSet;


public class RepositoryProvider {

    private RepositoryProvider(){

    }

    public static RepositoryProvider getInstance(){
        if(instance == null){
            synchronized (RepositoryProvider.class){
                if(instance == null){
                    instance = new RepositoryProvider();
                }
            }
        }
        return instance;
    }

    public Repository getRepository(Context context, String username, String jwt) {

        ConnectivityManager manager = (ConnectivityManager)
                context
                        .getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo nInfo = manager.getActiveNetworkInfo();

        boolean isConnected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();

        if(isConnected){
            System.out.println("Online!");
            contextRepositoryOnlineHashMap.putIfAbsent(context, new NetworkRepository(context, jwt, true));
            return contextRepositoryOnlineHashMap.get(context);
        }

        contextRepositoryOfflineHashMap.putIfAbsent(
                context,
                new LocalRepository(context, username, true)
        );

        System.out.println("Offline!");
        return contextRepositoryOfflineHashMap.get(context);
    }


    public Repository getLocalRepo(Context context, String username){
        return  new LocalRepository(context, username, false);
    }

    public Repository getNetRepo(Context context, String jwt){
        return  new NetworkRepository(context, jwt, false);
    }

    private static volatile RepositoryProvider instance;
    private HashMap<Context, Repository> contextRepositoryOnlineHashMap = new HashMap<>();
    private HashMap<Context, Repository> contextRepositoryOfflineHashMap = new HashMap<>();
}
