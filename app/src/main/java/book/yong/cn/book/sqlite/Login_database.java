package book.yong.cn.book.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Login_database extends SQLiteOpenHelper {
    public Login_database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE manager(\n" +
                "_id Integer Primary key autoIncrement,\n" +
                "name varchar unique not null,\n" +
                "password varchar not null,\n" +
                "icon varchar\n" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion){
            onCreate(db);
        }
    }
}
