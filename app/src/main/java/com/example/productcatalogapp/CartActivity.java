package com.example.productcatalogapp;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.productcatalogapp.adapters.RecyclerViewCartAdapter;
import com.example.productcatalogapp.classes.Cart;
import com.example.productcatalogapp.classes.Client;
import com.example.productcatalogapp.classes.Command;

public class CartActivity extends AppCompatActivity {


    private RecyclerView recyclerViewCardProducts = null;
    private Button buttonBack = null;
    private Button buttonCreateCommand = null;

    private View.OnClickListener onClickListenerButtonBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    private View.OnClickListener onClickListenerButtonCreateCommand = new View.OnClickListener() {

        private AlertDialog.Builder createCommandDialogBuilder;
        private AlertDialog createCommandAlert;

        private EditText editTextFullName;
        private EditText editTextCity;
        private EditText editTextAddress;
        private EditText editTextMainPhoneNumber;
        private EditText editTextSecondPhoneNumber;
        private EditText editTextEmail;
        private Button buttonCreateCommand;

        private View.OnClickListener onClickListenerCreateCommand= new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Client client = new Client();

                client.setName(editTextFullName.getText().toString());
                client.setTown(editTextCity.getText().toString());
                client.setEmail(editTextEmail.getText().toString());
                client.setAddress(editTextAddress.getText().toString());
                client.setMainPhoneNumber(editTextMainPhoneNumber.getText().toString());
                client.setSecondPhoneNumber(editTextSecondPhoneNumber.getText().toString());

                if (client.getName().equals("")){
                    Toast.makeText(CartActivity.this, R.string.error_name_required, Toast.LENGTH_SHORT).show();
                }
                else if (client.getTown().equals("")){
                    Toast.makeText(CartActivity.this, R.string.error_town_required, Toast.LENGTH_SHORT).show();
                }
                else if(client.getAddress().equals("")){
                    Toast.makeText(CartActivity.this, R.string.error_address_required, Toast.LENGTH_SHORT).show();
                }
                else if(client.getMainPhoneNumber().equals("")){
                    Toast.makeText(CartActivity.this, R.string.error_main_phone_required, Toast.LENGTH_SHORT).show();
                }
                else {
                    client.setId(LoadingActivity.DB.addClient(client));
                    LoadingActivity.DB.addCommand(LoadingActivity.cart.createCommand(client));
                    recyclerViewCartAdapter.notifyDataSetChanged();
                    CatalogActivity.buttonCart.setText(getString(R.string.dashboard_activity_button_cart, LoadingActivity.cart.getCount(), LoadingActivity.cart.getTotal()));
                    createCommandAlert.dismiss();
                }
            }
        };

        @Override
        public void onClick(View v) {

            View dialogClientInfo = getLayoutInflater().inflate(R.layout.dialog_client_info, null);

            this.editTextFullName = dialogClientInfo.findViewById(R.id.idEditTextName);
            this.editTextCity = dialogClientInfo.findViewById(R.id.idEditTextTown);
            this.editTextAddress = dialogClientInfo.findViewById(R.id.idEditTextAddress);
            this.editTextMainPhoneNumber = dialogClientInfo.findViewById(R.id.idEditTextMainPhoneNumber);
            this.editTextSecondPhoneNumber = dialogClientInfo.findViewById(R.id.idEditTextSecondPhoneNumber);
            this.editTextEmail = dialogClientInfo.findViewById(R.id.idEditTextEmail);
            this.buttonCreateCommand = dialogClientInfo.findViewById(R.id.idButtonCreateCommand);

            this.buttonCreateCommand.setOnClickListener(onClickListenerCreateCommand);

            this.createCommandDialogBuilder = new AlertDialog.Builder(CartActivity.this);
            this.createCommandDialogBuilder.setView(dialogClientInfo);
            this.createCommandAlert = createCommandDialogBuilder.create();
            this.createCommandAlert.show();

        }
    };

    public static RecyclerViewCartAdapter recyclerViewCartAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        this.recyclerViewCardProducts = this.findViewById(R.id.idRecyclerViewCardProducts);
        this.buttonBack = this.findViewById(R.id.idButtonBack);
        this.buttonCreateCommand = this.findViewById(R.id.idButtonCreateCommand);

        this.buttonBack.setOnClickListener(onClickListenerButtonBack);
        this.buttonCreateCommand.setOnClickListener(onClickListenerButtonCreateCommand);

    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerViewCartAdapter = new RecyclerViewCartAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        recyclerViewCardProducts.setLayoutManager(gridLayoutManager);
        recyclerViewCardProducts.setAdapter(recyclerViewCartAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}