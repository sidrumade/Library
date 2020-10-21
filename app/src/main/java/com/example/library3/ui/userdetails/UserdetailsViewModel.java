package com.example.library3.ui.userdetails;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.library3.ui.PremiumUsers;
import com.example.library3.ui.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserdetailsViewModel extends ViewModel {

    List<String> non_prem_emails = new ArrayList<>();//=new ArrayList<String>();
    List<String> prem_emails = new ArrayList<>();//=new ArrayList<String>();
    private MutableLiveData<String> mText;
    private MutableLiveData<String> UserName;
    private MutableLiveData<String> UserEmail;
    private MutableLiveData<String> Subscription;
    private MutableLiveData<String> StartDate;
    private MutableLiveData<String> EndDate;
    private FirebaseUser user;
    private FirebaseFirestore db;


    public UserdetailsViewModel() {
        mText = new MutableLiveData<>();
        UserName = new MutableLiveData<>();
        UserEmail = new MutableLiveData<>();
        Subscription = new MutableLiveData<>();
        StartDate = new MutableLiveData<>();
        EndDate = new MutableLiveData<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        getAllUsersInfo();


    }

    public MutableLiveData<String> getUserName() {
        return UserName;
    }

    public MutableLiveData<String> getUserEmail() {
        return UserEmail;
    }

    public MutableLiveData<String> getSubscription() {
        return Subscription;
    }

    public MutableLiveData<String> getStartDate() {
        return StartDate;
    }

    public MutableLiveData<String> getEndDate() {
        return EndDate;
    }

    public LiveData<String> getText() {
        return mText;
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
                                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (non_prem_emails.contains(user.getEmail()) == true) {
                                            UserName.setValue(user.getDisplayName());
                                            UserEmail.setValue(user.getEmail());
                                            Subscription.setValue("Non-Premium");
                                            StartDate.setValue("-");
                                            EndDate.setValue("-");
                                        } else if (prem_emails.contains(user.getEmail()) == true) {

                                            db.collection("premium-users").document(user.getEmail())
                                                    .get()
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            PremiumUsers pu = documentSnapshot.toObject(PremiumUsers.class);

                                                            UserName.setValue(pu.getName());
                                                            UserEmail.setValue(pu.getEmail());
                                                            Subscription.setValue("Premium");
                                                            StartDate.setValue("Start: "+pu.getStart());
                                                            EndDate.setValue("End :"+pu.getExpire());
                                                            //System.out.println("my exp:"+pu.getExpire());
                                                        }
                                                    });


                                        }
                                    }

                                });
                    }

                });
    }
}


