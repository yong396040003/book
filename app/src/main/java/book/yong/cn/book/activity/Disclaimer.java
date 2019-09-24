package book.yong.cn.book.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import book.yong.cn.book.R;
import book.yong.cn.book.jutil.FontTextView;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 免责声明
 *
 * @author yong
 * @time 2019/9/17 16:46
 */
public class Disclaimer extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.left_text)
    FontTextView leftText;
    @Bind(R.id.middle_text)
    TextView middleText;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.main_activity_top_nav)
    AppBarLayout mainActivityTopNav;

    @Override
    public int getLayoutId() {
        return R.layout.activity_disclaimer;
    }

    @Override
    public void initDate() {

    }

    @Override
    public void initView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        leftText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_text:
                finish();
                break;
            default:
                break;
        }
    }
}
