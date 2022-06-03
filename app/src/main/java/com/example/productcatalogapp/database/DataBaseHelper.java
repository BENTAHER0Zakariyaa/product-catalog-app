package com.example.productcatalogapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.productcatalogapp.classes.Category;
import com.example.productcatalogapp.classes.Client;
import com.example.productcatalogapp.classes.Command;
import com.example.productcatalogapp.classes.CommandLine;
import com.example.productcatalogapp.classes.Product;
import com.example.productcatalogapp.classes.ProductImage;
import com.example.productcatalogapp.classes.User;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "productCatalogApp";
    private static final int DATABASE_VERSION = 18;

    // Table Names
    private static final String TABLE_ = "";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_PRODUCT_IMAGES = "productImages";
    private static final String TABLE_CLIENTS = "clients";
    private static final String TABLE_COMMANDS = "commands";
    private static final String TABLE_COMMAND_LINES = "commandLines";


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
    private static final String KEY_PRODUCT_IMAGES_FULL_PATH = "fullPath";
    private static final String KEY_PRODUCT_IMAGES_DIR = "dir";
    private static final String KEY_PRODUCT_IMAGES_FILE_NAME = "fileName";


    // Clients Table Columns
    private static final String KEY_CLIENT_ = "";
    private static final String KEY_CLIENT_ID = "id";
    private static final String KEY_CLIENT_NAME = "name";
    private static final String KEY_CLIENT_EMAIL = "email";
    private static final String KEY_CLIENT_TOWN = "town";
    private static final String KEY_CLIENT_ADDRESS = "address";
    private static final String KEY_CLIENT_MAIN_PHONE_NUMBER = "mainPhoneNumber";
    private static final String KEY_CLIENT_SECOND_PHONE_NUMBER = "secondPhoneNumber";

    // Command Table Columns
    private static final String KEY_COMMAND_ = "";
    private static final String KEY_COMMAND_ID = "id";
    private static final String KEY_COMMAND_CLIENT_ID = "clientId";
    private static final String KEY_COMMAND_TOTAL = "total";
    private static final String KEY_COMMAND_IS_SYNC = "isSync";

    // Command line Table Columns
    private static final String KEY_COMMAND_LINES_ = "";
    private static final String KEY_COMMAND_LINES_ID = "id";
    private static final String KEY_COMMAND_LINES_COMMAND_ID = "commandId";
    private static final String KEY_COMMAND_LINES_PRODUCT_ID = "productId";
    private static final String KEY_COMMAND_LINES_QUANTITY = "quantity";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {

        String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY NOT NULL," +
                KEY_USER_NAME + " TEXT NOT NULL," +
                KEY_USER_PASSWORD + " TEXT NOT NULL," +
                KEY_USER_IS_CONNECTED + " INTEGER DEFAULT 0 NOT NULL," +
                KEY_USER_TOKEN + " TEXT NOT NULL" +
                ")";

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES +
                "(" +
                KEY_CATEGORY_ID + " INTEGER PRIMARY KEY NOT NULL," +
                KEY_CATEGORY_ID_PARENT_ID + " INTEGER NOT NULL," +
                KEY_CATEGORY_NAME + " TEXT NOT NULL" +
                ")";

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS +
                "(" +
                KEY_PRODUCT_ID + " INTEGER PRIMARY KEY NOT NULL," +
                KEY_PRODUCT_CATEGORY_ID + " INTEGER NOT NULL," +
                KEY_PRODUCT_TITLE + " TEXT NOT NULL," +
                KEY_PRODUCT_DESCRIPTION + " TEXT," +
                KEY_PRODUCT_PRICE + " REAL NOT NULL," +
                "FOREIGN KEY(" + KEY_PRODUCT_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + KEY_CATEGORY_ID + ")" +
                ")";

        String CREATE_PRODUCT_IMAGES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT_IMAGES +
                "(" +
                KEY_PRODUCT_IMAGES_ID + " INTEGER PRIMARY KEY NOT NULL," +
                KEY_PRODUCT_IMAGES_PRODUCT_ID + " INTEGER NOT NULL," +
                KEY_PRODUCT_IMAGES_FULL_PATH + " TEXT NOT NULL," +
                KEY_PRODUCT_IMAGES_DIR + " TEXT NOT NULL," +
                KEY_PRODUCT_IMAGES_FILE_NAME + " TEXT NOT NULL," +
                "FOREIGN KEY(" + KEY_PRODUCT_IMAGES_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + KEY_PRODUCT_ID + ")" +
                ")";

        String CREATE_CLIENT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CLIENTS +
                "(" +
                KEY_CLIENT_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                KEY_CLIENT_NAME + " TEXT NOT NULL," +
                KEY_CLIENT_TOWN + " TEXT NOT NULL," +
                KEY_CLIENT_EMAIL + " TEXT ," +
                KEY_CLIENT_ADDRESS + " TEXT NOT NULL," +
                KEY_CLIENT_MAIN_PHONE_NUMBER + " TEXT NOT NULL," +
                KEY_CLIENT_SECOND_PHONE_NUMBER + " TEXT" +
                ")";

        String CREATE_COMMAND_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_COMMANDS +
                "(" +
                KEY_COMMAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                KEY_COMMAND_CLIENT_ID + " INTEGER NOT NULL," +
                KEY_COMMAND_TOTAL + " REAL DEFAULT 0," +
                KEY_COMMAND_IS_SYNC + " INTEGER DEFAULT 0," +
                "FOREIGN KEY(" + KEY_COMMAND_CLIENT_ID + ") REFERENCES " + TABLE_CLIENTS + "(" + KEY_CLIENT_ID + ")" +
                ")";

        String CREATE_COMMAND_LINE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_COMMAND_LINES +
                "(" +
                KEY_COMMAND_LINES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                KEY_COMMAND_LINES_COMMAND_ID + " INTEGER NOT NULL," +
                KEY_COMMAND_LINES_PRODUCT_ID + " INTEGER NOT NULL," +
                KEY_COMMAND_LINES_QUANTITY + " INTEGER NOT NULL," +
                "FOREIGN KEY(" + KEY_COMMAND_LINES_COMMAND_ID + ") REFERENCES " + TABLE_COMMANDS + "(" + KEY_COMMAND_ID + ")," +
                "FOREIGN KEY(" + KEY_COMMAND_LINES_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + KEY_PRODUCT_ID + ")" +
                ")";



        // Execute queries
        database.execSQL(CREATE_USERS_TABLE);
        database.execSQL(CREATE_CATEGORIES_TABLE);
        database.execSQL(CREATE_PRODUCTS_TABLE);
        database.execSQL(CREATE_PRODUCT_IMAGES_TABLE);
        database.execSQL(CREATE_CLIENT_TABLE);
        database.execSQL(CREATE_COMMAND_TABLE);
        database.execSQL(CREATE_COMMAND_LINE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {

            String DROP_USERS_TABLE = "DROP TABLE IF EXISTS " + TABLE_USERS;
            String DROP_PRODUCT_IMAGES_TABLE = "DROP TABLE IF EXISTS " + TABLE_PRODUCT_IMAGES;
            String DROP_PRODUCTS_TABLE = "DROP TABLE IF EXISTS " + TABLE_PRODUCTS;
            String DROP_CATEGORIES_TABLE = "DROP TABLE IF EXISTS " + TABLE_CATEGORIES;
            String DROP_COMMAND_LINES_TABLE = "DROP TABLE IF EXISTS " + TABLE_COMMAND_LINES;
            String DROP_COMMANDS_TABLE = "DROP TABLE IF EXISTS " + TABLE_COMMANDS;
            String DROP_CLIENT_TABLE = "DROP TABLE IF EXISTS " + TABLE_CLIENTS;

            database.execSQL(DROP_USERS_TABLE);
            database.execSQL(DROP_PRODUCT_IMAGES_TABLE);
            database.execSQL(DROP_PRODUCTS_TABLE);
            database.execSQL(DROP_CATEGORIES_TABLE);
            database.execSQL(DROP_COMMAND_LINES_TABLE);
            database.execSQL(DROP_COMMANDS_TABLE);
            database.execSQL(DROP_CLIENT_TABLE);

            onCreate(database);
        }
    }

    public boolean isUserExist(int id){
        String query = " SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USER_ID + " = " + String.valueOf(id);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        long count = cursor.getCount();
        db.close();
        return count != 0;
    }

    public User getUser(String username, String password){
        User user = null;
        String query = " SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USER_NAME + " = '" + username +"' AND " + KEY_USER_PASSWORD + " = '" + password + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setConnected(cursor.getInt(3) == 1);
            user.setToken(cursor.getString(4));
        }
        cursor.close();
        return user;
    }

    public User getUser(int userId){
        User user = null;
        String query = " SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USER_ID + " = " + userId ;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setConnected(cursor.getInt(3) == 1);
            user.setToken(cursor.getString(4));

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
        String query = " SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + KEY_CATEGORY_ID + " = " + String.valueOf(id);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        long count = cursor.getCount();
        db.close();
        return count != 0;
    }

    public Category getCategory(int id){
        Category category = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectCategoryQuery = " SELECT * FROM " + TABLE_CATEGORIES + " WHERE " + KEY_CATEGORY_ID + " = " + id ;
        Cursor categoryCursor = db.rawQuery(selectCategoryQuery, null);
        if (categoryCursor.moveToFirst()) {
            category = new Category();
            category.setId(categoryCursor.getInt(0));
            category.setParentId(categoryCursor.getInt(1));
            category.setName(categoryCursor.getString(2));
        }
        categoryCursor.close();
        db.close();
        return category;
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

    public boolean isProductExist(int id){
        String query = " SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_PRODUCT_ID + " = " + String.valueOf(id);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        long count = cursor.getCount();
        db.close();
        return count != 0;
    }

    public boolean isProductImageExist(int id){
        String query = " SELECT * FROM " + TABLE_PRODUCT_IMAGES + " WHERE " + KEY_PRODUCT_IMAGES_ID + " = " + String.valueOf(id);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        long count = cursor.getCount();
        db.close();
        return count != 0;
    }

    public long addProduct(Product product) {
        long added = 0;
        if (!this.isProductExist(product.getId())){

            SQLiteDatabase db = this.getReadableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(KEY_PRODUCT_ID, product.getId());
            cv.put(KEY_PRODUCT_CATEGORY_ID, product.getCategory().getId());
            cv.put(KEY_PRODUCT_TITLE, product.getLabel());
            cv.put(KEY_PRODUCT_DESCRIPTION, product.getDescription());
            cv.put(KEY_PRODUCT_PRICE, product.getPrice());
            added = db.insert(TABLE_PRODUCTS, null, cv);
            db.close();

            ArrayList<ProductImage> productImages = product.getImages();
            for (int j = 0; j < productImages.size(); j++) {
                ProductImage image = productImages.get(j);
                this.addProductImage(image);
            }

            return added;
        }
        return added;
    }

    public ArrayList<Product> getProducts(){
        ArrayList<Product> products = null;
        String selectProductsQuery = "SELECT * FROM " + TABLE_PRODUCTS ;

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursorProducts = db.rawQuery(selectProductsQuery, null);
        if (cursorProducts.moveToFirst()){
            products = new ArrayList<Product>();
            do {
                Product p = new Product();
                p.setId(cursorProducts.getInt(0));
                p.setCategory(getCategory(p.getId()));
                p.setLabel(cursorProducts.getString(2));
                p.setDescription(cursorProducts.getString(3));
                p.setPrice(cursorProducts.getFloat(4));
                p.setImages(getProductImage(p.getId()));
                products.add(p);
            } while(cursorProducts.moveToNext());
        }
        cursorProducts.close();
        db.close();
        return products;
    }

    public Product getProduct(int id){
        Product product = null;
        String selectProductsQuery = "SELECT * FROM " + TABLE_PRODUCTS +" WHERE " + KEY_PRODUCT_ID + " = " + String.valueOf(id) ;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursorProducts = db.rawQuery(selectProductsQuery, null);
        if (cursorProducts.moveToFirst()){
            product = new Product();
            product.setId(cursorProducts.getInt(0));
            product.setCategory(getCategory(product.getId()));
            product.setLabel(cursorProducts.getString(2));
            product.setDescription(cursorProducts.getString(3));
            product.setPrice(cursorProducts.getFloat(4));
            product.setImages(getProductImage(product.getId()));
        }
        cursorProducts.close();
        db.close();
        return product;
    }

    public void addProductImage(ProductImage image) {
        if (!isProductImageExist(image.getId())){
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_PRODUCT_IMAGES_ID, image.getId());
            cv.put(KEY_PRODUCT_IMAGES_DIR, image.getFilePath());
            cv.put(KEY_PRODUCT_IMAGES_FILE_NAME, image.getFileName());
            cv.put(KEY_PRODUCT_IMAGES_FULL_PATH, image.getPath());
            cv.put(KEY_PRODUCT_IMAGES_PRODUCT_ID, image.getProductId());
            db.insert(TABLE_PRODUCT_IMAGES, null, cv);
            db.close();
        }
    }

    public ArrayList<ProductImage> getProductImage(int id){
        ArrayList<ProductImage> images = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectProductImageQuery = " SELECT * FROM " + TABLE_PRODUCT_IMAGES + " WHERE " + KEY_PRODUCT_IMAGES_PRODUCT_ID + " = " + id ;
        Cursor productImageCursor = db.rawQuery(selectProductImageQuery, null);
        if (productImageCursor.moveToFirst()) {
            images = new ArrayList<ProductImage>();
            do {
                ProductImage image = new ProductImage();
                image.setId(productImageCursor.getInt(0));
                image.setProductId(productImageCursor.getInt(1));
                image.setPath(productImageCursor.getString(2));
                image.setFilePath(productImageCursor.getString(3));
                image.setFileName(productImageCursor.getString(4));
                images.add(image);

            } while(productImageCursor.moveToNext());
        }
        productImageCursor.close();
        db.close();
        return images;
    }

    public int isClientExist(String phoneNumber){
        int id = -1;
        String query = " SELECT * FROM " + TABLE_CLIENTS + " WHERE " + KEY_CLIENT_MAIN_PHONE_NUMBER + " = '" + phoneNumber + "'";
        Log.d("DATABASE", "isClientExist: " + query);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst())
        {
            id = cursor.getInt(0);
            Log.d("DATABASE", "isClientExist: " + id);
        }
        db.close();
        return id;
    }

    public Client getClient(int clientId){
        Client client = null;
        String selectClientQuery = " SELECT * FROM " + TABLE_CLIENTS + " WHERE " + KEY_CLIENT_ID + " = " + String.valueOf(clientId);
        SQLiteDatabase db = getReadableDatabase();
        Cursor selectClientCursor = db.rawQuery(selectClientQuery, null);
        if (selectClientCursor.moveToFirst())
        {
            client = new Client();
            client.setId(selectClientCursor.getInt(0));
            client.setName(selectClientCursor.getString(1));
            client.setEmail(selectClientCursor.getString(2));
            client.setTown(selectClientCursor.getString(3));
            client.setAddress(selectClientCursor.getString(4));
            client.setMainPhoneNumber(selectClientCursor.getString(5));
            client.setSecondPhoneNumber(selectClientCursor.getString(6));

        }
        db.close();
        return client;
    }

    public int addClient(Client client){
        int clientId = isClientExist(client.getMainPhoneNumber());
        Log.d("DATABASE", "addClient: " + client.toString());
        Log.d("DATABASE", "addClient: BEFORE" + clientId);

        if (clientId == -1){

            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_CLIENT_NAME, client.getName());
            cv.put(KEY_CLIENT_EMAIL, client.getEmail());
            cv.put(KEY_CLIENT_ADDRESS, client.getAddress());
            cv.put(KEY_CLIENT_TOWN, client.getTown());
            cv.put(KEY_CLIENT_MAIN_PHONE_NUMBER, client.getMainPhoneNumber());
            cv.put(KEY_CLIENT_SECOND_PHONE_NUMBER, client.getSecondPhoneNumber());
            clientId = (int)db.insert(TABLE_CLIENTS, null, cv);

            Log.d("DATABASE", "addClient: AFTER INSERT" + clientId);

            db.close();
            return clientId;
        }
        return clientId;
    }

    public long addCommandLine(CommandLine commandLine){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_COMMAND_LINES_COMMAND_ID, commandLine.getCommand().getId());
        cv.put(KEY_COMMAND_LINES_PRODUCT_ID, commandLine.getProduct().getId());
        cv.put(KEY_COMMAND_LINES_QUANTITY, commandLine.getQuantity());
        long added = db.insert(TABLE_COMMAND_LINES, null, cv);
        db.close();
        return added;
    }

    public ArrayList<CommandLine> getCommandLines(int commandId){
        ArrayList<CommandLine> commandLines = null;
        String selectCommandLinesQuery = " SELECT * FROM " + TABLE_COMMAND_LINES + " WHERE " + KEY_COMMAND_LINES_COMMAND_ID + " = " + String.valueOf(commandId);
        SQLiteDatabase db = getReadableDatabase();
        Cursor selectCommandLinesCursor = db.rawQuery(selectCommandLinesQuery, null);
        if (selectCommandLinesCursor.moveToFirst())
        {
            commandLines = new ArrayList<CommandLine>();
            CommandLine commandLine = null;
            do {
                commandLine = new CommandLine();
                commandLine.setId(selectCommandLinesCursor.getInt(0));
                commandLine.setProduct(getProduct(selectCommandLinesCursor.getInt(2)));
                commandLine.setQuantity(selectCommandLinesCursor.getInt(3));
                commandLines.add(commandLine);
            } while (selectCommandLinesCursor.moveToNext());
        }
        db.close();
        return commandLines;
    }

    public long addCommand(Command command){
        Log.d("DATABASE", "addCommand: " + command.toString());

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_COMMAND_CLIENT_ID, command.getClient().getId());
        cv.put(KEY_COMMAND_TOTAL, command.getTotal());
        long added = db.insert(TABLE_COMMANDS, null, cv);
        db.close();
        command.setId(Integer.valueOf((int)added));
        for (int i = 0; i < command.commandLines.size(); i++) {
            addCommandLine(command.commandLines.get(i));
        }
        return added;
    }

    public ArrayList<Command> getCommands(){
        ArrayList<Command> commands = new ArrayList<Command>();
        String selectCommandsQuery = "SELECT * FROM " + TABLE_COMMANDS + " WHERE " + KEY_COMMAND_IS_SYNC + " = 0";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursorCommands = db.rawQuery(selectCommandsQuery, null);
        if (cursorCommands.moveToFirst()){
            commands = new ArrayList<Command>();
            Command command = null;
            do {
                command = new Command();
                command.setId(cursorCommands.getInt(0));
                command.setClient(getClient(cursorCommands.getInt(1)));
                command.setTotal(cursorCommands.getFloat(2));
                command.setSync(cursorCommands.getInt(3) != 0);
                command.commandLines = getCommandLines(command.getId());
                commands.add(command);
            } while (cursorCommands.moveToNext());
        }
        cursorCommands.close();
        db.close();
        return commands;
    }

    public void syncCommand(ArrayList<Command> commands){
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i < commands.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(KEY_COMMAND_IS_SYNC, 1);
            db.update(TABLE_COMMANDS, cv,KEY_COMMAND_ID + " =  ? ", new String[] {commands.get(i).getId().toString()});
        }
        db.close();
    }

}
