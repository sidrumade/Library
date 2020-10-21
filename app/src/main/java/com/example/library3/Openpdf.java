package com.example.library3;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

public class Openpdf extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    PDFView pdfView;
    Switch nightswitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openpdf);

        nightswitch = (Switch) findViewById(R.id.night_switch);
        nightswitch.setChecked(false);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        Bundle extras = getIntent().getExtras();
        displayPdf(extras.getString("book_name"));
        nightswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    pdfView.setNightMode(true);
                } else {
                    // The toggle is disabled
                    pdfView.setNightMode(false);
                }
            }
        });
    }


    private void displayPdf(String book_name) {

        File book = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS) + "/" + book_name + ".pdf");
        Toast.makeText(getApplicationContext(), book.exists() + "", Toast.LENGTH_SHORT).show();
        if (book != null && book.exists() == false) { //if pdf is not downloaded

            View v1 = findViewById(android.R.id.content).getRootView();
            Snackbar.make(v1, "Book is not available", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        } else {

            pdfView.fromFile(new File(book.getPath()))
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .enableAntialiasing(false) // improve rendering a little bit on low-res screens
                    .spacing(0)
                    .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                    .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                    .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                    .pageSnap(false) // snap pages to screen boundaries
                    .pageFling(false) // make a fling change only a single page like ViewPager
                    .nightMode(false) // toggle night mode
                    .load();
        }
    }


}