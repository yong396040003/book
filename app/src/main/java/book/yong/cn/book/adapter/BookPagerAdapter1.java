package book.yong.cn.book.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigDecimal;

import book.yong.cn.book.R;
import book.yong.cn.book.jutil.Font;
import book.yong.cn.book.jutil.JustifyTextView;
import book.yong.cn.book.jutil.StaticConstant;
import book.yong.cn.book.pojo.BookPage;

/**
 * 书籍翻页适配器更新1.0
 *
 * @author yong
 * @time 2019/10/3 16:12
 */
public class BookPagerAdapter1 extends PagerAdapter {
    //存储每一页content
    private BookPage[] bookPageList;

    private Context context;

    //自定义滑动适配器
    private BookPagerAdapter1.MyOnScrollListener myOnScrollListener;

    public BookPagerAdapter1(Context context, BookPage[] bookPageList) {
        this.context = context;
        this.bookPageList = bookPageList;
    }

    @Override
    public int getCount() {
        return bookPageList.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater view = LayoutInflater.from(context);
        View cur = view.inflate(R.layout.body_cur, container, false);
        if (cur.getParent() != container) {
            container.addView(cur);
        }
        if (bookPageList[position] == null) {
            LinearLayout linearLayout = cur.findViewById(R.id.loading);
            linearLayout.setVisibility(View.VISIBLE);
            RelativeLayout relativeLayout = cur.findViewById(R.id.content_body);
            relativeLayout.setVisibility(View.GONE);
            if (position == 0) {
                myOnScrollListener.updatePre();
            } else {
                myOnScrollListener.updateNex();
            }
            return cur;
        }
        LinearLayout linearLayout = cur.findViewById(R.id.loading);
        linearLayout.setVisibility(View.GONE);
        RelativeLayout relativeLayout = cur.findViewById(R.id.content_body);
        relativeLayout.setVisibility(View.VISIBLE);
        //内容
        JustifyTextView body = cur.findViewById(R.id.body);
        //章节名
        TextView textView = cur.findViewById(R.id.zj_name);
        //如果是首页
        if (bookPageList[position].getOne()) {
            TextView index = cur.findViewById(R.id.zj_name_);
            index.setVisibility(View.VISIBLE);
            textView.setText(bookPageList[position].getBookName());
            index.setText(bookPageList[position].getZjName());
        } else {
            textView.setText(bookPageList[position].getZjName());
        }

        body.setTextSize(Font.fontSize);
        body.setLineSpacing(4, 1.5f);
        body.setMaxLines(Font.lines);
        body.setMaxEms(Font.width / Font.textSize + 1);
        body.setText(bookPageList[position].getContent());

        //底部当前位置
        TextView textView1 = cur.findViewById(R.id.pageCount);
        float path = (float) bookPageList[position].getCount() / StaticConstant.ZJCOUNT * 100;
        BigDecimal b = new BigDecimal(path);
        path = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        textView1.setText(path + "%");

        return cur;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    /**
     * 自定义更新接口
     */
    public interface MyOnScrollListener {
        void updateNex();

        void updatePre();
    }

    public void setMyOnScrollListener(MyOnScrollListener myOnScrollListener) {
        this.myOnScrollListener = myOnScrollListener;
    }
}
