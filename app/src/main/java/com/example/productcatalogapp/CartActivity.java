package com.example.productcatalogapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.productcatalogapp.adapters.ListClientAdapter;
import com.example.productcatalogapp.adapters.RecyclerViewCartAdapter;
import com.example.productcatalogapp.classes.Cart;
import com.example.productcatalogapp.classes.Client;
import com.example.productcatalogapp.classes.Command;
import com.example.productcatalogapp.classes.CustomToast;
import com.example.productcatalogapp.classes.DialogCreateCommand;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class CartActivity extends AppCompatActivity {


    private RecyclerView recyclerViewCardProducts = null;
    private ImageButton imageButtonBack = null;
    private Button buttonCreateCommand = null;

    public static TextView textViewTotal = null;
    public static TextView textViewItemsCount = null;

    private View.OnClickListener onClickListenerImageButtonBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    private View.OnClickListener onClickListenerButtonCreateCommand = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (LoadingActivity.cart.isNotEmpty()){
                Intent intent = new Intent(CartActivity.this, CreateCommandActivity.class);
                startActivity(intent);
            }else{
                CustomToast toast = new CustomToast(CartActivity.this, R.string.error_cart_is_empty, R.drawable.ic_cart_64);
                toast.Make();
                toast.Show();
                finish();
            }
        }
    };

    public static RecyclerViewCartAdapter recyclerViewCartAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        this.recyclerViewCardProducts = this.findViewById(R.id.idRecyclerViewCardProducts);
        this.imageButtonBack = this.findViewById(R.id.idImageButtonBack);
        this.buttonCreateCommand = this.findViewById(R.id.idButtonCreateCommand);
        this.textViewTotal = this.findViewById(R.id.idTextViewTotal);
        this.textViewItemsCount = this.findViewById(R.id.idTextViewItemsCount);
        this.imageButtonBack.setOnClickListener(onClickListenerImageButtonBack);
        this.buttonCreateCommand.setOnClickListener(onClickListenerButtonCreateCommand);

        updateCart();
    }

    public static void updateCart(){
        textViewTotal.setText(String.valueOf(LoadingActivity.cart.getTotal()));
        textViewItemsCount.setText(textViewTotal.getContext().getString(R.string.text_items_count, LoadingActivity.cart.getCount()));
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