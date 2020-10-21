package com.example.library3;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.library3.ui.PremiumUsers;
import com.example.library3.ui.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NavigationDrawerActivity extends AppCompatActivity {
    private static final String TAG = "NavigationDrawerActivity";
    List<String> non_prem_emails = new ArrayList<>();//=new ArrayList<String>();
    List<String> prem_emails = new ArrayList<>();//=new ArrayList<String>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AppBarConfiguration mAppBarConfiguration;
    private String identity = "Non Premium";
    private TextView tv2;

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
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                startActivity(intent);

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(  //this is responsible for switching tabs like home premium etc.
                R.id.nav_free, R.id.nav_premium, R.id.nav_userdetails)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Get the Intent that started this activity and extract the string

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        View hView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        ImageView imgvw = (ImageView) hView.findViewById(R.id.imageView);
        TextView tv1 = (TextView) hView.findViewById(R.id.myname);
        tv2 = (TextView) hView.findViewById(R.id.myidentity);
        Picasso.get().load(user.getPhotoUrl()).resize(200, 200).centerCrop().into(imgvw);
        tv1.setText(user.getDisplayName());

        getAllUsersInfo();

    }


    private void getAllUsersInfo() {

        db.collection("non-premium-users")//db.collection("non-premium-users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Users u = d.toObject(Users.class);
                                non_prem_emails.add(u.getEmail());
                            }
                            setUserIdentity(non_prem_emails);

                        }
                    }

                    private void setUserIdentity(final List<String> non_prem_emails) {
                        db.collection("premium-users")//db.collection("non-premium-users")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                            for (DocumentSnapshot d : list) {
                                                Users u = d.toObject(Users.class); //just taking email so using same class
                                                prem_emails.add(u.getEmail());

                                            }
                                            setUserIdentity(non_prem_emails, prem_emails);

                                        }
                                    }

                                    private void setUserIdentity(List<String> non_prem_emails, List<String> prem_emails) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if ((non_prem_emails.contains(user.getEmail()) == false) && (prem_emails.contains(user.getEmail()) == false)) {
                                            System.out.println("user not exit so adding to db");
                                            Users u = new Users(user.getDisplayName(), user.getEmail(), user.getUid());
                                            db.collection("non-premium-users").document(u.getEmail()).set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                    //Toast.makeText(getApplicationContext(), "welcome new gust user", Toast.LENGTH_SHORT).show();
                                                    identity = "Non Premium";
                                                }
                                            })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            //Log.w(TAG, "Error deleting document", e);
                                                        }
                                                    });
                                        } else if ((non_prem_emails.contains(user.getEmail()) == false) && (prem_emails.contains(user.getEmail()) == true)) {
                                            System.out.println("premium user exist");
                                            Users u = new Users(user.getDisplayName(), user.getEmail(), user.getUid());
                                            //Toast.makeText(getApplicationContext(), "welcome old premium user", Toast.LENGTH_SHORT).show();
                                            identity = "Premium";
                                            identityValidation(user, db);

                                        }
                                        tv2.setText(identity);

                                    }

                                    private void identityValidation(final FirebaseUser user, final FirebaseFirestore db) {
                                        Log.d("test", "running");
                                        db.collection("premium-users").document(user.getEmail())
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        PremiumUsers pu = documentSnapshot.toObject(PremiumUsers.class);

                                                        //System.out.println("my exp:"+pu.getExpire());
                                                        Calendar calendar1 = Calendar.getInstance();
                                                        Date date = calendar1.getTime();//current date

                                                        Calendar calendar2 = Calendar.getInstance();
                                                        Date datex = calendar2.getTime();

                                                        try {
                                                            datex = new SimpleDateFormat("yyyy-MM-dd").parse(pu.getExpire());
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                        //Toast.makeText(getApplicationContext(),date+" "+datex,Toast.LENGTH_SHORT).show();

                                                        calendar2.setTime(datex);
                                                        int days = (int) Duration.between(calendar1.toInstant(), calendar2.toInstant()).toDays();
                                                        //Toast.makeText(getApplicationContext(),"expire in: "+days,Toast.LENGTH_SHORT).show();
                                                        if (days < 0) {
                                                            removePremiumUser(user, db);
                                                        }
                                                        else if (days>0 && days < 10){//contact developer if this generate crash.
                                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "M_CH_ID")
                                                                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                                                                    .setContentTitle("Premium Ending soon...")
                                                                    .setContentText(days+ " remaining...")
                                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                                        }
                                                    }

                                                    private void removePremiumUser(FirebaseUser user, FirebaseFirestore db) {
                                                        db.collection("premium-users").document(user.getEmail())
                                                                .delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                                        signOut();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        //Log.w(TAG, "Error deleting document", e);
                                                                    }
                                                                });
                                                    }


                                                });


                                    }

                                });


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
                        Toast.makeText(getApplicationContext(), "successful logout", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
