package com.example.library3.ui;


import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library3.BookDetails;
import com.example.library3.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {
    List<Card> mDataset;
    private Context cont;
    private StorageReference gsReference;

    //Provide a suitable constructor (depends on the kind of dataset)
    public CardAdapter(List<Card> myDataset) {
        this.mDataset = myDataset;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView bookname;
        public TextView bookauthor;
        public TextView bookdescription;
        public ImageView thumbnail;
        public LinearLayoutCompat pdfitemv;

        public MyViewHolder(final View itemview) {
            super(itemview);
            bookname=(TextView)itemview.findViewById(R.id.book_name);
            bookauthor=(TextView)itemview.findViewById(R.id.bookauthor);
            bookdescription=(TextView)itemview.findViewById(R.id.bookdisc);
            thumbnail=(ImageView)itemview.findViewById(R.id.thumbnail);
            pdfitemv=(LinearLayoutCompat)itemview.findViewById(R.id.pdfitem);


        }
    }



    // Create new views (invoked by the layout manager)
    @Override
    public CardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.pdfitemview,parent, false);
//
//        MyViewHolder vh = new MyViewHolder(v);
//        return vh;
        cont=parent.getContext();
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.pdfitemview,parent,false);//passing that catd layout file
        return new MyViewHolder(view);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Card data=mDataset.get(position);
        holder.bookname.setText(data.getName());
        holder.bookauthor.setText(data.getAuthor());
        holder.bookdescription.setText(data.book_type);

        gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(data.getThumbnail());
        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL

                Picasso.get().load(uri).resize(300, 400).centerCrop().into(holder.thumbnail);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Picasso.get().load("https://source.unsplash.com/200x300/?books").resize(300, 400).centerCrop().into(holder.thumbnail);
            }
        });

        holder.pdfitemv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(data.getName());
                Intent intent = new Intent(cont, BookDetails.class);// New activity
                intent.putExtra("book_name",data.getName());
                intent.putExtra("book_author", data.getAuthor());
                intent.putExtra("book_url",data.getBook_url());
                intent.putExtra("book_thumbnail", data.getThumbnail());
                intent.putExtra("book_type",data.getBook_type());
                cont.startActivity(intent); //cont is context here

            }
        });



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return  mDataset.size();//return
    }
}