package book.yong.cn.book.activity.fragment.main;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import book.yong.cn.book.R;
import book.yong.cn.book.activity.BookPageActivity1;
import book.yong.cn.book.activity.SearchActivity;
import book.yong.cn.book.adapter.BookshelfGridAdapter;
import book.yong.cn.book.jutil.FontTextView;
import book.yong.cn.book.jutil.GridSpacingItemDecoration;
import book.yong.cn.book.myInterface.GoBookCityListener;
import book.yong.cn.book.myInterface.OpenDrawerLayoutListener;
import book.yong.cn.book.myInterface.RecyclerViewOnItemClickListener;
import book.yong.cn.book.pojo.Bookshelf;
import book.yong.cn.book.pojo.Catalogue_bean;
import book.yong.cn.book.sqlite.Bookshelf_database;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 书架 grid版
 *
 * @author yong
 * @time 2019/8/2 11:15
 */
public class Bookshelf_home extends Fragment implements View.OnClickListener {

    @Bind(R.id.text1)
    TextView text1;
    @Bind(R.id.bookshelf_null)
    RelativeLayout bookshelfNull;
    @Bind(R.id.bookshelf_button)
    Button bookshelfButton;
    @Bind(R.id.bookshelf_grid)
    RecyclerView bookshelfGrid;
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.delete)
    TextView delete;
    @Bind(R.id.batch_select)
    TextView batchSelect;
    @Bind(R.id.batch_done)
    RelativeLayout batchDone;
    @Bind(R.id.msg)
    TextView msg;
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

    private List<Bookshelf> bookshelfList;

    private Bookshelf_database bookshelf_database;

    //批量删除
    private int[] batchDelete;

    //grid适配器
    private BookshelfGridAdapter bookshelfGridAdapter;

    //声明接口
    public GoBookCityListener goBookCityListener;

    public void setGoBookCityListener(GoBookCityListener goBookCityListener) {
        this.goBookCityListener = goBookCityListener;
    }

    public OpenDrawerLayoutListener openDrawerLayoutListener;

    public void setOpenDrawerLayoutListener(OpenDrawerLayoutListener openDrawerLayoutListener) {
        this.openDrawerLayoutListener = openDrawerLayoutListener;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookshelf, container, false);
        ButterKnife.bind(this, view);

        init();

        myInterface();

        return view;
    }

    private void myInterface() {
        bookshelfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goBookCityListener != null) {
                    goBookCityListener.GoBookCity();
                }
            }
        });

        leftText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawerLayoutListener.isOpenDrawerLayout(true);
            }
        });

        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Objects.requireNonNull(getActivity()), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化
     */
    private void init() {

        //bookshelf实例化
        bookshelfList = new ArrayList<>();

        bookshelfList = readDateBase();

        if (bookshelfList.size() > 0) {
            bookshelfGrid.setVisibility(View.VISIBLE);
            bookshelfNull.setVisibility(View.GONE);

            //定义所需要删除数组 默认值为-1
            batchDelete = new int[bookshelfList.size()];
            for (int i = 0; i < batchDelete.length; i++) {
                batchDelete[i] = -1;
            }

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
            //垂直显示
            gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
            //正方向显示
            gridLayoutManager.setReverseLayout(false);

            bookshelfGrid.setLayoutManager(gridLayoutManager);
            bookshelfGrid.addItemDecoration(new GridSpacingItemDecoration(3, 80, true));
            bookshelfGrid.setItemViewCacheSize(bookshelfList.size());

            bookshelfGridAdapter = new BookshelfGridAdapter(bookshelfList);
            bookshelfGrid.setAdapter(bookshelfGridAdapter);

            bookshelfGridAdapter.setRecyclerViewOnItemClickListener(new RecyclerViewOnItemClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onItemClick(View view, int position) {
                    if (position == -1) {
                        goBookCityListener.GoBookCity();
                    } else if (position >= 10000) {
                        //长按之后批量选择操作
                        msg.setText("你已选择" + bookshelfGridAdapter.getSelectCount() + "本书");
                        batchDelete = bookshelfGridAdapter.getSelectArray();
                        bookshelfGridAdapter.isSelectAll = false;
                        isSelect = false;
                        batchSelect.setText("全选");
                    } else {
                        String bookNumber = String.valueOf(bookshelfList.get(position).getNumber());
                        Intent intent = new Intent(getActivity(), BookPageActivity1.class);
                        intent.putExtra("page", 0);
                        intent.putExtra("count", bookshelfList.get(position).getCount());
                        intent.putExtra("catalogueList", bookNumber);
                        intent.putExtra("bookName", bookshelfList.get(position).getName());
                        startActivity(intent);
                        getActivity().finish();
                    }
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onLongItemClick(View view, int position) {
                    if (position > -1) {
                        bookshelfGridAdapter.isOnLong = true;
                        batchDone.setVisibility(View.VISIBLE);
                        msg.setVisibility(View.VISIBLE);
                        msg.setText("你已选择" + bookshelfGridAdapter.getSelectCount() + "本书");
                        bookshelfGridAdapter.setBatchImgSelect();
                        bookshelfGridAdapter.setLast();
                        batchDelete = bookshelfGridAdapter.getSelectArray();

                    }
                }
            });

            batchSelect.setOnClickListener(this);
            cancel.setOnClickListener(this);
            delete.setOnClickListener(this);
        } else {
            bookshelfGrid.setVisibility(View.GONE);
            bookshelfNull.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 从数据库读取数据
     *
     * @return
     */
    private List<Bookshelf> readDateBase() {
        List<Bookshelf> bookshelves = new ArrayList<>();

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

            File file = new File(Objects.requireNonNull(getActivity()).getCacheDir() + "/img/" + number + ".jpg");
            if (file.exists()) {
                bookshelf.setBitmap(BitmapFactory.decodeFile(getActivity().getCacheDir() + "/img/" + number + ".jpg"));
            }

            bookshelves.add(bookshelf);
        }

        return bookshelves;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    //是否选择
    private Boolean isSelect = false;
    private int count = 0;

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.batch_select:
                if (!isSelect) {
                    //全选
                    bookshelfGridAdapter.setSelectAll();
                    bookshelfGridAdapter.isSelectAll = true;
                    batchSelect.setText("取消全选");
                    isSelect = true;
                    msg.setText("你已选择" + bookshelfList.size() + "本书");
                } else {
                    //取消全选
                    bookshelfGridAdapter.setNotSelectAll();
                    bookshelfGridAdapter.isSelectAll = false;
                    batchSelect.setText("全选");
                    isSelect = false;
                    msg.setText("你已选择" + bookshelfGridAdapter.getSelectCount() + "本书");
                    for (int i = 0; i < batchDelete.length; i++) {
                        batchDelete[i] = -1;
                    }
                }
                break;
            case R.id.cancel:
                cancel();
                break;
            case R.id.delete:
                //初始化
                count = 0;
                if (getActivity() != null) {
                    int i = -1;
                    for (int i1 : batchDelete) {
                        if (i1 > -1) {
                            count++;
                            i = i1;
                        }
                    }
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    if (count == 0) {
                        builder.setMessage("你还没有选择任何数据");
                    } else if (isSelect) {
                        builder.setMessage("是否将所有数据都删除！！！");
                    } else if (count == 1) {
                        builder.setMessage("是否将《" + bookshelfList.get(batchDelete[i]).getName() + "》移除书架");
                    } else {
                        builder.setMessage("是否将所选数据都删除！！！");
                    }

                    builder.setTitle("提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //删除该数据
                                    SQLiteDatabase database = bookshelf_database.getReadableDatabase();
                                    if (isSelect) {
                                        database.execSQL("DELETE FROM bookshelf");
                                        bookshelfList = null;
                                        bookshelfGridAdapter.notifyDataSetChanged();
                                        cancel();
                                        bookshelfGrid.setVisibility(View.GONE);
                                        bookshelfNull.setVisibility(View.VISIBLE);
                                    } else if (count > 0) {
                                        String sql = "DELETE FROM bookshelf WHERE ";
                                        for (int i : batchDelete) {
                                            if (i > -1) {
                                                sql += " number = " + bookshelfList.get(i).getNumber() + " OR";
                                            }
                                        }
                                        sql = sql.substring(0, sql.length() - 2);
                                        database.execSQL(sql);

                                        int isOne = 0;
                                        for (int i1 : batchDelete) {
                                            if (i1 > -1) {
                                                bookshelfList.remove(i1 - isOne);
                                                isOne++;
                                            }
                                        }
                                        bookshelfGridAdapter.notifyDataSetChanged();
                                        cancel();
                                        if (bookshelfList.size() == 0) {
                                            bookshelfGrid.setVisibility(View.GONE);
                                            bookshelfNull.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    database.close();

                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }
                break;
            default:
                break;
        }
    }

    private void cancel() {
        bookshelfGridAdapter.setCancelSelect();
        bookshelfGridAdapter.isOnLong = false;
        bookshelfGridAdapter.isSelectAll = false;
        isSelect = false;
        batchSelect.setText("全选");
        bookshelfGridAdapter.setNotSelectAll();
        batchDone.setVisibility(View.GONE);
        msg.setVisibility(View.GONE);
    }
}

