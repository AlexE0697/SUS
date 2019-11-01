package com.example.sus.Activities.Core;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sus.Activities.Adapters.EventAdapter;
import com.example.sus.Activities.Models.Event_Model;
import com.example.sus.Activities.Models.User_Model;
import com.example.sus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity implements FirebaseHandler.onSingleUserProfileAcquired,
        FirebaseHandler.onAllEventsAcquired {

    static Dialog new_event_dialog;

    Menu menu;
    User_Model current_user;
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        initComponents();
    }

    private void initComponents() {
        //SHOW A PROGRES DIALOD
        context = this;

        //add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get Current User Profile
        FirebaseHandler.getSingleUserProfile(this, FirebaseAuth.getInstance().getCurrentUser().getUid());

        //Get all events
        FirebaseHandler.getAllEvents(this);
    }

    private void setupAdaptor(ArrayList<Event_Model> events) {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        ((RecyclerView) findViewById(R.id.events_rv)).setLayoutManager(mLayoutManager);
        ((RecyclerView) findViewById(R.id.events_rv)).setAdapter(new EventAdapter(events, context));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_event) {
            //Show new Article Dialog input box for the Admin to create a new Artcile
            showNewEventDialog();

        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Only allow Admin Users see the menu to add new Events
     * @param menu
     * @return Boolean : visibility
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (current_user.getaccess_level().equalsIgnoreCase("admin")) {
            getMenuInflater().inflate(R.menu.ehome, menu);
            return true;
        }
        return false;
    }

    /**
     * Show a dialog to allow the user to add new Events to the database
     */
    public void showNewEventDialog() {
        new_event_dialog = new Dialog(this);
        new_event_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        new_event_dialog.setContentView(R.layout.new_event_dialog);
        new_event_dialog.setCancelable(true);

        lp.copyFrom(new_event_dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        new_event_dialog.show();


        new_event_dialog.findViewById(R.id.event_bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_event_dialog.dismiss();
            }
        });

        new_event_dialog.findViewById(R.id.event_bt_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event_Model model = new Event_Model();
                model.setevent_title(((EditText) new_event_dialog.findViewById(R.id.event_title_et)).getText().toString());
                model.setevent_description(((EditText) new_event_dialog.findViewById(R.id.event_description_et)).getText().toString());
                model.setevent_price(((EditText) new_event_dialog.findViewById(R.id.event_price_et)).getText().toString());
                model.setevent_by(current_user.getfull_name());
                model.setevent_timestamp(Utils.getTimestamp());

                //Write new event model to Fire base
                FirebaseDatabase.getInstance().getReference().child("subjects").child("events").child(model.getevent_timestamp()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            getApplicationContext();
                            Toast.makeText(getApplicationContext(), "Event added successfully", Toast.LENGTH_SHORT).show();
                            new_event_dialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to add new event", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    //pressing the back button on the phone
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                finish();
                startActivity(new Intent(EventsActivity.this, EventsActivity.class));
            }
        }

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public void onSingleUserProfileAcquired(User_Model user_model) {
        this.current_user = user_model;
    }

    @Override
    public void onAllEventsAcquired(ArrayList<Event_Model> allEvents) {
        if (allEvents.size() > 0) {
            setupAdaptor(allEvents);
        }


    }
}
