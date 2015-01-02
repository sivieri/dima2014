package it.polimi.dima2014.data;

import it.polimi.dima2014.MainActivity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "dimanotes";
    public static final int TABLE_VERSION = 2;
    public static final String ID = "_id";
    public static final String KEY = "title";
    public static final String VALUE = "content";
    public static final String TIMESTAMP = "ts";
    public static final String LAT = "lat";
    public static final String LNG = "lng";

    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY + " TEXT, " + VALUE + " TEXT," + TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, " + LAT + " double DEFAULT 0, " + LNG + " double DEFAULT 0);";
    private static final String UPGRADE_1_TO_2_P1 = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + LAT + " double DEFAULT 0;";
    private static final String UPGRADE_1_TO_2_P2 = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + LNG + " double DEFAULT 0;";
    private static final String WELCOME_MSG = "INSERT INTO " + TABLE_NAME + "(" + KEY + ", " + VALUE + ") VALUES ('Welcome', 'Welcome to the todo/notes app for Design and Implementation of Mobile Applications (DIMA) course (A.A. 2014/2015)!')";

    public NotesOpenHelper(Context context) {
        super(context, TABLE_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        Log.d(MainActivity.TAG, "Creating the db, version " + TABLE_VERSION + ": " + TABLE_CREATE);
        arg0.execSQL(TABLE_CREATE);
        arg0.execSQL(WELCOME_MSG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        Log.d(MainActivity.TAG, "Upgrade from " + arg1 + " to " + arg2);
        if (arg1 == 1 && arg2 == 2) {
            arg0.execSQL(UPGRADE_1_TO_2_P1);
            arg0.execSQL(UPGRADE_1_TO_2_P2);
        }
        else {
            arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(arg0);
        }
    }

}
