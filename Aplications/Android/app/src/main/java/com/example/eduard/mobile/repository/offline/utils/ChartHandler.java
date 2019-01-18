package com.example.eduard.mobile.repository.offline.utils;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.example.eduard.mobile.repository.room.dao.ContactDao;
import com.example.eduard.mobile.repository.room.entity.Contact;

import java.lang.ref.WeakReference;
import java.util.List;

public class ChartHandler extends AsyncTask<String, Void, LiveData<List<Contact>>> {

    public ChartHandler(Context context, ContactDao contactDao){
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
            new MapData(context.get(), contacts).execute();
        }));
    }

    private ContactDao contactDao;
    private WeakReference<Context> context;
}
