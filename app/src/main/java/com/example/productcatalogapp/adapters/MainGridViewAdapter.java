package com.example.productcatalogapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.productcatalogapp.CartActivity;
import com.example.productcatalogapp.CatalogActivity;
import com.example.productcatalogapp.LoadingActivity;
import com.example.productcatalogapp.ProductDetailsActivity;
import com.example.productcatalogapp.R;
import com.example.productcatalogapp.classes.Cart;
import com.example.productcatalogapp.classes.CartLine;
import com.example.productcatalogapp.classes.Product;
import com.example.productcatalogapp.classes.ProductImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        View view = inflater.inflate(R.layout.item_main_grid, parent, false);
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

            private TextView textViewQuantity   = null;
            private Button buttonIncrement      = null;
            private Button buttonDecrement      = null;

            private AlertDialog.Builder addToCartBuilder = null;

            private View.OnClickListener onClickListenerButtonIncrement = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity = Integer.valueOf(textViewQuantity.getText().toString());
                    quantity++;
                    textViewQuantity.setText(String.valueOf(quantity));
                }
            };

            private View.OnClickListener onClickListenerButtonDecrement = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity = Integer.valueOf(textViewQuantity.getText().toString());
                    if (quantity > 0){
                        quantity--;
                        textViewQuantity.setText(String.valueOf(quantity));
                    }
                }
            };

            private DialogInterface.OnClickListener onClickListenerNegativeButton = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    textViewQuantity.setText("0");
                }
            };

            private DialogInterface.OnClickListener onClickListenerPositiveButton = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    quantity = Integer.valueOf(textViewQuantity.getText().toString());
                    if (quantity != 0){
                        LoadingActivity.cart.addCartLine(new CartLine(products.get(getAdapterPosition()), quantity));
                        CatalogActivity.buttonCart.setText(context.getString(R.string.dashboard_activity_button_cart, LoadingActivity.cart.getCount(), LoadingActivity.cart.getTotal()));
                    }
                    textViewQuantity.setText("0");
                }
            };

            @Override
            public boolean onLongClick(View v) {

                View addToCartView      = inflater.inflate(R.layout.dialog_add_to_cart, null);

                this.textViewQuantity   = addToCartView.findViewById(R.id.idTextViewQuantity);
                this.buttonIncrement    = addToCartView.findViewById(R.id.idButtonIncrement);
                this.buttonDecrement    = addToCartView.findViewById(R.id.idButtonDecrement);

                this.buttonIncrement.setOnClickListener(onClickListenerButtonIncrement);
                this.buttonDecrement.setOnClickListener(onClickListenerButtonDecrement);

                addToCartBuilder = new AlertDialog.Builder(itemView.getContext());
                addToCartBuilder
                        .setTitle(R.string.dialog_add_to_cart)
                        .setView(addToCartView)
                        .setPositiveButton(R.string.action_add, onClickListenerPositiveButton)
                        .setNegativeButton(R.string.action_cancel, onClickListenerNegativeButton);
                AlertDialog addToCartAlert = addToCartBuilder.create();
                addToCartAlert.show();
                return false;
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
