package com.example.prith.perfectbike;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBBike extends SQLiteOpenHelper
{
    private final static String DB_NAME = "BikeDB";
    private final static String TB_NAME_USER = "UserInfo";
    private final static String TB_NAME_PROFILE = "ProfileInfo";
    private final static String TB_NAME_BIKE = "BikeInfo";
    public DBBike(Context context) { super(context, DB_NAME, null, 2); }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try {
            String CREATE_TABLE = "CREATE TABLE " +
                    TB_NAME_USER + " (Id INTEGER AUTO_INCREMENT ," +
                    "Name VARCHAR(30)," +
                    "Email VARCHAR(50) PRIMARY KEY, " +
                    "Password VARCHAR(30)," +
                    "CPassword VARCHAR(20)," +
                    "Phone VARCHAR(30) , Address VARCHAR(10) , City VARCHAR(10))";
            Log.v("On create table : ", CREATE_TABLE);
            db.execSQL(CREATE_TABLE);
            String CREATE_TABLE_PROFILE = "CREATE TABLE " +
                    TB_NAME_PROFILE + " (Id INTEGER AUTO_INCREMENT ," +
                    "PImage BLOB, FName VARCHAR(30)," +
                    "LName VARCHAR(30))";
            Log.v("On create table : ", CREATE_TABLE_PROFILE);
            db.execSQL(CREATE_TABLE_PROFILE);
            String CREATE_TABLE_BIKE = "CREATE TABLE " +
                    TB_NAME_BIKE + "(Id INTEGER AUTO_INCREMENT , " +
                    "Brand VARCHAR(30), Year VARCHAR(30)," +
                    "BImage BLOB)";
            Log.v("On create table : ", CREATE_TABLE_BIKE);
            db.execSQL(CREATE_TABLE_BIKE);
            }
            catch (Exception e)
            {
                Log.e("DBCreater", e.getMessage());
            }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TB_NAME_USER);

            db.execSQL("DROP TABLE IF EXISTS " + TB_NAME_PROFILE);

            db.execSQL("DROP TABLE IF EXISTS " + TB_NAME_BIKE);
            onCreate(db);
        }catch (Exception e){ Log.e("DBBike",e.getMessage()); }
    }
}
