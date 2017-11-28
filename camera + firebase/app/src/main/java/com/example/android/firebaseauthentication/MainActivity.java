package com.example.android.firebaseauthentication;


/**
 * Created by taylan on 16.11.2017.
 *
 * eklenecekler
 *      firebase storage ile calışmaya devam
 *      face detectiona bak
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import management.GroupManagement;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;                  // reference to the database
    private DatabaseReference myRef;                    // reference to what to write
    private ChildEventListener listenerChild;           // listener to listen the child nodes
    private FirebaseAuth mAuth;                         // authentication
    private FirebaseAuth.AuthStateListener mAuthListener;   // listener to listen the authentication situation
    public static final int RC_SIGN_IN = 1;
    private ArrayList<Item> menuList;
    private GridView gridview;
    private TextView noConnection;
    private ImageAdapter myAdapter;
    private boolean isConnected;
    private boolean isInitialized = false;
    private String eventID = "VAPPU"; // event ID dummy data
    private String userID;


    FirebaseOptions options = new FirebaseOptions.Builder()
            .setApplicationId("1:648550706523:android:8704e81e156a4b1e")
            .setApiKey("AIzaSyBBmC6bGNvikJr7hWhpwrIJRh_zEgzmj5U")
            .setDatabaseUrl("https://fir-authentication-482c6.firebaseio.com")
            .setProjectId("fir-authentication-482c6")
            .build();
    //AIzaSyBBmC6bGNvikJr7hWhpwrIJRh_zEgzmj5U
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        if(!isInitialized)
            initializeFirebase();

        gridview = (GridView) findViewById(R.id.gridview);
        noConnection = (TextView) findViewById(R.id.noConnection);

        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected)
        {

            noConnection.setVisibility(View.INVISIBLE);

            mAuth = FirebaseAuth.getInstance();

            database = FirebaseDatabase.getInstance();
            myRef = database.getReference().child("messages"); //database.getReference() gives the root node

            setGrid();
            // check if the user is signed in or not
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        userID = user.getUid();
                        onSignedInInitialize(user.getDisplayName()); // if signed in
                    } else {
                        // User is signed out
                        // sign in page will come here, code below is all about UI
                        onSignedOutCleanUp();
                        /*startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setIsSmartLockEnabled(false)
                                        .setAvailableProviders(
                                                Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
                                                        ))
                                        .build(),
                                RC_SIGN_IN);;*/
                        startActivityForResult(
                                // Get an instance of AuthUI based on the default app
                                AuthUI.getInstance().createSignInIntentBuilder().build(),
                                RC_SIGN_IN);

                    }

                }
            };
        }
        else
        {

            gridview.setVisibility(View.INVISIBLE);
            noConnection.setVisibility(View.VISIBLE);
        }

    }
    public void initializeFirebase()
    {
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps(this);
        for(FirebaseApp app:firebaseApps)
        {
            if(app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
            {
               isInitialized = true;
            }
            else
            {
                isInitialized = false;
            }

        }
        if(!isInitialized)
            FirebaseApp.initializeApp(this, options);


    }
    private void setGrid()
    {
        gridview.setVisibility(View.VISIBLE);

        menuList = new ArrayList<>();
        menuList.add(new Item("Gallery",R.drawable.gallery));
        menuList.add(new Item("Take Photo",R.drawable.takephoto));
        menuList.add(new Item("Management",R.drawable.management));
        menuList.add(new Item("Settings",R.drawable.settings));


        myAdapter =new ImageAdapter(this,R.layout.linear_layout_content_main_page,menuList);
        gridview.setAdapter(myAdapter);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id){
                Intent i;
                switch (position) {
                    case 0:
                        i = new Intent(getApplicationContext(), PhotoGallery.class);
                        // Pass image index
                        i.putExtra("id", position);
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent(getApplicationContext(), TakePhoto.class);
                        // Pass image index
                        i.putExtra("eventID", eventID);
                        i.putExtra("userID", userID);

                        i.putExtra("id", position);
                        startActivity(i);
                        break;
                    case 2:
                        i = new Intent(getApplicationContext(), GroupManagement.class);
                        // Pass image index
                        i.putExtra("id", position);
                        startActivity(i);
                        break;
                    case 3:
                        i = new Intent(getApplicationContext(), SettingsActivity.class);
                        // Pass image index
                        i.putExtra("id", position);
                        startActivity(i);
                        break;
                    default:
                        break;
                }

            }
        });
    }

    // this part protects to not to go back
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
                finish();
                Toast.makeText(getApplicationContext(), "Signed in cancelled", Toast.LENGTH_SHORT).show();
            }

        }
    }
    // method for the menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(isConnected)
        {
            inflater.inflate(R.menu.main_menu, menu);
            return true;
        }
        return false;

    }

    @Override
    public void onResume() {
        super.onStart();
        if(mAuth != null)
        {
            mAuth.addAuthStateListener(mAuthListener); // if app is resumed, continue with authentication listener
        }
    }
    @Override
    public void onPause() {
        super.onPause();
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
