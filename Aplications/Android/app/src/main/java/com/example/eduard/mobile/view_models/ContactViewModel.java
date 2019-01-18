package com.example.eduard.mobile.view_models;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.eduard.mobile.repository.RepositoryProvider;
import com.example.eduard.mobile.repository.room.entity.Contact;
import com.example.eduard.mobile.view_models.utils.ModelView;

import java.util.List;

public class ContactViewModel extends ModelView {

    public ContactViewModel(String jwt, String username, Activity activity){
        super(username, jwt, activity);
        createRepository();
        repository.getAllContactsForUser();
    }

    @Override
    public void refreshView() {
        repository.getAllContactsForUser();
    }

    public MutableLiveData<List<Contact>> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts.setValue(contacts);
    }

    private MutableLiveData<List<Contact>> contacts = new MutableLiveData<>();
}
