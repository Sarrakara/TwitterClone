package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SendTweetActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtSendTweet;
    Button btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        edtSendTweet = findViewById(R.id.edtSendTweet);
        btnSend = findViewById(R.id.btnSendTweet);

        btnSend.setOnClickListener(SendTweetActivity.this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSendTweet:
                ParseObject parseObject = new ParseObject("MyTweet");
                parseObject.put("tweet", edtSendTweet.getText().toString());
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                final ProgressDialog progressDialog = new ProgressDialog(SendTweetActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            FancyToast.makeText(SendTweetActivity.this, ParseUser
                                            .getCurrentUser().getUsername()+"'s tweet:\n\"" + edtSendTweet
                                            .getText().toString() + "\" is saved!", FancyToast.LENGTH_LONG,
                                    FancyToast.SUCCESS, false).show();
                        }else{
                            FancyToast.makeText(SendTweetActivity.this, e.getMessage(), FancyToast
                                    .LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                        progressDialog.dismiss();
                    }
                });
                break;


        }
    }
}