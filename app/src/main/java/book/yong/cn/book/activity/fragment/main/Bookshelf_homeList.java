package book.yong.cn.book.activity.fragment.main;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import book.yong.cn.book.R;
import book.yong.cn.book.adapter.BookshelfBaseAdapter;
import book.yong.cn.book.jutil.swipemenulistview.SwipeMenu;
import book.yong.cn.book.jutil.swipemenulistview.SwipeMenuCreator;
import book.yong.cn.book.jutil.swipemenulistview.SwipeMenuItem;
import book.yong.cn.book.jutil.swipemenulistview.SwipeMenuListView;
import book.yong.cn.book.myInterface.GoBookCityListener;
import book.yong.cn.book.pojo.Bookshelf;
import book.yong.cn.book.sqlite.Bookshelf_database;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 书架 list版
 *
 * @author yong
 * @time 2019/8/2 11:15
 */
public class Bookshelf_homeList extends Fragment implements SwipeMenuListView.OnMenuItemClickListener, AdapterView.OnItemClickListener {

    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.bookshelf_null)
    RelativeLayout bookshelfNull;
    @Bind(R.id.bookshelf_list)
    SwipeMenuListView bookshelfListView;
    @Bind(R.id.bookshelf_button)
    Button bookshelfButton;

    private List<Bookshelf> bookshelfList;

    private Bookshelf_database bookshelf_database;

    //适配器
    private BookshelfBaseAdapter bookshelfBaseAdapter;

    //声明接口
    public GoBookCityListener goBookCityListener;

    public void setGoBookCityListener(GoBookCityListener goBookCityListener) {
        this.goBookCityListener = goBookCityListener;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookshelf_list, container, false);
        ButterKnife.bind(this, view);

        init();

        bookshelfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goBookCityListener != null) {
                    goBookCityListener.GoBookCity();
                }
            }
        });

        return view;
    }

    /**
     * 初始化
     */
    private void init() {
        //bookshelf实例化
        bookshelfList = new ArrayList<>();

        bookshelf_database = new Bookshelf_database(getActivity(), "bookshelf.db", null, 1);
        SQLiteDatabase database = bookshelf_database.getReadableDatabase();
        Cursor cursor = database.query("bookshelf", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Bookshelf bookshelf = new Bookshelf();

            int _id = cursor.getInt(0);
            int number = cursor.getInt(1);
            String name = cursor.getString(2);
            String data = cursor.getString(3);
            String catalogue = cursor.getString(4);
            int count = cursor.getInt(5);
            int page = cursor.getInt(6);

            bookshelf.set_id(_id);
            bookshelf.setNumber(number);
            bookshelf.setName(name);
            bookshelf.setCatalogue(catalogue);
            bookshelf.setData(data);
            bookshelf.setCount(count);
            bookshelf.setPage(page);

            File file = new File(getActivity().getCacheDir() + "/img/" + number + ".jpg");
            if (file.exists()) {
                bookshelf.setBitmap(BitmapFactory.decodeFile(getActivity().getCacheDir() + "/img/" + number + ".jpg"));
            }

            bookshelfList.add(bookshelf);
        }

        if (bookshelfList.size() > 0) {
            bookshelfListView.setVisibility(View.VISIBLE);
            bookshelfNull.setVisibility(View.GONE);

            bookshelfBaseAdapter = new BookshelfBaseAdapter(getActivity(), bookshelfList);

            bookshelfListView.setAdapter(bookshelfBaseAdapter);

            // 第1步：设置创建器，并且在其中生成我们需要的菜单项，将其添加进菜单中
            SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
                @Override
                public void create(SwipeMenu menu) {
                    SwipeMenuItem delete = new SwipeMenuItem(getActivity());
                    delete.setTitle("删除");
                    delete.setTitleColor(Color.WHITE);
                    delete.setTitleSize(18);
                    delete.setWidth(dp2px(90));
                    delete.setBackground(R.color.colorAccent);
                    menu.addMenuItem(delete);
                }
            };

            bookshelfListView.setMenuCreator(swipeMenuCreator);

            bookshelfListView.setOnItemClickListener(this);

            bookshelfListView.setOnMenuItemClickListener(this);
        } else {
            bookshelfListView.setVisibility(View.GONE);
            bookshelfNull.setVisibility(View.VISIBLE);
        }

    }

    // 将dp转换为px

    private int dp2px(int value) {

        // 第一个参数为我们待转的数据的单位，此处为 dp（dip）

        // 第二个参数为我们待转的数据的值的大小

        // 第三个参数为此次转换使用的显示量度（Metrics），它提供屏幕显示密度（density）和缩放信息

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,

                getResources().getDisplayMetrics());

    }

    //另一种将dp转换为px的方法

    private int dp2px(float value) {

        final float scale = getResources().getDisplayMetrics().density;

        return (int) (value * scale + 0.5f);

    }

    /**
     * 列表点击阅读操作
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), bookshelfList.get(position).toString(), Toast.LENGTH_SHORT).show();
    }

    /**
     * 列表删除操作
     *
     * @param position
     * @param menu
     * @param index
     * @return
     */
    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index) {
            case 0:
                //删除该数据
                SQLiteDatabase database = bookshelf_database.getReadableDatabase();
                String[] args = {bookshelfList.get(position).getNumber() + ""};
                database.delete("bookshelf", "number=?", args);
                database.close();
                Toast.makeText(getActivity(), bookshelfList.get(position).getName() + " 已移除", Toast.LENGTH_SHORT).show();
                bookshelfList.remove(position);
                bookshelfBaseAdapter.notifyDataSetChanged();
                if (bookshelfList.size() == 0) {
                    bookshelfListView.setVisibility(View.GONE);
                    bookshelfNull.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
