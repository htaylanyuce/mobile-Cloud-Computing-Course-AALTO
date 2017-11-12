package com.example.android.firebaseauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;                  // reference to the database
    private DatabaseReference myRef;                    // reference to what to write
    private ChildEventListener listenerChild;           // listener to listen the child nodes
    private FirebaseAuth mAuth;                         // authentication
    private FirebaseAuth.AuthStateListener mAuthListener;   // listener to listen the authentication situation
    public static final int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button push = (Button) findViewById(R.id.push);


        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference().child("messages"); //database.getReference() gives the root node

        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myRef.push().setValue("Data come");
             }
        });

        // check if the user is signed in or not
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignedInInitialize(user.getDisplayName()); // if signed in
                 } else {
                    // User is signed out
                    // sign in page will come here, code below is all about UI
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);;
                 }

            }
        };
    }

    // this part protect to not to go back
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN)
        {
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(getApplicationContext(), "Signed in", Toast.LENGTH_SHORT).show();

            }
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(getApplicationContext(), "Signed in cancelled", Toast.LENGTH_SHORT).show();

            }


        }
    }
    // method for the menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener); // if app is resumed, continue with authentication listener
    }
    @Override
    public void onPause() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener); // if app is stopped, continue with authentication listener
        }
        detachListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout:
                AuthUI.getInstance().signOut(this); // when menu item is clicked, sign out
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onSignedInInitialize(String username)
    {
        attachListener();

    }
    private void onSignedOutCleanUp()
    {
       detachListener();
    }
    private void attachListener()   // it attaches child database listener to the database
    {
        if(listenerChild != null) {


            listenerChild = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String str = dataSnapshot.getValue(String.class);
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            myRef.addChildEventListener(listenerChild); // child event listener is added to the main listener
        }
    }
    private void detachListener() // it detaches child database listener to the database
    {
        if(listenerChild != null) {
            myRef.removeEventListener(listenerChild);
            listenerChild = null;
        }

    }
}
