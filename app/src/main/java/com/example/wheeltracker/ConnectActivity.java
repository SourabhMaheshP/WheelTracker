package com.example.wheeltracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class ConnectActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectFragment.ConnectFragmentListener {

    private DrawerLayout drawer;
    private TextView greeting_name,greeting_email;
    private NavigationView navigationView;
    private String name,email;
    private UserData userData;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        progressDialog = new ProgressDialog(this);
        userData = UserData.getInstance(ConnectActivity.this);

        //fetching the values required user info
        // from UserData class
        name = (String) userData.getValue("name");
        email = (String) userData.getValue("email");

        //extracting the TextView id from nav_header.xml file
        //and setting value to the value fetched from above
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        /*setting the value*/

        greeting_name = headerView.findViewById(R.id.greeting_name);

        if(name!=null)
            greeting_name.setText("Hello!, "+name);

        greeting_email = headerView.findViewById(R.id.greeting_email);

        if(email!=null)
            greeting_email.setText(email);

        /*--------------------------------*/

        Toolbar custom_toolbar = findViewById(R.id.custom_toolbar2);
        setSupportActionBar(custom_toolbar);
        getSupportActionBar().setTitle("");

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, custom_toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.hamcolor));

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ConnectFragment()).commit();
            navigationView.setCheckedItem(R.id.connect);
        }

        SharedPreferences prefs = getSharedPreferences("LoginData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("LastActivity", getClass().getName());
        editor.commit();

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.connect :
                getSupportFragmentManager().beginTransaction().
                        setCustomAnimations(R.anim.enterright,R.anim.exitright,R.anim.enterright,R.anim.exitright)
                        .replace(R.id.fragment_container,
                        new ConnectFragment()).commit();
                break;
            case R.id.aboutus:
            getSupportFragmentManager().beginTransaction().
                    setCustomAnimations(R.anim.enterright,R.anim.exitright,R.anim.enterright,R.anim.exitright).
                    replace(R.id.fragment_container,
                            new AboutFragment()).commit();
                break;
            case R.id.contactus:
                getSupportFragmentManager().beginTransaction().
                        setCustomAnimations(R.anim.enterright,R.anim.exitright,R.anim.enterright,R.anim.exitright).
                        replace(R.id.fragment_container,
                        new ContactFragment()).commit();
                break;
            case R.id.logout :
                    //IMPORTANT :
                    // for logout change src_status and dest_src to 0 and clear the key from shared preference
                    // clear uesr data hashmap
                new Logout(ConnectActivity.this).start();
                break;
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().
                        setCustomAnimations(R.anim.enterright,R.anim.exitright,R.anim.enterright,R.anim.exitright).
                        replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                    break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //this function is called when the connect button is clicked
    //which is called by connectFragment (Interface)
    @Override
    public void startConnecting(Button connect_device) {
       String sensor_id = UserData.getInstance(ConnectActivity.this).getValue("sensor_id").toString();
        new ConnectToDevice(ConnectActivity.this,progressDialog).execute(sensor_id);
    }
}
