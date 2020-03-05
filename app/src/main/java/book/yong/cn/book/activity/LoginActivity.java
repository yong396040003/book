package book.yong.cn.book.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import book.yong.cn.book.R;
import book.yong.cn.book.jutil.Http;
import book.yong.cn.book.jutil.MD5;
import book.yong.cn.book.jutil.LoginDialogFalse;
import book.yong.cn.book.jutil.StaticConstant;
import book.yong.cn.book.sqlite.Login_database;

/**
 * 登陆界面
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //账号
    private EditText username;
    //密码
    private EditText password;
    //忘记密码
    private TextView forget;
    //新用户注册
    private TextView register;
    //登陆按钮
    private Button login;
    //获取编辑框数据
    private String usernameText;
    private String passwordText;
    //mHandler
    private Handler mHandler;
    //对话框
    private AlertDialog.Builder builder;

    private Login_database login_database;
    ContentValues managers;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        forget = findViewById(R.id.forget);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        forget.setOnClickListener(this);
        register.setOnClickListener(this);
        login.setOnClickListener(this);

        login_database = new Login_database(this, "manager.db", null, 1);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if ("true".equals(msg.obj)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    //sqLite保存
                    SQLiteDatabase database = login_database.getReadableDatabase();
                    managers = new ContentValues();
                    managers.put("name", usernameText);
                    managers.put("password", MD5.MD5toString(passwordText));
                    managers.put("icon", "无");
                    database.insert("manager", null, managers);
                    database.close();
                    //SP保存，用于自动登陆
                    SharedPreferences sp = getSharedPreferences("isLogin", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("name", usernameText).apply();
                    finish();
                    startActivity(intent);
                } else {
                    LoginDialogFalse dialogFalse = new LoginDialogFalse(LoginActivity.this);
                    dialogFalse.show();
                }

                if (msg.what == 2 && msg.obj != null) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject((String) msg.obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        assert jsonObject != null;
                        if (jsonObject.getInt("code") == 0) {
                            Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v == forget) {
            Toast.makeText(LoginActivity.this, "对不起，暂未开放（forget）", Toast.LENGTH_SHORT).show();
        } else if (v == register) {
            Intent intent = new Intent();
            intent.setClass(this, RegisterActivity.class);
            startActivity(intent);
        } else if (v == login) {
            //正则判断一下输入是否合法
            usernameText = username.getText().toString();
            passwordText = password.getText().toString();

            if (!"".equals(usernameText) && !"".equals(passwordText)) {
                String pattern = "^[1-9]\\d*$";
                //如果不是root用户则需判断账号是否为纯数字，是否合法
                if (!"root".equals(usernameText)) {
                    boolean isNumber = Pattern.matches(pattern, usernameText);
                    if (isNumber) {
                        //向服务器发送数据进行验证(坑点：android4.0以后访问网络不能在主线程)
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String msg = Http.sendPost(StaticConstant.URL_USER_PHONE_LOGIN,
                                        "userEmail=" + usernameText
                                                + "&userPassword=" + passwordText);
                                Message message = new Message();
                                message.what = 2;
                                message.obj = msg;
                                mHandler.sendMessage(message);
                            }
                        });
                        thread.start();
                    } else {
                        Toast.makeText(this, "账号格式错误（仅为数字）", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //关闭软件盘
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null && getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(
                                getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    Toast.makeText(this, "登陆中...", Toast.LENGTH_LONG).show();
                    //若本地存储，只需本地先对照
                    SQLiteDatabase database = login_database.getReadableDatabase();
                    Cursor cursor = database.query("manager", null, "name=? and password=?", new String[]{usernameText, MD5.MD5toString(passwordText)}, null, null, null);
                    if (cursor.getCount() > 0) {
                        cursor.close();
                        database.close();
                        //SP保存，用于自动登陆
                        SharedPreferences sp = getSharedPreferences("isLogin", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("name", usernameText).apply();
                        Intent intent = new Intent(this, MainActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        cursor.close();
                        database.close();
                        //向服务器发送数据进行验证(root用户)
                        new Thread() {
                            @Override
                            public void run() {
                                String url = "http://49.233.93.71/loginPhone.do";
                                String parameter = "?username=" + usernameText + "&password=" + passwordText;
                                String jsonString = Http.sendGet(url + parameter);
                                try {
                                    JSONObject jsonObject = new JSONObject(jsonString);
                                    Message msg = new Message();
                                    msg.obj = jsonObject.get("code");
                                    //两秒后发送到主线程
                                    mHandler.sendMessageDelayed(msg, 1000);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                }
            } else {
                Toast.makeText(this, "请输入账号或密码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "未知错误(44login)...", Toast.LENGTH_SHORT).show();
        }
    }
}

