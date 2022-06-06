package com.example.productcatalogapp;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.productcatalogapp.adapters.ListClientAdapter;
import com.example.productcatalogapp.classes.Client;

import java.util.ArrayList;

public class CreateCommandActivity extends AppCompatActivity {


    private ImageButton imageButtonAddClient;
    private ImageButton imageButtonBack;

    private RecyclerView recyclerViewClients;
    private ListClientAdapter listClientAdapter;
    private GridLayoutManager gridLayoutManagerClient;

    private ArrayList<Client> clients = null;


    private View.OnClickListener imageButtonBackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };


    private View.OnClickListener imageButtonAddClientOnClickListener = new View.OnClickListener() {

        private AlertDialog.Builder createClientAlertBuilder;
        private AlertDialog createClientAlert;

        private EditText editTextName;
        private EditText editTextTown;
        private EditText editTextAddress;
        private EditText editTextMainPhoneNumber;
        private EditText editTextSecondPhoneNumber;
        private EditText editTextEmail;
        private Button buttonCreateClient;


        private View.OnClickListener buttonCreateClientOnClickListener= new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Client client = new Client();

                client.setName(editTextName.getText().toString());
                client.setTown(editTextTown.getText().toString());
                client.setEmail(editTextEmail.getText().toString());
                client.setAddress(editTextAddress.getText().toString());
                client.setMainPhoneNumber(editTextMainPhoneNumber.getText().toString());
                client.setSecondPhoneNumber(editTextSecondPhoneNumber.getText().toString());

                if (client.getName().equals("")){
                    Toast.makeText(CreateCommandActivity.this, R.string.error_name_required, Toast.LENGTH_SHORT).show();
                }
                else if (client.getTown().equals("")){
                    Toast.makeText(CreateCommandActivity.this, R.string.error_town_required, Toast.LENGTH_SHORT).show();
                }
                else if(client.getAddress().equals("")){
                    Toast.makeText(CreateCommandActivity.this, R.string.error_address_required, Toast.LENGTH_SHORT).show();
                }
                else if(client.getMainPhoneNumber().equals("")){
                    Toast.makeText(CreateCommandActivity.this, R.string.error_main_phone_required, Toast.LENGTH_SHORT).show();
                }
                else {
                    LoadingActivity.DB.addClient(client);
                    fillRecyclerViewClients();
                    createClientAlert.dismiss();
                }
            }
        };

        @Override
        public void onClick(View v) {
            View dialogClientInfo = getLayoutInflater().inflate(R.layout.dialog_add_client, null);

            this.editTextName = dialogClientInfo.findViewById(R.id.idEditTextName);
            this.editTextTown = dialogClientInfo.findViewById(R.id.idEditTextTown);
            this.editTextAddress = dialogClientInfo.findViewById(R.id.idEditTextAddress);
            this.editTextMainPhoneNumber = dialogClientInfo.findViewById(R.id.idEditTextMainPhoneNumber);
            this.editTextSecondPhoneNumber = dialogClientInfo.findViewById(R.id.idEditTextSecondPhoneNumber);
            this.editTextEmail = dialogClientInfo.findViewById(R.id.idEditTextEmail);
            this.buttonCreateClient = dialogClientInfo.findViewById(R.id.idButtonCreateClient);

            this.buttonCreateClient.setOnClickListener(buttonCreateClientOnClickListener);


            this.createClientAlertBuilder = new AlertDialog.Builder(CreateCommandActivity.this);

            this.createClientAlertBuilder.setView(dialogClientInfo);
            this.createClientAlert = createClientAlertBuilder.create();
            this.createClientAlert.show();
        }
    };

//    private View.OnClickListener onClickListenerCreateCommand= new View.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            Client client = new Client();
//
//            client.setName(editTextFullName.getText().toString());
//            client.setTown(editTextCity.getText().toString());
//            client.setEmail(editTextEmail.getText().toString());
//            client.setAddress(editTextAddress.getText().toString());
//            client.setMainPhoneNumber(editTextMainPhoneNumber.getText().toString());
//            client.setSecondPhoneNumber(editTextSecondPhoneNumber.getText().toString());
//
//            if (client.getName().equals("")){
//                Toast.makeText(CreateCommandActivity.this, R.string.error_name_required, Toast.LENGTH_SHORT).show();
//            }
//            else if (client.getTown().equals("")){
//                Toast.makeText(CreateCommandActivity.this, R.string.error_town_required, Toast.LENGTH_SHORT).show();
//            }
//            else if(client.getAddress().equals("")){
//                Toast.makeText(CreateCommandActivity.this, R.string.error_address_required, Toast.LENGTH_SHORT).show();
//            }
//            else if(client.getMainPhoneNumber().equals("")){
//                Toast.makeText(CreateCommandActivity.this, R.string.error_main_phone_required, Toast.LENGTH_SHORT).show();
//            }
//            else {
//                client.setId(LoadingActivity.DB.addClient(client));
//                LoadingActivity.DB.addCommand(LoadingActivity.cart.createCommand(client));
//                CartActivity.recyclerViewCartAdapter.notifyDataSetChanged();
//                CatalogActivity.buttonCart.setText(getString(R.string.cart, LoadingActivity.cart.getCount(), LoadingActivity.cart.getTotal()));
//                listClientAdapter.notifyDataSetChanged();
//                finish();
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_command);

        this.imageButtonAddClient = this.findViewById(R.id.idImageButtonAddClient);
        this.imageButtonBack = this.findViewById(R.id.idImageButtonBack);
        this.recyclerViewClients = this.findViewById(R.id.idRecyclerViewClients);

        this.fillRecyclerViewClients();

        this.imageButtonAddClient.setOnClickListener(this.imageButtonAddClientOnClickListener);
        this.imageButtonBack.setOnClickListener(this.imageButtonBackOnClickListener);

    }

    void fillRecyclerViewClients(){
        this.clients = LoadingActivity.DB.getClients();
        this.listClientAdapter = new ListClientAdapter(this, clients);
        this.gridLayoutManagerClient = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        this.recyclerViewClients.setLayoutManager(this.gridLayoutManagerClient);
        this.recyclerViewClients.setAdapter(this.listClientAdapter);
    }
}