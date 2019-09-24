package book.yong.cn.book.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import book.yong.cn.book.R;
import book.yong.cn.book.adapter.BookListBaseAdapter;
import book.yong.cn.book.jutil.FontTextView;
import book.yong.cn.book.jutil.JsonUtil;
import book.yong.cn.book.pojo.Book;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 根据作者指定搜索查看其其他书籍
 *
 * @author yong
 * @time 2019/6/8 19:02
 */
public class AuthorOtherBook extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.back)
    FontTextView back;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.author_other_book_list)
    ListView authorOtherBookList;
    @Bind(R.id.author_name)
    TextView authorName;

    private JSONArray array;

    private BookListBaseAdapter bookListBaseAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_author_other_book;
    }

    @Override
    public void initDate() {
        String author = getIntent().getExtras().getString("author");
        authorName.setText(author);

        String data = getIntent().getExtras().getString("data");
        try {
            array = new JSONArray(data);
            List<Book> bookList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                String str = array.get(i).toString();
                Book book = new Book();
                book = (Book) JsonUtil.jsonToPojo(new JSONObject(str), book);
                File file = new File(getCacheDir() + "/img/" + book.getNumber() + ".jpg");
                if (file.exists()) {
                    book.setImg(BitmapFactory.decodeFile(getCacheDir() + "/img/" + book.getNumber() + ".jpg"));
                }
                bookList.add(book);
            }
            bookListBaseAdapter = new BookListBaseAdapter(this, bookList);
            authorOtherBookList.setAdapter(bookListBaseAdapter);

            authorOtherBookList.setOnItemClickListener(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initView() {
        initDate();

        back.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, BookDetailActivity.class);
        if (array != null) {
            try {
                intent.putExtra("book", array.get(position).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        startActivity(intent);
    }
}
