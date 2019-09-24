package book.yong.cn.book.jutil;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;

import book.yong.cn.book.activity.LoginActivity;
import book.yong.cn.book.activity.MainActivity;
import book.yong.cn.book.activity.WelcomeActivity;
import book.yong.cn.book.sqlite.Login_database;

import static android.content.Context.MODE_PRIVATE;

/**
 * 动态权限管理
 */
public class DynamicAuthorityManagement {

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };

    /**
     * 手机存储权限
     *
     * @param activity
     */
    public static void verifyStoragePermissions(final Activity activity) {
        // Check if we have write permission
        int write_permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int read_permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (read_permission != PackageManager.PERMISSION_GRANTED && write_permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the u
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 1);
        } else if (read_permission == PackageManager.PERMISSION_GRANTED && write_permission == PackageManager.PERMISSION_GRANTED) {
            //创建一个线程用于跳转到Login activity
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(2000);
                        SharedPreferences sp = activity.getSharedPreferences("isLogin", MODE_PRIVATE);
                        String name = sp.getString("name", "null");
                        //若本地存储，只需本地先对照
                        Login_database login_database = new Login_database(activity, "manager.db", null, 1);
                        SQLiteDatabase database = login_database.getReadableDatabase();
                        Cursor cursor = database.query("manager", null, "name=?", new String[]{name}, null, null, null);
                        if (cursor.getCount() <= 0) {
                            cursor.close();
                            login_database.close();
                            //显示意图，直接跳转到登陆activity
                            Intent intent = new Intent(activity, LoginActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                        } else {
                            cursor.close();
                            login_database.close();
                            //显示意图，直接跳转到登陆activity
                            Intent intent = new Intent(activity, MainActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
