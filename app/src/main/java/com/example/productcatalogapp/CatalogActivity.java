package com.example.productcatalogapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CatalogActivity extends AppCompatActivity {

    private Button buttonLogout = null;
    private Button buttonUpdate = null;

    private View.OnClickListener buttonLogoutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity.preferences.edit()
                    .putBoolean("isSaved", false)
                    .putInt("id", -1)
                    .apply();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        this.buttonLogout = this.findViewById(R.id.idButtonLogout);
        this.buttonUpdate = this.findViewById(R.id.idButtonUpdate);

        this.buttonLogout.setOnClickListener(this.buttonLogoutOnClickListener);
    }
}