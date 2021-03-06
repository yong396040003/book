package book.yong.cn.book.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import book.yong.cn.book.R;
import book.yong.cn.book.adapter.BookPagerAdapter1;
import book.yong.cn.book.adapter.BookViewPager;
import book.yong.cn.book.jutil.Font;
import book.yong.cn.book.jutil.FontTextView;
import book.yong.cn.book.jutil.Http;
import book.yong.cn.book.jutil.PageUtil;
import book.yong.cn.book.jutil.StaticConstant;
import book.yong.cn.book.pojo.BookPage;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 书籍activity更新1.0
 *
 * @author yong
 * @time 2019/10/3 16:13
 */
public class BookPageActivity1 extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.loading)
    LinearLayout loading;
    @Bind(R.id.viewPager_body)
    BookViewPager viewPagerBody;
    @Bind(R.id.back)
    FontTextView back;
    @Bind(R.id.more)
    FontTextView more;
    @Bind(R.id.top)
    LinearLayout top;
    @Bind(R.id.catalogue)
    FontTextView catalogue;
    @Bind(R.id.classify)
    TextView classify;
    @Bind(R.id.pro)
    FontTextView pro;
    @Bind(R.id.setting)
    FontTextView setting;
    @Bind(R.id.time)
    FontTextView time;
    @Bind(R.id.bottom)
    LinearLayout bottom;
    @Bind(R.id.join_bookmark)
    TextView joinBookmark;
    @Bind(R.id.one)
    LinearLayout one;
    @Bind(R.id.two)
    LinearLayout two;
    @Bind(R.id.three)
    LinearLayout three;
    @Bind(R.id.four)
    LinearLayout four;

    private String bodys;

    private String bookNumber;
    private int nowPage = 0;

    private Boolean isStop = false;

    //存储每一页content
    private List<BookPage> bookPageList;
    private BookPage[] bookPageListBody;
    private BookPagerAdapter1 pagerAdapter;

    //章节名
    private String zjName;
    //书名
    private String bookName;
    //缓存到的章节
    private int count;

    //判断每一章节是否为首页
    private Boolean isOn = false;

    //当前页码
    private int position = 0;

    //用于判断左右滑动
    private boolean isLeft = false;
    private boolean isRight = false;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            List<BookPage> bookPages = new ArrayList<>();
            if (msg.obj != null && msg.arg1 == 1) {
                JSONObject jsonObject = (JSONObject) msg.obj;
                try {
                    bodys = jsonObject.getString("body");
                    Log.e("success",bodys);
                    bodys = bodys.replaceAll("<span id=\"contents\">", "");
                    bodys = bodys.replaceAll("</span>", "");
                    bodys = bodys.replaceAll("<br /><br />", "\n");
                    bodys = bodys.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;", "\u3000\u3000");
                    bodys = bodys.replaceAll("&nbsp;", "  ");

                    bodys = halfToFull(bodys);

                    //存储每一页页数
                    int page = 0;
                    if (!isStop) {
                        while (!"".equals(bodys)) {
                            String pageContent;
                            BookPage bookPage = new BookPage();
                            if (page == 0) {
                                pageContent = PageUtil.pageContent(bodys, Font.lines - 3, Font.width, Font.textSize);
                                //是否为首页
                                bookPage.setOne(true);
                            } else {
                                pageContent = PageUtil.pageContent(bodys, Font.lines, Font.width, Font.textSize);
                                bookPage.setOne(false);
                            }
                            if (pageContent == null) {
                                break;
                            }
                            bookPage.setCount(count);
                            //第几页
                            bookPage.setPage(page);
                            //内容
                            bookPage.setContent(pageContent);
                            //书号
                            bookPage.setId(Integer.parseInt(bookNumber));
                            //书名
                            bookPage.setBookName(bookName);
                            //章节名
                            if (zjName != null) {
                                bookPage.setZjName(zjName);
                            } else {
                                bookPage.setZjName(bookName);
                            }
                            bookPages.add(bookPage);
                            pageContent = pageContent.replaceAll("\n\n", "\n");
                            bodys = bodys.replace(pageContent, "");
                            page++;
                        }

                        if (!isLeft && !isRight) {
                            bookPageList.addAll(bookPages);
                        } else if (isLeft) {
                            int i = 0;
                            for (BookPage bookPage : bookPages) {
                                bookPageList.add(i, bookPage);
                                i++;
                            }
                            position = bookPages.size();
                            bookPageListBody[0] = bookPages.get(position - 1);
                            bookPageListBody[1] = bookPageList.get(position);
                            bookPageListBody[2] = bookPageList.get(position + 1);
                            position++;
                            pagerAdapter.notifyDataSetChanged();
                            viewPagerBody.setCurrentItem(1, false);
                        } else {
                            position = bookPageList.size();
                            bookPageList.addAll(bookPages);
                            bookPageListBody[0] = bookPageList.get(position - 1);
                            bookPageListBody[1] = bookPageList.get(position);
                            bookPageListBody[2] = bookPageList.get(position + 1);
                            position++;
                            pagerAdapter.notifyDataSetChanged();
                            viewPagerBody.setCurrentItem(1, false);
                        }

                        Log.e("success", bookPageList.toString());

                        if (!isOn) {
                            isOn = true;
                            Message message = Message.obtain();
                            message.obj = "ok";
                            message.arg1 = 2;
                            mHandler.sendMessage(message);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (msg.obj != null && msg.arg1 == 2) {
                pageShow();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_book_page, null);
        view.setBackgroundColor(Color.rgb(198, 238, 206));
        setContentView(view);

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //设置章节
        bookPageList = new ArrayList<>();

        //默认设置栏关闭
        setVisibility(false);

        //设置监听注册
        back.setOnClickListener(this);
        more.setOnClickListener(this);
        joinBookmark.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);


        init();
    }

    private Boolean isHD = true;

    /**
     * 显示页面
     */
    private void pageShow() {

        LinearLayout linearLayout = findViewById(R.id.loading);
        linearLayout.setVisibility(View.GONE);

        //若当前章节只有一页
        if (bookPageList.size() == 1) {
            BookPageActivity1.MyThread myThread = new BookPageActivity1.MyThread();
            myThread.start();
        }

        bookPageListBody = new BookPage[3];
        position = nowPage;

        bookPageListBody[0] = bookPageList.get(position);
        position++;
        bookPageListBody[1] = bookPageList.get(position);
        position++;
        bookPageListBody[2] = bookPageList.get(position);

        pagerAdapter = new BookPagerAdapter1(this, bookPageListBody);
        viewPagerBody.setAdapter(pagerAdapter);

        //如果是第一章第一页
        if (nowPage == 0 && count == 1) {
            viewPagerBody.setCurrentItem(0);
            isHD = false;
            viewPagerBody.setCount(count, 0);
        } else if (nowPage == 0) {
            viewPagerBody.setCurrentItem(1);
            BookPage bookPage = bookPageListBody[0];
            bookPageListBody[0] = null;
            bookPageListBody[2] = bookPageListBody[1];
            bookPageListBody[1] = bookPage;
            pagerAdapter.notifyDataSetChanged();
        }

        Log.e("success", "一次");


        viewPagerBody.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                //重置一下
                viewPagerBody.setCount(0, 0);
                if (count - 1 == 1 && i == 0 && !isHD && position == 2) {
                    viewPagerBody.setCount(count, bookPageListBody[i].getPage());
                    return;
                }
                if (i == 0 && position > 1) {
                    position--;
                    bookPageListBody[2] = bookPageList.get(position);
                    if (position - 1 >= 0) {
                        bookPageListBody[1] = bookPageList.get(position - 1);
                        if (position - 2 >= 0) {
                            bookPageListBody[0] = bookPageList.get(position - 2);
                        } else {
                            bookPageListBody[0] = null;
                        }
                    } else {
                        bookPageListBody[1] = null;
                        bookPageListBody[0] = null;
                    }

                    pagerAdapter.notifyDataSetChanged();
                    viewPagerBody.setCurrentItem(1, false);
                } else if (i == 2) {
                    if (position + 1 < bookPageList.size()) {
                        position++;
                        bookPageListBody[2] = bookPageList.get(position);
                        bookPageListBody[1] = bookPageList.get(position - 1);
                        bookPageListBody[0] = bookPageList.get(position - 2);
                        pagerAdapter.notifyDataSetChanged();
                        viewPagerBody.setCurrentItem(1, false);
                    } else if (position + 1 == bookPageList.size()) {
                        bookPageListBody[2] = null;
                        bookPageListBody[1] = bookPageList.get(position);
                        bookPageListBody[0] = bookPageList.get(position - 1);
                        position++;
                        pagerAdapter.notifyDataSetChanged();
                        viewPagerBody.setCurrentItem(1, false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        viewPagerBody.setOnTouchLister(new BookViewPager.MyOnTouchLister() {
            @Override
            public void update() {
            }

            @Override
            public void show() {
                setVisibility(true);
            }

            @Override
            public void hide() {
                setVisibility(false);
            }
        });

        pagerAdapter.setMyOnScrollListener(new BookPagerAdapter1.MyOnScrollListener() {
            @Override
            public void updateNex() {
                count = bookPageList.get(bookPageList.size() - 1).getCount() + 1;
                BookPageActivity1.MyThread myThread = new BookPageActivity1.MyThread();
                myThread.start();
                isLeft = false;
                isRight = true;
            }

            @Override
            public void updatePre() {
                count = bookPageList.get(0).getCount();
                if (count - 1 == 0) {
                    return;
                }
                count--;
                isLeft = true;
                isRight = false;
                BookPageActivity1.MyThread myThread = new BookPageActivity1.MyThread();
                myThread.start();
            }
        });
    }

    private void init() {

        LayoutInflater view = LayoutInflater.from(BookPageActivity1.this);
        View cur = view.inflate(R.layout.body_cur, null, false);
        TextView textView = cur.findViewById(R.id.pageCount);
        setPageContent(textView);

        Bundle bundle = getIntent().getExtras();
        //目录数据
        bookNumber = bundle.getString("catalogueList");
        //当前页
        nowPage = bundle.getInt("page");
        //书名
        bookName = bundle.getString("bookName");
        //当前章节
        count = bundle.getInt("count");

        //获取目录数据
        BookPageActivity1.MyThread catalogueList = new BookPageActivity1.MyThread();
        catalogueList.start();
    }

    /**
     * 设置page content
     *
     * @param body
     */
    private void setPageContent(TextView body) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        body.setTextSize(Font.fontSize);
        body.setLineSpacing(4, 1.5f);
        Font.lines = dm.heightPixels / body.getLineHeight() - 2;
        Font.width = dm.widthPixels;
        Font.textSize = (int) body.getTextSize();
        body.setMaxLines(Font.lines);
    }

    @Override
    protected void onDestroy() {
        //结束进程
        isStop = true;
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.join_bookmark:
                Toast.makeText(this, "加入书签", Toast.LENGTH_SHORT).show();
                break;
            case R.id.more:
                Toast.makeText(this, "更多", Toast.LENGTH_SHORT).show();
                break;
            case R.id.one:
                Intent intent = new Intent();
                intent.setClass(this, CatalogueActivity.class);
                intent.putExtra("bookName", bookName);
                intent.putExtra("catalogueList", bookNumber);
                startActivity(intent);
                break;

        }
    }

    /**
     * 获取目录数据，根据当前页在获取nowPage章节
     */
    private class MyThread extends Thread {
        @Override
        public void run() {
            if (!isStop) {
                String jsonString = Http.sendPost(StaticConstant.URL_BOOK_DETAIL_NUMBER_COUNT, "number=" + bookNumber + "&count=" + count);

                if (jsonString != null) {
                    try {
                        final JSONObject jsonObject = new JSONObject(jsonString);
                        final JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        if (jsonObject1 != null && nowPage > -1) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        zjName = jsonObject1.getString("name");
                                        String url = jsonObject1.getString("url");
                                        String body = Http.sendPost(StaticConstant.URL_BOOK_BODY, "url=" + url);
                                        jsonObject1.put("body", body);
                                        Message msg = new Message();
                                        msg.obj = jsonObject1;
                                        msg.arg1 = 1;
                                        mHandler.sendMessage(msg);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    //数据加载失败
                }
            }
        }
    }

    /**
     * 设置是否显示设置栏
     *
     * @param is
     */
    private void setVisibility(Boolean is) {
        if (is) {
            top.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.VISIBLE);
        } else {
            top.setVisibility(View.INVISIBLE);
            bottom.setVisibility(View.INVISIBLE);
        }
    }

    // 功能：字符串半角转换为全角
    // 说明：半角空格为32,全角空格为12288.
    // 		 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
    // 输入参数：input -- 需要转换的字符串
    // 输出参数：无：
    // 返回值: 转换后的字符串
    public static String halfToFull(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            //半角空格
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }        //根据实际情况，过滤不需要转换的符号
            // if (c[i] == 46) //半角点号，不转换
            // continue;
            if (c[i] > 32 && c[i] < 127)
                //其他符号都转换为全角
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }
}
