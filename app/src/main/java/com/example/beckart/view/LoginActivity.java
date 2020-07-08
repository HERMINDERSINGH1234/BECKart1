package com.example.beckart.view;

import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.example.beckart.R;
import com.example.beckart.ViewModel.LoginViewModel;
import com.example.beckart.databinding.ActivityLoginBinding;
import com.example.beckart.storage.LoginUtils;
import com.example.beckart.utils.Validation;

import static com.example.beckart.storage.LanguageUtils.loadLocale;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));

        binding.buttonLogin.setOnClickListener(this);
        binding.textViewSignUp.setOnClickListener(this);
        binding.forgetPassword.setOnClickListener(this);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // If user logged in, go directly to ProductActivity
        if (LoginUtils.getInstance(this).isLoggedIn()) {
            goToProductActivity();
        }
    }

    private void logInUser() {
        String email = binding.inputEmail.getText().toString();
        String password = binding.inputPassword.getText().toString();

        if (email.isEmpty()) {
            binding.inputEmail.setError(getString(R.string.email_required));
            binding.inputEmail.requestFocus();
        }

        if (Validation.isValidEmail(email)) {
            binding.inputEmail.setError(getString(R.string.enter_a_valid_email_address));
            binding.inputEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            binding.inputPassword.setError(getString(R.string.password_required));
            binding.inputPassword.requestFocus();
            return;
        }

        if (!Validation.isValidPassword(password)) {
            binding.inputPassword.setError(getString(R.string.password__at_least_8_characters));
            binding.inputPassword.requestFocus();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dialog);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        loginViewModel.getLoginResponseLiveData(email,password).observe((LifecycleOwner) this, loginApiResponse -> {
            if (!loginApiResponse.isError()) {
                LoginUtils.getInstance(this).saveUserInfo(loginApiResponse);
                Toast.makeText(this, loginApiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                goToProductActivity();
            }else {
                progressDialog.cancel();
                Toast.makeText(this, loginApiResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLogin:
                logInUser();
                break;
            case R.id.textViewSignUp:
                goToSignUpActivity();
                break;
            case R.id.forgetPassword:
                goToPasswordAssistantActivity();
                break;
        }
    }

    private void goToSignUpActivity() {
        Intent intent = new Intent(this, com.example.beckart.view.SignUpActivity.class);
        startActivity(intent);
    }

    private void goToProductActivity() {
        Intent intent = new Intent(this, com.example.beckart.view.ProductActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToPasswordAssistantActivity() {
        Intent intent = new Intent(this, com.example.beckart.view.PasswordAssistantActivity.class);
        startActivity(intent);
    }

}
