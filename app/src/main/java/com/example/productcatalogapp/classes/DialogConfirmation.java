package com.example.productcatalogapp.classes;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.productcatalogapp.CatalogActivity;
import com.example.productcatalogapp.R;

public class DialogConfirmation {

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private Context context;
    private int message;
    private int icon;
    private View.OnClickListener buttonAcceptOnClickListener;
    private View.OnClickListener buttonCancelOnClickListener;


    public DialogConfirmation(Context context, int message, int icon, View.OnClickListener buttonAcceptOnClickListener, View.OnClickListener buttonCancelOnClickListener) {
        this.context = context;
        this.message = message;
        this.icon = icon;
        this.buttonAcceptOnClickListener = buttonAcceptOnClickListener;
        this.buttonCancelOnClickListener = buttonCancelOnClickListener;
    }

    public void Create(){
        View dialogConfirmation = LayoutInflater.from(context).inflate(R.layout.dialog_confirmation, null);

        ImageView imageViewIcon = dialogConfirmation.findViewById(R.id.idImageViewIcon);
        TextView textView = dialogConfirmation.findViewById(R.id.idTextViewMessage);
        Button buttonAccept = dialogConfirmation.findViewById(R.id.idButtonAccept);
        Button buttonCancel = dialogConfirmation.findViewById(R.id.idButtonCancel);

        imageViewIcon.setImageResource(this.icon);
        textView.setText(this.message);

        buttonAccept.setOnClickListener(this.buttonAcceptOnClickListener);
        buttonCancel.setOnClickListener(this.buttonCancelOnClickListener);

        this.builder = new AlertDialog.Builder(context);
        builder.setView(dialogConfirmation);
        this.alertDialog = builder.create();
    }

    public void Show(){
        this.alertDialog.show();
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }
}
