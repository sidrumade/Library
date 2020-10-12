package com.example.library3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.library3.ui.home.Card;
import com.example.library3.ui.home.Users;
import com.example.library3.ui.home.UsersAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private AppBarConfiguration mAppBarConfiguration;
    private List<Users> usersList; //declare variable to store users
    private UsersAdapter uadapter;
    private Boolean exist=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(  //this is responsible for switching tabs like home gallery etc.
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Get the Intent that started this activity and extract the string

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Toast.makeText(this,user.getDisplayName(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this,user.getPhotoUrl()+"", Toast.LENGTH_SHORT).show();

        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        ImageView imgvw = (ImageView)hView.findViewById(R.id.imageView);
        TextView tv1 = (TextView)hView.findViewById(R.id.textView1);
        Picasso.get().load(user.getPhotoUrl()).resize(200, 200).centerCrop().into(imgvw);
        tv1.setText(user.getDisplayName());
        usersList=new ArrayList<>(); //initialize empy array list
        checkUserExist(user);



    }

    public void checkUserExist(final FirebaseUser user) {

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Users u=new Users(user.getDisplayName(),user.getEmail());
//        db.collection("non-premium-users").add(u).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Log.d("navdrawactivity","DocumentSnapshot added with ID: " + documentReference.getId());
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w("navdrawactivity", "Error adding document", e);
//            }
//        });

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("non-premium-users")//db.collection("non-premium-users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot d:list){
                                Users u=d.toObject(Users.class);
                                usersList.add(u);
                                //Toast.makeText(getApplicationContext(),u.getEmail(),Toast.LENGTH_SHORT).show();
                            }
                        }
                        List<String> emails=new ArrayList<String>();
                        for (Users i : usersList) {
                            Toast.makeText(getApplicationContext(),"i have"+i.getEmail(),Toast.LENGTH_SHORT).show();
                            emails.add(i.getEmail());
                        }
                        if (!emails.contains(user.getEmail())){
                            System.out.println("user not exit so adding to db");
                            Users u=new Users(user.getDisplayName(),user.getEmail(),user.getUid());
                            db.collection("non-premium-users").add(u).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    //og.d("navdrawactivity","DocumentSnapshot added with ID: " + documentReference.getId());
                                    Toast.makeText(getApplicationContext(),"welcome new user",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("navdrawactivity", "Error adding document", e);
                                }
                            });
                        }

                    }
                });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {  //responsible for opening menubar option
        switch (item.getItemId()) {
            case R.id.action_signout:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void signOut() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>
                        () {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Toast.makeText(getApplicationContext(),"successful logout", Toast.LENGTH_LONG).show();

                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }
                });

        FirebaseAuth.getInstance().signOut(); //firebase sign out


    }

    }
