package com.example.productcatalogapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.productcatalogapp.CartActivity;
import com.example.productcatalogapp.CatalogActivity;
import com.example.productcatalogapp.LoadingActivity;
import com.example.productcatalogapp.R;
import com.example.productcatalogapp.classes.Cart;
import com.example.productcatalogapp.classes.CartLine;
import com.example.productcatalogapp.classes.ProductImage;

import java.io.File;
import java.util.ArrayList;

public class RecyclerViewCartAdapter extends RecyclerView.Adapter<RecyclerViewCartAdapter.ViewHolder> {


    private LayoutInflater inflater = null;
    private Context context;


    public RecyclerViewCartAdapter(Context ctx){
        this.inflater = LayoutInflater.from(ctx);
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.item_cart_item, parent, false);
        return new RecyclerViewCartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CartLine cartLine = LoadingActivity.cart.getCartLine(i);

        viewHolder.textViewTotal.setText(String.valueOf(cartLine.getTotalPrice())+" Dh");
        viewHolder.textViewLabel.setText(cartLine.getProduct().getLabel());
        viewHolder.editViewQuantity.setText(String.valueOf(cartLine.getQuantity()));
        if (cartLine.getProduct().getImages() != null){
            ProductImage image = cartLine.getProduct().getImages().get(0);
            File imgFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/ProductCatalogApp/" + image.getPath());
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                viewHolder.imageViewProduct.setImageBitmap(myBitmap);
            }
        }
    }

    @Override
    public int getItemCount() {
        return LoadingActivity.cart.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageViewProduct = null;
        private TextView textViewLabel = null;
        private Button buttonDecrement = null;
        private EditText editViewQuantity = null;
        private Button buttonIncrement = null;
        private TextView textViewTotal = null;
        private Button buttonDelete = null;


        private View.OnClickListener onClickListenerButtonIncrement = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingActivity.cart.getCartLine(getAdapterPosition()).IncrementQuantity();
                editViewQuantity.setText(String.valueOf(LoadingActivity.cart.getCartLine(getAdapterPosition()).getQuantity()));
                textViewTotal.setText(String.valueOf(LoadingActivity.cart.getCartLine(getAdapterPosition()).getTotalPrice())+" Dh");
                CatalogActivity.buttonCart.setText(context.getString(R.string.dashboard_activity_button_cart, LoadingActivity.cart.getCount(), LoadingActivity.cart.getTotal()));
            }
        };

        private View.OnClickListener onClickListenerButtonDecrement = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoadingActivity.cart.getCartLine(getAdapterPosition()).getQuantity() > 1){
                    LoadingActivity.cart.getCartLine(getAdapterPosition()).DecrementQuantity();
                    editViewQuantity.setText(String.valueOf(LoadingActivity.cart.getCartLine(getAdapterPosition()).getQuantity()));
                    textViewTotal.setText(String.valueOf(LoadingActivity.cart.getCartLine(getAdapterPosition()).getTotalPrice()));
                    CatalogActivity.buttonCart.setText(context.getString(R.string.dashboard_activity_button_cart, LoadingActivity.cart.getCount(), LoadingActivity.cart.getTotal()));
                }
            }
        };

        private View.OnClickListener onClickListenerButtonDelete = new View.OnClickListener() {

            AlertDialog.Builder deleteCommandLineAlertDialog = null;
            AlertDialog deleteCommandLineAlert = null;

            DialogInterface.OnClickListener deleteCommandLineAlertDialogPositiveButton = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LoadingActivity.cart.removeCartLine(LoadingActivity.cart.getCartLine(getAdapterPosition()));
                    CartActivity.recyclerViewCartAdapter.notifyDataSetChanged();
                    CatalogActivity.buttonCart.setText(context.getString(R.string.dashboard_activity_button_cart, LoadingActivity.cart.getCount(), LoadingActivity.cart.getTotal()));
                    deleteCommandLineAlert.dismiss();
                }
            };

            DialogInterface.OnClickListener deleteCommandLineAlertDialogNegativeButton = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteCommandLineAlert.cancel();
                }
            };

            @Override
            public void onClick(View v) {
                this.deleteCommandLineAlertDialog = new AlertDialog.Builder(context);
                this.deleteCommandLineAlertDialog.setTitle(R.string.action_confirmation);
                this.deleteCommandLineAlertDialog.setMessage(R.string.cart_activity_delete_confirmation_message);
                this.deleteCommandLineAlertDialog.setPositiveButton(R.string.action_delete, deleteCommandLineAlertDialogPositiveButton);
                this.deleteCommandLineAlertDialog.setNegativeButton(R.string.action_cancel, deleteCommandLineAlertDialogNegativeButton);
                this.deleteCommandLineAlert = deleteCommandLineAlertDialog.create();
                this.deleteCommandLineAlert.show();

            }
        };



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageViewProduct = itemView.findViewById(R.id.idImageViewProduct);
            this.textViewLabel = itemView.findViewById(R.id.idTextViewLabel);
            this.buttonDecrement = itemView.findViewById(R.id.idButtonDecrement);
            this.editViewQuantity = itemView.findViewById(R.id.idEditViewQuantity);
            this.buttonIncrement = itemView.findViewById(R.id.idButtonIncrement);
            this.textViewTotal = itemView.findViewById(R.id.idTextViewTotal);
            this.buttonDelete = itemView.findViewById(R.id.idButtonDelete);

            buttonIncrement.setOnClickListener(onClickListenerButtonIncrement);
            buttonDecrement.setOnClickListener(onClickListenerButtonDecrement);
            buttonDelete.setOnClickListener(onClickListenerButtonDelete);

        }
    }
}
