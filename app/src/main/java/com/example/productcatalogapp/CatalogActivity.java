package com.example.productcatalogapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.productcatalogapp.classes.Category;
import com.example.productcatalogapp.classes.Product;
import com.example.productcatalogapp.classes.ProductImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CatalogActivity extends AppCompatActivity {

    private static final String CATALOG_ACTIVITY_IMAGES_FOLDER = "/images";

    private ArrayList<Category> categories = null;
    private ArrayList<Product> products = null;

    private Button buttonLogout = null;
    private Button buttonUpdate = null;

    private View.OnClickListener buttonLogoutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity.preferences.edit()
                    .putBoolean("isSaved", false)
                    .putInt("id", -1)
                    .apply();
            finish();
        }
    };

    private View.OnClickListener buttonUpdateOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateCategory("product");
            updateProduct();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        this.buttonLogout = this.findViewById(R.id.idButtonLogout);
        this.buttonUpdate = this.findViewById(R.id.idButtonUpdate);

        this.buttonLogout.setOnClickListener(this.buttonLogoutOnClickListener);
        this.buttonUpdate.setOnClickListener(this.buttonUpdateOnClickListener);


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
                        c.setName(category.getString("label"));
                        c.setParentId(category.getInt("fk_parent"));
                        categories.add(c);
                        Log.d("Category", "onResponse: " + MainActivity.DB.addCategory(c));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
        String URL = "https://demo.datacium.com/erp-crm/getProducts.php";

        JsonArrayRequest requestProducts = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>(){
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

                        JSONArray images = product.getJSONArray("images");

                        for (int j = 0; j < images.length() ; j++){
                            ProductImage image = new ProductImage();
                            image.setId(images.getJSONObject(j).getInt("id"));
                            image.setName(images.getJSONObject(j).getString("label"));
                            image.setFileName(images.getJSONObject(j).getString("filename"));
                            image.setFilePath(images.getJSONObject(j).getString("filepath"));
                            image.setPath("/"+image.getFilePath()+"/"+image.getFileName());
                            p.addImage(image);
                            Log.d("URL", "onResponse: " + "https://demo.datacium.com/erp-crm/documents"+image.getPath());
                            Bitmap img = getBitmapFromURL("https://demo.datacium.com/erp-crm/documents"+image.getPath());

                        }
                        p.setCategory(c);
                        products.add(p);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                for (int i = 0; i < products.size(); i++) {
                    Log.d("proc", "onClick: " + products.get(i).toString());
                }
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


    public void saveImageToExternal(String imgName, Bitmap bm) throws IOException {
        //Create Path to save Image
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+CATALOG_ACTIVITY_IMAGES_FOLDER); //Creates app specific folder
        path.mkdirs();
        File imageFile = new File(path, imgName); // Imagename.png
        FileOutputStream out = new FileOutputStream(imageFile);
        try{
            bm.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
            out.flush();
            out.close();

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(CatalogActivity.this,new String[] { imageFile.getAbsolutePath() }, null,new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });
        } catch(Exception e) {
            throw new IOException();
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}


