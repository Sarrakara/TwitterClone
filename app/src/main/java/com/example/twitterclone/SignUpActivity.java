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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    // UI components
    private EditText edtSignupEmail, edtSignupUsername, edtSignupPassword;
    private TextView txtLoginActivity;
    private Button btnSignup;
    private ConstraintLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activity);

        rootLayout = findViewById(R.id.rootLayout);
        edtSignupEmail = findViewById(R.id.edtSignupEmail);
        edtSignupUsername = findViewById(R.id.edtSignupUsername);
        edtSignupPassword = findViewById(R.id.edtSignupPassword);
        btnSignup = findViewById(R.id.btnSignup);
        txtLoginActivity = findViewById(R.id.txtLoginActivity);

        edtSignupPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                // if the key that is pressed is the enter key
                if(keyCode == keyEvent.KEYCODE_ENTER &&
                        // if there is an action down on this key
                        keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    // btn sign up is a view
                    onClick(btnSignup);
                }
                return false;
            }
        });

        btnSignup.setOnClickListener(SignUpActivity.this);
        txtLoginActivity.setOnClickListener(SignUpActivity.this);
        rootLayout.setOnClickListener(SignUpActivity.this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignup:

                if(edtSignupEmail.getText().toString().equals("") ||
                edtSignupUsername.getText().toString().equals("") ||
                edtSignupPassword.getText().toString().equals("")){

                    FancyToast.makeText(SignUpActivity.this,
                            "Email, Username, Password are required!",
                            FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                            false).show();

                }else {

                    final ParseUser appUser = new ParseUser();
                    appUser.setEmail(edtSignupEmail.getText().toString());
                    appUser.setUsername(edtSignupUsername.getText().toString());
                    appUser.setPassword(edtSignupPassword.getText().toString());

                    ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing up...");
                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                FancyToast.makeText(SignUpActivity.this,
                                        appUser.getUsername() + " is signed up",
                                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                                        false).show();
                                edtSignupEmail.setText("");
                                edtSignupUsername.setText("");
                                edtSignupPassword.setText("");
                            } else {
                                FancyToast.makeText(SignUpActivity.this,
                                        "there was an error " + e.getMessage(),
                                        FancyToast.LENGTH_SHORT, FancyToast.ERROR,
                                        false).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }

                break;
            case R.id.txtLoginActivity:
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);

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
}