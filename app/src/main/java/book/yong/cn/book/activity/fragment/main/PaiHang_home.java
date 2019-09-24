package book.yong.cn.book.activity.fragment.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import book.yong.cn.book.R;
import book.yong.cn.book.activity.fragment.paiHang.MonthHits;
import book.yong.cn.book.activity.fragment.paiHang.TotalHits;
import book.yong.cn.book.activity.fragment.paiHang.WeekHits;
import book.yong.cn.book.jutil.FontTextView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class PaiHang_home extends Fragment {

    @Bind(R.id.left_text)
    FontTextView leftText;
    @Bind(R.id.middle_text)
    TextView middleText;
    @Bind(R.id.right_text)
    FontTextView rightText;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.main_activity_top_nav)
    AppBarLayout mainActivityTopNav;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private List<Fragment> fragments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pai_hang, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {

        middleText.setText("排行");

        //可移动选项卡
        tabView();
    }

    private void tabView() {
        tabLayout = getActivity().findViewById(R.id.tabs);
        viewPager = getActivity().findViewById(R.id.viewPager);
        final String[] tabTitle = {"总排行", "月排行", "周排行"};
        for (int i = 0; i < tabTitle.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabTitle[i]));
        }

        fragments = new ArrayList<>();
        TotalHits totalHits = TotalHits.getInstance();
        fragments.add(totalHits);
        MonthHits monthHits = MonthHits.getInstance();
        fragments.add(monthHits);
        WeekHits weekHits = WeekHits.getInstance();
        fragments.add(weekHits);

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getFragmentManager()) {
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
