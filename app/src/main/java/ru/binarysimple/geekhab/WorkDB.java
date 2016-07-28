package ru.binarysimple.geekhab;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WorkDB {
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_URL = "url";
    private DBHelper dbHelper;

    public User getUserById(Context context, String id) {

        Cursor cursor = getData(context, "select * from " + Main.TABLE_NAME + " WHERE id = ?", new String[]{id});
        User user = new User();
        System.out.println(cursor.getCount());
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            user.setLogin(cursor.getString(cursor.getColumnIndexOrThrow("login")));
            user.setId(cursor.getString(cursor.getColumnIndexOrThrow("id")));
            user.setScore(cursor.getString(cursor.getColumnIndexOrThrow("score")));
            user.setAvatar_url(cursor.getString(cursor.getColumnIndexOrThrow("avatar_url")));
            user.setUrl(cursor.getString(cursor.getColumnIndexOrThrow("url")));
            user.setLikes(cursor.getInt(cursor.getColumnIndexOrThrow("likes")));
            user.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow("status")));
        }
        cursor.close();
        return user;
    }

    public void deleteData(Context context) {
        dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + Main.TABLE_NAME);
        dbHelper.close();
    }


    public long insertRecord(Context context, String tableName, ContentValues cv) {
        dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(Main.LOG_TAG, "Insert one record into " + tableName);
        long rowID = db.insertWithOnConflict(tableName, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        dbHelper.close();
        return rowID;
    }

    public void updateRecord(Context context, String tableName, ContentValues cv, String id) {
        dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(Main.LOG_TAG, "Update record in " + tableName);
        db.update(tableName, cv, "id = ?", new String[]{id});
        dbHelper.close();
    }

    public Cursor getData(Context context, String query, String[] args) {
        dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.rawQuery(query, args);
    }

    public Cursor getUsersListCursor(Context context) {
        return getData(context, "select * from " + Main.TABLE_NAME, null);
    }

    private ContentValues createCV(User user) {
        ContentValues cv = new ContentValues();
        cv.put("login", user.getLogin());
        cv.put("id", user.getId());
        cv.put("score", user.getScore());
        cv.put("avatar_url", user.getAvatar_url());
        cv.put("url", user.getUrl());
        cv.put("likes", user.getLikes());
        cv.put("status", user.getStatus());
        return cv;
    }

    public void insertUsersList(Context context, UserList userList) {

        for (User user : userList.items
                ) {
            insertRecord(context, Main.TABLE_NAME, createCV(user));
        }
    }

    public void updateUser(Context context, User user) {
        updateRecord(context, Main.TABLE_NAME, createCV(user), user.getId());
    }


}
