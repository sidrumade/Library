package com.example.library3;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.library3.databinding.ActivityPaymentBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class PaymentActivity extends AppCompatActivity {
    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    String amount;
    String plane;
    String name = "DAMODAR SAHDEV RUMADE";
    String upiId = "damodarrumade143@oksbi";
    String transactionNote = "Library Payment";
    String status;
    Uri uri;
    private ActivityPaymentBinding binding; //this name is depend on our xml file name
    private RadioButton radioAmountButton;

    private static boolean isAppInstalled(Context context, String packagename) {
        try {
            context.getPackageManager().getApplicationInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private static Uri getUpiPaymentUri(String name, String upiId, String transactionNote, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", transactionNote)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_payment);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.googlePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int selectedId = binding.radioGroup.getCheckedRadioButtonId();
                //radioAmountButton = (RadioButton) findViewById(selectedId);
                Integer price = 0;
                //Toast.makeText(getApplicationContext(),"working "+radioAmountButton.getId()+" "+R.id.radioButton1,Toast.LENGTH_LONG).show();
                if (selectedId == R.id.radioButton1) {
                    price = 1;
                    plane = "1";
                    //Toast.makeText(getApplicationContext(),"1 pressed",Toast.LENGTH_SHORT).show();

                } else if (selectedId == R.id.radioButton2) {
                    price = 2;
                    plane = "6";
                    //Toast.makeText(getApplicationContext(),"2 pressed",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong! contact ot developer", Toast.LENGTH_LONG).show();
                    return;
                }
                amount = price.toString();
                if (!amount.isEmpty()) {
                    uri = getUpiPaymentUri(name, upiId, transactionNote, amount);
                    payWithGPay(); //calling for payment
                } else {
                    Toast.makeText(PaymentActivity.this, "Account is required", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });


    }

    private void payWithGPay() {
        if (isAppInstalled(this, GOOGLE_PAY_PACKAGE_NAME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        } else {
            Toast.makeText(PaymentActivity.this, "Please Installl the app first", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            status = data.getStringExtra("Status").toLowerCase();
        }

        if ((RESULT_OK == resultCode) && status.equals("success")) {
            updateUserStatus();
            Toast.makeText(PaymentActivity.this, "Transaction Successful", Toast.LENGTH_LONG).show();
        } else {
            //updateUserStatus();//remove this //this for adding user to db
            Toast.makeText(PaymentActivity.this, "Transaction Fail", Toast.LENGTH_LONG).show();
        }
    }

    private void updateUserStatus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String name = user.getDisplayName();
        String email = user.getEmail();

        removeGustUserData(user, db); //remove data from gust user

        Calendar calendar1=Calendar.getInstance();
        Date date=calendar1.getTime();
        String full = new SimpleDateFormat("yyyy-MM-dd").format(date); // e.g. 2015-01-18
        String start = full;//calendar1.getTime().toString(); //getting current time
        String expirationDate = null;

        if (plane == "1") {
            calendar1.add(Calendar.MONTH, 1);
            date=calendar1.getTime();
            expirationDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        } else if (plane == "6") {
            calendar1.add(Calendar.MONTH, 5);
            date=calendar1.getTime();
            expirationDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        }

        addPremiumUserData(db, name, email, plane, start, expirationDate); //adding data to premium users
        //after that sign out
        signOut();

    }


    private void removeGustUserData(FirebaseUser user, FirebaseFirestore db) {
        db.collection("non-premium-users").document(user.getEmail())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error deleting document", e);
                    }
                });


    }

    private void addPremiumUserData(FirebaseFirestore db, String name, String email, String plane, String start, String expirationDate) {
        //adding data to db
        Map<String, Object> premium = new HashMap<>();
        premium.put("name", name);
        premium.put("email", email);
        premium.put("plane", plane);
        premium.put("start", start);
        premium.put("expire", expirationDate);

        // Add a new document with a generated ID
        db.collection("premium-users")
                .document(email)
                .set(premium).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Log.d(TAG, "DocumentSnapshot successfully written!");
                Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log.w(TAG, "Error writing document", e);
                    }
                });


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