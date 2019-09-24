package book.yong.cn.book.activity.fragment.classify;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import book.yong.cn.book.R;
import book.yong.cn.book.activity.BookDetailActivity;
import book.yong.cn.book.adapter.BookListBaseAdapter;
import book.yong.cn.book.jutil.Http;
import book.yong.cn.book.jutil.JsonUtil;
import book.yong.cn.book.jutil.StaticConstant;
import book.yong.cn.book.pojo.Book;
import butterknife.Bind;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class End extends Fragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.end)
    PullToRefreshListView end;
    private JSONObject jsonObject;
    private ListView listView;
    //json转换为实体类
    private List<Book> books;
    //当前视图
    private ViewGroup viewGroup;
    //加载中
    private View loading;

    private JSONArray array;

    //四大参数
    private int limit = 10;
    private int page = 1;
    private String status = "完本";
    private String sort = "totalRecommendedNumber";
    private String category;

    private BookListBaseAdapter adapter;

    @SuppressLint("ValidFragment")
    public End(String category) {
        this.category = category;
    }

    public static End getInstance(String category) {
        End end = new End(category);
        return end;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (msg.obj != null && end != null) {
                        books = (List<Book>) msg.obj;
                        adapter = new BookListBaseAdapter(getActivity(), books);
                        viewGroup.removeView(loading);
                        listView.setAdapter(adapter);
                        end.onRefreshComplete();
                        listView.setOnItemClickListener(End.this);
                    } else if (end != null) {
                        TextView view = loading.findViewById(R.id.loading_text);
                        view.setText("服务器数据请求失败");
                        end.onRefreshComplete();
                    }
                    break;
                case 1:
                    if (msg.obj != null && end != null) {
                        books = (List<Book>) msg.obj;
                        adapter.notifyDataSetChanged();
                        end.onRefreshComplete();
                        listView.setOnItemClickListener(End.this);
                    } else if (end != null) {
                        Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();
                        end.onRefreshComplete();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.end, container, false);
        ButterKnife.bind(this, viewGroup);
        loading = inflater.inflate(R.layout.dialog_loading, container, false);
        viewGroup.addView(loading);
        init();
        return viewGroup;
    }

    private void init() {
        listView = end.getRefreshableView();
        //第一次进入
        sendPost();

        end.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            /**
             * 下拉刷新
             * @param refreshView
             */
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                new End.GetDataTask().execute();
            }

            /**
             * 上拉加载
             * @param refreshView
             */
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                new End.GetDataTask().execute();
            }
        });
    }

    public void sendPost() {
        if (books != null) {
            adapter = new BookListBaseAdapter(getActivity(), books);
            viewGroup.removeView(loading);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        } else {
            update();
        }
    }

    /**
     * 更新
     */
    private void update() {
        String parameter = "page=" + page + "&limit=" + limit + "&category=" + category + "&sort=" + sort + "&status=" + status;
        LoadingDate loadingDate = new LoadingDate(getContext(), mHandler, books, parameter);
        loadingDate.loadingDate();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), BookDetailActivity.class);
        JSONObject jsonObject = JsonUtil.pojoToJson(books.get(position));
        intent.putExtra("book", jsonObject.toString());
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
            if (books == null || end == null) {
                return;
            }
            if (PullToRefreshBase.Mode.PULL_FROM_START == end.getCurrentMode()) {
                if (books.size() > 0) {
                    Toast.makeText(getActivity(), "无数据更新", Toast.LENGTH_SHORT).show();
                    end.onRefreshComplete();
                } else {
                    update();
                }
            } else if (PullToRefreshBase.Mode.PULL_FROM_END == end.getCurrentMode()) {
                page++;
                update();
            }
            super.onPostExecute(books);
        }
    }
}
