package com.example.eduard.mobile.contacts;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eduard.mobile.R;
import com.example.eduard.mobile.repository.room.entity.Contact;
import com.example.eduard.mobile.utils.NetworkVolatile;
import com.example.eduard.mobile.utils.gui.GuiUtils;
import com.example.eduard.mobile.view_models.ContactViewModel;
import com.example.eduard.mobile.view_models.DeleteContactModel;
import com.example.eduard.mobile.view_models.factories.ContactViewModelFactory;
import com.example.eduard.mobile.view_models.factories.DeleteContactModelFactory;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteContact extends NetworkVolatile implements  Observer<List<Contact>> , AdapterView.OnItemClickListener {

    public static  String BROADCAST = "delete-contact";

    public DeleteContact() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        if (bundle != null){
            username = bundle.getString("username");
            jwt = bundle.getString("jwt");
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete_contact, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if(getActivity() == null){
            return;
        }

        contactModel = ViewModelProviders.of(
                getActivity(),
                new DeleteContactModelFactory(
                        getActivity(),
                        jwt,
                        username
                )
        ).get(DeleteContactModel.class);


        contactViewModel = ViewModelProviders.of(
                getActivity(),
                new ContactViewModelFactory(
                        getActivity(),
                        jwt,
                        username
                )
        ).get(ContactViewModel.class);

        contactViewModel.getContacts().observe(this, this);

        register(getActivity(), BROADCAST);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.list_view_delete);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void connectionLost() {
        contactModel.createRepository();
    }

    @Override
    public void connectionAvailable() {
        contactModel.createRepository();
    }

    @Override
    public void broadcastReceived() {
        contactModel.refreshView();
    }

    @Override
    public void onChanged(@Nullable List<Contact> contacts) {

        if(contacts == null){
            return;
        }

        ListAdapter adapter = new ListAdapter(contacts);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        contactModel.createRepository();

        Contact contact = (Contact)parent.getAdapter().getItem(position);

        GuiUtils.displayQuestionDialog(
                "Are you sure you want to delete contact?",
                getActivity(),
                "Question",
                ()-> contactModel.deleteContact(contact.getFirstName())
        );
    }

    public  static  DeleteContact getInstance(){

        if(instance == null){
            synchronized (DeleteContact.class){
                if(instance == null){
                    instance = new DeleteContact();
                }
            }
        }

        return instance;
    }

    @Override
    public void onResume() {
        contactViewModel.createRepository();
        super.onResume();
    }


    private  class  ListAdapter extends BaseAdapter {

        ListAdapter(List<Contact> contacts){
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

            ((TextView)view.findViewById(R.id.first_name))
                    .setText(contacts.get(position).getFirstName());

            ((TextView)view.findViewById(R.id.last_name))
                    .setText(contacts.get(position).getLastName());

            ((TextView)view.findViewById(R.id.phone_number))
                    .setText(contacts.get(position).getPhoneNumber());

            return view;
        }

        private List<Contact> contacts;
    }

    private String  username;
    private String jwt;
    private DeleteContactModel contactModel;
    private ListView listView;
    private ContactViewModel contactViewModel;
    @SuppressLint("StaticFieldLeak")
    private static volatile DeleteContact instance;
}
