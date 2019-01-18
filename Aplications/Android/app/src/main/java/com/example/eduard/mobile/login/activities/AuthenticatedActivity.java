package com.example.eduard.mobile.login.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eduard.mobile.R;
import com.example.eduard.mobile.charts.ChartFragment;
import com.example.eduard.mobile.contacts.AddContact;
import com.example.eduard.mobile.contacts.Contacts;
import com.example.eduard.mobile.contacts.DeleteContact;
import com.example.eduard.mobile.contacts.UpdateContact;
import com.example.eduard.mobile.login.handles.LogoutHandle;
import com.example.eduard.mobile.repository.room.database.AppDatabase;
import com.example.eduard.mobile.repository.room.entity.Fetch;
import com.example.eduard.mobile.services.SyncService;
import com.example.eduard.mobile.utils.configuration.Config;
import com.example.eduard.mobile.utils.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;


public class AuthenticatedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        stopService(new Intent(this, SyncService.class));
        try {
            unregisterReceiver(receiver);
            manager.unregisterNetworkCallback(callback);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        // if the user is still authenticated then the back button does nothing
        if (isAuthenticated()) {
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.user_menu_logout:
                handleLogout();
                return false;

            case R.id.all_contacts:
                setFragment(
                        Contacts.getInstance(),
                        getDataForFragment()
                );
                break;

            case R.id.add_contact:
                setFragment(AddContact.getInstance(), getDataForFragment());
                break;

            case R.id.delete_contact:
                setFragment(DeleteContact.getInstance(), getDataForFragment());
                break;

            case R.id.update_contact:
                setFragment(UpdateContact.getInstance(), getDataForFragment());
                break;

            case R.id.user_menu_stats:
                setFragment(ChartFragment.getInstance(), getDataForFragment());
                break;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticated);

        drawerLayout = findViewById(R.id.drawer_layout);
        config = Config.getInstance();

        setToolbars();
        saveToken();

        if (savedInstanceState == null) {
            setFragment(new Contacts(), getDataForFragment());
            navigationView.setCheckedItem(R.id.all_contacts);
        }

        registerOnlineOffline();
    }

    private void registerOnlineOffline() {
        manager = (ConnectivityManager) this
                .getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        callback = new ConnectivityManager.NetworkCallback() {

            @Override
            public void onAvailable(Network network) {
                super.onAvailable(network);

                GuiUtils.showSnackBar(
                        navigationView,
                        "Online"
                );
            }

            @Override
            public void onLost(Network network) {
                super.onLost(network);
                GuiUtils.showSnackBar(
                        navigationView,
                        "Offline"
                );
            }
        };

        manager.registerDefaultNetworkCallback(callback);
    }


    /**
     * Method that is called when the logout button is pressed
     */
    private void handleLogout() {

        drawerLayout.closeDrawer(GravityCompat.START);

        String name = config.getProperty(this, "shared-pref-name");

        SharedPreferences preferences = getSharedPreferences(name, Context.MODE_PRIVATE);

        new LogoutHandle(
                this,
                preferences.getString("username", ""),
                preferences.getString("jwt-token", "")
        ).execute();
    }

    /**
     * Save jwt token in shared preferences, to be accessed
     */
    private void saveToken() {

        String name = config.getProperty(getApplicationContext(), "shared-pref-name");

        Intent intent = getIntent();

        String jwtToken = intent.getStringExtra("Token");
        String username = intent.getStringExtra("Username");

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.logged_in_username))
                .setText(username);

        if (jwtToken.isEmpty()) {
            return;
        }

        SharedPreferences preferences = getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("jwt-token", jwtToken);
        editor.putString("username", username);
        editor.apply();


        intent = new Intent(this, SyncService.class);

        intent.putExtra("jwt", jwtToken);
        intent.putExtra("username", username);

        startService(intent);
    }

    public void addReceiverToUnregister(BroadcastReceiver receiverN) {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        receiver = receiverN;
    }

    /**
     * @return true if the user is authenticated of false contrary
     */
    private boolean isAuthenticated() {

        String name = config.getProperty(this, "shared-pref-name");

        SharedPreferences preferences = getSharedPreferences(name, Context.MODE_PRIVATE);

        return !preferences.getString("jwt-token", "").isEmpty();
    }

    /**
     * Set the toolbar and makes the connection
     * between hamburger menu and drawer layout
     * Also sets the callback that will be called when an item is checked
     */
    private void setToolbars() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.drawer_opened, R.string.drawer_closed);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setFragment(Fragment fragment, Bundle bundle) {

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

        fragmentTransaction.replace(R.id.fragment_container, fragment)
                .commit();

        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private Bundle getDataForFragment() {

        Bundle bundle = new Bundle();

        String name = config.getProperty(getApplicationContext(), "shared-pref-name");
        SharedPreferences preferences = getSharedPreferences(name, Context.MODE_PRIVATE);

        String jwtToken = preferences.getString("jwt-token", "");
        String username = preferences.getString("username", "");

        bundle.putString("jwt", jwtToken);
        bundle.putString("username", username);

        return bundle;
    }

    private Config config;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ConnectivityManager manager;
    private ConnectivityManager.NetworkCallback callback;
    private BroadcastReceiver receiver;
}
