package com.example.eduard.mobile.view_models.factories;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.eduard.mobile.view_models.UpdateViewModel;

public class UpdateContactModelFactory implements ViewModelProvider.Factory {

    public UpdateContactModelFactory(Activity activity, String jwt, String username){
        this.activity = activity;
        this.jwt = jwt;
        this.username = username;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UpdateViewModel(
                activity, jwt, username
        );
    }

    private Activity activity;
    private String jwt;
    private String username;
}
