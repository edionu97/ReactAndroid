package com.example.eduard.mobile.contacts;


import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eduard.mobile.R;
import com.example.eduard.mobile.repository.room.entity.Contact;
import com.example.eduard.mobile.utils.NetworkVolatile;
import com.example.eduard.mobile.utils.gui.GuiUtils;
import com.example.eduard.mobile.utils.validators.UpdateContactValidator;
import com.example.eduard.mobile.view_models.ContactViewModel;
import com.example.eduard.mobile.view_models.UpdateViewModel;
import com.example.eduard.mobile.view_models.factories.ContactViewModelFactory;
import com.example.eduard.mobile.view_models.factories.UpdateContactModelFactory;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateContact extends NetworkVolatile implements Observer<List<Contact>> {

    public  static  String BROADCAST ="update-contact";

    public static UpdateContact getInstance() {

        if (instance == null) {
            synchronized (UpdateContact.class) {
                if (instance == null) {
                    instance = new UpdateContact();
                }
            }
        }

        return instance;
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
        @SuppressLint({"ViewHolder", "InflateParams"})
        public View getView(int position, View convertView, ViewGroup parent) {

            view = getLayoutInflater().inflate(R.layout.select_addapter, null);

            ((TextView) view.findViewById(R.id.first_name))
                    .setText(contacts.get(position).getFirstName());

            ((TextView) view.findViewById(R.id.last_name))
                    .setText(contacts.get(position).getLastName());


            return view;
        }

        private View view;
        private List<Contact> contacts;
    }


    public UpdateContact() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        if (bundle != null) {
            jwt = bundle.getString("jwt");
            username = bundle.getString("username");
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_contact, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if (getActivity() == null) {
            return;
        }

        viewModel = ViewModelProviders.of(
                getActivity(),
                new UpdateContactModelFactory(
                        getActivity(),
                        jwt,
                        username
                )
        ).get(UpdateViewModel.class);

        register(getActivity(), BROADCAST);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        if (getActivity() == null) {
            return;
        }

        ViewModelProviders.of(
                getActivity(),
                new ContactViewModelFactory(getActivity(), jwt, username)
        ).get(ContactViewModel.class).getContacts().observe(this, this);

        firstName = view.findViewById(R.id.first_name_update);
        lastName = view.findViewById(R.id.second_name_update);
        phoneNumber = view.findViewById(R.id.phone_number_update);

        view.findViewById(R.id.update_button).setOnClickListener(this::handleUpdateClick);
        view.findViewById(R.id.imageViewUpdate).setOnClickListener(this::handleImageClick);
    }

    @Override
    public void connectionLost() {
        viewModel.createRepository();
    }

    @Override
    public void connectionAvailable() {
        viewModel.createRepository();
    }

    @Override
    public void broadcastReceived() {
        viewModel.refreshView();
    }

    @Override
    public void onChanged(@Nullable List<Contact> contacts) {
        this.contacts = contacts;
    }

    private void handleUpdateClick(View view) {

        viewModel.createRepository();

        String first = firstName.getText().toString();
        String last = lastName.getText().toString();
        String phone = phoneNumber.getText().toString();

        try{

            UpdateContactValidator.validate(
                    first, last, phone
            );

            viewModel.updateContact(
                    first, last, phone
            );

        }catch (Exception e){
            GuiUtils.displayErrorDialog(
                    e.getMessage(), getActivity(), "Error"
            );
        }
    }

    private void handleImageClick(View view) {

        viewModel.createRepository();

        GuiUtils.displayContentDialog(
                getActivity(),
                "Select contact to update",
                (ad, index)->{
                    setTexts(
                            (Contact)ad.getItem(index)
                    );
                },
                new ListAdapter(contacts)
        );
    }

    private void setTexts(Contact contact){

        firstName.setText(contact.getFirstName());
        lastName.setText(contact.getLastName());
        phoneNumber.setText(contact.getPhoneNumber());
    }

    @Override
    public void onResume() {
        viewModel.createRepository();
        super.onResume();
    }


    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;

    private List<Contact> contacts;

    private String username;
    private String jwt;

    private UpdateViewModel viewModel;

    private static volatile UpdateContact instance;


}
