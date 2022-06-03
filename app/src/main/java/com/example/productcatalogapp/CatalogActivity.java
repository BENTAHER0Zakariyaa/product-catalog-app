package com.example.productcatalogapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.productcatalogapp.API.APIHelper;
import com.example.productcatalogapp.adapters.MainGridViewAdapter;
import com.example.productcatalogapp.classes.Category;
import com.example.productcatalogapp.classes.Command;
import com.example.productcatalogapp.classes.CustomToast;
import com.example.productcatalogapp.classes.DialogConfirmation;
import com.example.productcatalogapp.classes.Product;
import com.example.productcatalogapp.classes.ProductImage;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CatalogActivity extends AppCompatActivity {

    private ArrayList<Category> categories = null;
    private ArrayList<Product> products = null;

    private Button buttonLogout = null;
    private Button buttonUpdate = null;
    private Button buttonUpdateCommands = null;
    private RecyclerView recyclerViewProduct = null;

    public static Button buttonCart = null;

    private MainGridViewAdapter mainGridViewAdapter = null;

    private View.OnClickListener buttonLogoutOnClickListener = new View.OnClickListener() {

        DialogConfirmation logoutDialogConfirmation = null;

        View.OnClickListener logoutDialogConfirmationAcceptButton = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutDialogConfirmation.getAlertDialog().cancel();
                LoadingActivity.preferences.edit().putBoolean("isSessionSaved", false).putInt("userId", -1).apply();
                Intent intent = new Intent();
                setResult(LoadingActivity.CATALOG_ACTIVITY_START_CODE, intent);
                LoadingActivity.cart.removeAll();
                finish();
            }
        };

        View.OnClickListener logoutDialogConfirmationCancelButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialogConfirmation.getAlertDialog().cancel();
            }
        };

        @Override
        public void onClick(View v) {

            this.logoutDialogConfirmation = new DialogConfirmation(CatalogActivity.this, R.string.confirmation_logout_message, R.drawable.ic_warning, logoutDialogConfirmationAcceptButton, logoutDialogConfirmationCancelButton);
            this.logoutDialogConfirmation.Create();
            this.logoutDialogConfirmation.Show();
        }
    };

    private View.OnClickListener buttonCartOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (LoadingActivity.cart.isNotEmpty()){
                Intent intent = new Intent(CatalogActivity.this, CartActivity.class);
                startActivity(intent);
            }else{
                CustomToast toast = new CustomToast(CatalogActivity.this,  R.string.error_cart_is_empty, R.drawable.ic_cart_64);
                toast.Make();
                toast.Show();
            }
        }
    };

    private View.OnClickListener buttonUpdateCommandsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(LoadingActivity.isConnected()) {
                ArrayList<Command> commands = LoadingActivity.DB.getCommands();

                CatalogActivity.this.buttonUpdateCommands.setEnabled(false);
                CatalogActivity.this.buttonUpdateCommands.setText(R.string.action_syncing);

                if (commands.size() != 0 ) {

                    Gson gson = new Gson();
                    String json = gson.toJson(commands);
                    RequestQueue requestCreateCommandQueue = Volley.newRequestQueue(CatalogActivity.this);
                    String URL = APIHelper.CUSTOM_API_URL + "?page=createCommand";

                    StringRequest jsonObjReq = new StringRequest(
                            Request.Method.PUT,
                            URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    LoadingActivity.DB.syncCommand(commands);
                                    ArrayList<Command> commands = new ArrayList<Command>();
                                    CatalogActivity.this.buttonUpdateCommands.setEnabled(true);
                                    CatalogActivity.this.buttonUpdateCommands.setText(R.string.action_sync_commands);
                                    CustomToast toast = new CustomToast(CatalogActivity.this,  R.string.success_command_sync, R.drawable.ic_success_64);
                                    toast.Make();
                                    toast.Show();

                                }
                            },
                            null
                    ) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap header = new HashMap();
                            header.put("DOLAPIKEY", LoadingActivity.currentUser.getToken());
                            return header;
                        }

                        public byte[] getBody() throws AuthFailureError {
                            return json.getBytes();
                        }

                    };
                    requestCreateCommandQueue.add(jsonObjReq);
                }
                else {
                    CustomToast toast = new CustomToast(CatalogActivity.this,  R.string.error_command_sync, R.drawable.ic_warning);
                    toast.Make();
                    toast.Show();
                    CatalogActivity.this.buttonUpdateCommands.setEnabled(true);
                    CatalogActivity.this.buttonUpdateCommands.setText(R.string.action_sync_commands);
                }
            }
            else {
                CustomToast toast = new CustomToast(CatalogActivity.this,  R.string.error_you_are_not_connected, R.drawable.ic_no_internet_64);
                toast.Make();
                toast.Show();
                CatalogActivity.this.buttonUpdateCommands.setEnabled(true);
                CatalogActivity.this.buttonUpdateCommands.setText(R.string.action_sync_commands);

            }
        }
    };

    private View.OnClickListener buttonUpdateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CatalogActivity.this.buttonUpdate.setEnabled(false);
            CatalogActivity.this.buttonUpdate.setText(R.string.action_syncing);
            if(LoadingActivity.isConnected()) {
                updateCategory("product");
            }
            else {
                CatalogActivity.this.buttonUpdate.setEnabled(true);
                CatalogActivity.this.buttonUpdate.setText(R.string.action_sync_products);
                CustomToast toast = new CustomToast(CatalogActivity.this,  R.string.error_you_are_not_connected, R.drawable.ic_no_internet_64);
                toast.Make();
                toast.Show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_catalog);

        // INIT VIEWS
        this.buttonLogout = this.findViewById(R.id.idButtonLogout);
        this.buttonUpdate = this.findViewById(R.id.idButtonUpdate);
        this.buttonUpdateCommands = this.findViewById(R.id.idButtonUpdateCommands);
        this.buttonCart = this.findViewById(R.id.idButtonCart);


        LoadingActivity.preferences = this.getSharedPreferences(LoadingActivity.APP_NAME, this.MODE_PRIVATE);

        int userId = LoadingActivity.preferences.getInt("userId", -1);

        if (LoadingActivity.currentUser == null){
            LoadingActivity.currentUser = LoadingActivity.DB.getUser(userId);
        }

        this.recyclerViewProduct = this.findViewById(R.id.idRecyclerViewProducts);

        // SET EVENTS
        this.buttonLogout.setOnClickListener(this.buttonLogoutOnClickListener);
        this.buttonUpdate.setOnClickListener(this.buttonUpdateOnClickListener);

        this.buttonUpdateCommands.setOnClickListener(this.buttonUpdateCommandsOnClickListener);
        this.buttonCart.setOnClickListener(buttonCartOnClickListener);


        // IF OFFLINE DO
        this.products = LoadingActivity.DB.getProducts();
        this.fillGrid();

        this.buttonCart.setText(getString(R.string.cart, LoadingActivity.cart.getCount(), LoadingActivity.cart.getTotal()));
    }

    @Override
    public void onBackPressed() {

    }

    public void fillGrid(){

        this.mainGridViewAdapter = new MainGridViewAdapter(this, products);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);

        this.recyclerViewProduct.setLayoutManager(gridLayoutManager);
        this.recyclerViewProduct.setAdapter(mainGridViewAdapter);
    }

    void updateCategory(String type){
        RequestQueue requestCategoriesQueue = Volley.newRequestQueue(CatalogActivity.this);
        String URL = APIHelper.API_URL + "/categories?type="+type;
        JsonArrayRequest requestCategories = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                categories = new ArrayList<Category>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject category = response.getJSONObject(i);

                        Category c = new Category();

                        c.setId(category.getInt("id"));
                        c.setParentId(category.getInt("fk_parent"));
                        c.setName(category.getString("label"));

                        categories.add(c);

                        long isAdded = LoadingActivity.DB.addCategory(c);

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
                updateProduct();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomToast toast = new CustomToast(CatalogActivity.this,  R.string.error_from_server, R.drawable.ic_warning);
                toast.Make();
                toast.Show();
                CatalogActivity.this.buttonUpdateCommands.setEnabled(true);
                CatalogActivity.this.buttonUpdateCommands.setText(R.string.action_sync_commands);

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap();
                header.put("DOLAPIKEY", LoadingActivity.currentUser.getToken());
                return header;
            }
        };
        requestCategoriesQueue.add(requestCategories);
    }

    void updateProduct(){
        RequestQueue requestProductsQueue = Volley.newRequestQueue(CatalogActivity.this);
        String URL = APIHelper.CUSTOM_API_URL+"?page=products";
        JsonArrayRequest requestProducts = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                products = new ArrayList<Product>();
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject product = response.getJSONObject(i);

                        Product p = new Product();

                        p.setId(product.getInt("id"));
                        p.setLabel(product.getString("label"));
                        p.setDescription(product.getString("description"));
                        p.setPrice((float) product.getDouble("price"));

                        Category c = new Category();

                        c.setId(product.getJSONObject("categories").getInt("id"));
                        c.setParentId(product.getJSONObject("categories").getInt("parentId"));
                        c.setName(product.getJSONObject("categories").getString("label"));

                        p.setCategory(c);

                        JSONArray images = product.getJSONArray("images");

                        for (int j = 0; j < images.length(); j++) {

                            ProductImage image = new ProductImage();

                            image.setId(images.getJSONObject(j).getInt("id"));
                            image.setName(images.getJSONObject(j).getString("label"));
                            image.setFileName(images.getJSONObject(j).getString("filename"));
                            image.setFilePath(images.getJSONObject(j).getString("filepath"));
                            image.setPath("/" + image.getFilePath() + "/" + image.getFileName());
                            image.setProductId(p.getId());

                            p.addImage(image);
                            new DownloadsImage().execute(APIHelper.DOCUMENTS_PATH + image.getPath(), "/" + image.getFilePath(), "/" + image.getFileName());
                        }

                        products.add(p);

                        long isAdded = LoadingActivity.DB.addProduct(p);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                fillGrid();
                CustomToast toast = new CustomToast(CatalogActivity.this, R.string.action_done, R.drawable.ic_success_64);
                toast.Make();
                toast.Show();
                CatalogActivity.this.buttonUpdate.setEnabled(true);
                CatalogActivity.this.buttonUpdate.setText(R.string.action_sync_products);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CustomToast toast = new CustomToast(CatalogActivity.this,  R.string.error_from_server, R.drawable.ic_warning);
                toast.Make();
                toast.Show();
                CatalogActivity.this.buttonUpdateCommands.setEnabled(true);
                CatalogActivity.this.buttonUpdateCommands.setText(R.string.action_sync_commands);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap();
                header.put("DOLAPIKEY", LoadingActivity.currentUser.getToken());
                return header;
            }
        };
        requestProductsQueue.add(requestProducts);
    }

    class DownloadsImage extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bm = null;
            try {
                bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Create Path to save Image
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/ProductCatalogApp" + strings[1]); //Creates app specific folder

            if (!path.exists()) {
                path.mkdirs();
            }

            File imageFile = new File(path, strings[2]); // Imagename.png
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(imageFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                bm.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
                out.flush();
                out.close();
                // Tell the media scanner about the new file so that it is
                // immediately available to the user.
                MediaScannerConnection.scanFile(CatalogActivity.this, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}


