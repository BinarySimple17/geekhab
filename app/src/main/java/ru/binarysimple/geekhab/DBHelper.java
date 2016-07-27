package ru.binarysimple.geekhab;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;

    public static synchronized DBHelper getInstance(Context context) {
        // Use application context
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DBHelper(Context context) {
        super(context, Main.TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + Main.TABLE_NAME +
                " (_id integer PRIMARY KEY AUTOINCREMENT," +
                " login text NOT NULL," +
                " id text NOT NULL," +
                " score text," +
                " avatar_url text," +
                " url text" +
                " likes integer" +
                ");"
        );
        db.execSQL("CREATE UNIQUE INDEX id_UNIQUE ON "+Main.TABLE_NAME+" (id ASC);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}