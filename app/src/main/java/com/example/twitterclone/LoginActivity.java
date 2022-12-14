package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;

import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // UI components
    ConstraintLayout rootLayout;
    EditText edtLoginEmail, edtLoginPassword;
    Button btnLogin;
    TextView txtSignupActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rootLayout = findViewById(R.id.rootLayout);
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        edtLoginPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                // if the key that is pressed is the enter key
                if(keyCode == keyEvent.KEYCODE_ENTER &&
                        // if there is an action down on this key
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    // btn sign up is a view
                    onClick(btnLogin);
                }
                return false;
            }
        });

        btnLogin = findViewById(R.id.btnLogin);
        txtSignupActivity = findViewById(R.id.txtSignupActivity);

        btnLogin.setOnClickListener(LoginActivity.this);
        txtSignupActivity.setOnClickListener(LoginActivity.this);
        rootLayout.setOnClickListener(LoginActivity.this);

        // Here I'm simply making sure that the parse user is valid and logged in.
        // If the parse user is valid,
        if(ParseUser.getCurrentUser() != null){
            //transitionToTwitterUsersActivity();
            ParseUser.getCurrentUser().logOut();
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLogin:

                if (edtLoginPassword.getText().toString().equals("") &&
                        edtLoginPassword.getText().toString().equals("")) {

                    FancyToast.makeText(LoginActivity.this,
                            "Email, Password are required!",
                            FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                            false).show();
                } else {

                    ParseUser appUser = new ParseUser();

                    ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("logging");
                    progressDialog.show();

                    appUser.logInInBackground(edtLoginEmail.getText().toString(),
                            edtLoginPassword.getText().toString(), new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {

                                    if (e == null && user != null) {

                                        FancyToast.makeText(LoginActivity.this,
                                                user.getUsername() + " is logged in",
                                                FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                                                false).show();
                                        edtLoginEmail.setText("");
                                        edtLoginPassword.setText("");
                                        transitionToTwitterUsersActivity();

                                    } else {

                                        FancyToast.makeText(LoginActivity.this, e.getMessage(),
                                                FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                                                false).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });
                }
                break;
            case R.id.txtSignupActivity:
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.rootLayout:
                hideKeyboard(rootLayout);
                break;
        }
    }
        public void hideKeyboard(View view){

            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);

        }

        private void transitionToTwitterUsersActivity(){
            Intent intent = new Intent(LoginActivity.this, TwitterUsers.class);
            startActivity(intent);
            finish();
        }
    }
