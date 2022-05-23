package com.example.productcatalogapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.productcatalogapp.classes.Category;
import com.example.productcatalogapp.classes.User;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "productCatalogApp";
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
    private static final String KEY_USER_PASSWORD = "password";
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
    private static final String KEY_CATEGORY_ID_PARENT_ID = "parentId";
    private static final String KEY_CATEGORY_NAME = "name";

    // Product Images Table Columns
    private static final String KEY_PRODUCT_IMAGES_ = "";
    private static final String KEY_PRODUCT_IMAGES_ID = "id";
    private static final String KEY_PRODUCT_IMAGES_PRODUCT_ID = "productId";
    private static final String KEY_PRODUCT_IMAGES_PATH = "path";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY NOT NULL," +
                KEY_USER_NAME + " TEXT NOT NULL," +
                KEY_USER_PASSWORD + " TEXT NOT NULL," +
                KEY_USER_IS_CONNECTED + " INTEGER DEFAULT 0 NOT NULL," +
                KEY_USER_TOKEN + " TEXT NOT NULL" +
                ")";

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES +
                "(" +
                KEY_CATEGORY_ID + " INTEGER PRIMARY KEY NOT NULL," +
                KEY_CATEGORY_ID_PARENT_ID + " INTEGER NOT NULL," +
                KEY_CATEGORY_NAME + " TEXT NOT NULL" +
                ")";

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS +
                "(" +
                KEY_PRODUCT_ID + " INTEGER PRIMARY KEY NOT NULL," +
                KEY_PRODUCT_CATEGORY_ID + " INTEGER NOT NULL," +
                KEY_PRODUCT_TITLE + " TEXT NOT NULL," +
                KEY_PRODUCT_DESCRIPTION + " TEXT," +
                KEY_PRODUCT_PRICE + " REAL NOT NULL," +
                "FOREIGN KEY(" + KEY_PRODUCT_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + KEY_CATEGORY_ID + ")" +
                ")";

        String CREATE_PRODUCT_IMAGES_TABLE = "CREATE TABLE " + TABLE_PRODUCT_IMAGES +
                "(" +
                KEY_PRODUCT_IMAGES_ID + " INTEGER PRIMARY KEY NOT NULL," +
                KEY_PRODUCT_IMAGES_PRODUCT_ID + " INTEGER NOT NULL," +
                KEY_PRODUCT_IMAGES_PATH + " TEXT NOT NULL," +
                "FOREIGN KEY(" + KEY_PRODUCT_IMAGES_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + KEY_PRODUCT_ID + ")" +
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

            String DROP_USERS_TABLE = "DROP TABLE " + TABLE_USERS;
            String DROP_PRODUCT_IMAGES_TABLE = "DROP TABLE " + TABLE_PRODUCT_IMAGES;
            String DROP_PRODUCTS_TABLE = "DROP TABLE " + TABLE_PRODUCTS;
            String DROP_CATEGORIES_TABLE = "DROP TABLE " + TABLE_CATEGORIES;

            database.execSQL(DROP_USERS_TABLE);
            database.execSQL(DROP_PRODUCT_IMAGES_TABLE);
            database.execSQL(DROP_PRODUCTS_TABLE);
            database.execSQL(DROP_CATEGORIES_TABLE);

            onCreate(database);
        }
    }

    public boolean isUserExist(int id){
        String query = " SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USER_ID + " = " + Integer.valueOf(id);
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_USERS);
        db.close();
        return count != 0;
    }

    public User getUser(String username, String password){
        User user = null;
        String query = " SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USER_NAME + " = '" + username +"' AND " + KEY_USER_PASSWORD + " = '" + password + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
           user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getInt(4) == 1);
        }
        cursor.close();
        return user;
    }

    public User getUser(int id){
        User user = null;
        String query = " SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USER_ID + " = " + id ;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getInt(4) == 1);
        }
        cursor.close();
        return user;
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_USER_ID, user.getId());
        cv.put(KEY_USER_NAME, user.getUsername());
        cv.put(KEY_USER_PASSWORD, user.getPassword());
        cv.put(KEY_USER_TOKEN, user.getToken());
        cv.put(KEY_USER_IS_CONNECTED, user.getConnected() ? 1 : 0);
        db.insert(TABLE_USERS, null, cv);
        db.close();
    }

    public boolean isCategoryExist(int id){
        String query = " SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + KEY_CATEGORY_ID + " = " + Integer.valueOf(id);
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_USERS);
        db.close();
        return count != 0;
    }

    public long addCategory(Category category){
        if (!isCategoryExist(category.getId())){
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_CATEGORY_ID, category.getId());
            cv.put(KEY_CATEGORY_ID_PARENT_ID, category.getParentId());
            cv.put(KEY_CATEGORY_NAME, category.getName());
            long added = db.insert(TABLE_CATEGORIES, null, cv);
            db.close();
            return added;
        }
        return 0;
    }



}
