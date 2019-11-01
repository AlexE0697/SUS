package com.example.sus.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sus.Activities.Adapters.ArticleAdapter;
import com.example.sus.Activities.Models.Article_Model;
import com.example.sus.Activities.Models.User_Model;
import com.example.sus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    User_Model current_user;

    Context context;

    Menu menu;

    ArrayList<User_Model> all_users = new ArrayList<>();
    ArrayList<Article_Model> all_articles = new ArrayList<>();

    static Dialog new_article_dialog;
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

    //Layout Manager
    LinearLayoutManager mLayoutManager;

    //google maps related code
    private static final String TAG = "Home";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get current user
        context = this;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Get Current user
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    current_user = dataSnapshot.getValue(User_Model.class);

                    if (current_user != null) {
                        //Toast.makeText(context, "You are a " + current_user.getaccess_level(), Toast.LENGTH_SHORT).show();
                        updateNavHeader();

                        switch (current_user.getaccess_level()) {
                            case "Student":
                                //Do stuff for a student here
                                if (menu != null) {
                                    menu.findItem(R.id.action_add_article).setVisible(false);
                                }
                                break;

                            case "Admin":
                                //Do stuff for an Admin here
                                if (menu != null) {
                                    menu.findItem(R.id.action_add_article).setVisible(true);
                                }
                                break;

                            default:break;
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Get all articles
        FirebaseDatabase.getInstance().getReference().child("subjects").child("articles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                all_articles.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds != null) {
                        all_articles.add(ds.getValue(Article_Model.class));
                    }
                }

                if (all_articles.size() > 0) {
                    ArticleAdapter articleAdapter = new ArticleAdapter(all_articles, context);
                    mLayoutManager = new LinearLayoutManager(context);
                    mLayoutManager.setReverseLayout(true);
                    mLayoutManager.setStackFromEnd(true);
                    ((RecyclerView) findViewById(R.id.article_rv)).setLayoutManager(mLayoutManager);
                    ((RecyclerView) findViewById(R.id.article_rv)).setAdapter(articleAdapter);
                }

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Your permission to the database has been refused", Toast.LENGTH_SHORT).show();
            }
        });



        //Get a list of all user models
        FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds != null) {
                        User_Model user = ds.child("profile").getValue(User_Model.class);
                        all_users.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //ini
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }


    //showing the articles
    public void showNewArticleDialog() {
        new_article_dialog = new Dialog(this);
        new_article_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        new_article_dialog.setContentView(R.layout.new_article_dialog);
        new_article_dialog.setCancelable(true);

        lp.copyFrom(new_article_dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        new_article_dialog.show();


        ((ImageButton) new_article_dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_article_dialog.dismiss();
            }
        });

        ((Button) new_article_dialog.findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Article_Model model = new Article_Model();
                model.settitle(((EditText) new_article_dialog.findViewById(R.id.title_et)).getText().toString());
                model.setdescription(((EditText) new_article_dialog.findViewById(R.id.description_et)).getText().toString());
                model.setarticle_by(current_user.getfull_name());
                model.settimestamp(getTimestamp());

                //Write new article model to Fire base
                    FirebaseDatabase.getInstance().getReference().child("subjects").child("articles").child(model.gettimestamp()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Article added successfully", Toast.LENGTH_SHORT).show();
                            new_article_dialog.dismiss();
                        } else {
                            Toast.makeText(context, "Failed to add new article", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


    }


    //pressing the back button on the phone
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            startActivity(new Intent(Home.this, Home.class));
        }

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    //creating the menu in action bar to create events or articles
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    //pressing the action button that will create events and articles
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_article) {
            //Show new Article Dialog input box for the Admin to create a new Artcile
            showNewArticleDialog();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_home:
                startActivity(new Intent(context, Home.class));

                break;

            case R.id.nav_events:
                startActivity(new Intent(context, EventsActivity.class));

                break;

            case R.id.nav_maps:
                startActivity(new Intent(context, MapsActivity.class));
                break;

            case R.id.nav_user_profile:
                startActivity(new Intent(context, UserProfile.class));

                break;

            case R.id.nav_contact:
                startActivity(new Intent(context, ContactActivity.class));

                break;

            case R.id.nav_logout: {

                FirebaseAuth.getInstance().signOut();
                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
                finish();
                break;
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateNavHeader() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.nav_userName);
        TextView navUserEmail = headerView.findViewById(R.id.nav_userEmail);
        ImageView navUserPhoto = headerView.findViewById(R.id.nav_userPhoto);

        navUserEmail.setText(current_user.getemail());
        navUserName.setText(current_user.getfull_name());

        //Set profile photo to Nav Bar if available
        if (current_user.getprofile_photo_url() != null && !current_user.getprofile_photo_url().equals("")) {
            Picasso.with(context).load(current_user.getprofile_photo_url()).into(((ImageView) findViewById(R.id.nav_userPhoto)));
        }

    }

        public static String getTimestamp() {
            DateFormat timestamp_format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date currentDate = new Date();
            return timestamp_format.format(currentDate);
    }

}



