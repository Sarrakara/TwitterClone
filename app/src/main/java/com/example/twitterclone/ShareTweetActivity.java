package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShareTweetActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtShareTweet;
    Button btnSend;

    ListView viewTweetsListView;
    Button btnViewTweets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_tweet);
        setTitle("Share & View tweets");

        edtShareTweet = findViewById(R.id.edtSendTweet);
        btnSend = findViewById(R.id.btnShareTweet);

        viewTweetsListView = findViewById(R.id.viewTweetsListView);
        btnViewTweets = findViewById(R.id.btnViewTweets);

        btnSend.setOnClickListener(ShareTweetActivity.this);
        btnViewTweets.setOnClickListener(ShareTweetActivity.this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnShareTweet:
                if(edtShareTweet.getText().toString().equals("")){
                    FancyToast.makeText(ShareTweetActivity.this, "Add a tweet!",
                            FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                }else {
                    ParseObject parseObject = new ParseObject("MyTweet");
                    parseObject.put("tweet", edtShareTweet.getText().toString());
                    parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                    final ProgressDialog progressDialog = new ProgressDialog(ShareTweetActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(ShareTweetActivity.this, ParseUser
                                                .getCurrentUser().getUsername() + "'s tweet:\n\"" + edtShareTweet
                                                .getText().toString() + "\" is saved!", FancyToast.LENGTH_LONG,
                                        FancyToast.SUCCESS, false).show();
                            } else {
                                FancyToast.makeText(ShareTweetActivity.this, e.getMessage(), FancyToast
                                        .LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;

            case R.id.btnViewTweets:
                final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
                // create a simple adapter
                final SimpleAdapter simpleAdapter = new SimpleAdapter(ShareTweetActivity
                        .this, tweetList, android.R.layout.simple_list_item_2, new String[]
                        {"tweetUsername", "tweetValue"}, new int[]{android.R.id.text1,
                         android.R.id.text2});
                // to prevent crashing the app if there is no tweet
                try {
                    ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
                    // get the list of the users that the current user is following
                    parseQuery.whereContainedIn("username", ParseUser.getCurrentUser().
                            getList("Following"));
                    parseQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if(e == null && objects.size() > 0){
                                for (ParseObject tweetObject : objects){
                                    // in each iteration we create a new hashmap
                                    HashMap<String, String> userTweet = new HashMap<>();
                                    userTweet.put("tweetUsername", tweetObject.getString("username"));
                                    userTweet.put("tweetValue", tweetObject.getString("tweet"));
                                   /* add the hashmap objects to the array list that accepts objects of
                                    of type hashmap */
                                    tweetList.add(userTweet);
                                }
                                viewTweetsListView.setAdapter(simpleAdapter);
                            }
                        }
                    });

                }catch(Exception e){
                    e.printStackTrace();
                }

                break;

        }
    }
}