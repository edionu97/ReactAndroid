package com.example.eduard.mobile.contacts;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eduard.mobile.R;
import com.example.eduard.mobile.repository.room.entity.Contact;
import com.example.eduard.mobile.utils.NetworkVolatile;
import com.example.eduard.mobile.utils.gui.GuiUtils;
import com.example.eduard.mobile.view_models.ContactViewModel;
import com.example.eduard.mobile.view_models.factories.ContactViewModelFactory;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class Contacts extends NetworkVolatile implements Observer<List<Contact>> {

    public static  String BROADCAST = "all-contact";

    public Contacts() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        if (bundle != null) {
            username = bundle.getString("username");
            jwt = bundle.getString("jwt");
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if (getActivity() == null || getView() == null) {
            return;
        }

        listView = getView().findViewById(R.id.list_view);


        contactViewModel = ViewModelProviders
                .of(getActivity(), new ContactViewModelFactory(
                        getActivity(),
                        jwt,
                        username
                ))
                .get(ContactViewModel.class);

        contactViewModel
                .getContacts()
                .observe(this, this);

        register(getActivity(), BROADCAST);
    }

    @Override
    public void onChanged(@Nullable List<Contact> contacts) {

        if (contacts == null) {
            return;
        }

        ListAdapter adapter = new ListAdapter(contacts);
        listView.setAdapter(adapter);
    }

    public static Contacts getInstance() {

        if (instance == null) {
            synchronized (Contacts.class) {
                if (instance == null) {
                    instance = new Contacts();
                }
            }
        }

        return instance;
    }

    @Override
    public void connectionLost() {
        contactViewModel.createRepository();
        contactViewModel.refreshView();
    }

    @Override
    public void onResume() {
        contactViewModel.createRepository();
        contactViewModel.refreshView();
        super.onResume();
    }

    @Override
    public void connectionAvailable() {
        contactViewModel.createRepository();
        contactViewModel.refreshView();
    }

    @Override
    public void broadcastReceived() {
        contactViewModel.refreshView();
    }

    private class ListAdapter extends BaseAdapter {

        ListAdapter(List<Contact> contacts) {
            this.contacts = contacts;
        }

        @Override
        public int getCount() {
            return contacts.size();
        }

        @Override
        public Object getItem(int position) {
            return contacts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            @SuppressLint({"ViewHolder", "InflateParams"})
            View view = getLayoutInflater().inflate(R.layout.list_item, null);

            ((TextView) view.findViewById(R.id.first_name))
                    .setText(contacts.get(position).getFirstName());

            ((TextView) view.findViewById(R.id.last_name))
                    .setText(contacts.get(position).getLastName());

            ((TextView) view.findViewById(R.id.phone_number))
                    .setText(contacts.get(position).getPhoneNumber());

            return view;
        }

        private List<Contact> contacts;
    }

    private ListView listView;
    private String username;
    private String jwt;

    private ContactViewModel contactViewModel;

    @SuppressLint("StaticFieldLeak")
    private static volatile Contacts instance;
}
