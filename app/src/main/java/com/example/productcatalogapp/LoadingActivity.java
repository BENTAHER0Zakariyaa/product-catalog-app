package com.example.productcatalogapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.productcatalogapp.classes.User;
import com.example.productcatalogapp.database.DataBaseHelper;

import java.util.ArrayList;

public class LoadingActivity extends AppCompatActivity {
    public static final String APP_NAME = "com.example.productcatalogapp";

    public static final int MAIN_ACTIVITY_START_CODE = 1;
    public static final int CATALOG_ACTIVITY_START_CODE = 2;

    public static final String[] APP_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
    };

    public static DataBaseHelper DB = null;
    public static SharedPreferences preferences = null;
    public static User currentUser = null;

    private Intent startActivityIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_loading);

        this.DB = new DataBaseHelper(this);

        this.preferences = this.getSharedPreferences(this.APP_NAME, this.MODE_PRIVATE);

        boolean isSessionSaved = this.preferences.getBoolean("isSessionSaved", false);
        int userId = this.preferences.getInt("userId", -1);

        if (isSessionSaved && userId != -1) {
            this.currentUser = DB.getUser(userId);
            if (currentUser != null) {
                startActivityIntent = new Intent(LoadingActivity.this, CatalogActivity.class);
                startActivityForResult(startActivityIntent, CATALOG_ACTIVITY_START_CODE);
            }
            else{
                startActivityIntent = new Intent(LoadingActivity.this, MainActivity.class);
                startActivityForResult(startActivityIntent, MAIN_ACTIVITY_START_CODE);
            }
        }
        else {
            startActivityIntent = new Intent(LoadingActivity.this, MainActivity.class);
            startActivityForResult(startActivityIntent, MAIN_ACTIVITY_START_CODE);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case MAIN_ACTIVITY_START_CODE :
                this.finish();
            break;
            case CATALOG_ACTIVITY_START_CODE:
                Log.d("TAG", "onActivityResult: ayah kandoz");
                startActivityIntent = new Intent(LoadingActivity.this, MainActivity.class);
                startActivityForResult(startActivityIntent, MAIN_ACTIVITY_START_CODE);
            break;
        }
    }
}