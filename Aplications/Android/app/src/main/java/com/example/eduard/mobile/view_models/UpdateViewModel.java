package com.example.eduard.mobile.view_models;

import android.app.Activity;

import com.example.eduard.mobile.view_models.utils.ModelView;

public class UpdateViewModel extends ModelView {

    public UpdateViewModel(Activity activity, String jwt, String username) {
        super(username, jwt, activity);
        createRepository();
    }

    public void updateContact(String firstName, String lastName, String phoneNumber) {
        repository
                .contactUpdate(
                    firstName,
                    lastName,
                    phoneNumber
            );
    }

    @Override
    public void refreshView() {

    }
}
