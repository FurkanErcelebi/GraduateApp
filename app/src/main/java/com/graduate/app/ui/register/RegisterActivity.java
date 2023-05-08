package com.graduate.app.ui.register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.graduate.app.databinding.ActivityRegisterBinding;
import com.graduate.app.ui.login.LoginActivity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        final Button registerButton = binding.register;
        final EditText firstname = binding.firstname;
        final EditText lastname = binding.lastname;
        final EditText entanceDate = binding.entanceDate;
        final EditText graduateDate = binding.graduateDate;
        final EditText email = binding.email;
        final EditText password = binding.password;

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String firstnameValue = firstname.getText().toString();
                String lastnameValue = lastname.getText().toString();
                String entanceDateValue = entanceDate.getText().toString();
                String graduateDateValue = graduateDate.getText().toString();
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();

                System.out.println(", firstname: " + firstnameValue +
                        ", lastname: " + lastnameValue +
                        ", entanceDate: " + entanceDateValue +
                        ", graduateDate: " + graduateDateValue +
                        ", email: " + emailValue +
                        ", password: " + passwordValue);

                SharedPreferences sharedPreferences =
                        getSharedPreferences("com.graduate.app", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Set<String> user_infos = new HashSet<>();
                user_infos.add("firstname: " + firstnameValue);
                user_infos.add("lastname: " + lastnameValue);
                user_infos.add("entranceYear: " + entanceDateValue);
                user_infos.add("graduateYear: " + graduateDateValue);
                user_infos.add("email: " + emailValue);
                user_infos.add("password: " + passwordValue);
                editor.putStringSet(UUID.randomUUID().toString(), user_infos);
                editor.apply();

                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }

        });

    }

}