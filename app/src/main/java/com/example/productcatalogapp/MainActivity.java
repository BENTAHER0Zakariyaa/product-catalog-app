package com.example.productcatalogapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.productcatalogapp.classes.User;
import com.example.productcatalogapp.database.DataBaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String APP_NAME = "com.example.productcatalogapp";

    public static DataBaseHelper DB = null;

    private EditText editTextUserName = null;
    private EditText editTextPassword = null;
    private CheckBox checkBoxSaveSession = null;
    private Button buttonLogin = null;

    public static SharedPreferences preferences = null;
    public static User currentUser = null;



    private View.OnClickListener buttonLoginOnClickListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            buttonLogin.setEnabled(false);
            currentUser = new User(editTextUserName.getText().toString(), editTextPassword.getText().toString());

            boolean isConnected = isConnected();

            currentUser.setConnected(isConnected);
            if(isConnected){
                if (currentUser.getUsername().equals("")){
                    Toast.makeText(MainActivity.this, R.string.main_activity_error_username_required, Toast.LENGTH_SHORT).show();
                    buttonLogin.setEnabled(true);
                }
                else if (currentUser.getPassword().equals("")){
                    Toast.makeText(MainActivity.this, R.string.main_activity_error_password_required, Toast.LENGTH_SHORT).show();
                    buttonLogin.setEnabled(true);
                }
                else {
                    String URL = APIHelper.API_URL + "/login?login=" + currentUser.getUsername() + "&password=" + currentUser.getPassword();
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                currentUser.setToken(response.getJSONObject("success").getString("token"));

                                RequestQueue requestInfoQueue = Volley.newRequestQueue(MainActivity.this);
                                String URL = APIHelper.API_URL + "/users/login/" + currentUser.getUsername();

                                JsonObjectRequest requestInfo = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            currentUser.setId(Integer.valueOf(response.getString("id")));
                                            if (!DB.isUserExist(currentUser.getId())){
                                                DB.addUser(currentUser);
                                            }
                                            Intent intent = new Intent(MainActivity.this, CatalogActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(intent);
                                            saveSession(currentUser.getId());
                                            buttonLogin.setEnabled(true);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            buttonLogin.setEnabled(true);
                                        }
                                    }

                                }, null){
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        HashMap header = new HashMap();
                                        header.put("DOLAPIKEY", currentUser.getToken());
                                        return header;
                                    }
                                };
                                requestInfoQueue.add(requestInfo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                buttonLogin.setEnabled(true);
                            }
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, R.string.main_activity_error_invalid_fields, Toast.LENGTH_SHORT).show();
                            buttonLogin.setEnabled(true);
                        }
                    });
                    requestQueue.add(request);
                }
            }
            else {
                currentUser = DB.getUser(currentUser.getUsername(), currentUser.getPassword());

                if (currentUser!=null) {
                    currentUser.setConnected(false);
                    Intent intent = new Intent(MainActivity.this, CatalogActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    saveSession(currentUser.getId());
                    buttonLogin.setEnabled(true);
                }
                else
                    Toast.makeText(MainActivity.this, R.string.main_activity_error_invalid_fields, Toast.LENGTH_SHORT).show();
                    buttonLogin.setEnabled(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DB = new DataBaseHelper(this);

        this.editTextUserName = this.findViewById(R.id.idEditTextUserName);
        this.editTextPassword = this.findViewById(R.id.idEditTextPassword);

        editTextUserName.setText(APIHelper.TEST_USER_NAME);
        editTextPassword.setText(APIHelper.TEST_PASSWORD);

        this.checkBoxSaveSession = this.findViewById(R.id.idCheckBoxSaveSession);
        this.buttonLogin = this.findViewById(R.id.idButtonLogin);
        buttonLogin.setEnabled(false);

        preferences = getSharedPreferences(APP_NAME, this.MODE_PRIVATE);
        boolean isSaved = preferences.getBoolean("isSaved", false);
        if (isSaved){
            int id = preferences.getInt("id", -1);
            if (id != -1)
            {
                currentUser = DB.getUser(id);
                if (currentUser != null) {
                    currentUser.setConnected(isConnected());
                    Intent intent = new Intent(MainActivity.this, CatalogActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    saveSession(currentUser.getId());
                    buttonLogin.setEnabled(true);
                }
            }
        }else{
            buttonLogin.setEnabled(true);
        }

        this.buttonLogin.setOnClickListener(this.buttonLoginOnClickListener);
    }

    public boolean isConnected() {
        try {
            String command = "ping -c 1 google.com";
            return Runtime.getRuntime().exec(command).waitFor() == 0;
        } catch (InterruptedException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public void saveSession(int id){
        preferences.edit()
                .putBoolean("isSaved", this.checkBoxSaveSession.isChecked())
                .putInt("id", id)
                .apply();
    }





}