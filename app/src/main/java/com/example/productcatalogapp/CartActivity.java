package com.example.productcatalogapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CartActivity extends AppCompatActivity {

    TextView idTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        idTest = findViewById(R.id.idTest);

        for (int i = 0; i < CatalogActivity.cart.getCount(); i++) {
            idTest.setText(idTest.getText()+ CatalogActivity.cart.getCartLine(i).getProduct().toString() + "\n");
        }
    }
}