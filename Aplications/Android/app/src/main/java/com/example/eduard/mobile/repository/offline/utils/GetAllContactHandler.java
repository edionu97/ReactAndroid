package com.example.eduard.mobile.repository.offline.utils;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.example.eduard.mobile.repository.room.dao.ContactDao;
import com.example.eduard.mobile.repository.room.entity.Contact;
import com.example.eduard.mobile.view_models.ContactViewModel;

import java.lang.ref.WeakReference;
import java.util.List;

public class GetAllContactHandler extends AsyncTask<String, Void, LiveData<List<Contact>>> {

    public GetAllContactHandler(Context context, ContactDao contactDao){
        this.context = new WeakReference<>(context);
        this.contactDao = contactDao;
    }

    @Override
    protected LiveData<List<Contact>> doInBackground(String... strings) {
        return contactDao.getContactsForUser(
                strings[0]
            );
    }

    @Override
    protected void onPostExecute(LiveData<List<Contact>> liveContacts) {

        super.onPostExecute(liveContacts);

        liveContacts.observe((LifecycleOwner) context.get(), (contacts -> {
            // update the interface through its model
            FragmentActivity activity = (FragmentActivity) context.get();
            ContactViewModel model = ViewModelProviders.of(activity).get(ContactViewModel.class);
            model.setContacts(contacts);
        }));
    }

    private ContactDao contactDao;
    private WeakReference<Context> context;
}
