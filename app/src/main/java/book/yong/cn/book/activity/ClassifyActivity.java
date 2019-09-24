package book.yong.cn.book.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import book.yong.cn.book.activity.fragment.classify.End;
import book.yong.cn.book.activity.fragment.classify.Hots;
import book.yong.cn.book.activity.fragment.classify.New;
import book.yong.cn.book.activity.fragment.classify.Recommend;
import book.yong.cn.book.jutil.FontTextView;

/**
 *书籍分类里的activity
 *@author yong
 *@time 2019/6/8 15:20
 *
 */
public class ClassifyActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragments;

    private FontTextView classify_left_text;
    private TextView classify_middle_text;
    private FontTextView classify_right_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_content);

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
        classify_left_text = findViewById(R.id.classify_left_text);
        classify_middle_text = findViewById(R.id.classify_middle_text);
        classify_right_text = findViewById(R.id.classify_right_text);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);

        classify_middle_text.setText(getIntent().getExtras().getString("data"));

        final String []tabTitle = {"最热","最新","推荐","完结"};
        for (int i = 0; i < tabTitle.length; i++){
            tabLayout.addTab(tabLayout.newTab().setText(tabTitle[i]));
        }

        String category = getIntent().getExtras().getString("data");
        fragments = new ArrayList<>();
        Hots hots = Hots.getInstance(category);
        fragments.add(hots);
        New new_ = New.getInstance(category);
        fragments.add(new_);
        Recommend recommend = Recommend.getInstance(category);
        fragments.add(recommend);
        End end = End.getInstance(category);
        fragments.add(end);

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
                return tabTitle[position];
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        classify_left_text.setOnClickListener(this);
        classify_right_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.classify_left_text:
                finish();
                break;
            case R.id.classify_right_text:
                break;
        }
    }
}
