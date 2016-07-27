package ru.binarysimple.geekhab;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WorkDB {
    private DBHelper dbHelper;

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_URL = "url";



    public long insertRecord(Context context, String tableName, ContentValues cv) {
        dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(Main.LOG_TAG, "Insert one record into " + tableName);
//        long rowID = db.insert(tableName, null, cv); // insert new record to db and return its rowId
        long rowID = db.insertWithOnConflict(tableName, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        dbHelper.close();
        return rowID;
    }

    public void updateRecord(Context context, String tableName, ContentValues cv, String id) {
        dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(Main.LOG_TAG, "Update record in " + tableName);
        db.update(tableName, cv, "_id = ?", new String[]{id});
        dbHelper.close();
    }

    public Cursor getData(Context context, String query, String[] args) {
        dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.rawQuery(query, args);
    }

    public Cursor getUsersListCursor (Context context){
        return getData(context,"select * from "+Main.TABLE_NAME,null);
    }

    private ContentValues createCV (User user){
        ContentValues cv = new ContentValues();
        cv.put("login",user.getLogin());
        cv.put("id",user.getId());
        cv.put("score",user.getScore());
        cv.put("avatar_url",user.getAvatar_url());
        cv.put("url",user.getUrl());
        return cv;


    }

    public void insertUsersList (Context context, UserList userList){

        for (User user:userList.items
             ) {
            insertRecord(context, Main.TABLE_NAME, createCV(user));
        }
    }


}
