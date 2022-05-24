package com.example.productcatalogapp.adapters;

import android.content.Context;
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
        viewHolder.label.setText(products.get(i).getLabel());
        viewHolder.price.setText(String.valueOf(products.get(i).getPrice())+ " Dh");
        ProductImage image = products.get(i).getImages().get(0);
        File imgFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/ProductCatalogApp/" + image.getPath());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            viewHolder.image.setImageBitmap(myBitmap);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (this.products != null)
            count= this.products.size();
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView label = null;
        TextView price = null;
        ImageView image = null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.label = itemView.findViewById(R.id.idTextViewLabel);
            this.price = itemView.findViewById(R.id.idTextViewPrice);
            this.image = itemView.findViewById(R.id.idImageView);

        }
    }

}
