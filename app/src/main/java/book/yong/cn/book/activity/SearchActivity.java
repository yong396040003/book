package book.yong.cn.book.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.List;

import book.yong.cn.book.R;
import book.yong.cn.book.adapter.BookListBaseAdapter;
import book.yong.cn.book.jutil.FontTextView;
import book.yong.cn.book.jutil.JsonUtil;
import book.yong.cn.book.jutil.LoadingDate;
import book.yong.cn.book.jutil.StaticConstant;
import book.yong.cn.book.pojo.Book;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 搜索页面
 *
 * @author yong
 * @time 2019/8/13 19:11
 */
public class SearchActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    @Bind(R.id.search_edit)
    EditText searchEdit;
    @Bind(R.id.search)
    TextView search;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.nav)
    AppBarLayout nav;
    @Bind(R.id.loading)
    RelativeLayout loading;
    @Bind(R.id.pull_book)
    PullToRefreshListView pullBook;
    @Bind(R.id.back)
    FontTextView back;
    @Bind(R.id.pro)
    ProgressBar pro;
    @Bind(R.id.error)
    TextView error;

    private ListView listView;

    private String edit;

    private BookListBaseAdapter adapter;
    private List<Book> books;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (msg.obj != null && pullBook != null) {
                        loading.setVisibility(View.GONE);
                        books = (List<Book>) msg.obj;
                        initDate();
                        adapter = new BookListBaseAdapter(SearchActivity.this, books);
                        listView.setAdapter(adapter);
                        pullBook.onRefreshComplete();
                        listView.setOnItemClickListener(SearchActivity.this);
                    } else if (pullBook != null) {
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                            pullBook.onRefreshComplete();
                        }
                        error.setVisibility(View.VISIBLE);
                    }
                    break;
                case 1:
                    if (msg.obj != null && pullBook != null) {
                        books = (List<Book>) msg.obj;
                        adapter.notifyDataSetChanged();
                        pullBook.onRefreshComplete();
                        listView.setOnItemClickListener(SearchActivity.this);
                    } else if (pullBook != null) {
                        Toast.makeText(SearchActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
                        pullBook.onRefreshComplete();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initDate() {
        init();
    }

    private void init() {
        listView = pullBook.getRefreshableView();

        if (books != null) {
            if (books.size() > 10) {
                pullBook.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    /**
                     * 下拉刷新
                     *
                     * @param refreshView
                     */
                    @Override
                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                        String label = DateUtils.formatDateTime(SearchActivity.this, System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                        new GetDataTask().execute();
                    }

                    /**
                     * 上拉加载
                     *
                     * @param refreshView
                     */
                    @Override
                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                        String label = DateUtils.formatDateTime(SearchActivity.this, System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                        new GetDataTask().execute();
                    }
                });
            } else {
                pullBook.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                pullBook.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                        String label = DateUtils.formatDateTime(SearchActivity.this, System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                        new GetDataTask().execute();
                    }

                });
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (books != null) {
            Intent intent = new Intent();
            intent.setClass(SearchActivity.this, BookDetailActivity.class);
            JSONObject jsonObject = JsonUtil.pojoToJson(books.get(position));
            intent.putExtra("book", jsonObject.toString());
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.search:
                //清空listView
                if (books != null) {
                    books.clear();
                }
                //终止线程
                if (loadingDate != null) {
                    loadingDate.exit = true;
                    loadingDate = null;
                }
                error.setVisibility(View.GONE);
                search();
                break;
            default:
                break;
        }
    }

    /**
     * 搜索
     */
    private void search() {
        loading.setVisibility(View.VISIBLE);
        //关闭软件盘
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        if (searchEdit.getText() != null) {
            edit = searchEdit.getText().toString();
            update();
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return books;
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            if (books == null || pullBook == null) {
                return;
            }
            if (PullToRefreshBase.Mode.PULL_FROM_START == pullBook.getCurrentMode()) {
                if (books.size() > 0) {
                    Toast.makeText(SearchActivity.this, "无数据更新", Toast.LENGTH_SHORT).show();
                    pullBook.onRefreshComplete();
                } else {
                    update();
                }
            } else if (PullToRefreshBase.Mode.PULL_FROM_END == pullBook.getCurrentMode()) {
                page++;
                update();
            }
            super.onPostExecute(books);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void initView() {
        search.setOnClickListener(this);
        back.setOnClickListener(this);
    }


    private int page = 1;
    private int limit = 10;


    private LoadingDate loadingDate;

    /**
     * 更新
     */
    private void update() {
        String parameter = "?page=" + page + "&limit=" + limit + "&name=" + edit + "&author=" + edit;
        loadingDate = new LoadingDate(this, StaticConstant.URL_BOOK_Search, mHandler, books, parameter);
        loadingDate.loadingDate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        initView();
    }
}
