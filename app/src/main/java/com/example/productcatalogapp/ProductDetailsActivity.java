package com.example.productcatalogapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.productcatalogapp.classes.Product;
import com.example.productcatalogapp.classes.ProductImage;

import java.io.File;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView imageViewProduct = null;
    private TextView textViewLabel = null;
    private TextView textViewPrice = null;
    private TextView textViewDescription = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        this.textViewDescription = this.findViewById(R.id.idTextViewDescription);
        this.imageViewProduct = this.findViewById(R.id.idImageViewProduct);
        this.textViewLabel = this.findViewById(R.id.idTextViewLabel);
        this.textViewPrice = this.findViewById(R.id.idTextViewPrice);

        Bundle bundle = getIntent().getExtras();
        int productId = bundle.getInt("productId");

        Product product = MainActivity.DB.getProduct(productId);

        this.textViewPrice.setText(String.valueOf(product.getPrice()) + " DH");
        this.textViewDescription.setText(product.getDescription());
        this.textViewLabel.setText(product.getLabel());

        if (product.getImages() !=  null){
            ProductImage image = product.getImages().get(0);
            File imgFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/ProductCatalogApp/" + image.getPath());
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                this.imageViewProduct.setImageBitmap(myBitmap);
            }
        }
    }
}