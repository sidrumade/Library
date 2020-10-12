package com.example.library3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.library3.ui.home.CardAdapter;

public class BookDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Bundle extras=getIntent().getExtras();
        //System.out.println(extras.getString("book_name"));
        //System.out.println(extras.getString("book_auther"));
        //System.out.println(extras.getString("book_url"));
        //System.out.println(extras.getString("book_thumbnail"));
        Toast.makeText(this,extras.getString("book_name"),Toast.LENGTH_SHORT).show();

    }
}