package com.example.sus.Activities.Core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.sus.Activities.Models.Event_Model;
import com.example.sus.Activities.Models.LocationModel;
import com.example.sus.Activities.Models.User_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseHandler {

    public interface onSingleUserProfileAcquired {
        void onSingleUserProfileAcquired(User_Model user_model);
    }

    public interface onAllEventsAcquired {
        void onAllEventsAcquired(ArrayList<Event_Model> allEvents);
    }

    public interface onAllLocationsAcquired {
        void onAllLocationsAcquired(ArrayList<LocationModel> allLocations);
    }


    /**
     * Returns a User_Model for the specified UID that are not marked as deleted
     *
     * @param context  : Context of the calling activity
     * @param user_uid : Filter Users by UID
     */
    public static void getSingleUserProfile(final Context context, String user_uid) {
        final onSingleUserProfileAcquired[] listener = new onSingleUserProfileAcquired[1];

        FirebaseDatabase.getInstance().getReference().child("users").child(user_uid).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(User_Model.class) != null) {
                    User_Model user = dataSnapshot.getValue(User_Model.class);

                    listener[0] = (FirebaseHandler.onSingleUserProfileAcquired) context;
                    listener[0].onSingleUserProfileAcquired(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Get All Events from Firebase
     */
    public static void getAllEvents(final Context context) {
        final onAllEventsAcquired[] listener = new onAllEventsAcquired[1];
        final ArrayList<Event_Model> allEvents = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("subjects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild("events")) {
                    listener[0] = (FirebaseHandler.onAllEventsAcquired) context;
                    listener[0].onAllEventsAcquired(allEvents);
                    return;
                }

                for (DataSnapshot ds : dataSnapshot.child("events").getChildren()) {
                    if (ds != null) {
                        allEvents.add(ds.getValue(Event_Model.class));
                    }
                }
                listener[0] = (FirebaseHandler.onAllEventsAcquired) context;
                listener[0].onAllEventsAcquired(allEvents);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    /**
     * Pass in the timestamp to delete the event
     * TODO: Handle removing the last event from the list
     * @param context
     * @param eventTimestamp
     */
    public static void removeEvent(final Context context, String eventTimestamp) {
        FirebaseDatabase.getInstance().getReference().child("subjects").child("events").child(eventTimestamp).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Event deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    /**
     * Get all locations
     * @param context
     */
    public static void getAllLocations(final Context context) {
        final onAllLocationsAcquired[] listener = new onAllLocationsAcquired[1];
        final ArrayList<LocationModel> allLocations = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("collections").child("locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    LocationModel location = ds.getValue(LocationModel.class);
                    allLocations.add(location);
                }
                listener[0] = (FirebaseHandler.onAllLocationsAcquired) context;
                listener[0].onAllLocationsAcquired(allLocations);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
