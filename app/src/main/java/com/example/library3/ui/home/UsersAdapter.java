package com.example.library3.ui.home;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.library3.R;

import java.util.List;

import static android.content.ContentValues.TAG;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {
    List<Users> mDataset;


     //Provide a suitable constructor (depends on the kind of dataset)
    public UsersAdapter(List<Users> myDataset) {
        this.mDataset = myDataset;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView uname;
        public MyViewHolder(View itemview) {
            super(itemview);
            //uname=(TextView)itemview.findViewById(R.id.tv_uname);

        }
    }



    // Create new views (invoked by the layout manager)
    @Override
    public UsersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.pdfitemview,parent, false);
//
//        MyViewHolder vh = new MyViewHolder(v);
//        return vh;
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.pdfitemview,parent,false);//passing that catd layout file
        return new MyViewHolder(view);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
         Users data=mDataset.get(position);
        //holder.uname.setText(data.getName());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return  mDataset.size();//return
    }
}