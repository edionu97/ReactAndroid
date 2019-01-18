package com.example.eduard.mobile.view_models;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.eduard.mobile.repository.Repository;
import com.example.eduard.mobile.repository.RepositoryProvider;
import com.example.eduard.mobile.view_models.utils.ModelView;

import java.lang.ref.WeakReference;

public class DeleteContactModel extends ModelView {

    public DeleteContactModel(String jwt, String username, Activity activity) {
        super(username, jwt, activity);
        createRepository();
    }

    @Override
    public void refreshView() {

    }

    public void deleteContact(String firstName) {
        repository.contactDelete(firstName);
    }
}
