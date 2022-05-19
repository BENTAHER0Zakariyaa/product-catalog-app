package com.example.productcatalogapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "";
    private static final int DATABASE_VERSION= 1;

    // Table Names
    private static final String TABLE_ = "";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_PRODUCT_IMAGES = "productImages";

    // User Table Columns
    private static final String KEY_USER_ = "";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_IS_CONNECTED = "isConnected";
    private static final String KEY_USER_TOKEN = "token";

    // Product Table Columns
    private static final String KEY_PRODUCT_ = "";
    private static final String KEY_PRODUCT_ID = "id";
    private static final String KEY_PRODUCT_CATEGORY_ID = "categoryId";
    private static final String KEY_PRODUCT_TITLE = "title";
    private static final String KEY_PRODUCT_DESCRIPTION = "description";
    private static final String KEY_PRODUCT_PRICE = "price";

    // Product Categories Table Columns
    private static final String KEY_CATEGORY_ = "";
    private static final String KEY_CATEGORY_ID = "id";
    private static final String KEY_CATEGORY_NAME = "name";

    // Product Images Table Columns
    private static final String KEY_PRODUCT_IMAGES_ = "";
    private static final String KEY_PRODUCT_IMAGES_ID = "id";
    private static final String KEY_PRODUCT_IMAGES_PRODUCT_ID = "productId";
    private static final String KEY_PRODUCT_IMAGES_PATH = "path";

    public DataBaseHelper(@Nullable Context context) {
        super(context, this.DATABASE_NAME, null, this.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {

        String CREATE_USERS_TABLE = "CREATE TABLE " + this.TABLE_USERS +
                "(" +
                this.KEY_USER_ID + " INTEGER PRIMARY KEY NOT NULL," +
                this.KEY_USER_NAME + " TEXT NOT NULL," +
                this.KEY_USER_IS_CONNECTED + " INTEGER DEFAULT 0 NOT NULL," +
                this.KEY_USER_TOKEN + " TEXT NOT NULL" +
                ")";

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + this.TABLE_CATEGORIES +
                "(" +
                this.KEY_CATEGORY_ID + " INTEGER PRIMARY KEY NOT NULL," +
                this.KEY_CATEGORY_NAME + " TEXT NOT NULL" +
                ")";

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + this.TABLE_PRODUCTS +
                "(" +
                this.KEY_PRODUCT_ID + " INTEGER PRIMARY KEY NOT NULL," +
                this.KEY_PRODUCT_CATEGORY_ID + " INTEGER NOT NULL," +
                this.KEY_PRODUCT_TITLE + " TEXT NOT NULL," +
                this.KEY_PRODUCT_DESCRIPTION + " TEXT," +
                this.KEY_PRODUCT_PRICE + " REAL NOT NULL," +
                "FOREIGN KEY(" + this.KEY_PRODUCT_CATEGORY_ID + ") REFERENCES " + this.TABLE_CATEGORIES + "(" + this.KEY_CATEGORY_ID + ")" +
                ")";

        String CREATE_PRODUCT_IMAGES_TABLE = "CREATE TABLE " + this.TABLE_PRODUCT_IMAGES +
                "(" +
                this.KEY_PRODUCT_IMAGES_ID + " INTEGER PRIMARY KEY NOT NULL," +
                this.KEY_PRODUCT_IMAGES_PRODUCT_ID + " INTEGER NOT NULL," +
                this.KEY_PRODUCT_IMAGES_PATH + " TEXT NOT NULL," +
                "FOREIGN KEY(" + this.KEY_PRODUCT_IMAGES_PRODUCT_ID + ") REFERENCES " + this.TABLE_PRODUCTS + "(" + this.KEY_PRODUCT_ID + ")" +
                ")";

        // Execute queries
        database.execSQL(CREATE_USERS_TABLE);
        database.execSQL(CREATE_CATEGORIES_TABLE);
        database.execSQL(CREATE_PRODUCTS_TABLE);
        database.execSQL(CREATE_PRODUCT_IMAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            onCreate(database);
        }
    }
}
