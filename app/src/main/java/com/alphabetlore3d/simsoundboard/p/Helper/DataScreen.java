package com.alphabetlore3d.simsoundboard.p.Helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alphabetlore3d.simsoundboard.p.Models.AppScreen;

import java.util.ArrayList;

public class DataScreen extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ScreenDatabase.sqlite";
    private static final int DB_VERSION = 2;

    //banner
    private static final String COLUMN_INDEX_BANNER = "screen_index_banner";
    private static final String COLUMN_LINK_BANNER = "screen_link_banner";
    private static final String COLUMN_ALl_APPS_BANNER = "AllApps_banner";
    private static final String TABLE_NAME_BANNER = "MotyaScreen_banner";
    private String CREATE_TABLE_BANNER = "CREATE TABLE " + TABLE_NAME_BANNER + "("
            + COLUMN_INDEX_BANNER + " TEXT,"
            + COLUMN_LINK_BANNER + " TEXT,"
            + COLUMN_ALl_APPS_BANNER + " TEXT"
            + ")";


    //inters
    private static final String COLUMN_INDEX_INTERS = "screen_index_inters";
    private static final String COLUMN_LINK_INTERS = "screen_link_inters";
    private static final String COLUMN_ALl_APPS_INTERS = "AllApps_inters";
    private static final String TABLE_NAME_INTERS = "MotyaScreen_inters";
    private String CREATE_TABLE_INTERS = "CREATE TABLE " + TABLE_NAME_INTERS + "("
            + COLUMN_INDEX_INTERS + " TEXT,"
            + COLUMN_LINK_INTERS + " TEXT,"
            + COLUMN_ALl_APPS_INTERS + " TEXT"
            + ")";


    public DataScreen(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_BANNER);
        db.execSQL(CREATE_TABLE_INTERS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BANNER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INTERS);
        onCreate(db);
    }

    public void addBanner(int index, String screen) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_INDEX_BANNER, index);
        contentValues.put(COLUMN_LINK_BANNER, screen);
        contentValues.put(COLUMN_ALl_APPS_BANNER, index + screen);
        database.insert(TABLE_NAME_BANNER, null, contentValues);
        database.close();
    }

    public void addInters(int index, String screen) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_INDEX_INTERS, index);
        contentValues.put(COLUMN_LINK_INTERS, screen);
        contentValues.put(COLUMN_ALl_APPS_INTERS, index + screen);
        database.insert(TABLE_NAME_INTERS, null, contentValues);
        database.close();
    }

    public Integer delete(String names, String icon, String downloads, String packageName, String appPreview) {
        String[] allColumn = new String[]{names + icon + downloads + packageName + appPreview};
        return getWritableDatabase().delete(TABLE_NAME_BANNER, COLUMN_ALl_APPS_BANNER + " = ?", allColumn);
    }

    public int numberOfTable() {
        return (int) DatabaseUtils.queryNumEntries(getWritableDatabase(), TABLE_NAME_BANNER);
    }

    public ArrayList<AppScreen> getAllBannerScreen() {
        ArrayList<AppScreen> screenList = new ArrayList();
        ArrayList<AppScreen> appScreenNulls = new ArrayList();
        boolean isNull = true;
        String select = "SELECT  * FROM " + TABLE_NAME_BANNER;
        Cursor cursor = getReadableDatabase().rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {

                @SuppressLint("Range")
                int index = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_INDEX_BANNER)));
                @SuppressLint("Range")
                String screen = cursor.getString(cursor.getColumnIndex(COLUMN_LINK_BANNER));
                AppScreen appScreen = new AppScreen(index,screen);
                screenList.add(appScreen);
                isNull = false;
            } while (cursor.moveToNext());
        }
        return isNull ? appScreenNulls : screenList;
    }


    public ArrayList<AppScreen> getAllIntersScreen() {
        ArrayList<AppScreen> screenList = new ArrayList();
        ArrayList<AppScreen> appScreenNulls = new ArrayList();
        boolean isNull = true;
        String select = "SELECT  * FROM " + TABLE_NAME_INTERS;
        Cursor cursor = getReadableDatabase().rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {

                @SuppressLint("Range")
                int index = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_INDEX_INTERS)));
                @SuppressLint("Range")
                String screen = cursor.getString(cursor.getColumnIndex(COLUMN_LINK_INTERS));
                AppScreen appScreen = new AppScreen(index,screen);
                screenList.add(appScreen);
                isNull = false;
            } while (cursor.moveToNext());
        }
        return isNull ? appScreenNulls : screenList;
    }

}
