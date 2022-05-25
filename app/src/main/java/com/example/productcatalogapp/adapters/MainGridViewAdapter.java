package com.example.productcatalogapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.productcatalogapp.ProductDetailsActivity;
import com.example.productcatalogapp.R;
import com.example.productcatalogapp.classes.Product;
import com.example.productcatalogapp.classes.ProductImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainGridViewAdapter extends RecyclerView.Adapter<MainGridViewAdapter.ViewHolder> {

    ArrayList<Product> products;
    LayoutInflater inflater;

    public MainGridViewAdapter(Context ctx, ArrayList<Product> products){
        this.products = products;
        this.inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.item_main_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Product product = products.get(i);
        viewHolder.textViewCategory.setText(product.getCategory().getName());
        if (product.getImages() !=  null){
            ProductImage image = product.getImages().get(0);
            File imgFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/ProductCatalogApp/" + image.getPath());
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                viewHolder.imageViewImage.setImageBitmap(myBitmap);
            }
        }
        viewHolder.textViewLabel.setText(product.getLabel());
        viewHolder.textViewPrice.setText(String.valueOf(product.getPrice())+ " Dh");
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (this.products != null) {
            count = this.products.size();
        }
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCategory = null;
        ImageView imageViewImage = null;
        TextView textViewLabel = null;
        TextView textViewPrice = null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewCategory = itemView.findViewById(R.id.idTextViewCategory);
            this.imageViewImage = itemView.findViewById(R.id.idImageView);
            this.textViewLabel = itemView.findViewById(R.id.idTextViewLabel);
            this.textViewPrice = itemView.findViewById(R.id.idTextViewPrice);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                    intent.putExtra("productId", products.get(getAdapterPosition()).getId());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

}
