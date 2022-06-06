package com.example.productcatalogapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.productcatalogapp.R;
import com.example.productcatalogapp.classes.ProductImage;

import java.io.File;
import java.util.ArrayList;

public class ListProductImagesAdapter extends RecyclerView.Adapter<ListProductImagesAdapter.ViewHolder> {

    private ArrayList<ProductImage> images;
    private LayoutInflater inflater;
    private Context context;

    public ListProductImagesAdapter(Context ctx, ArrayList<ProductImage> images){
        this.images = images;
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.item_product_image, parent, false);
        return new ListProductImagesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (images != null){
            ProductImage image = images.get(i);
            File imgFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/ProductCatalogApp/" + image.getPath());
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                viewHolder.imageViewProduct.setImageBitmap(myBitmap);
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.images != null ? this.images.size() : 0 ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageViewProduct = itemView.findViewById(R.id.idImageViewProduct);
        }
    }
}
