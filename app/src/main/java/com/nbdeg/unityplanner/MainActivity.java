package com.nbdeg.unityplanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nbdeg.unityplanner.data.Assignments;
import com.nbdeg.unityplanner.data.Classes;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings({"CanBeFinal", "MismatchedQueryAndUpdateOfCollection"})
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView dueAssignments;
    private FirebaseUser user;
    private String UID;

    private ArrayList<Assignments> assignmentList = new ArrayList<>();
    private ArrayList<Classes> classList = new ArrayList<>();
    private ArrayList<String> classListNames = new ArrayList<>();

    private final String TAG = "Database";
    private static final int RC_SIGN_IN = 145;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Signs in user if not logged in
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .build(),
                    RC_SIGN_IN);
        }
        else {
            user = FirebaseAuth.getInstance().getCurrentUser();
            //noinspection ConstantConditions
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        // Sets button to send user to add assignment page when clicked
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch Add Homework Activity
                Intent intent = new Intent(MainActivity.this, addAssignment.class);
                intent.putExtra("classListNames", classListNames);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dueAssignments = (TextView) findViewById(R.id.assignments_due);

        // Sets disk persistence
        try {FirebaseDatabase.getInstance().setPersistenceEnabled(true);}
        catch (com.google.firebase.database.DatabaseException e){
            e.printStackTrace();
        }

        // FirebaseDatabase.getInstance().getReference().setValue(null);   // Use to reset database
        DatabaseReference assignmentDb = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("assignments");
        DatabaseReference classDb = FirebaseDatabase.getInstance().getReference().child("users").child(UID).child("classes");

        // Gets all assignments
        assignmentDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                assignmentList.clear();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Assignments assignment = userSnapshot.getValue(Assignments.class);
                    assignmentList.add(assignment);
                    Log.i(TAG, "Assignment loaded: " + assignment.getAssignmentName());

                    dueAssignments.setText("");
                    dueAssignments.append(assignment.getAssignmentName() + "\n\n\n");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error loading assignments: " + databaseError.getMessage());
            }
        });

        // Gets all classes
        classDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                classList.clear();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Classes mClass = userSnapshot.getValue(Classes.class);
                    classList.add(mClass);
                    classListNames.add(mClass.getClassName());
                    Log.i(TAG, "Class loaded: " + mClass.getClassName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error getting assignments: " + databaseError.getMessage());
            }
        });

        View headerView = navigationView.getHeaderView(0);
        final TextView userName = (TextView) headerView.findViewById(R.id.user_name);
        final TextView userEmail = (TextView) headerView.findViewById(R.id.user_email);
        final ImageView userPhoto = (ImageView) headerView.findViewById(R.id.user_photo);

        userName.setText(user.getDisplayName());
        userEmail.setText(user.getEmail());
        Picasso.with(this).load(user.getPhotoUrl())
                .resize(150, 150)
                .into(userPhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) userPhoto.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        userPhoto.setImageDrawable(imageDrawable);
                    }
                    @Override
                    public void onError() {
                        userPhoto.setImageResource(R.mipmap.ic_launcher);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_assignments) {
            Intent intent = new Intent(MainActivity.this, assignmentViewer.class);
            intent.putExtra("classListNames", classListNames);
            startActivity(intent);
        } else if (id == R.id.nav_classes) {
            startActivity(new Intent(MainActivity.this, classViewer.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout) {
            AuthUI.getInstance()
                    .delete(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                View view = findViewById(R.id.main_view);
                                Snackbar snackbar = Snackbar
                                        .make(view, "Signed Out Successfully", Snackbar.LENGTH_LONG);
                                snackbar.show();

                                startActivityForResult(
                                        AuthUI.getInstance()
                                                .createSignInIntentBuilder()
                                                .setProviders(Arrays.asList(
                                                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                                                .build(),
                                        RC_SIGN_IN);
                            } else {
                                View view = findViewById(R.id.main_view);
                                Snackbar snackbar = Snackbar
                                        .make(view, "Sign Ou t Failed, Please Try Again.", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}