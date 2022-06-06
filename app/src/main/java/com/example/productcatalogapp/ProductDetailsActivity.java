package com.example.productcatalogapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.constraint.utils.ImageFilterButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.productcatalogapp.adapters.ListProductImagesAdapter;
import com.example.productcatalogapp.classes.Product;
import com.example.productcatalogapp.classes.ProductImage;

import java.io.File;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView textViewLabel = null;
    private TextView textViewPrice = null;
    private TextView textViewDescription = null;
    private TextView textViewCategory = null;
    private ImageButton imageButtonBack = null;
    private RecyclerView recyclerViewProductImages = null;

    private View.OnClickListener onClickListenerImageButtonBackBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        this.textViewDescription        = this.findViewById(R.id.idTextViewDescription);
        this.textViewLabel              = this.findViewById(R.id.idTextViewLabel);
        this.textViewPrice              = this.findViewById(R.id.idTextViewPrice);
        this.textViewCategory  = this.findViewById(R.id.idTextViewCategory);
        this.recyclerViewProductImages  = this.findViewById(R.id.idRecyclerViewProductImages);

        this.imageButtonBack = this.findViewById(R.id.idImageButtonBack);
        this.imageButtonBack.setOnClickListener(this.onClickListenerImageButtonBackBack);

        Bundle bundle = getIntent().getExtras();
        int productId = bundle.getInt("productId");

        Product product = LoadingActivity.DB.getProduct(productId);

        this.textViewPrice.setText(String.valueOf(product.getPrice()));
        this.textViewDescription.setText(product.getDescription());
        this.textViewLabel.setText(product.getLabel());
        this.textViewCategory.setText(product.getCategory().getName());

        if (product.getImages() !=  null){
            ListProductImagesAdapter listProductImagesAdapter = new ListProductImagesAdapter(this, product.getImages());
            GridLayoutManager listProductImagesLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);

            this.recyclerViewProductImages.setLayoutManager(listProductImagesLayoutManager);
            this.recyclerViewProductImages.setAdapter(listProductImagesAdapter);
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(recyclerViewProductImages);
        }
    }
}