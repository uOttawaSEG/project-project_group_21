package com.example.seg2105_projectui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.seg2105_projectui.Member;
import com.example.seg2105_projectui.Tutor;
import com.example.seg2105_projectui.Student;
import com.example.seg2105_projectui.User;
import com.example.seg2105_projectui.Sessions;



public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "UserData.db";
    private static final int DATABASE_VERSION = 3;

    // Table Name
    public static final String TABLE_USERS = "users";
    public static final String TABLE_SESSIONS = "sessions";

    // Users Table Columns
    public static final String COLUMN_ID = "id";//i am unsure as to what id
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password"; // In a real app, HASH THIS!
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_PHONE = "phone_number";
    public static final String COLUMN_ROLE = "role"; // "Student" or "Tutor"
    public static final String COLUMN_ACCOUNT_STATUS = "account_status";


    //sessions
    public static final String COLUMN_SESSION_ID = "session_id";//i am unsure as to what id
    public static final String COLUMN_TUTOR_USERNAME = "tutor_username";
    public static final String COLUMN_DATE = "date";//idk how to make dates work in this
    public static final String COLUMN_START_TIME = "start_time";//idk how to make time work in this
    public static final String COLUMN_PENDING_STUDENTS = "pending_students";//do lists work in this????
    public static final String COLUMN_APPROVED_STUDENTS = "approved_students";
    public static final String COLUMN_REJECTED_STUDENTS = "rejected_students";

    public static final String COLUMN_CANCELLED_STUDENTS = "cancelled_students";


    // Tutor-specific columns (can be null for students)
    public static final String COLUMN_DEGREE = "degree";
    public static final String COLUMN_COURSES = "courses"; // Stored as a comma-separated string

    //Student specifc
    public static final String COLUMN_PROGRAM = "program";

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
                + COLUMN_PROGRAM + " TEXT,"
                + COLUMN_ACCOUNT_STATUS + " INTEGER DEFAULT 0" + ")";
        db.execSQL(CREATE_USERS_TABLE);


        String CREATE_SESSIONS_TABLE = "CREATE TABLE " + TABLE_SESSIONS + "("
                + COLUMN_SESSION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TUTOR_USERNAME + " TEXT NOT NULL, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_START_TIME + " TEXT, "
                + COLUMN_PENDING_STUDENTS + " TEXT, "
                + COLUMN_APPROVED_STUDENTS + " TEXT, "
                + COLUMN_REJECTED_STUDENTS + " TEXT, "
                + COLUMN_CANCELLED_STUDENTS + " TEXT" + ")";
        db.execSQL(CREATE_SESSIONS_TABLE);


        //admin
        ContentValues admin = new ContentValues();
        admin.put(COLUMN_USERNAME, "admin");
        admin.put(COLUMN_PASSWORD, "adminpassword");
        admin.put(COLUMN_ROLE, "Admin");

        db.insert(TABLE_USERS, null, admin);

        //test student
        ContentValues alsotest1 = new ContentValues();
        alsotest1.put(COLUMN_USERNAME, "tester");
        alsotest1.put(COLUMN_PHONE, "1234");
        alsotest1.put(COLUMN_ROLE, "Student");

        db.insert(TABLE_USERS, null, alsotest1);

        ContentValues test1 = new ContentValues();
        test1.put(COLUMN_DATE, "11/9/2025");
        test1.put(COLUMN_START_TIME, "10:00");
        test1.put(COLUMN_PENDING_STUDENTS, "tester");

        db.insert(TABLE_SESSIONS, null, test1);

    }

    // This is called when the database needs to be upgraded.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);

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
        values.put(COLUMN_PROGRAM, student.getProgram());
        values.put(COLUMN_ACCOUNT_STATUS, 0); //0 by default


        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        return result != -1;
    }
    // ... (at the end of your DatabaseHelper.java file, inside the class)

    // Method to check if a user exists with the given credentials
    public void approveUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACCOUNT_STATUS, 1);

        db.update(
                TABLE_USERS,
                values,
                COLUMN_USERNAME + " = ?", //parameter placeholder?
                new String[]{username}
        );
        db.close();
    }

    public Member checkUser(String username, String password) { // Changed return type to Member for clarity
        SQLiteDatabase db = this.getReadableDatabase();
        Member member = null; // This will be our return value

        String[] columns = {
                COLUMN_ID,
                COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME,
                COLUMN_PHONE,
                COLUMN_ROLE
        };

        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE));

            if (role.equals("Admin")) {
                member = new Member(username, password, "", "Admin", "", "System Admin");
            } else {
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));

                // Assign the created member object to the 'member' variable
                member = new Member(username, password, lastName, firstName, phoneNumber, role);
            }
        }

        // It's crucial to close the cursor and database
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        // Return the member object, which is either null or the user's data
        return member;
    }

    // In DatabaseHelper.java
    public boolean isUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USERNAME},
                COLUMN_USERNAME + "=?", new String[]{username},
                null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }


    public void rejectUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACCOUNT_STATUS, 2);

        db.update(
                TABLE_USERS,
                values,
                COLUMN_USERNAME + " = ?", //parameter placeholder?
                new String[]{username}
        );
        db.close();
    }

    public Cursor getUsersByStatus(int status) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS,
                null,
                COLUMN_ACCOUNT_STATUS + " = ?",
                new String[]{String.valueOf(status)},
                null,
                null,
                COLUMN_LAST_NAME + " ASC");

    }


    // In DatabaseHelper.java

    public List<Member> getUsersByStatusList(int status) {
        List<Member> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_ACCOUNT_STATUS + " = ?";
        String[] selectionArgs = {String.valueOf(status)};

        // Make sure you are querying ALL necessary columns, including role-specific ones
        // like COLUMN_PROGRAM, COLUMN_DEGREE, COLUMN_COURSES
        Cursor cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                // --- THIS IS THE CRITICAL FIX ---
                String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME));
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE));

                Member member; // Declare a variable to hold the new object

                if ("Student".equals(role)) {
                    // If the role is "Student", get student-specific data...
                    String program = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROGRAM));
                    // ...and create a NEW STUDENT OBJECT.
                    member = new Student(username, password, lastName, firstName, phone, role, program);
                    userList.add(member);

                } else if ("Tutor".equals(role)) {
                    // If the role is "Tutor", get tutor-specific data...
                    String degree = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEGREE));
                    String coursesStr = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSES));
                    String[] courses = (coursesStr != null) ? coursesStr.split(",") : new String[0];
                    // ...and create a NEW TUTOR OBJECT.
                    member = new Tutor(username, password, lastName, firstName, phone, role, degree, courses);
                    userList.add(member);

                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userList;
    }

    public int checkUserStatus(String username, String password) { // Changed return type to Member for clarity
        SQLiteDatabase db = this.getReadableDatabase();
        int status = 999; // This will be our return value

        String[] columns = {
                COLUMN_ID,
                COLUMN_FIRST_NAME,
                COLUMN_LAST_NAME,
                COLUMN_PHONE,
                COLUMN_ROLE,
                COLUMN_ACCOUNT_STATUS
        };

        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_USERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE));

            if (role.equals("Admin")) {
                status = 1;
            } else {
                status = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACCOUNT_STATUS)));
            }
        }

        cursor.close();
        db.close();
        return status;
    }


    //new sessions methods

    public List<String> stringToList(String str) {
        if (str == null || str.isBlank()) {
            return new ArrayList<>();
        }//this makes it so i dont need to ever worry if str doesn't exist
        return new ArrayList<>(Arrays.asList(str.split(",")));
    }

    //

    public String listToString(List<String> list) {
        return String.join(",", list);
    }


    public void createSession(String tutorUsername, String data, String startTime) {//stores session in database given parameters
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TUTOR_USERNAME, tutorUsername);
        values.put(COLUMN_DATE, data);
        values.put(COLUMN_START_TIME, startTime);

        values.put(COLUMN_PENDING_STUDENTS, "");
        values.put(COLUMN_APPROVED_STUDENTS, "");
        values.put(COLUMN_REJECTED_STUDENTS, "");

        db.insert(TABLE_SESSIONS, null, values);
        db.close();

    }

    public void deleteSession(Sessions session) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SESSIONS,
                // Find the session that matches all three columns
                COLUMN_TUTOR_USERNAME + " = ? AND " + COLUMN_DATE + " = ? AND " + COLUMN_START_TIME + " = ?",
                // The values to match against
                new String[]{session.tutorUsername, session.date, session.startTime});
        db.close();
    }

    public void studentPending(String tutorUsername, String date, String startTime, String studentUsername) {//adds student to a sessions waitlist
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SESSIONS, new String[]{COLUMN_PENDING_STUDENTS},
                COLUMN_TUTOR_USERNAME + "=? AND " + COLUMN_DATE + "=? AND " + COLUMN_START_TIME + "=?",
                new String[]{tutorUsername, date, startTime}, null, null, null);

        if (cursor.moveToFirst()) {
            String pending = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PENDING_STUDENTS));
            List<String> pendingList = stringToList(pending);


            if (!pendingList.contains(studentUsername)) {//best to not have duplicates //because that would make removal annoying
                pendingList.add(studentUsername);
            }


            ContentValues values = new ContentValues();
            values.put(COLUMN_PENDING_STUDENTS, listToString(pendingList));
            db.update(TABLE_SESSIONS, values, COLUMN_TUTOR_USERNAME + "=? AND " + COLUMN_DATE + "=? AND " + COLUMN_START_TIME + "=?",
                    new String[]{tutorUsername, date, startTime});

        }

        cursor.close();
        db.close();

    }

    public void approveStudent(String tutorUsername, String date, String startTime, String studentUsername) {//moves student from pending to approved list
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SESSIONS, new String[]{COLUMN_PENDING_STUDENTS, COLUMN_APPROVED_STUDENTS},
                COLUMN_TUTOR_USERNAME + "=? AND " + COLUMN_DATE + "=? AND " + COLUMN_START_TIME + "=?",
                new String[]{tutorUsername, date, startTime}, null, null, null);

        if (cursor.moveToFirst()) {
            String pending = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PENDING_STUDENTS));
            String approved = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APPROVED_STUDENTS));
            List<String> pendingList = stringToList(pending);
            List<String> approvedList = stringToList(approved);

            pendingList.remove(studentUsername);

            if (!approvedList.contains(studentUsername)) {//best to not have duplicates //because that would make removal annoying
                approvedList.add(studentUsername);
            }

            ContentValues values = new ContentValues();
            values.put(COLUMN_PENDING_STUDENTS, listToString(pendingList));//update pending with new string of students (which does not include the studentUsername)
            values.put(COLUMN_APPROVED_STUDENTS, listToString(approvedList));
            db.update(TABLE_SESSIONS, values, COLUMN_TUTOR_USERNAME + "=? AND " + COLUMN_DATE + "=? AND " + COLUMN_START_TIME + "=?",
                    new String[]{tutorUsername, date, startTime});

        }

        cursor.close();
        db.close();
    }

    public void rejectStudent(String tutorUsername, String date, String startTime, String studentUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SESSIONS, new String[]{COLUMN_PENDING_STUDENTS, COLUMN_REJECTED_STUDENTS},
                COLUMN_TUTOR_USERNAME + "=? AND " + COLUMN_DATE + "=? AND " + COLUMN_START_TIME + "=?",
                new String[]{tutorUsername, date, startTime}, null, null, null);

        if (cursor.moveToFirst()) {
            String pending = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PENDING_STUDENTS));
            String rejected = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REJECTED_STUDENTS));
            List<String> pendingList = stringToList(pending);
            List<String> rejectedList = stringToList(rejected);

            pendingList.remove(studentUsername);

            if (!rejectedList.contains(studentUsername)) {//best to not have duplicates //because that would make removal annoying
                rejectedList.add(studentUsername);
            }

            ContentValues values = new ContentValues();
            values.put(COLUMN_PENDING_STUDENTS, listToString(pendingList));//update pending with new string of students (which does not include the studentUsername)
            values.put(COLUMN_REJECTED_STUDENTS, listToString(rejectedList));
            db.update(TABLE_SESSIONS, values, COLUMN_TUTOR_USERNAME + "=? AND " + COLUMN_DATE + "=? AND " + COLUMN_START_TIME + "=?",
                    new String[]{tutorUsername, date, startTime});

        }

        cursor.close();
        db.close();
    }


    public List<Sessions> getTutorSessions(String tutorUsername) {//returns all sessions a specific tutor has
        SQLiteDatabase db = this.getReadableDatabase();

        List<Sessions> sessions = new ArrayList<>();


        Cursor cursor = db.query(TABLE_SESSIONS, null,
                COLUMN_TUTOR_USERNAME + "=?", new String[]{tutorUsername}, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_TIME));

                sessions.add(new Sessions(tutorUsername, date, startTime));

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return sessions;
    }

    public List<String> getApprovedStudents(String tutorUsername, String date, String startTime) {//returns all approved students of a session
        SQLiteDatabase db = this.getReadableDatabase();

        List<String> approved = new ArrayList<>();

        Cursor cursor = db.query(TABLE_SESSIONS, new String[]{COLUMN_APPROVED_STUDENTS},
                COLUMN_TUTOR_USERNAME + "=? AND " + COLUMN_DATE + "=? AND " + COLUMN_START_TIME + "=?",
                new String[]{tutorUsername, date, startTime}, null, null, null);

        if (cursor.moveToFirst()) {
            approved = stringToList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APPROVED_STUDENTS)));
        }

        cursor.close();
        db.close();
        return approved;

    }

    public List<String> getRejectedStudents(String tutorUsername, String date, String startTime) {
        SQLiteDatabase db = this.getReadableDatabase();

        List<String> rejected = new ArrayList<>();

        Cursor cursor = db.query(TABLE_SESSIONS, new String[]{COLUMN_REJECTED_STUDENTS},
                COLUMN_TUTOR_USERNAME + "=? AND " + COLUMN_DATE + "=? AND " + COLUMN_START_TIME + "=?",
                new String[]{tutorUsername, date, startTime}, null, null, null);

        if (cursor.moveToFirst()) {
            rejected = stringToList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REJECTED_STUDENTS)));
        }

        cursor.close();
        db.close();
        return rejected;

    }

    public List<String> getPendingStudents(String tutorUsername, String date, String startTime) {
        SQLiteDatabase db = this.getReadableDatabase();

        List<String> pending = new ArrayList<>();

        Cursor cursor = db.query(TABLE_SESSIONS, new String[]{COLUMN_PENDING_STUDENTS},
                COLUMN_TUTOR_USERNAME + "=? AND " + COLUMN_DATE + "=? AND " + COLUMN_START_TIME + "=?",
                new String[]{tutorUsername, date, startTime}, null, null, null);

        if (cursor.moveToFirst()) {
            pending = stringToList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PENDING_STUDENTS)));
        }

        cursor.close();
        db.close();
        return pending;

    }

    public List<Sessions> sessionsNoStudents() {//returns ALL sessions that have no approved students as of yet
        List<Sessions> sessions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SESSIONS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                List<String> approved = stringToList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APPROVED_STUDENTS)));

                if (approved.isEmpty()) {
                    String tutorUsername = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_USERNAME));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                    String startTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_TIME));

                    sessions.add(new Sessions(tutorUsername, date, startTime));
                }
            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return sessions;
    }

    public List<Sessions> allSessions() {//returns ALL sessions
        List<Sessions> sessions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SESSIONS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                String tutorUsername = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_USERNAME));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_TIME));

                sessions.add(new Sessions(tutorUsername, date, startTime));

            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return sessions;
    }


    public boolean sessionOverlap(String tutorUsername, String date, String startTime) {//checks if the given parameters already exist
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SESSIONS, new String[]{COLUMN_SESSION_ID},
                COLUMN_TUTOR_USERNAME + "=? AND " + COLUMN_DATE + "=? AND " + COLUMN_START_TIME + "=?",
                new String[]{tutorUsername, date, startTime}, null, null, null);

        boolean overlap = cursor.moveToFirst();//this is false if this is empty, so if false session doesn't exist

        cursor.close();
        db.close();
        return overlap;
    }

    public Student getStudent(String studentUsername) {//just returns a student object with all a students info from the given username
        // only works if student exists so pls check that first

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COLUMN_USERNAME + "=?",
                new String[]{studentUsername}, null, null, null);

        Student student = null;

        if (cursor.moveToFirst()) {
            String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));
            String program = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROGRAM));
            String role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE));//must be Student or db explodes

            student = new Student(studentUsername, password, lastName, firstName, phone, role, program);
        }
        cursor.close();
        db.close();
        return student;

    }

    //for cancellations (pretty much a reskin of approve and reject, moves a student from approved to cancelled)
    public void cancelStudent(String tutorUsername, String date, String startTime, String studentUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_SESSIONS, new String[]{COLUMN_APPROVED_STUDENTS, COLUMN_CANCELLED_STUDENTS},
                COLUMN_TUTOR_USERNAME + "=? AND " + COLUMN_DATE + "=? AND " + COLUMN_START_TIME + "=?",
                new String[]{tutorUsername, date, startTime}, null, null, null);

        if (cursor.moveToFirst()) {
            String approved = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APPROVED_STUDENTS));
            String cancelled = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CANCELLED_STUDENTS));
            List<String> approvedList = stringToList(approved);
            List<String> cancelledList = stringToList(cancelled);

            approvedList.remove(studentUsername);

            if (!cancelledList.contains(studentUsername)) {//best to not have duplicates //because that would make removal annoying
                cancelledList.add(studentUsername);
            }

            ContentValues values = new ContentValues();
            values.put(COLUMN_APPROVED_STUDENTS, listToString(approvedList));//update approved with new string of students (which does not include the studentUsername)
            values.put(COLUMN_CANCELLED_STUDENTS, listToString(cancelledList));
            db.update(TABLE_SESSIONS, values, COLUMN_TUTOR_USERNAME + "=? AND " + COLUMN_DATE + "=? AND " + COLUMN_START_TIME + "=?",
                    new String[]{tutorUsername, date, startTime});

        }

        cursor.close();
        db.close();
    }

    public List<Sessions> getTutorDay(String tutorUsername, String date) {//retunrs all of a tutors session on that day
        SQLiteDatabase db = this.getReadableDatabase();
        List<Sessions> sessions = new ArrayList<>();

        Cursor cursor = db.query(TABLE_SESSIONS, null,
                COLUMN_TUTOR_USERNAME + "=? AND " + COLUMN_DATE + "=?",
                new String[]{tutorUsername, date}, null, null, COLUMN_START_TIME + " ASC");//should sort sessions by start time

        if (cursor.moveToFirst()) {
            do {
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_TIME));
                sessions.add(new Sessions(tutorUsername, date, startTime));

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return sessions;

    }

    public List<Sessions> getDay(String date) {//retunrs all sessions on given day
        SQLiteDatabase db = this.getReadableDatabase();
        List<Sessions> sessions = new ArrayList<>();

        Cursor cursor = db.query(TABLE_SESSIONS, null,
                COLUMN_DATE + "=?",
                new String[]{date}, null, null,
                COLUMN_TUTOR_USERNAME + " ASC, " + COLUMN_START_TIME + " ASC");//should sort sessions by tutorname and starttime??

        if (cursor.moveToFirst()) {
            do {
                String tutorUsername = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TUTOR_USERNAME));
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_TIME));
                sessions.add(new Sessions(tutorUsername, date, startTime));

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        return sessions;

    }



}


