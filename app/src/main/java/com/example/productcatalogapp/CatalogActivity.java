package com.example.productcatalogapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.productcatalogapp.API.APIHelper;
import com.example.productcatalogapp.adapters.MainGridViewAdapter;
import com.example.productcatalogapp.classes.Category;
import com.example.productcatalogapp.classes.Product;
import com.example.productcatalogapp.classes.ProductImage;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CatalogActivity extends AppCompatActivity {

    // TAGS
    private static final String LOG_DATABASE_TAG = "DATABASE";

    private static final String CATALOG_ACTIVITY_IMAGES_FOLDER = "/images";

    private ArrayList<Category> categories = null;
    private ArrayList<Product> products = null;

    private Button buttonLogout = null;
    private Button buttonUpdate = null;
    private RecyclerView recyclerViewProduct = null;

    MainGridViewAdapter mainGridViewAdapter = null;

    private View.OnClickListener buttonLogoutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity.preferences.edit().putBoolean("isSaved", false).putInt("id", -1).apply();
            finish();
        }
    };

    private View.OnClickListener buttonUpdateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateCategory("product");

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // INIT VIEWS
        this.buttonLogout = this.findViewById(R.id.idButtonLogout);
        this.buttonUpdate = this.findViewById(R.id.idButtonUpdate);
        this.recyclerViewProduct = this.findViewById(R.id.idRecyclerViewProducts);
        // SET EVENTS
        this.buttonLogout.setOnClickListener(this.buttonLogoutOnClickListener);
        this.buttonUpdate.setOnClickListener(this.buttonUpdateOnClickListener);

        // IF OFFLINE DO
        products = MainActivity.DB.getProducts();


        fillGrid();

    }


    public void fillGrid(){
        mainGridViewAdapter = new MainGridViewAdapter(this, products);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);

        this.recyclerViewProduct.setLayoutManager(gridLayoutManager);
        this.recyclerViewProduct.setAdapter(mainGridViewAdapter);
    }


    void updateCategory(String type){
        RequestQueue requestCategoriesQueue = Volley.newRequestQueue(CatalogActivity.this);
        String URL = APIHelper.API_URL + "/categories?type="+type;
        JsonArrayRequest requestCategories = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {

                categories = new ArrayList<Category>();

                for (int i=0; i < response.length(); i++) {
                    try {
                        JSONObject category = response.getJSONObject(i);

                        Category c = new Category();

                        c.setId(category.getInt("id"));
                        c.setParentId(category.getInt("fk_parent"));
                        c.setName(category.getString("label"));

                        categories.add(c);

                        long isAdded = MainActivity.DB.addCategory(c);

                        Log.d(LOG_DATABASE_TAG, "Is Category ADDED TO ARRAY LIST: " + c.toString());
                        Log.d(LOG_DATABASE_TAG, "Is Category ADDED TO DATABASE : " + isAdded);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                updateProduct();
            }
        }, null){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap();
                header.put("DOLAPIKEY", MainActivity.currentUser.getToken());
                return header;
            }
        };
        requestCategoriesQueue.add(requestCategories);
    }

    void updateProduct(){
        RequestQueue requestProductsQueue = Volley.newRequestQueue(CatalogActivity.this);
        JsonArrayRequest requestProducts = new JsonArrayRequest(Request.Method.GET, APIHelper.CUSTOM_API_URL, null, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {

                products = new ArrayList<Product>();
                for (int i=0; i < response.length(); i++) {
                    try {

                        JSONObject product = response.getJSONObject(i);

                        Product p = new Product();

                        p.setId(product.getInt("id"));
                        p.setLabel(product.getString("label"));
                        p.setDescription(product.getString("description"));
                        p.setPrice((float)product.getDouble("price"));

                        Category c = new Category();

                        c.setId(product.getJSONObject("categories").getInt("id"));
                        c.setParentId(product.getJSONObject("categories").getInt("parentId"));
                        c.setName(product.getJSONObject("categories").getString("label"));

                        p.setCategory(c);

                        JSONArray images = product.getJSONArray("images");

                        for (int j = 0; j < images.length() ; j++){

                            ProductImage image = new ProductImage();

                            image.setId(images.getJSONObject(j).getInt("id"));
                            image.setName(images.getJSONObject(j).getString("label"));
                            image.setFileName(images.getJSONObject(j).getString("filename"));
                            image.setFilePath(images.getJSONObject(j).getString("filepath"));
                            image.setPath("/"+image.getFilePath()+"/"+image.getFileName());
                            image.setProductId(p.getId());

                            p.addImage(image);

                            DownloadImage(APIHelper.DOCUMENTS_PATH+image.getPath(), "/"+image.getFilePath(), "/"+image.getFileName());
                        }

                        products.add(p);

                        long isAdded = MainActivity.DB.addProduct(p);

                        Log.d(LOG_DATABASE_TAG, "Is Product ADDED TO ARRAY LIST: " + p.toString());
                        Log.d(LOG_DATABASE_TAG, "Is Product ADDED TO DATABASE : " + isAdded);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                fillGrid();
            }
        }, null){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap();
                header.put("DOLAPIKEY", MainActivity.currentUser.getToken());
                return header;
            }
        };
        requestProductsQueue.add(requestProducts);
    }



    void DownloadImage(String ImageUrl,String Dir,String ImageName) {

        if (    ContextCompat.checkSelfPermission(CatalogActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(CatalogActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CatalogActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MainActivity.READ_STORAGE_PERMISSION_CODE);
                ActivityCompat.requestPermissions(CatalogActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.WRITE_STORAGE_PERMISSION_CODE);
//            showToast("Need Permission to access storage for Downloading Image");
        } else {
//            showToast("Downloading Image...");
            new DownloadsImage().execute(ImageUrl, Dir, ImageName);
        }
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
                         Log.i("ExternalStorage", "Scanned " + path + ":");
                         Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            showToast("Image Saved!");
        }
    }

    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}


