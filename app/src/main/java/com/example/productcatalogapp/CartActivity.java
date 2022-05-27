package com.example.productcatalogapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.productcatalogapp.adapters.RecyclerViewCartAdapter;

public class CartActivity extends AppCompatActivity {


    private RecyclerView recyclerViewCardProducts = null;
    private Button buttonBack = null;

    private View.OnClickListener onClickListenerButtonBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public static RecyclerViewCartAdapter recyclerViewCartAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        this.recyclerViewCardProducts = this.findViewById(R.id.idRecyclerViewCardProducts);
        this.buttonBack = this.findViewById(R.id.idButtonBack);
        this.buttonBack.setOnClickListener(onClickListenerButtonBack);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerViewCartAdapter = new RecyclerViewCartAdapter(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);

        recyclerViewCardProducts.setLayoutManager(gridLayoutManager);
        recyclerViewCardProducts.setAdapter(recyclerViewCartAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}