package book.yong.cn.book.activity.fragment.main;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import book.yong.cn.book.R;
import book.yong.cn.book.adapter.BookCityAdapterHorizontal;
import book.yong.cn.book.adapter.BookCityAdapterVertical;
import book.yong.cn.book.jutil.DisplayUtil;
import book.yong.cn.book.jutil.GridSpacingItemDecoration;
import book.yong.cn.book.jutil.StaticConstant;
import book.yong.cn.book.pojo.Book;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 书城
 *
 * @author yong
 * @time 2019/8/7 16:09
 */
public class BookCity_home extends Fragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.hot_book)
    TextView hotBook;
    @Bind(R.id.hot_book_v)
    RecyclerView hotBookV;
    @Bind(R.id.hot_book_h)
    RecyclerView hotBookH;
    @Bind(R.id.my_book)
    TextView myBook;
    @Bind(R.id.my_book_v)
    RecyclerView myBookV;
    @Bind(R.id.my_book_h)
    RecyclerView myBookH;
    @Bind(R.id.new_book)
    TextView newBook;
    @Bind(R.id.new_book_v)
    RecyclerView newBookV;
    @Bind(R.id.new_book_h)
    RecyclerView newBookH;
    @Bind(R.id.end_book)
    TextView endBook;
    @Bind(R.id.end_book_v)
    RecyclerView endBookV;
    @Bind(R.id.end_book_h)
    RecyclerView endBookH;
    @Bind(R.id.rv_main)
    NestedScrollView rvMain;

    private List<Book> hotBookListV;
    private List<Book> hotBookListH;
    private List<Book> myBookListV;
    private List<Book> myBookListH;
    private List<Book> newBookListV;
    private List<Book> newBookListH;
    private List<Book> endBookListV;
    private List<Book> endBookListH;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (msg.obj != null) {
                        List<Book> bookList;
                        bookList = (List<Book>) msg.obj;
                        hotBookListV = new ArrayList<>();
                        hotBookListH = new ArrayList<>();
                        myBookListV = new ArrayList<>();
                        myBookListH = new ArrayList<>();
                        newBookListV = new ArrayList<>();
                        newBookListH = new ArrayList<>();
                        endBookListV = new ArrayList<>();
                        endBookListH = new ArrayList<>();
                        //把数据进行分组
                        bookListClassify(bookList);

                        //数据适配
                        bookListAdapter();


                    } else {
                        Toast.makeText(getContext(), "数据加载异常！！！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if (msg.obj != null) {
                        List<Book> bookList;
                        bookList = (List<Book>) msg.obj;
                        //更新数据 新删除后添加
                        clearListClassify();
                        bookListClassify(bookList);
                        updateBook();
                    }
                    break;
                default:
                    break;
            }
        }

    };

    private BookCityAdapterVertical hotBookCityAdapterVertical;
    private BookCityAdapterHorizontal hotBookCityAdapterHorizontal;
    private BookCityAdapterVertical myBookCityAdapterVertical;
    private BookCityAdapterHorizontal myBookCityAdapterHorizontal;
    private BookCityAdapterVertical newBookCityAdapterVertical;
    private BookCityAdapterHorizontal newBookCityAdapterHorizontal;
    private BookCityAdapterHorizontal endBookCityAdapterHorizontal0;
    private BookCityAdapterHorizontal endBookCityAdapterHorizontal;

    /**
     * 适配器
     */
    private void bookListAdapter() {
        hotBookCityAdapterVertical = new BookCityAdapterVertical(getContext(), hotBookListV);
        hotBookV.setAdapter(hotBookCityAdapterVertical);
        hotBookCityAdapterHorizontal = new BookCityAdapterHorizontal(getContext(), hotBookListH);
        hotBookH.setAdapter(hotBookCityAdapterHorizontal);

        myBookCityAdapterVertical = new BookCityAdapterVertical(getContext(), myBookListV);
        myBookV.setAdapter(myBookCityAdapterVertical);
        myBookCityAdapterHorizontal = new BookCityAdapterHorizontal(getContext(), myBookListH);
        myBookH.setAdapter(myBookCityAdapterHorizontal);

        newBookCityAdapterVertical = new BookCityAdapterVertical(getContext(), newBookListV);
        newBookV.setAdapter(newBookCityAdapterVertical);
        newBookCityAdapterHorizontal = new BookCityAdapterHorizontal(getContext(), newBookListH);
        newBookH.setAdapter(newBookCityAdapterHorizontal);

        endBookCityAdapterHorizontal0 = new BookCityAdapterHorizontal(getContext(), endBookListV);
        endBookV.setAdapter(endBookCityAdapterHorizontal0);
        endBookCityAdapterHorizontal = new BookCityAdapterHorizontal(getContext(), endBookListH);
        endBookH.setAdapter(endBookCityAdapterHorizontal);

    }

    @Override
    public void onResume() {
        super.onResume();
        endBookV.addItemDecoration(new GridSpacingItemDecoration(3, 20, false));
        endBookH.addItemDecoration(new GridSpacingItemDecoration(3, 20, false));
        myBookH.addItemDecoration(new GridSpacingItemDecoration(3, 20, false));
    }

    /**
     * 数据更新
     */
    private void updateBook() {
        hotBookCityAdapterVertical.notifyDataSetChanged();
        hotBookCityAdapterHorizontal.notifyDataSetChanged();
        myBookCityAdapterVertical.notifyDataSetChanged();
        myBookCityAdapterHorizontal.notifyDataSetChanged();
        newBookCityAdapterVertical.notifyDataSetChanged();
        newBookCityAdapterHorizontal.notifyDataSetChanged();
        endBookCityAdapterHorizontal0.notifyDataSetChanged();
        endBookCityAdapterHorizontal.notifyDataSetChanged();
    }

    /**
     * 把数据分配到每一类
     *
     * @param bookList
     */
    private void bookListClassify(List<Book> bookList) {
        int i = 0;
        for (Book book : bookList) {
            if (book == null) {
                return;
            }
            if (i < 2) {
                hotBookListV.add(book);
            } else if (i < 6) {
                hotBookListH.add(book);
            } else if (i < 9) {
                myBookListV.add(book);
            } else if (i < 12) {
                myBookListH.add(book);
            } else if (i < 13) {
                newBookListV.add(book);
            } else if (i < 18) {
                newBookListH.add(book);
            } else if (i < 21) {
                endBookListV.add(book);
            } else {
                endBookListH.add(book);
            }
            i++;
        }
    }

    private void clearListClassify() {
        hotBookListV.clear();
        hotBookListH.clear();
        myBookListV.clear();
        myBookListH.clear();
        newBookListV.clear();
        newBookListH.clear();
        endBookListV.clear();
        endBookListH.clear();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_city, container, false);
        ButterKnife.bind(this, view);
        initView();
        initDate();
        return view;
    }

    private void initDate() {
        BookCityLoadingDate loadingDate = new BookCityLoadingDate(getContext(), StaticConstant.URL_BOOK_CITY, mHandler, null, null);
        loadingDate.loadingDate();
    }

    private void initView() {
        collapsingToolbarLayout.setTitle("书城");
        //通过CollapsingToolbarLayout修改字体颜色
        //设置还没收缩时状态下字体颜色
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        //设置收缩后Toolbar上字体的颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
