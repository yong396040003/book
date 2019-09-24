package book.yong.cn.book.activity.fragment.bookDetail;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import book.yong.cn.book.R;
import book.yong.cn.book.activity.AuthorOtherBook;
import book.yong.cn.book.activity.BookDetailActivity;
import book.yong.cn.book.activity.CatalogueActivity;
import book.yong.cn.book.jutil.FontTextView;
import book.yong.cn.book.jutil.Http;
import book.yong.cn.book.jutil.JsonUtil;
import book.yong.cn.book.jutil.StaticConstant;
import book.yong.cn.book.model.BookDetailFragmentModel;
import book.yong.cn.book.pojo.Book;
import book.yong.cn.book.sqlite.Bookshelf_database;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 书籍详情页面碎片
 *
 * @author yong
 * @time 2019/6/1 18:09
 */
@SuppressLint("ValidFragment")
public class BookDetailFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.author_one_img)
    ImageView authorOneImg;
    @Bind(R.id.author_one_text)
    TextView authorOneText;
    @Bind(R.id.author_one)
    LinearLayout authorOne;
    @Bind(R.id.author_two_img)
    ImageView authorTwoImg;
    @Bind(R.id.author_two_text)
    TextView authorTwoText;
    @Bind(R.id.author_two)
    LinearLayout authorTwo;
    @Bind(R.id.author_three_img)
    ImageView authorThreeImg;
    @Bind(R.id.author_three_text)
    TextView authorThreeText;
    @Bind(R.id.author_three)
    LinearLayout authorThree;
    @Bind(R.id.author_four_img)
    ImageView authorFourImg;
    @Bind(R.id.author_four_text)
    TextView authorFourText;
    @Bind(R.id.author_four)
    LinearLayout authorFour;
    @Bind(R.id.author_more)
    TextView authorMore;
    private View view;

    //返回键
    private FontTextView back;
    //添加到书架
    private TextView add_bookshelf;
    //是否添加 true:是 false:不是
    private Boolean isSelect = false;

    private ImageView imageView;
    private TextView book_name;
    private TextView book_author;
    private TextView book_category;
    private TextView book_status;
    private TextView book_wordNumber;
    private TextView book_synopsis;
    private TextView update;
    private TextView update_zj;

    private LinearLayout catalogue;

    private String bookNumber;
    private String bookName;
    private String author;

    //接收到的完整json
    private String jsonString;

    private Bookshelf_database bookshelf_database;
    //用于插入数据
    private ContentValues values;

    private Bitmap bitmap;

    private Boolean isStop = false;

    private Context context;
    private String str;

    private Intent intent;

    //该作者其他作品
    private int AUTHOR_WHAT = 0;
    private List<Book> bookList;
    private int AUTHOR_ONE = 0;
    private int AUTHOR_TWO = 0;
    private int AUTHOR_THREE = 0;
    private int AUTHOR_FOUR = 0;

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://ta的其他作品请求
                    if (msg.obj != null && getActivity() != null) {
                        bookList = (List<Book>) msg.obj;
                        int index = -1;
                        WindowManager manager = getActivity().getWindowManager();
                        DisplayMetrics outMetrics = new DisplayMetrics();
                        manager.getDefaultDisplay().getMetrics(outMetrics);
                        int width = outMetrics.widthPixels;
                        int height = outMetrics.heightPixels;
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.width = width / 4;
                        for (int i = 0; i < bookList.size(); i++) {
                            if (bookList.get(i).getNumber() == Integer.parseInt(bookNumber)) {
                                continue;
                            }
                            index++;
                            if (index == 0) {
                                authorOne.setVisibility(View.VISIBLE);
                                authorOne.setLayoutParams(layoutParams);
                                authorOneImg.setImageBitmap(bookList.get(i).getImg());
                                authorOneText.setText(bookList.get(i).getName());
                                authorOne.setOnClickListener(BookDetailFragment.this);
                                AUTHOR_ONE = i;
                            } else if (index == 1) {
                                authorTwo.setVisibility(View.VISIBLE);
                                authorTwo.setLayoutParams(layoutParams);
                                authorTwoImg.setImageBitmap(bookList.get(i).getImg());
                                authorTwoText.setText(bookList.get(i).getName());
                                authorTwo.setOnClickListener(BookDetailFragment.this);
                                AUTHOR_TWO = i;
                            } else if (index == 2) {
                                authorThree.setVisibility(View.VISIBLE);
                                authorThree.setLayoutParams(layoutParams);
                                authorThreeImg.setImageBitmap(bookList.get(i).getImg());
                                authorThreeText.setText(bookList.get(i).getName());
                                authorThree.setOnClickListener(BookDetailFragment.this);
                                AUTHOR_THREE = i;
                            } else if (index == 3) {
                                authorFour.setVisibility(View.VISIBLE);
                                authorFour.setLayoutParams(layoutParams);
                                authorFourImg.setImageBitmap(bookList.get(i).getImg());
                                authorFourText.setText(bookList.get(i).getName());
                                authorFour.setOnClickListener(BookDetailFragment.this);
                                AUTHOR_FOUR = i;
                            } else {
                                break;
                            }
                            authorMore.setOnClickListener(BookDetailFragment.this);
                        }
                    }
                    break;
                case 1://图片请求
                    if (msg.obj != null) {
                        bitmap = (Bitmap) msg.obj;
                        imageView.setImageBitmap(bitmap);
                        File file = new File(getActivity().getCacheDir() + "/img/" + bookNumber + ".jpg");
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                                FileOutputStream outputStream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                case 2://章节请求
                    if (msg.obj != null) {
                        jsonString = (String) msg.obj;
                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);
                            JSONObject jsonObject1 = new JSONObject(jsonObject.get("data").toString());
                            String zj = jsonObject1.get("name").toString();
                            StaticConstant.ZJCOUNT = jsonObject1.getInt("count");
                            if (zj != null) {
                                update_zj.setText(zj);
                                values.put("catalogue", zj);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };

    public BookDetailFragment(Context context, String str) {
        this.context = context;
        this.str = str;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_book_detail, null);
        initView();
        ButterKnife.bind(this, view);
        return view;
    }

    private void initView() {
        init();
    }

    private void init() {
        setBookDetail();

        getAuthorBook();

        back = view.findViewById(R.id.back);
        add_bookshelf = view.findViewById(R.id.add_bookshelf);

        catalogue = view.findViewById(R.id.catalogue);

        //判断该书是否在我的书架里
        isBookshelf();

        back.setOnClickListener(this);
        add_bookshelf.setOnClickListener(this);
        catalogue.setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    private void setBookDetail() {
        imageView = view.findViewById(R.id.img);
        book_name = view.findViewById(R.id.book_name);
        book_author = view.findViewById(R.id.book_author);
        book_category = view.findViewById(R.id.book_category);
        book_status = view.findViewById(R.id.book_status);
        book_wordNumber = view.findViewById(R.id.book_wordNumber);
        book_synopsis = view.findViewById(R.id.book_synopsis);

        update = view.findViewById(R.id.update);
        update_zj = view.findViewById(R.id.update_zj);

        bookshelf_database = new Bookshelf_database(context, "bookshelf.db", null, 1);
        values = new ContentValues();

        try {
            final JSONObject jsonObject = new JSONObject(str);
            bookNumber = jsonObject.getString("number");
            bookName = jsonObject.get("name").toString();
            book_name.setText(bookName);
            author = jsonObject.get("author").toString();
            book_author.setText(author);
            book_category.setText(jsonObject.get("category").toString());
            book_status.setText(jsonObject.get("status").toString());
            String wordNumber = jsonObject.get("wordNumber").toString();
            if (!"null".equals(wordNumber)) {
                wordNumber = wordNumber.replaceAll("字", "");
                int num = Integer.parseInt(wordNumber);
                if (num > 10000) {
                    num = num / 10000;
                    book_wordNumber.setText(num + " 万字");
                } else {
                    book_wordNumber.setText(num + " 字");
                }
            }
            book_synopsis.setText(jsonObject.get("synopsis").toString());
            String data = jsonObject.getString("data");
            update.setText(update.getText().toString() + jsonObject.getString("data"));

            //数据先用values临时存储起来，便于等会插入数据到数据库
            values.put("number", bookNumber);
            values.put("name", bookName);
            values.put("data", data);
            values.put("count", 1);
            values.put("page", 1);

            //如果图片已缓存就不用在网络请求
            File file = new File(Objects.requireNonNull(getActivity()).getCacheDir() + "/img/" + bookNumber + ".jpg");
            if (file.exists()) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(getActivity().getCacheDir() + "/img/" + bookNumber + ".jpg"));
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isStop) {
                            try {
                                Bitmap bitmap = Http.getRemoteImage((String) jsonObject.get("imgUrl"));
                                if (bitmap != null) {
                                    Message msg = new Message();
                                    msg.obj = bitmap;
                                    msg.what = 1;
                                    handler.sendMessage(msg);
                                } else {
                                    //图片请求失败
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取章节
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!isStop) {
                    if (bookNumber != null) {
                        jsonString = Http.sendPost(StaticConstant.URL_BOOK_LastDETAIL, "number=" + bookNumber);
                        Message msg = new Message();
                        msg.obj = jsonString;
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                }
            }
        }).start();
    }

    private void isBookshelf() {
        //判断该书是否在我的书架里
        SQLiteDatabase database = bookshelf_database.getReadableDatabase();
        String[] args = {bookNumber};
        Cursor cursor = database.query("bookshelf", null, "number=?", args, null, null, null);
        if (cursor.getCount() > 0) {
            isSelect = true;
            add_bookshelf.setSelected(true);
            add_bookshelf.setText("  已添加  ");
        } else {
            isSelect = false;
            add_bookshelf.setSelected(false);
            add_bookshelf.setText("   + 书架  ");
        }
        cursor.close();
        database.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_bookshelf:
                if (isSelect) {
                    //删除该数据
                    SQLiteDatabase database = bookshelf_database.getReadableDatabase();
                    String[] args = {bookNumber};
                    database.delete("bookshelf", "number=?", args);
                    database.close();

                    isSelect = false;
                    add_bookshelf.setSelected(false);
                    add_bookshelf.setText("   + 书架  ");
                } else {
                    //插入该数据
                    SQLiteDatabase database = bookshelf_database.getReadableDatabase();
                    long _id = database.insert("bookshelf", null, values);
                    if (_id > 0) {
                        Toast.makeText(context, "成功加入书架", Toast.LENGTH_SHORT).show();
                    }
                    database.close();

                    isSelect = true;
                    add_bookshelf.setSelected(true);
                    add_bookshelf.setText("  已添加  ");
                }
                break;
            case R.id.back:
                getActivity().finish();
                break;
            case R.id.catalogue:
                Intent intent = new Intent();
                intent.setClass(context, CatalogueActivity.class);
                intent.putExtra("catalogueList", bookNumber);
                intent.putExtra("bookName", bookName);
                startActivity(intent);
                break;
            case R.id.author_more:
                if (bookList.size() <= 5) {
                    Toast.makeText(context, "没有更多该作者相关书籍", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent();
                    intent.setClass(context, AuthorOtherBook.class);
                    intent.putExtra("author", author);
                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < bookList.size(); i++) {
                        JSONObject jsonObject = JsonUtil.pojoToJson(bookList.get(i));
                        try {
                            jsonArray.put(i, jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    intent.putExtra("data", jsonArray.toString());
                    startActivity(intent);
                }
                break;
            case R.id.author_one:
                startToOtherBook(AUTHOR_ONE);
                break;
            case R.id.author_two:
                startToOtherBook(AUTHOR_TWO);
                break;
            case R.id.author_three:
                startToOtherBook(AUTHOR_THREE);
                break;
            case R.id.author_four:
                startToOtherBook(AUTHOR_FOUR);
                break;
        }
    }

    /**
     * 到其他书籍去
     */
    public void startToOtherBook(int index) {
        Intent intent = new Intent();
        intent.setClass(Objects.requireNonNull(getActivity()), BookDetailActivity.class);
        JSONObject jsonObject = JsonUtil.pojoToJson(bookList.get(index));
        intent.putExtra("book", jsonObject.toString());
        getActivity().finish();
        startActivity(intent);
    }

    /**
     * 获取该作者其他作品
     */
    public void getAuthorBook() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Book> bookList = BookDetailFragmentModel.getAuthorBook(author);
                    if (bookList != null && bookList.size() != 0 && getActivity() != null) {
                        for (int i = 0; i < bookList.size(); i++) {
                            File file = new File(getActivity().getCacheDir() + "/img/" + bookList.get(i).getNumber() + ".jpg");
                            if (file.exists()) {
                                bookList.get(i).setImg(BitmapFactory.decodeFile(getActivity().getCacheDir() + "/img/" + bookList.get(i).getNumber() + ".jpg"));
                            } else {
                                Bitmap bitmap = Http.getRemoteImage(bookList.get(i).getImgUrl());
                                if (bitmap != null && getActivity() != null) {
                                    bookList.get(i).setImg(bitmap);
                                    try {
                                        File file1 = new File(getActivity().getCacheDir() + "/img/" + bookList.get(i).getNumber() + ".jpg");
                                        if (!file1.exists()) {
                                            file1.createNewFile();
                                            FileOutputStream outputStream = new FileOutputStream(file1);
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            Message message = new Message();
                            message.what = AUTHOR_WHAT;
                            message.obj = bookList;
                            handler.sendMessage(message);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
