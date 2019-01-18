package com.example.eduard.mobile.contacts;


import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.eduard.mobile.R;
import com.example.eduard.mobile.utils.NetworkVolatile;
import com.example.eduard.mobile.utils.gui.GuiUtils;
import com.example.eduard.mobile.utils.validators.UpdateContactValidator;
import com.example.eduard.mobile.view_models.AddContactViewModel;
import com.example.eduard.mobile.view_models.factories.AddContactModelFactory;

import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContact extends NetworkVolatile {

    public static  String BROADCAST = "add-contact";

    public AddContact() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        if(bundle != null){
            jwt = bundle.getString("jwt");
            username = bundle.getString("username");
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_contact, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() == null) {
            return;
        }

        contactViewModel = ViewModelProviders.of(
                getActivity(),
                new AddContactModelFactory(
                        getActivity(),
                        jwt,
                        username
                )
        ).get(AddContactViewModel.class);

        register(getActivity(), BROADCAST);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        (view.findViewById(R.id.button_add_contact))
                .setOnClickListener(this::handleAddContact);


        firstName = view.findViewById(R.id.first_name);
        lastName = view.findViewById(R.id.second_name);
        phoneNumber = view.findViewById(R.id.phone_number);
    }

    @Override
    public void connectionLost() {
        contactViewModel.createRepository();
    }

    @Override
    public void onResume() {
        contactViewModel.createRepository();
        super.onResume();
    }

    @Override
    public void connectionAvailable() {
        contactViewModel.createRepository();
    }

    @Override
    public void broadcastReceived() {
        contactViewModel.refreshView();
    }

    public void handleAddContact(View view) {

        contactViewModel.createRepository();

        String first = firstName.getText().toString();
        String last = lastName.getText().toString();
        String phone = phoneNumber.getText().toString();

        try {

            UpdateContactValidator.validate(
                    first, last, phone
            );

            contactViewModel.addContact(
                    first, last, phone
            );

        } catch (Exception e) {
            GuiUtils.displayErrorDialog(
                    e.getMessage(), getActivity(), "Error"
            );
        }
    }

    public static AddContact getInstance() {

        if (instance == null) {
            synchronized (AddContact.class) {
                if (instance == null) {
                    instance = new AddContact();
                }
            }
        }

        return instance;
    }


    private String username;
    private String jwt;

    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;

    private AddContactViewModel contactViewModel;

    @SuppressLint("StaticFieldLeak")
    private static volatile AddContact instance;


}
