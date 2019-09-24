package book.yong.cn.book.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import book.yong.cn.book.R;
import book.yong.cn.book.jutil.CleanCache;
import book.yong.cn.book.jutil.FontTextView;
import book.yong.cn.book.activity.fragment.main.BookCity_home;
import book.yong.cn.book.activity.fragment.main.Bookshelf_home;
import book.yong.cn.book.activity.fragment.main.Classify_home;
import book.yong.cn.book.activity.fragment.main.PaiHang_home;
import book.yong.cn.book.myInterface.GoBookCityListener;
import book.yong.cn.book.myInterface.OpenDrawerLayoutListener;

/**
 * 主页面
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoBookCityListener, OpenDrawerLayoutListener {

    //中间四个区域
    private Bookshelf_home bookshelf_home;
    private BookCity_home bookCity_home;
    private PaiHang_home paiHang_home;
    private Classify_home classify_home;

    //底部四个导航栏
    private LinearLayout bookshelf_bottom;
    private LinearLayout bookCity_bottom;
    private LinearLayout paiHang_bottom;
    private LinearLayout classify_bottom;

    //图标 字体
    private FontTextView bookshelf_icon;
    private TextView bookshelf;
    private FontTextView bookCity_icon;
    private TextView bookCity;
    private FontTextView paiHang_icon;
    private TextView paiHang;
    private FontTextView classify_icon;
    private TextView classify;

    //drawLayout
    private DrawerLayout drawerLayout;

    //loginExit
    private TextView loginExit;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        init();

        //侧面布局必须在主布局下，不然监听无法起作用（别问我为啥，我也不知到）
        NavigationView navigationView = findViewById(R.id.navigation_header_container);
        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "这是headView", Toast.LENGTH_SHORT).show();
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                switch (menuItem + "") {
                    case "阅读记录":
                        break;
                    case "同步书架":
                        break;
                    case "清除缓存":
                        final File book = new File(getCacheDir() + "/book");
                        final File img = new File(getCacheDir() + "/img");
                        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                .setIcon(android.R.drawable.stat_notify_error)
                                .setTitle("警告")
                                .setMessage("图片缓存：\t" + CleanCache.getFolderSize(img) + " mb\n"
                                        + "书籍缓存：\t" + CleanCache.getFolderSize(book) + " mb\n" +
                                        "当前操作不可逆是否继续？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        CleanCache.cleanCache(book);
                                        CleanCache.cleanCache(img);
                                        Toast.makeText(MainActivity.this,"清除缓存成功",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .create();
                        dialog.show();
                        break;
                    case "免责声明":
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, Disclaimer.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        loginExit = findViewById(R.id.loginExit);
        loginExit.setOnClickListener(this);
    }

    /**
     * 初始化
     */
    private void init() {

        drawerLayout = findViewById(R.id.drawer_layout);

        bookshelf_bottom = findViewById(R.id.bookshelf_bottom);
        bookCity_bottom = findViewById(R.id.bookCity_bottom);
        paiHang_bottom = findViewById(R.id.paiHang_bottom);
        classify_bottom = findViewById(R.id.classify_bottom);

        bookshelf_icon = findViewById(R.id.bookshelf_icon);
        bookshelf = findViewById(R.id.bookshelf);
        bookCity_icon = findViewById(R.id.bookCity_icon);
        bookCity = findViewById(R.id.bookCity);
        paiHang_icon = findViewById(R.id.paiHang_icon);
        paiHang = findViewById(R.id.paiHang);
        classify_icon = findViewById(R.id.classify_icon);
        classify = findViewById(R.id.classify);

        //第一个导航栏默认被选中
        bookshelf_icon.setSelected(true);
        bookshelf.setSelected(true);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //默认书架显示
        bookshelf_home = new Bookshelf_home();
        transaction.add(R.id.frameLayout, bookshelf_home).commit();

        bookshelf_bottom.setOnClickListener(this);
        bookCity_bottom.setOnClickListener(this);
        paiHang_bottom.setOnClickListener(this);
        classify_bottom.setOnClickListener(this);

        //自定义接口 去书城
        bookshelf_home.setGoBookCityListener(this);
        bookshelf_home.setOpenDrawerLayoutListener(this);
    }

    @Override
    public void onClick(View v) {
        selected();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (v.getId()) {
            case R.id.bookshelf_bottom:
                bookshelf_home = new Bookshelf_home();
                transaction.add(R.id.frameLayout, bookshelf_home).commit();
                bookshelf_icon.setSelected(true);
                bookshelf.setSelected(true);

                bookshelf_home.setGoBookCityListener(this);
                bookshelf_home.setOpenDrawerLayoutListener(this);
                break;
            case R.id.bookCity_bottom:
                //禁止手势滑动
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                if (bookCity_home == null) {
                    bookCity_home = new BookCity_home();
                    transaction.add(R.id.frameLayout, bookCity_home).commit();
                } else {
                    transaction.show(bookCity_home).commit();
                }
                bookCity_icon.setSelected(true);
                bookCity.setSelected(true);
                break;
            case R.id.paiHang_bottom:
                //禁止手势滑动
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                if (paiHang_home == null) {
                    paiHang_home = new PaiHang_home();
                    transaction.add(R.id.frameLayout, paiHang_home).commit();
                } else {
                    transaction.show(paiHang_home).commit();
                }
                paiHang_icon.setSelected(true);
                paiHang.setSelected(true);
                break;
            case R.id.classify_bottom:
                //禁止手势滑动
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                if (classify_home == null) {
                    classify_home = new Classify_home();
                    transaction.add(R.id.frameLayout, classify_home).commit();
                } else {
                    transaction.show(classify_home).commit();
                }
                classify_icon.setSelected(true);
                classify.setSelected(true);
                break;
            case R.id.loginExit:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.stat_notify_error)
                        .setTitle("警告")
                        .setMessage("是否确认退出当前账号")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //SP保存，用于解除自动登陆
                                SharedPreferences sp = getSharedPreferences("isLogin", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("name", "null").commit();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                dialog.show();
                break;
            default:
                break;
        }
    }

    /**
     * 重置底部按钮
     */
    public void selected() {
        bookshelf_icon.setSelected(false);
        bookshelf.setSelected(false);
        bookCity_icon.setSelected(false);
        bookCity.setSelected(false);
        paiHang_icon.setSelected(false);
        paiHang.setSelected(false);
        classify_icon.setSelected(false);
        classify.setSelected(false);
    }

    /**
     * 重置所有Fragment
     *
     * @param transaction
     */
    public void hideFragment(FragmentTransaction transaction) {
        if (bookshelf_home != null) {
            transaction.hide(bookshelf_home);
        }
        if (bookCity_home != null) {
            transaction.hide(bookCity_home);
        }
        if (paiHang_home != null) {
            transaction.hide(paiHang_home);
        }
        if (classify_home != null) {
            transaction.hide(classify_home);
        }
    }

    @Override
    public void GoBookCity() {
        selected();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);

        if (bookCity_home == null) {
            bookCity_home = new BookCity_home();
            transaction.add(R.id.frameLayout, bookCity_home).commit();
        } else {
            transaction.show(bookCity_home).commit();
        }
        bookCity_icon.setSelected(true);
        bookCity.setSelected(true);
    }

    private boolean isBack = false;

    /**
     * 返回键监听
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (isBack) {
            super.onBackPressed();
        } else {
            isBack = true;
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        isBack = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    public void isOpenDrawerLayout(Boolean isOpen) {
        if (isOpen) {
            //允许手势滑动
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
}
