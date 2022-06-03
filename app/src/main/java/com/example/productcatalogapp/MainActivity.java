package com.example.productcatalogapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.productcatalogapp.API.APIHelper;
import com.example.productcatalogapp.classes.CustomToast;
import com.example.productcatalogapp.classes.User;
import com.example.productcatalogapp.database.DataBaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSIONS_CODE = 1;

    private EditText editTextUserName = null;
    private EditText editTextPassword = null;
    private CheckBox checkBoxSaveSession = null;
    private Button buttonLogin = null;

    private View.OnClickListener buttonLoginOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (
                    ContextCompat.checkSelfPermission(MainActivity.this, LoadingActivity.APP_PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED
                    ||
                    ContextCompat.checkSelfPermission(MainActivity.this, LoadingActivity.APP_PERMISSIONS[1]) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(MainActivity.this, LoadingActivity.APP_PERMISSIONS, MainActivity.PERMISSIONS_CODE);
            }
            else {
                MainActivity.this.buttonLogin.setEnabled(false);
                LoadingActivity.currentUser = new User(MainActivity.this.editTextUserName.getText().toString(), MainActivity.this.editTextPassword.getText().toString());
                LoadingActivity.currentUser.setConnected(LoadingActivity.isConnected());
                if (LoadingActivity.currentUser.getUsername().equals("")) {
                    CustomToast toast = new CustomToast(MainActivity.this,  R.string.main_activity_error_username_required, R.drawable.ic_warning);
                    toast.Make();
                    toast.Show();
                    MainActivity.this.buttonLogin.setEnabled(true);
                }
                else if (LoadingActivity.currentUser.getPassword().equals("")) {
                    CustomToast toast = new CustomToast(MainActivity.this,  R.string.main_activity_error_password_required, R.drawable.ic_warning);
                    toast.Make();
                    toast.Show();
                    MainActivity.this.buttonLogin.setEnabled(true);
                }
                else {
                    if (LoadingActivity.currentUser.getConnected()) {
                        MainActivity.this.userConnected();
                    } else {
                        MainActivity.this.userDeConnected();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.editTextUserName       = this.findViewById(R.id.idEditTextUserName);
        this.editTextPassword       = this.findViewById(R.id.idEditTextPassword);
        this.checkBoxSaveSession    = this.findViewById(R.id.idCheckBoxSaveSession);
        this.buttonLogin            = this.findViewById(R.id.idButtonLogin);

        // remove
        this.editTextUserName.setText(APIHelper.TEST_USER_NAME);
        this.editTextPassword.setText(APIHelper.TEST_PASSWORD);

        this.buttonLogin.setOnClickListener(this.buttonLoginOnClickListener);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(LoadingActivity.MAIN_ACTIVITY_START_CODE, intent);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.buttonLogin.setEnabled(true);
    }

    void userConnected(){
        String loginURL = APIHelper.API_URL + "/login?login=" + LoadingActivity.currentUser.getUsername() + "&password=" + LoadingActivity.currentUser.getPassword();

        RequestQueue loginRequestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.GET, loginURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    LoadingActivity.currentUser.setToken(response.getJSONObject("success").getString("token"));
                    RequestQueue requestInfoQueue = Volley.newRequestQueue(MainActivity.this);
                    String userDataURL = APIHelper.API_URL + "/users/login/" + LoadingActivity.currentUser.getUsername();

                    JsonObjectRequest userDataRequest = new JsonObjectRequest(Request.Method.GET, userDataURL, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                LoadingActivity.currentUser.setId(Integer.valueOf(response.getString("id")));

                                if (!LoadingActivity.DB.isUserExist(LoadingActivity.currentUser.getId())) {
                                    LoadingActivity.DB.addUser(LoadingActivity.currentUser);
                                    Log.d("sss", "onResponse: "+ LoadingActivity.DB.getUser(2).toString());
                                }
                                MainActivity.this.startCatalogActivity();
                                MainActivity.this.saveSession(LoadingActivity.currentUser.getId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                MainActivity.this.buttonLogin.setEnabled(true);
                            }
                        }

                    }, null) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap header = new HashMap();
                            header.put("DOLAPIKEY", LoadingActivity.currentUser.getToken());
                            return header;
                        }
                    };
                    requestInfoQueue.add(userDataRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                    MainActivity.this.buttonLogin.setEnabled(true);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, R.string.main_activity_error_invalid_fields, Toast.LENGTH_SHORT).show();
                MainActivity.this.buttonLogin.setEnabled(true);
            }
        });
        loginRequestQueue.add(loginRequest);
    }

    void userDeConnected (){
        LoadingActivity.currentUser = LoadingActivity.DB.getUser(LoadingActivity.currentUser.getUsername(), LoadingActivity.currentUser.getPassword());
        if (LoadingActivity.currentUser != null) {
            LoadingActivity.currentUser.setConnected(LoadingActivity.isConnected());
            this.startCatalogActivity();
            this.saveSession(LoadingActivity.currentUser.getId());
        } else {
            Toast.makeText(MainActivity.this, R.string.main_activity_error_invalid_fields, Toast.LENGTH_SHORT).show();
            this.buttonLogin.setEnabled(true);
        }
    }

    void startCatalogActivity(){
        Intent startCatalogActivityIntent = new Intent(MainActivity.this, CatalogActivity.class);
        startActivityForResult(startCatalogActivityIntent, LoadingActivity.MAIN_ACTIVITY_START_CODE);
    }

    public void saveSession(int id) {
        LoadingActivity
                .preferences.edit()
                .putBoolean("isSessionSaved", this.checkBoxSaveSession.isChecked())
                .putInt("userId", id)
                .apply();
    }


}