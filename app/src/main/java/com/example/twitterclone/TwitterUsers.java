package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    ArrayList<String> tUsers;
    ArrayAdapter adapter;
    String followedUsers = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        FancyToast.makeText(TwitterUsers.this, "Welcome "+
                ParseUser.getCurrentUser().getUsername(), FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS, false).show();

        listView = findViewById(R.id.listView);
        tUsers = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked,
                tUsers);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);

        // we use it in case there is no users to query
        try {

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(e == null && objects.size() > 0){

                        for(ParseUser twitterUser : objects){

                            tUsers.add(twitterUser.getUsername());
                        }
                        listView.setAdapter(adapter);
                        /** this for loop is to prevent following user 2 times */
                        for(String twitterUser : tUsers){
                            /* make sur that there is a value inside Following column so that the
                            app doesn't crash */
                            if(ParseUser.getCurrentUser().getList("Following") != null){
                                if(ParseUser.getCurrentUser().getList("Following").contains(twitterUser)) {

                                    /**extra functionality : get all the following of the current user*/
                                    followedUsers = followedUsers + twitterUser + "\n";

                                    /* if twitter user is inside tUsers array then check the item listview
                                        that is related to this twitterUser object */
                                    listView.setItemChecked(tUsers.indexOf(twitterUser), true);
                                }
                            }
                        }
                        FancyToast.makeText(TwitterUsers.this, ParseUser.getCurrentUser().
                                        getUsername() + " is following:\n" + followedUsers, FancyToast.LENGTH_SHORT,
                                FancyToast.INFO, false).show();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout_item:

                ParseUser.getCurrentUser().logOutInBackground(e -> {

                    Intent intent = new Intent(TwitterUsers.this, LoginActivity.class);
                    startActivity(intent);
                    this.finish();

                });
                break;
            case  R.id.sendTweetItem:
                Intent intent = new Intent(TwitterUsers.this, ShareTweetActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        CheckedTextView checkedTextView = (CheckedTextView) view;
        if(checkedTextView.isChecked()){

            FancyToast.makeText(TwitterUsers.this, "You have followed " +
                            tUsers.get(i) , FancyToast.LENGTH_SHORT, FancyToast.INFO,
                    false).show();
            // follow a user
            ParseUser.getCurrentUser().add("Following", tUsers.get(i));
        }else{
            FancyToast.makeText(TwitterUsers.this, "You have unfollow "+
                            tUsers.get(i), FancyToast.LENGTH_SHORT, FancyToast.INFO,
                    false).show();
            // unfollow a user
            ParseUser.getCurrentUser().getList("Following").remove(tUsers.get(i));
            // create a new list that contains the following after removing the unfollow user
            List currentUserFollowingList = ParseUser.getCurrentUser().getList("Following");
            // remove all the users inside Following column
            ParseUser.getCurrentUser().remove("Following");
            // put the list created before inside Following column
            ParseUser.getCurrentUser().put("Following", currentUserFollowingList);
        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null ) {
                    FancyToast.makeText(TwitterUsers.this,
                                    " saved", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS,
                            false).show();
                }
            }
        });
    }
}