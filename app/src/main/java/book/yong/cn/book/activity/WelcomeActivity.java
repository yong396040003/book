package book.yong.cn.book.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import book.yong.cn.book.R;
import book.yong.cn.book.jutil.DynamicAuthorityManagement;
import book.yong.cn.book.sqlite.Login_database;

/**
 * 启动欢迎界面
 */
public class WelcomeActivity extends AppCompatActivity {
    private Boolean isStop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        DynamicAuthorityManagement.verifyStoragePermissions(this);

        File file = new File(getCacheDir() + "/img");
        if (!file.exists()) {
            file.mkdir();
        }
        File file1 = new File(getCacheDir() + "/book");
        if (!file1.exists()) {
            file1.mkdir();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //创建一个线程用于跳转到Login activity
                new Thread() {
                    public void run() {
                        if (!isStop) {
                            try {
                                Thread.sleep(2000);
                                SharedPreferences sp = getSharedPreferences("isLogin", MODE_PRIVATE);
                                String name = sp.getString("name", "null");
                                //若本地存储，只需本地先对照
                                Login_database login_database = new Login_database(WelcomeActivity.this, "manager.db", null, 1);
                                SQLiteDatabase database = login_database.getReadableDatabase();
                                Cursor cursor = database.query("manager", null, "name=?", new String[]{name}, null, null, null);
                                if (cursor.getCount() <= 0) {
                                    cursor.close();
                                    login_database.close();
                                    //显示意图，直接跳转到登陆activity
                                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    cursor.close();
                                    login_database.close();
                                    //显示意图，直接跳转到登陆activity
                                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                //拒绝
                Toast.makeText(this, "该应用需要存储权限", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        isStop = true;
        super.onDestroy();
    }
}
