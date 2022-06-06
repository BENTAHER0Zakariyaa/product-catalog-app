package com.example.productcatalogapp.classes;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.productcatalogapp.R;

public class DialogCreateCommand {


    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private Context context;
    private Client client;
    private Command command;
    private View.OnClickListener buttonCreateOnClickListener;


    public DialogCreateCommand(Context context, Client client, Command command, View.OnClickListener buttonCreateOnClickListener) {
        this.context = context;
        this.client = client;
        this.command = command;
        this.buttonCreateOnClickListener = buttonCreateOnClickListener;
    }

    public void Create(){
        View dialogCreateCommand = LayoutInflater.from(context).inflate(R.layout.dialog_create_command, null);

        TextView textViewName = dialogCreateCommand.findViewById(R.id.idTextViewName);
        TextView textViewTown = dialogCreateCommand.findViewById(R.id.idTextViewTown);
        TextView textViewPhone = dialogCreateCommand.findViewById(R.id.idTextViewPhone);
        TextView textViewAddress = dialogCreateCommand.findViewById(R.id.idTextViewAddress);

        TextView textViewTotal = dialogCreateCommand.findViewById(R.id.idTextViewTotal);
        Button buttonCreateCommand = dialogCreateCommand.findViewById(R.id.idButtonCreateCommand);

        buttonCreateCommand.setOnClickListener(buttonCreateOnClickListener);

        textViewName.setText(this.client.getName());
        textViewTown.setText(this.client.getTown());
        textViewPhone.setText(this.client.getMainPhoneNumber());
        textViewAddress.setText(this.client.getAddress());
        textViewTotal.setText(String.valueOf(this.command.getTotal()));

        this.builder = new AlertDialog.Builder(context);
        builder.setView(dialogCreateCommand);
        this.alertDialog = builder.create();
    }

    public void Show(){
        this.alertDialog.show();
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

}
