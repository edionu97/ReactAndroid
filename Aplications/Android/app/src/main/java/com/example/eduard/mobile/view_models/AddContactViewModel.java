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

public class AddContactViewModel extends ModelView {

    public AddContactViewModel(Activity activity, String jwt, String username) {
        super(username,jwt, activity);
        createRepository();
    }

    public void addContact(String firstName, String lastName, String phoneNumber) {
        repository.contactAdd(firstName, lastName, phoneNumber);
    }

    @Override
    public void refreshView() {

    }
}
