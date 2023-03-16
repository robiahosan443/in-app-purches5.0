package com.alphabetlore3d.simsoundboard.p.Helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alphabetlore3d.simsoundboard.p.Models.AppModels;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.sqlite";
    private static final int DB_VERSION = 3;


    //banner
    private static final String COLUMN_NAME_BANNER = "appNameBanner";
    private static final String COLUMN_ICON_BANNER = "appIconBanner";
    private static final String COLUMN_DOWNLOADS_BANNER = "appDownloadBanner";
    private static final String COLUMN_PACKAGE_BANNER = "appPackageBanner";
    private static final String COLUMN_PREVIEW_BANNER = "appPreviewBanner";
    private static final String COLUMN_ALl_APPS_BANNER = "AllAppsBanner";


    private static final String TABLE_NAME_BANNER = "MotyaAdApps";
    private String CREATE_TABLE_BANNER= "CREATE TABLE " + TABLE_NAME_BANNER + "("
            + COLUMN_NAME_BANNER + " TEXT,"
            + COLUMN_ICON_BANNER + " TEXT,"
            + COLUMN_DOWNLOADS_BANNER + " TEXT,"
            + COLUMN_PACKAGE_BANNER + " TEXT,"
            + COLUMN_PREVIEW_BANNER + " TEXT,"
            + COLUMN_ALl_APPS_BANNER + " TEXT"
            + ")";


    //inters
    private static final String COLUMN_NAME_INTERS = "appNameInters";
    private static final String COLUMN_ICON_INTERS = "appIconInters";
    private static final String COLUMN_DOWNLOADS_INTERS = "appDownloadInters";
    private static final String COLUMN_PACKAGE_INTERS = "appPackageInters";
    private static final String COLUMN_PREVIEW_INTERS= "appPreviewInters";
    private static final String COLUMN_ALl_APPS_INTERS= "AllAppsInters";


    private static final String TABLE_NAME_INTERS = "MotyaAdAppsInters";
    private String CREATE_TABLE_INTERS = "CREATE TABLE " + TABLE_NAME_INTERS + "("
            + COLUMN_NAME_INTERS + " TEXT,"
            + COLUMN_ICON_INTERS + " TEXT,"
            + COLUMN_DOWNLOADS_INTERS+ " TEXT,"
            + COLUMN_PACKAGE_INTERS + " TEXT,"
            + COLUMN_PREVIEW_INTERS + " TEXT,"
            + COLUMN_ALl_APPS_INTERS + " TEXT"
            + ")";


    public DatabaseHelper(Context context) {
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

    public void addBanner(String names, String icon, String downloads, String packageName, String appPreview) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_BANNER, names);
        contentValues.put(COLUMN_ICON_BANNER, icon);
        contentValues.put(COLUMN_DOWNLOADS_BANNER, downloads);
        contentValues.put(COLUMN_PACKAGE_BANNER, packageName);
        contentValues.put(COLUMN_PREVIEW_BANNER, appPreview);
        contentValues.put(COLUMN_ALl_APPS_BANNER, names + icon + downloads + packageName+appPreview);
        database.insert(TABLE_NAME_BANNER, null, contentValues);
        database.close();
    }

    public void addInters(String names, String icon, String downloads, String packageName, String appPreview) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_INTERS, names);
        contentValues.put(COLUMN_ICON_INTERS, icon);
        contentValues.put(COLUMN_DOWNLOADS_INTERS, downloads);
        contentValues.put(COLUMN_PACKAGE_INTERS, packageName);
        contentValues.put(COLUMN_PREVIEW_INTERS, appPreview);
        contentValues.put(COLUMN_ALl_APPS_INTERS, names + icon + downloads + packageName+appPreview);
        database.insert(TABLE_NAME_INTERS, null, contentValues);
        database.close();
    }

    public Integer delete(String names, String icon, String downloads, String packageName,String appPreview) {
        String[] allColumn = new String[]{names + icon + downloads + packageName+appPreview};
        return getWritableDatabase().delete(TABLE_NAME_BANNER, COLUMN_ALl_APPS_BANNER + " = ?",allColumn);
    }

    public int numberOfTable() {
        return (int) DatabaseUtils.queryNumEntries(getWritableDatabase(), TABLE_NAME_BANNER);
    }

    public ArrayList<AppModels> getAllBannerApps() {
        ArrayList<AppModels> appModels = new ArrayList();
        ArrayList<AppModels> appModelsNulls = new ArrayList();
        boolean isNull = true;
        String select = "SELECT  * FROM " + TABLE_NAME_BANNER;
        Cursor cursor = getReadableDatabase().rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {

                @SuppressLint("Range")
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_BANNER));
                @SuppressLint("Range")
                String icons = cursor.getString(cursor.getColumnIndex(COLUMN_ICON_BANNER));
                @SuppressLint("Range")
                String downloads = cursor.getString(cursor.getColumnIndex(COLUMN_DOWNLOADS_BANNER));
                @SuppressLint("Range")
                String packageName = cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGE_BANNER));

                @SuppressLint("Range")
                String appPreview = cursor.getString(cursor.getColumnIndex(COLUMN_PREVIEW_BANNER));

                AppModels apps = new AppModels(name, icons, downloads, packageName,appPreview);
                appModels.add(apps);
                isNull = false;
            } while (cursor.moveToNext());
        }
        return isNull ? appModelsNulls : appModels;
    }

    public ArrayList<AppModels> getAllIntersApps() {
        ArrayList<AppModels> appModels = new ArrayList();
        ArrayList<AppModels> appModelsNulls = new ArrayList();
        boolean isNull = true;
        String select = "SELECT  * FROM " + TABLE_NAME_INTERS;
        Cursor cursor = getReadableDatabase().rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {

                @SuppressLint("Range")
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_INTERS));
                @SuppressLint("Range")
                String icons = cursor.getString(cursor.getColumnIndex(COLUMN_ICON_INTERS));
                @SuppressLint("Range")
                String downloads = cursor.getString(cursor.getColumnIndex(COLUMN_DOWNLOADS_INTERS));
                @SuppressLint("Range")
                String packageName = cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGE_INTERS));

                @SuppressLint("Range")
                String appPreview = cursor.getString(cursor.getColumnIndex(COLUMN_PREVIEW_INTERS));

                AppModels apps = new AppModels(name, icons, downloads, packageName,appPreview);
                appModels.add(apps);
                isNull = false;
            } while (cursor.moveToNext());
        }
        return isNull ? appModelsNulls : appModels;
    }


}
