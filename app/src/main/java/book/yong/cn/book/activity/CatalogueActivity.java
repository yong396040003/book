package book.yong.cn.book.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import book.yong.cn.book.R;
import book.yong.cn.book.activity.fragment.catalogue.Catalogue_left;
import book.yong.cn.book.activity.fragment.catalogue.Catalogue_right;
import book.yong.cn.book.jutil.FontTextView;
import book.yong.cn.book.jutil.Http;
import book.yong.cn.book.jutil.StaticConstant;

/**
 *
 * 目录页面
 *@author yong
 *@time 2019/6/8 15:20
 *
 */
public class CatalogueActivity extends AppCompatActivity implements View.OnClickListener {
    private FontTextView left_text;
    private TextView middle_text;
    private FontTextView right_text;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragments;

    //默认向下排序
    private boolean sort = true;

    private Catalogue_left catalogue_left;
    private Catalogue_right catalogue_right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_book_catalogue);

        //设置状态栏透明 且渐变
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        init();
    }

    private void init() {

        left_text = findViewById(R.id.left_text);
        middle_text = findViewById(R.id.middle_text);
        right_text = findViewById(R.id.right_text);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);

        String bookName = getIntent().getExtras().getString("bookName");
        middle_text.setText(bookName);

        String json = getIntent().getExtras().getString("catalogueList");

        final String[] tabTitles = {"目录","书签"};
        fragments = new ArrayList<>();
        catalogue_left = Catalogue_left.getInstance(json,bookName);
        fragments.add(catalogue_left);
        catalogue_right = Catalogue_right.getInstance();
        fragments.add(catalogue_right);

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitles[position];
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        left_text.setOnClickListener(this);
        right_text.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_text:
                finish();
                break;
            case R.id.right_text:
                if (sort){
                    right_text.setText(R.string.sort_u);
                    catalogue_left.sort_u();
                    sort = false;
                }else {
                    right_text.setText(R.string.sort_d);
                    catalogue_left.sort_d();
                    sort = true;
                }
                break;
        }
    }
}
