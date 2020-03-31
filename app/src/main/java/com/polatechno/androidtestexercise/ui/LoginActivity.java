package com.polatechno.androidtestexercise.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.polatechno.androidtestexercise.AppUtils.MyHelperMethods;
import com.polatechno.androidtestexercise.R;
import com.polatechno.androidtestexercise.model.PartnerAccount;
import com.polatechno.androidtestexercise.network.ApiClient;
import com.polatechno.androidtestexercise.network.ApiInterface;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //UI views
    private Button buttonLogin;
    private EditText inputLogin, inputPassword;
    private ProgressBar progressBarHorizontal;

    //Data variables
    private PartnerAccount partnerUser;
    private Gson gson;
    private ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(getString(R.string.app_name) + getString(R.string.loginScreenSuffix));
        }

        gson = new Gson();

        // Checks if user has already logged in or not. if true opens MainActivity
        if (MyHelperMethods.getLoggedInUser(this, gson) != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        apiService = ApiClient.getClient().create(ApiInterface.class);

        initViews();
    }

    private void initViews() {
        inputLogin = findViewById(R.id.inputLogin);
        inputPassword = findViewById(R.id.inputPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        progressBarHorizontal = findViewById(R.id.progressBarHorizontal);
        progressBarHorizontal.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogin) {

            boolean isFormValidated = true;
            if (TextUtils.isEmpty(inputLogin.getText())) {
                isFormValidated = false;
                inputLogin.setError("Login field is required...");
            }

            if (TextUtils.isEmpty(inputPassword.getText())) {
                isFormValidated = false;
                inputPassword.setError("Password field is required...");
            }

            if (isFormValidated) {
                checkCredentials();
            }

        }
    }

    //checks if provided credentials are valid or not, by communicating with backend
    private void checkCredentials() {

        progressBarHorizontal.setVisibility(View.VISIBLE);

        //This object will be used as form post body params
        partnerUser = new PartnerAccount();
        partnerUser.setLogin(inputLogin.getText().toString());
        partnerUser.setPassword(inputPassword.getText().toString());

        Call<String> apiCall = apiService.login(partnerUser);

        apiCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBarHorizontal.setVisibility(View.GONE);

                //if response success, save data as cache, and open MainActivity
                if (response.code() == 200) {
                    if (response.body().length() > 0) {
                        partnerUser.setPasskey(response.body());
                        MyHelperMethods.saveUserInfo(LoginActivity.this, partnerUser, gson);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                } else {

                    Toast.makeText(LoginActivity.this, "Authentication failed. Check your credentials and try again!", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                progressBarHorizontal.setVisibility(View.GONE);

                Log.i("onFailure", "Retrofit OnFailure: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }
}
