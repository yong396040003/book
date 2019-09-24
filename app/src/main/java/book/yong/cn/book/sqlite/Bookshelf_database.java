package book.yong.cn.book.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 我的书架数据
 */
public class Bookshelf_database extends SQLiteOpenHelper {

    public Bookshelf_database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("success","onCreate");
        String sql = "CREATE TABLE bookshelf(\n" +
                "_id Integer Primary key autoIncrement,\n" +
                "number int,\n" +
                "name varchar,\n" +
                "data data,\n" +
                "catalogue varchar,\n" +
                "count int,\n" +
                "page int\n" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion){
            onCreate(db);
        }
    }
}
