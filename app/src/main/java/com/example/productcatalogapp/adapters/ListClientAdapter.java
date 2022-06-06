package com.example.productcatalogapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.productcatalogapp.CartActivity;
import com.example.productcatalogapp.CatalogActivity;
import com.example.productcatalogapp.LoadingActivity;
import com.example.productcatalogapp.R;
import com.example.productcatalogapp.classes.Client;
import com.example.productcatalogapp.classes.Command;
import com.example.productcatalogapp.classes.CustomToast;
import com.example.productcatalogapp.classes.DialogCreateCommand;

import java.util.ArrayList;

public class ListClientAdapter extends RecyclerView.Adapter<ListClientAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Client> clients;

    public ListClientAdapter(Context context, ArrayList<Client> clients) {
        this.context = context;
        this.clients = clients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.item_client, viewGroup, false);
        return new ListClientAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.textViewName.setText(this.clients.get(i).getName());
        viewHolder.textViewTown.setText(this.clients.get(i).getTown());
        viewHolder.textViewPhone.setText(this.clients.get(i).getMainPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return this.clients != null ? this.clients.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        private TextView textViewName= null;
        private TextView textViewTown= null;
        private TextView textViewPhone= null;

        private View.OnClickListener itemViewOnClickListener = new View.OnClickListener() {

            private Client client = null;
            private Command command = null;
            DialogCreateCommand dialogCreateCommand;

            private View.OnClickListener createCommandOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  LoadingActivity.DB.addCommand(command);
                  LoadingActivity.cart.init();
                  CartActivity.recyclerViewCartAdapter.notifyDataSetChanged();
                  CartActivity.updateCart();
                  CatalogActivity.buttonCart.setText(context.getString(R.string.cart, LoadingActivity.cart.getCount(), LoadingActivity.cart.getTotal()));
                  dialogCreateCommand.getAlertDialog().dismiss();
                }
            };

            @Override
            public void onClick(View v) {
                if (LoadingActivity.cart.isNotEmpty()){
                    client = clients.get(getAdapterPosition());
                    command = LoadingActivity.cart.createCommand(client);
                    dialogCreateCommand = new DialogCreateCommand(context, client, command, createCommandOnClickListener);
                    dialogCreateCommand.Create();
                    dialogCreateCommand.Show();
                }else{
                    CustomToast toast = new CustomToast(context, R.string.error_cart_is_empty, R.drawable.ic_cart_64);
                    toast.Make();
                    toast.Show();
                }

            }
        };

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textViewName = itemView.findViewById(R.id.idTextViewName);
            this.textViewTown = itemView.findViewById(R.id.idTextViewTown);
            this.textViewPhone = itemView.findViewById(R.id.idTextViewPhone);

            itemView.setOnClickListener(this.itemViewOnClickListener);
        }
    }

}
