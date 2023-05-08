package com.graduate.app.users;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.appcompat.app.AppCompatActivity;

import com.graduate.app.R;
import com.graduate.app.databinding.ActivityPersonalInfoBinding;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PersonalInfoActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private ActivityPersonalInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPersonalInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                            drawerLayout,
                            R.string.nav_open,
                            R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.
        final Button editButton = binding.editButton;


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences =
                getSharedPreferences("com.graduate.app", Context.MODE_PRIVATE);

        Map<String, ?> allPreferences = sharedPreferences.getAll();
        String active_userUUID = (String) allPreferences.get("active_user");
        Set<String> allInfos = (Set<String>)allPreferences.get(active_userUUID);

        final TextView firstnameView = binding.firstname;
        final TextView lastnameView = binding.lastname;
        final TextView entranceYearView = binding.entranceYear;
        final TextView graduateYearView = binding.graduateYear;
        final TextView countryView = binding.country;
        final TextView cityView = binding.city;
        final TextView companyView = binding.company;
        final TextView emailView = binding.email;
        final TextView phoneView = binding.phone;

        Iterator<String> iterator = allInfos.iterator();

        String key, value;
        while (iterator.hasNext()){
            String key_value = iterator.next();
            String[] key_value_array = key_value.split(": ");
            if(key_value_array.length == 2){
                key = key_value_array[0];
                value = key_value_array[1];
                switch (key){
                    case "firstname":
                        firstnameView.setText(value);
                        break;
                    case "lastname":
                        lastnameView.setText(value);
                        break;
                    case "entranceYear":
                        entranceYearView.setText(value);
                        break;
                    case "graduateYear":
                        graduateYearView.setText(value);
                        break;
                    case "country":
                        countryView.setText(value);
                        break;
                    case "city":
                        cityView.setText(value);
                        break;
                    case "company":
                        companyView.setText(value);
                        break;
                    case "email":
                        emailView.setText(value);
                        break;
                    case "phone":
                        phoneView.setText(value);
                        break;
                }
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}