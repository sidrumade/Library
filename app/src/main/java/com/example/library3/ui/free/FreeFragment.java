package com.example.library3.ui.free;

import com.example.library3.ui.Card;
import com.example.library3.ui.CardAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library3.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import java.util.List;

public class FreeFragment extends Fragment {

    private FreeViewModel freeViewModel;
    //for recycler view
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //for recycler view

//    private List<Users> usersList; //declare variable to store users
//    private UsersAdapter uadapter;

    private List<Card> cardList; //declare variable to store users
    private CardAdapter cadapter;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        freeViewModel =
                ViewModelProviders.of(this).get(FreeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_free, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        //freeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
        //    @Override
        //    public void onChanged(@Nullable String s) {  //requesting mText
               // textView.setText(s);   //assigning that mText to textView
         //   }
        //});
        recyclerView = (RecyclerView)root.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        usersList=new ArrayList<>(); //initialize empy array list
//        uadapter=new UsersAdapter(usersList);
//        recyclerView.setAdapter(uadapter);
        cardList=new ArrayList<>(); //initialize empy array list
        cadapter=new CardAdapter(cardList);
        recyclerView.setAdapter(cadapter);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("free-books")//db.collection("non-premium-users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot d:list){
//                                Users u=d.toObject(Users.class);
//                                usersList.add(u);
                                Card c=d.toObject(Card.class);
                                c.setBook_type("Free");
                                cardList.add(c);
                            }
                            //uadapter.notifyDataSetChanged();
                            cadapter.notifyDataSetChanged();
                        }

                    }
                });
    }


}
