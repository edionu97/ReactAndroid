package com.example.eduard.mobile.repository.offline.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.eduard.mobile.repository.room.dao.ContactDao;
import com.example.eduard.mobile.repository.room.dao.UserDao;
import com.example.eduard.mobile.repository.room.entity.Contact;

import java.lang.ref.WeakReference;

public class AddContactHandler extends AsyncTask<String, Void, Boolean> {

    public AddContactHandler(Context context, UserDao userRepository, ContactDao contactDao, boolean toastNotify, Runnable onSuccess){
        this.context = new WeakReference<>(context);
        this.contactRepository = contactDao;
        this.userRepository = userRepository;
        this.onSuccess = onSuccess;
        this.toast = toastNotify;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        String username = strings[0];
        String firstName = strings[1];
        String lastName = strings[2];
        String phoneNumber = strings[3];

        int userId = userRepository.findByUsername(username).getUid();

        if(contactRepository.findContactByFirstNameAndUser(
                firstName, username
        ) != null){
            return false;
        }

        contactRepository.add(
                new Contact(
                    firstName,
                        lastName,
                        phoneNumber,
                        userId
                )
        );

        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        super.onPostExecute(aBoolean);

        if(aBoolean){

            if(toast) {
                Toast.makeText(
                        context.get(),
                        "Contact inserted successfully",
                        Toast.LENGTH_LONG
                ).show();
            }

            if(onSuccess != null){
                new Thread(onSuccess).start();
            }

            return;
        }

        if(toast) {
            Toast.makeText(
                    context.get(),
                    "Error:Duplicates not allowed!",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private boolean toast;
    private Runnable onSuccess;
    private UserDao userRepository;
    private ContactDao contactRepository;
    private WeakReference<Context> context;
}
