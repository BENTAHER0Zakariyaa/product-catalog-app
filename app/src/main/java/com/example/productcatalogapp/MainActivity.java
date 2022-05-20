package com.example.productcatalogapp;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.productcatalogapp.API.APIHelper;
import com.example.productcatalogapp.classes.User;
import com.example.productcatalogapp.database.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static DataBaseHelper DB = null;

    private EditText editTextUserName = null;
    private EditText editTextPassword = null;
    private CheckBox checkBoxSaveSession = null;
    private Button buttonLogin = null;

    private User currentUser = null;



    private View.OnClickListener buttonLoginOnClickListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            currentUser = new User(editTextUserName.getText().toString(), editTextPassword.getText().toString());

            boolean isConnected = false;
            String token = "";
            try {
                isConnected = isConnected();
            } catch (InterruptedException e) {e.printStackTrace();} catch (IOException e) {
                e.printStackTrace();
            }
            currentUser.setConnected(isConnected);
            if(isConnected){
                if (currentUser.getUsername().equals("")){
                    Toast.makeText(MainActivity.this, R.string.main_activity_error_username_required, Toast.LENGTH_SHORT).show();
                }
                else if (currentUser.getPassword().equals("")){
                    Toast.makeText(MainActivity.this, R.string.main_activity_error_password_required, Toast.LENGTH_SHORT).show();
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

                                        } catch (JSONException e) {
                                            e.printStackTrace();
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
                            }
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, R.string.main_activity_error_invalid_fields, Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(request);
                }
            }
            else {

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

        this.buttonLogin.setOnClickListener(this.buttonLoginOnClickListener);
        if (DB.getUser(APIHelper.TEST_USER_NAME, APIHelper.TEST_PASSWORD)==null)
            Toast.makeText(this, "KHAWI", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "3amr", Toast.LENGTH_SHORT).show();
    }

    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }



}