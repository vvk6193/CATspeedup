package com.cat.vvk.catspeedup.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cat.vvk.catspeedup.modal.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vivek-pc on 8/8/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    private static final String DATABASE_NAME = "database_name";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_RECORDS = "records";

    private static final String RECORDS_COLUMN_ID = "rc_id";
    private static final String RECORDS_COLUMN_DATE = "date";
    private static final String RECORDS_COLUMN_TIME_TAKEN = "duration";
    private static final String RECORDS_COLUMN_NUMBR_OF_DIGIT = "digit";
    private static final String RECORDS_COLUMN_IS_CORRECT = "is_correct";
    private static final String RECORDS_COLUMN_TYPE = "record_type";

    private static final String CREATE_RECORDS_TABLE  = "CREATE TABLE "
            + TABLE_RECORDS + "(" + RECORDS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + RECORDS_COLUMN_NUMBR_OF_DIGIT
            + " INTEGER," + RECORDS_COLUMN_DATE + " LONG," + RECORDS_COLUMN_TIME_TAKEN
            + " FLOAT," + RECORDS_COLUMN_IS_CORRECT + " INTEGER," + RECORDS_COLUMN_TYPE + " TEXT" + ")";

    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public long insertRecord(Record record) {
//        DatabaseHelper dh = DatabaseHelper.getInstance(context);
//        DatabaseManager dm = DatabaseManager.getInstance(this);
        SQLiteDatabase db = DatabaseManager.getInstance(this).openDatabase();
        ContentValues values = new ContentValues();
        values.put(RECORDS_COLUMN_NUMBR_OF_DIGIT, record.getNumDigit());
        values.put(RECORDS_COLUMN_DATE, record.getCreatedDate());
        values.put(RECORDS_COLUMN_TIME_TAKEN, record.getTimeTaken());
        values.put(RECORDS_COLUMN_IS_CORRECT,record.getIsCorrect());
        values.put(RECORDS_COLUMN_TYPE,record.getRecordType());
        // insert row
        long todo_id = db.insert(TABLE_RECORDS, null, values);
        DatabaseManager.getInstance(this).closeDatabase();
        return todo_id;
    }

    public List<Record> getAllRecords() {
        SQLiteDatabase db = DatabaseManager.getInstance(this).openDatabase();
        List<Record> records = new ArrayList<Record>();
        String selectQuery = "SELECT  * FROM " + TABLE_RECORDS;

        Log.e("vvk", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(c.getInt((c.getColumnIndex(RECORDS_COLUMN_ID))));
                record.setCreatedDate((c.getLong(c.getColumnIndex(RECORDS_COLUMN_DATE))));
                record.setTimeTaken(c.getFloat(c.getColumnIndex(RECORDS_COLUMN_TIME_TAKEN)));
                record.setNumDigit(c.getInt(c.getColumnIndex(RECORDS_COLUMN_NUMBR_OF_DIGIT)));
                record.setIsCorrect(c.getInt(c.getColumnIndex(RECORDS_COLUMN_IS_CORRECT)));
                record.setRecordType(c.getString(c.getColumnIndex(RECORDS_COLUMN_TYPE)));
                // adding to todo list
                records.add(record);
            } while (c.moveToNext());
        }
        DatabaseManager.getInstance(this).closeDatabase();
        return records;
    }

    public List<Record> getRecords(long startDate,long endDate,int numDigit,String recordType) {
        SQLiteDatabase db = DatabaseManager.getInstance(this).openDatabase();
        List<Record> records = new ArrayList<Record>();
        String selectQuery = "SELECT  * " +
                                "FROM " + TABLE_RECORDS +
                                " where " + RECORDS_COLUMN_DATE +" BETWEEN " + startDate +
                                " and " + endDate + " and " + RECORDS_COLUMN_TYPE +" = " + "'"+recordType+"'";

        if(numDigit != 0) {
            selectQuery += " and " + RECORDS_COLUMN_NUMBR_OF_DIGIT +
                    " = " + numDigit;
        }

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(c.getInt((c.getColumnIndex(RECORDS_COLUMN_ID))));
                record.setCreatedDate((c.getLong(c.getColumnIndex(RECORDS_COLUMN_DATE))));
                record.setTimeTaken(c.getFloat(c.getColumnIndex(RECORDS_COLUMN_TIME_TAKEN)));
                record.setNumDigit(c.getInt(c.getColumnIndex(RECORDS_COLUMN_NUMBR_OF_DIGIT)));
                record.setIsCorrect(c.getInt(c.getColumnIndex(RECORDS_COLUMN_IS_CORRECT)));
                // adding to todo list
                records.add(record);
            } while (c.moveToNext());
        }
        DatabaseManager.getInstance(this).closeDatabase();
        return records;
    }

    public int getNumberOfCorrectRecords(long startDate,long endDate,int numDigit,String recordType,int IsCorrect) {
        SQLiteDatabase db = DatabaseManager.getInstance(this).openDatabase();
        int count = 0;
        String selectQuery = "SELECT  count('*') AS count "+
                "FROM " + TABLE_RECORDS +
                " where " + RECORDS_COLUMN_DATE +" BETWEEN " + startDate +
                " and " + endDate + " and "+ RECORDS_COLUMN_IS_CORRECT + " = " + IsCorrect
                + " group by " + RECORDS_COLUMN_TYPE +" having " + RECORDS_COLUMN_TYPE + " = '"+recordType+"'";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                count = (c.getInt((c.getColumnIndex("count"))));
            } while (c.moveToNext());
        }
        DatabaseManager.getInstance(this).closeDatabase();
        return count;
    }





    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECORDS_TABLE);
//        db.execSQL(CREATE_TABLE_TAG);
//        db.execSQL(CREATE_TABLE_TODO_TAG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
    }

}
