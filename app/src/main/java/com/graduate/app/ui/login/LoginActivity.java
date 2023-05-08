package com.graduate.app.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.graduate.app.R;
import com.graduate.app.databinding.ActivityLoginBinding;
import com.graduate.app.ui.register.RegisterActivity;
import com.graduate.app.users.PersonalInfoActivity;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getSharedPreferences("com.graduate.app", Context.MODE_PRIVATE).getAll().clear();
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button redirectToRegisterButton = binding.redirectToRegister;
//        final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

//        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
//            @Override
//            public void onChanged(@Nullable LoginResult loginResult) {
//                if (loginResult == null) {
//                    return;
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//                if (loginResult.getError() != null) {
//                    showLoginFailed(loginResult.getError());
//                }
//                if (loginResult.getSuccess() != null) {
//                    updateUiWithUser(loginResult.getSuccess());
//                }
//                setResult(Activity.RESULT_OK);
//
//                //Complete and destroy login activity once successful
//                finish();
//            }
//        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    loginViewModel.login(usernameEditText.getText().toString(),
//                            passwordEditText.getText().toString());
//                }
//                return false;
//            }
//        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);
//                loginViewModel.login(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
                SharedPreferences sharedPreferences =
                        getSharedPreferences("com.graduate.app", Context.MODE_PRIVATE);

                Map.Entry foundedUser = sharedPreferences.getAll().entrySet().stream()
                                .filter((val) -> {
                                    if(!val.getKey().equals("active_user")){
                                        Set<String> valueSet = (Set<String>) val.getValue();
                                        if(valueSet.size() != 6){
                                            return false;
                                        }
                                        String emailVal = valueSet.stream().filter((x) -> {
                                            x.split(": ");
                                            return x.split(": ")[0].equals("email");
                                        }).findFirst().orElse(null);
                                        if(emailVal != null){
                                            if (emailVal.split(": ")[1].equals(usernameEditText.getText().toString())){
                                                return true;
                                            }
                                        }
                                        return false;
                                    }
                                    return false;
                                }).findFirst().orElse(null);
                if(foundedUser == null) {
//                      new AlertDialog.Builder(getApplicationContext())
//                            .setTitle("Delete entry")
//                            .setMessage("Are you sure you want to delete this entry?")
//                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    // Continue with delete operation
//                                }
//                            })
//                            .setNegativeButton(android.R.string.no, null)
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .show();
                    usernameEditText.setError("user name not found");
                }
                else {
                    Set<String> valueSet = (Set<String>) foundedUser.getValue();
                    String passwordValue = valueSet.stream().filter((x) -> {
                        x.split(": ");
                        return x.split(": ")[0].equals("password");
                    }).findFirst().get();
                    if(passwordValue.split(": ")[1].equals(passwordEditText.getText().toString())){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        long isActiveUserKeyExist =
                                sharedPreferences.getAll().keySet().stream()
                                                .filter((x) -> x.equals("active_user")).count();
                        if(isActiveUserKeyExist == 1){
                            editor.remove("active_user");
                        }
                        editor.putString("active_user", (String) foundedUser.getKey());
                        editor.apply();
                        startActivity(new Intent(LoginActivity.this,
                                                                PersonalInfoActivity.class));
                    }
                    else {
                        passwordEditText.setError("username or password are not valid");
                    }
                }
            }
        });

        redirectToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}