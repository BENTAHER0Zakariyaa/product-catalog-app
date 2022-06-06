package com.example.productcatalogapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.productcatalogapp.CatalogActivity;
import com.example.productcatalogapp.LoadingActivity;
import com.example.productcatalogapp.ProductDetailsActivity;
import com.example.productcatalogapp.R;
import com.example.productcatalogapp.classes.CartLine;
import com.example.productcatalogapp.classes.CustomToast;
import com.example.productcatalogapp.classes.Product;
import com.example.productcatalogapp.classes.ProductImage;

import java.io.File;
import java.util.ArrayList;

public class MainGridViewAdapter extends RecyclerView.Adapter<MainGridViewAdapter.ViewHolder> {

    private ArrayList<Product> products = null;
    private LayoutInflater inflater = null;
    private Context context = null;

    public MainGridViewAdapter(Context ctx, ArrayList<Product> products){
        this.products = products;
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Product product = products.get(i);


        if (product.getImages() !=  null){
            ProductImage image = product.getImages().get(0);
            File imgFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/ProductCatalogApp/" + image.getPath());
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                viewHolder.imageViewImage.setImageBitmap(myBitmap);
            }
        }

        viewHolder.textViewLabel.setText(product.getLabel());
        viewHolder.textViewPrice.setText(String.valueOf(product.getPrice()));
    }

    @Override
    public int getItemCount() {
        return this.products != null ? this.products.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewImage = null;
        private TextView textViewLabel = null;
        private TextView textViewPrice = null;

        private int quantity = 0;

        private View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), ProductDetailsActivity.class);
                intent.putExtra("productId", products.get(getAdapterPosition()).getId());
                itemView.getContext().startActivity(intent);
            }
        };

        private View.OnLongClickListener itemViewOnLongClickListener = new View.OnLongClickListener() {

            private EditText editTextQuantity   = null;
            private TextView textViewProductLabel   = null;
            private TextView textViewProductPrice   = null;
            private ImageView textViewProductImage   = null;
            private Button buttonIncrement      = null;
            private Button buttonDecrement      = null;
            private Button buttonAdd      = null;
            private Button buttonCancel      = null;

            private AlertDialog.Builder addToCartBuilder = null;
            private AlertDialog addToCartAlert = null;

            private View.OnClickListener onClickListenerButtonIncrement = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity = Integer.valueOf(editTextQuantity.getText().toString());
                    quantity++;
                    editTextQuantity.setText(String.valueOf(quantity));
                }
            };

            private View.OnClickListener onClickListenerButtonDecrement = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity = Integer.valueOf(editTextQuantity.getText().toString());
                    if (quantity > 0){
                        quantity--;
                        editTextQuantity.setText(String.valueOf(quantity));
                    }
                }
            };

            private View.OnClickListener onClickListenerButtonAdd = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity = Integer.valueOf(editTextQuantity.getText().toString());
                    if (quantity != 0){
                        LoadingActivity.cart.addCartLine(new CartLine(products.get(getAdapterPosition()), quantity));
                        CatalogActivity.buttonCart.setText(context.getString(R.string.cart, LoadingActivity.cart.getCount(), LoadingActivity.cart.getTotal()));
                    }
                    editTextQuantity.setText("0");
                    addToCartAlert.dismiss();
                    CustomToast toast = new CustomToast(context,  R.string.success_add, R.drawable.ic_success_64);
                    toast.Make();
                    toast.Show();
                }
            };

            private View.OnClickListener onClickListenerButtonCancel = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity = Integer.valueOf(editTextQuantity.getText().toString());
                    if (quantity > 0){
                        addToCartAlert.cancel();
                        editTextQuantity.setText("0");
                    }
                }
            };



            @Override
            public boolean onLongClick(View v) {

                Product product = products.get(getAdapterPosition());

                View addToCartView          = inflater.inflate(R.layout.dialog_add_to_cart, null);

                this.textViewProductLabel   = addToCartView.findViewById(R.id.idTextViewProductLabel);
                this.textViewProductPrice   = addToCartView.findViewById(R.id.idTextViewProductPrice);
                this.textViewProductImage   = addToCartView.findViewById(R.id.idTextViewProductImage);
                this.editTextQuantity       = addToCartView.findViewById(R.id.idEditTextQuantity);
                this.buttonIncrement        = addToCartView.findViewById(R.id.idButtonIncrement);
                this.buttonDecrement        = addToCartView.findViewById(R.id.idButtonDecrement);
                this.buttonAdd              = addToCartView.findViewById(R.id.idButtonAdd);
                this.buttonCancel           = addToCartView.findViewById(R.id.idButtonCancel);

                this.textViewProductLabel.setText(product.getLabel());
                this.textViewProductPrice.setText(String.valueOf(product.getPrice()));

                if (product.getImages() !=  null){
                    ProductImage image = product.getImages().get(0);
                    File imgFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/ProductCatalogApp/" + image.getPath());
                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        this.textViewProductImage.setImageBitmap(myBitmap);
                    }
                }

                this.buttonIncrement.setOnClickListener(this.onClickListenerButtonIncrement);
                this.buttonDecrement.setOnClickListener(this.onClickListenerButtonDecrement);
                this.buttonAdd.setOnClickListener(this.onClickListenerButtonAdd);
                this.buttonCancel.setOnClickListener(this.onClickListenerButtonCancel);

                this.addToCartBuilder = new AlertDialog.Builder(itemView.getContext());
                this.addToCartBuilder.setView(addToCartView);
                this.addToCartAlert = this.addToCartBuilder.create();
                this.addToCartAlert.show();

                return true;
            }
        };

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imageViewImage     = itemView.findViewById(R.id.idImageView);
            this.textViewLabel      = itemView.findViewById(R.id.idTextViewLabel);
            this.textViewPrice      = itemView.findViewById(R.id.idTextViewPrice);

            itemView.setOnClickListener(this.itemViewOnClickListener);

            itemView.setOnLongClickListener(this.itemViewOnLongClickListener);
        }
    }

}
