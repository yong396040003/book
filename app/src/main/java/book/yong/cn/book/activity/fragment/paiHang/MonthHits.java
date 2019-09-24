package book.yong.cn.book.activity.fragment.paiHang;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import book.yong.cn.book.R;
import book.yong.cn.book.activity.BookDetailActivity;
import book.yong.cn.book.adapter.BookListBaseAdapter;
import book.yong.cn.book.jutil.JsonUtil;
import book.yong.cn.book.pojo.Book;

public class MonthHits extends Fragment implements AdapterView.OnItemClickListener {

    private JSONObject jsonObject;
    private ListView listView;
    private Handler mHandler;
    //json转换为实体类
    private List<Book> books;
    //当前视图
    private ViewGroup viewGroup;
    //加载中
    private View loading;

    private JSONArray array;

    private int limit = 0;

    private BookListBaseAdapter adapter;

    public static MonthHits getInstance() {
        MonthHits fragment = new MonthHits();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.month_hits, container, false);
        loading = inflater.inflate(R.layout.dialog_loading, container, false);
        viewGroup.addView(loading);
        init();
        return viewGroup;
    }

    private void init() {
        listView = viewGroup.findViewById(R.id.monthHits);
        if (books != null) {
            BookListBaseAdapter bookListBaseAdapter = new BookListBaseAdapter(getActivity(), books);
            viewGroup.removeView(loading);
            listView.setAdapter(bookListBaseAdapter);
            listView.setOnItemClickListener(MonthHits.this);
        } else {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.arg1 == 1) {
                        books = (List<Book>) msg.obj;
                        adapter = new BookListBaseAdapter(getActivity(), books);
                        if (books != null) {
                            viewGroup.removeView(loading);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(MonthHits.this);
                        } else {
                            TextView view = loading.findViewById(R.id.loading_text);
                            view.setText("服务器数据请求失败");
                        }
                    } else if (msg.arg1 == 2) {
                        Bitmap bitmap = (Bitmap) msg.obj;
                        books.get(msg.arg2).setImg(bitmap);
                        adapter.notifyDataSetChanged();
                    }
                }
            };

            //加载数据
            LoadingDate loadingDate = new LoadingDate(getActivity(), mHandler, "monthlyClicks");
            loadingDate.loadingDate();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), BookDetailActivity.class);
        JSONObject jsonObject = JsonUtil.pojoToJson(books.get(position));
        intent.putExtra("book", jsonObject.toString());
        startActivity(intent);
    }
}
