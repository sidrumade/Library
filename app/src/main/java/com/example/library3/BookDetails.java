package com.example.library3;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.library3.ui.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class BookDetails extends AppCompatActivity {
    private static final String TAG = "BookDetails";
    private ImageView frame;
    private TextView name, author, desc;
    private Button butread, butdownload;
    private ProgressBar fprogressbar;
    private Bundle extras;
    private FirebaseFirestore db;
    private Boolean r = false;
    private Boolean w = false;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    // Create a reference to a file from a Google Cloud Storage URI
    private StorageReference gsReference,gsReferencebook;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        extras = getIntent().getExtras();
        //System.out.println(extras.getString("book_name"));
        //System.out.println(extras.getString("book_author"));
        //System.out.println(extras.getString("book_url"));
        //System.out.println(extras.getString("book_thumbnail"));
        frame = (ImageView) findViewById(R.id.frame);
        name = (TextView) findViewById(R.id.bname);
        author = (TextView) findViewById(R.id.bauth);
        desc = (TextView) findViewById(R.id.bdesc);
        butread = (Button) findViewById(R.id.butread);
        butdownload = (Button) findViewById(R.id.butdownload);
        fprogressbar = (ProgressBar) findViewById(R.id.f_progressbar);
        gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(extras.getString("book_url"));
        user = FirebaseAuth.getInstance().getCurrentUser();

        name.setText(extras.getString("book_name"));
        author.setText(extras.getString("book_author"));
        gsReferencebook=FirebaseStorage.getInstance().getReferenceFromUrl(extras.getString("book_thumbnail"));
        gsReferencebook.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL

                Picasso.get().load(uri).resize(200, 300).centerCrop().into(frame);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Picasso.get().load("https://source.unsplash.com/200x300/?books").resize(100, 130).centerCrop().into(frame);
            }
        });

        desc.setText(extras.getString("book_type"));
        if (extras.getString("book_type").equals("Premium")) {
            butread.setEnabled(false);
            butdownload.setEnabled(false);
            //Toast.makeText(getApplicationContext(), user.getEmail()+"", Toast.LENGTH_SHORT).show();
            db = FirebaseFirestore.getInstance();
            db.collection("premium-users")//db.collection("non-premium-users")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    Users u = d.toObject(Users.class);
                                    //Toast.makeText(getApplicationContext(), u.getEmail().equals(user.getEmail()) + " got", Toast.LENGTH_SHORT).show();
                                    if (user.getEmail().equals(u.getEmail()) == true) {
                                        //Toast.makeText(getApplicationContext(), "you are premium so removing restrictions", Toast.LENGTH_SHORT).show();
                                        butread.setEnabled(true);
                                        butdownload.setEnabled(true);
                                        break;
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Premium User Access Only", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(getApplicationContext(),PaymentActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                        }
                    });
        }

        butdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                //snakbar shows when gust try to access premium for gust users
                if (extras.getString("book_type").equals("Premium")) {
                    //Toast.makeText(getApplicationContext(), "Premium User Access", Toast.LENGTH_SHORT).show();
                    r = isReadStoragePermissionGranted();
                    w = isWriteStoragePermissionGranted();
                    if(r.equals(true) && w.equals(true)){
                        downloadPdfFile();
                    }
                } else {
                    r = isReadStoragePermissionGranted();
                    w = isWriteStoragePermissionGranted();
                    if(r.equals(true) && w.equals(true)){
                        downloadPdfFile();
                    }
                }


            }

        });


//        Handler handler = new Handler(getMainLooper());
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                fprogressbar.setVisibility(View.INVISIBLE);
//            }
//        });

        butread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);
                if (r.equals(true) && w.equals(true)) { //check for permission
                    File applictionFile = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS) + "/" + extras.getString("book_name") + ".pdf");
                    if (applictionFile != null && applictionFile.exists() == false) { //if pdf is not downloaded

                        View v1 = findViewById(android.R.id.content).getRootView();
                        Snackbar.make(v1, "Book is not downloaded", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    } else {
                        Intent i = new Intent(getApplicationContext(), Openpdf.class);
                        i.putExtra("book_name", extras.getString("book_name"));
                        startActivity(i);
                    }
                } else {
                    r = isReadStoragePermissionGranted();
                    w = isWriteStoragePermissionGranted();
                }


            }
        });


    }

    public void downloadPdfFile() {
        File applictionFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "/" + extras.getString("book_name") + ".pdf");


        if (applictionFile != null && applictionFile.exists() == false) {

            gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    Toast.makeText(getApplicationContext(), "download started", Toast.LENGTH_SHORT).show();
                    // [START_EXCLUDE silent]
                    fprogressbar.setVisibility(View.VISIBLE);
                    //stop user interaction while progress is going on
                    butdownload.setEnabled(false);

                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                    request.setTitle("Download");
                    request.setDescription(extras.getString("book_name"));

                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);

                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + extras.getString("book_name") + ".pdf");

                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);


                    Toast.makeText(getApplicationContext(), extras.getString("book_name") + " download complete", Toast.LENGTH_SHORT).show();
                    butdownload.setEnabled(true);
                    fprogressbar.setVisibility(View.GONE);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    butdownload.setEnabled(true);
                    fprogressbar.setVisibility(View.GONE);
                }
            });
        } else {
            View v = findViewById(android.R.id.content).getRootView();
            Snackbar.make(v, "Book is already downloaded", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();


            //Toast.makeText(this,extras.getString("book_name")+" is already downloaded", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted1");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted1");
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted2");

                return true;
            } else {

                Log.v(TAG, "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted2");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(TAG, "External storage2");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                    
                    // Toast.makeText(this,"permission granted1",Toast.LENGTH_SHORT).show();
                } else {
                    //progress.dismiss();
                }
                break;

            case 3:
                Log.d(TAG, "External storage1");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                    //SharePdfFile();
                    // Toast.makeText(this,"permission granted2",Toast.LENGTH_SHORT).show();
                } else {
                    //progress.dismiss();
                }
                break;
        }
    }
}