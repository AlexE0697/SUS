package com.example.sus.Activities.Core;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sus.Activities.Models.User_Model;
import com.example.sus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class UserProfile extends AppCompatActivity {

    User_Model current_user;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        context = this;

        //add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                current_user = dataSnapshot.getValue(User_Model.class);

                if(current_user != null) {
                    ((TextView) findViewById(R.id.full_name_tv)).setText(current_user.getfull_name());
                    ((TextView) findViewById(R.id.email_tv)).setText(current_user.getemail());
                    ((TextView) findViewById(R.id.student_no_tv)).setText(current_user.getstudent_no());

                    if(current_user.getprofile_photo_url() != null && !current_user.getprofile_photo_url().equals("")) {
                        Picasso.with(context).load(current_user.getprofile_photo_url()).into(((CircularImageView) findViewById(R.id.profile_photo_civ)));
                    }
                } else {
                    Toast.makeText(context, "Could not retrieve User Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
