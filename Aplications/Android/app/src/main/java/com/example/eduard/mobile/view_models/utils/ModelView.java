package com.example.eduard.mobile.view_models.utils;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import com.example.eduard.mobile.repository.Repository;
import com.example.eduard.mobile.repository.RepositoryProvider;

import java.lang.ref.WeakReference;

public abstract class ModelView extends AndroidViewModel {

    public ModelView(String username, String jwt, Activity activity) {
        super(activity.getApplication());

        this.jwt = jwt;
        this.username = username;
        this.activity = new WeakReference<>(activity);
    }

    public void createRepository(){
        repository = RepositoryProvider.getInstance().getRepository(
                activity.get(),username, jwt
        );
    }

    public abstract void refreshView();

    private String jwt;
    private String username;
    protected Repository repository;
    private WeakReference<Context> activity;
}
