package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Ucer on 10/8/2016.
 */

public class SQLITEHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLITEHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String USER_PROFILE = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "fullName";
    private static final String KEY_EMAIL = "eMail";
    private static final String KEY_UID = "uid";
    private static final String KEY_DATE_OF_BIRTH = "dateOfBirth";
    private static final String KEY_BLOODTYPE = "bloodType";
    private static final String KEY_PHONENUMBER = "phoneNumber";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_IMAGE = "image";


    public SQLITEHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + USER_PROFILE + "( "
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, "
                + KEY_EMAIL + " TEXT UNIQUE, " + KEY_UID + " TEXT, "
                + KEY_DATE_OF_BIRTH + " TEXT, " + KEY_BLOODTYPE + " TEXT, "
                + KEY_PHONENUMBER + " TEXT, " +
                  KEY_GENDER + " TEXT, " + KEY_IMAGE + " TEXT " + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + USER_PROFILE);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String fullName, String eMail, String uid, String dateOfBirth,
                        String bloodType, String phoneNumber , String Gender, String image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, fullName); // Name
        values.put(KEY_EMAIL, eMail); // Email
        values.put(KEY_UID, uid); // uid
        values.put(KEY_DATE_OF_BIRTH, dateOfBirth); // dateOfBirth
        values.put(KEY_BLOODTYPE, bloodType);
        values.put(KEY_PHONENUMBER, phoneNumber);
        values.put(KEY_GENDER, Gender);
        values.put(KEY_IMAGE, image);


        // Inserting Row
        long id = db.insert(USER_PROFILE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Storing user details in database
     * */

    public void updateUser(String uid, String fullName, String eMail, String dateOfBirth,
                           String phoneNumber, String bloodType, String image){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_UID, uid);
        values.put(KEY_NAME, fullName); // Name
        values.put(KEY_EMAIL, eMail); // Email
        values.put(KEY_DATE_OF_BIRTH, dateOfBirth); // dateOfBirth
        values.put(KEY_PHONENUMBER, phoneNumber); // phoneNumber
        values.put(KEY_BLOODTYPE, bloodType);// bloodType
        values.put(KEY_IMAGE, image); // image url

        long id = db.update(USER_PROFILE, values, KEY_UID + "=?", new String[]{uid});
        db.close(); //Closing database connection

        Log.d(TAG, "User's info was updated into sqlite: " + id);


    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + USER_PROFILE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("fullName", cursor.getString(1));
            user.put("eMail", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("dateOfBirth", cursor.getString(4));
            user.put("bloodType", cursor.getString(5));
            user.put("phoneNumber", cursor.getString(6));
            user.put("gender", cursor.getString(7));
            user.put("image", cursor.getString(8));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(USER_PROFILE, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}