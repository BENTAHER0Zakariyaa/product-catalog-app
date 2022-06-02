package com.example.productcatalogapp.classes;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.productcatalogapp.R;

public class CustomToast {

    private Toast toast;
    private Context context;
    private int message;
    private int icon;


    public CustomToast(Context context, int message, int icon){
        this.context = context;
        this.message = message;
        this.icon = icon;
    }

    public void Make(){
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View layout = inflater.inflate(R.layout.custem_toast, null);

        ImageView image = layout.findViewById(R.id.idImageViewIcon);
        image.setImageResource(this.icon);

        TextView text = layout.findViewById(R.id.idEditTextMessage);

        text.setText(this.message);

        this.toast = new Toast(this.context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
    }

    public void Show(){
        this.toast.show();
    }

}
