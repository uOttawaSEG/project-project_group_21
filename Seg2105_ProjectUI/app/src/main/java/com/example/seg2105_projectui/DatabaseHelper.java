package com.example.seg2105_projectui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.seg2105_projectui.Member;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "UserData.db";
    private static final int DATABASE_VERSION = 2;

    // Table Name
    public static final String TABLE_USERS = "users";

    // Users Table Columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password"; // In a real app, HASH THIS!
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_PHONE = "phone_number";
    public static final String COLUMN_ROLE = "role"; // "Student" or "Tutor"
    public static final String COLUMN_ACCOUNT_STATUS = "account_status";

    // Tutor-specific columns (can be null for students)
    public static final String COLUMN_DEGREE = "degree";
    public static final String COLUMN_COURSES = "courses"; // Stored as a comma-separated string

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // This is called the first time a database is accessed.
    // It should create all necessary tables.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT NOT NULL,"
                + COLUMN_PASSWORD + " TEXT NOT NULL,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_ROLE + " TEXT NOT NULL,"
                + COLUMN_DEGREE + " TEXT,"
                + COLUMN_COURSES + " TEXT,"
                + COLUMN_ACCOUNT_STATUS + " INTEGER DEFAULT 0" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        //admin
        ContentValues admin = new ContentValues();
        admin.put(COLUMN_USERNAME, "admin");
        admin.put(COLUMN_PASSWORD, "adminpassword");
        admin.put(COLUMN_ROLE, "Admin");

        db.insert(TABLE_USERS, null, admin);
    }

    // This is called when the database needs to be upgraded.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }

    // Method to add a new Tutor to the database
    public boolean addTutor(Tutor tutor) {
        // Gets the database in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, tutor.getUserName());
        values.put(COLUMN_PASSWORD, tutor.getUserPassword());
        values.put(COLUMN_FIRST_NAME, tutor.getFirstName());
        values.put(COLUMN_LAST_NAME, tutor.getLastName());
        values.put(COLUMN_PHONE, tutor.getPhoneNumber());
        values.put(COLUMN_ROLE, "Tutor"); // Set the role
        values.put(COLUMN_DEGREE, tutor.getHighestDegree());
        values.put(COLUMN_ACCOUNT_STATUS, 0); //0 by default

        // Convert the string array of courses to a single comma-separated string
        String coursesStr = String.join(",", tutor.getCoursesOffered());
        values.put(COLUMN_COURSES, coursesStr);

        // Insert the new row, returning the primary key value of the new row
        // A value of -1 indicates an error.
        long result = db.insert(TABLE_USERS, null, values);
        db.close(); // Close the database connection

        return result != -1; // Return true if insertion was successful
    }

    // Method to add a new Student to the database (Assuming you have a Student class)
    public boolean addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, student.getUserName());
        values.put(COLUMN_PASSWORD, student.getUserPassword());
        values.put(COLUMN_FIRST_NAME, student.getFirstName());
        values.put(COLUMN_LAST_NAME, student.getLastName());
        values.put(COLUMN_PHONE, student.getPhoneNumber());
        values.put(COLUMN_ROLE, "Student");
        values.put(COLUMN_ACCOUNT_STATUS, 0); //0 by default


        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result != -1;
    }
    // ... (at the end of your DatabaseHelper.java file, inside the class)

    // Method to check if a user exists with the given credentials
    public User checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();    User user = null;
        Member member = null;
        String[] columns = {
                COLUMN_ID,
                COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME,
                COLUMN_PHONE,
                COLUMN_ROLE

        };

        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = { username, password };
        System.out.println(selection);

        Cursor cursor = db.query(TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE));

            if (role.equals("Admin")){
                member = new Member(username, password, "", "Admin", "", "System Admin");
            }
            else{
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));

                member = new Member(username, password, lastName, firstName, phoneNumber, role);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return member;
    }

}


