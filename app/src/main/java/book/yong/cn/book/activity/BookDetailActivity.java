package book.yong.cn.book.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import book.yong.cn.book.R;
import book.yong.cn.book.activity.fragment.bookDetail.BookDetailFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 书籍详情
 *
 * @author yong
 * @time 2019/6/8 15:12
 */
public class BookDetailActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.downBook)
    Button downBook;
    @Bind(R.id.startBook)
    Button startBook;

    private String str;

    private BookDetailFragment bookDetailFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public void initDate() {

    }

    @Override
    public void initView() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        str = Objects.requireNonNull(getIntent().getExtras()).getString("book");
        bookDetailFragment = new BookDetailFragment(this, str);
        transaction.add(R.id.book_detail_fragment, bookDetailFragment).commit();

        startBook.setOnClickListener(this);
        downBook.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startBook:
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                    String bookNumber = jsonObject.getString("number");
                    String bookName = jsonObject.getString("name");
                    Intent intent = new Intent(this, BookPageActivity.class);
                    intent.putExtra("page", 0);
                    intent.putExtra("count", 1);
                    intent.putExtra("catalogueList", bookNumber);
                    intent.putExtra("bookName", bookName);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.downBook:
                break;
        }
    }
}
