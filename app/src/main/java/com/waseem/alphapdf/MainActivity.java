package com.waseem.alphapdf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Firebase;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private PDFView pdfView;
    private FirebaseAnalytics firebaseAnalytics;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton btn_fab;
    TextView btn_rateus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        btn_fab = findViewById(R.id.btn_fab);
        btn_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilePicker();

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add:
                                // Handle Home selection



                                return true;
                            case R.id.contactus:
                                // Handle Dashboard selection
                                return true;

                        }
                        return false;
                    }
                });

        // Obtain the FirebaseAnalytics instance.
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String deviceModel = Build.MODEL;
        firebaseAnalytics.setUserProperty("device_model", deviceModel);
        String language = Locale.getDefault().getLanguage();
        firebaseAnalytics.setUserProperty("language", language);

        pdfView = findViewById(R.id.pdfView);

    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                loadPDFFromUri(uri);
            } else {
                Toast.makeText(this, "Failed to retrieve PDF file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadPDFFromUri(Uri uri) {
        pdfView.fromUri(uri)

                .enableAnnotationRendering(true)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();


    }



    private void openPDF() {
        File pdfFile = new File("/path/to/your/pdf/file.pdf");
        if (pdfFile.exists()) {
            pdfView.fromFile(pdfFile)
                    .enableAnnotationRendering(true)
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {
                            // PDF loaded successfullyy
                        }
                    })
                    .onError(new OnErrorListener() {
                        @Override
                        public void onError(Throwable t) {
                            // Handle error
                            Toast.makeText(MainActivity.this, "Error loading PDF: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .enableAnnotationRendering(true)
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .defaultPage(0)
                    .load();
        } else {
            Toast.makeText(this, "PDF file not found", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open PDF
                openPDF();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(this, "Permission denied to read external storage", Toast.LENGTH_SHORT).show();
            }

        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
