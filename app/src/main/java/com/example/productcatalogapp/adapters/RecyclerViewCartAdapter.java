package com.example.productcatalogapp.adapters;

import static android.view.ViewGroup.*;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.productcatalogapp.CartActivity;
import com.example.productcatalogapp.CatalogActivity;
import com.example.productcatalogapp.LoadingActivity;
import com.example.productcatalogapp.R;
import com.example.productcatalogapp.classes.CartLine;
import com.example.productcatalogapp.classes.DialogConfirmation;
import com.example.productcatalogapp.classes.ProductImage;

import java.io.File;

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

        viewHolder.textViewTotal.setText(String.valueOf(cartLine.getTotalPrice()));
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
        private ImageButton imageButtonDelete = null;
        private LinearLayout linearLayoutDelete = null;


        private View.OnClickListener onClickListenerButtonIncrement = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingActivity.cart.getCartLine(getAdapterPosition()).IncrementQuantity();
                editViewQuantity.setText(String.valueOf(LoadingActivity.cart.getCartLine(getAdapterPosition()).getQuantity()));
                textViewTotal.setText(String.valueOf(LoadingActivity.cart.getCartLine(getAdapterPosition()).getTotalPrice()));
                CatalogActivity.buttonCart.setText(context.getString(R.string.cart, LoadingActivity.cart.getCount(), LoadingActivity.cart.getTotal()));
                CartActivity.updateCart();
            }
        };

        private View.OnClickListener onClickListenerButtonDecrement = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoadingActivity.cart.getCartLine(getAdapterPosition()).getQuantity() > 1){
                    LoadingActivity.cart.getCartLine(getAdapterPosition()).DecrementQuantity();
                    editViewQuantity.setText(String.valueOf(LoadingActivity.cart.getCartLine(getAdapterPosition()).getQuantity()));
                    textViewTotal.setText(String.valueOf(LoadingActivity.cart.getCartLine(getAdapterPosition()).getTotalPrice()));
                    CatalogActivity.buttonCart.setText(context.getString(R.string.cart, LoadingActivity.cart.getCount(), LoadingActivity.cart.getTotal()));
                    CartActivity.updateCart();
                }
            }
        };

        private View.OnClickListener onClickListenerButtonDelete = new View.OnClickListener() {

            DialogConfirmation deleteCommandLineDialogConfirmation = null;
            AlertDialog deleteCommandLineAlert = null;

            View.OnClickListener deleteCommandLineDialogConfirmationAcceptButton = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoadingActivity.cart.removeCartLine(LoadingActivity.cart.getCartLine(getAdapterPosition()));
                    CartActivity.recyclerViewCartAdapter.notifyDataSetChanged();
                    CatalogActivity.buttonCart.setText(context.getString(R.string.cart, LoadingActivity.cart.getCount(), LoadingActivity.cart.getTotal()));
                    CartActivity.updateCart();
                    deleteCommandLineDialogConfirmation.getAlertDialog().dismiss();
                }
            };

            View.OnClickListener deleteCommandLineDialogConfirmationCancelButton = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    deleteCommandLineDialogConfirmation.getAlertDialog().cancel();
                }
            };

            @Override
            public void onClick(View v) {
                this.deleteCommandLineDialogConfirmation = new DialogConfirmation(context, R.string.confirmation_delete_message, R.drawable.ic_warning, deleteCommandLineDialogConfirmationAcceptButton, deleteCommandLineDialogConfirmationCancelButton);
                this.deleteCommandLineDialogConfirmation.Create();
                this.deleteCommandLineDialogConfirmation.Show();
            }
        };

        private View.OnLongClickListener deleteContainerOnLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ViewGroup.LayoutParams params = v.findViewById(R.id.idLinearLayoutDelete).getLayoutParams();
                final float scale = context.getResources().getDisplayMetrics().density;
                if (params.width == 0){
                    params.width = (int) (105 * scale + 0.5f);
                }else{
                    params.width = 0;
                }
                v.findViewById(R.id.idLinearLayoutDelete).requestLayout();
                return false;
            }
        };

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imageViewProduct = itemView.findViewById(R.id.idImageViewProduct);
            this.textViewLabel = itemView.findViewById(R.id.idTextViewLabel);
            this.buttonDecrement = itemView.findViewById(R.id.idButtonDecrement);
            this.editViewQuantity = itemView.findViewById(R.id.idEditTextQuantity);
            this.buttonIncrement = itemView.findViewById(R.id.idButtonIncrement);
            this.textViewTotal = itemView.findViewById(R.id.idTextViewTotal);
            this.imageButtonDelete = itemView.findViewById(R.id.idButtonDelete);
            this.linearLayoutDelete = itemView.findViewById(R.id.idLinearLayoutDelete);

            buttonIncrement.setOnClickListener(onClickListenerButtonIncrement);
            buttonDecrement.setOnClickListener(onClickListenerButtonDecrement);
            imageButtonDelete.setOnClickListener(onClickListenerButtonDelete);

            itemView.setOnLongClickListener(this.deleteContainerOnLongClickListener);

        }
    }
}
