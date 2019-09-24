package book.yong.cn.book.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

import book.yong.cn.book.R;
import book.yong.cn.book.jutil.Font;
import book.yong.cn.book.jutil.JustifyTextView;
import book.yong.cn.book.jutil.StaticConstant;
import book.yong.cn.book.pojo.BookPage;

/**
 * 页面适配器
 *
 * @author yong
 * @time 2019/6/12 16:25
 */
public class BookPageAdapter extends PagerAdapter {
    //存储每一页content
    private List<BookPage> bookPageList;

    private Context context;

    //自定义滑动适配器
    private MyOnScrollListener myOnScrollListener;

    public BookPageAdapter(Context context, List<BookPage> bookPageList) {
        this.context = context;
        this.bookPageList = bookPageList;
    }

    @Override
    public int getCount() {
        return bookPageList.size();
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
        //内容
        JustifyTextView body = cur.findViewById(R.id.body);
        //章节名
        TextView textView = cur.findViewById(R.id.zj_name);
        //如果是首页
        if (bookPageList.get(position).getOne()) {
            TextView index = cur.findViewById(R.id.zj_name_);
            index.setVisibility(View.VISIBLE);
            textView.setText(bookPageList.get(position).getBookName());
            index.setText(bookPageList.get(position).getZjName());
        } else {
            textView.setText(bookPageList.get(position).getZjName());
        }

        body.setTextSize(Font.fontSize);
        body.setLineSpacing(4, 1.5f);
        body.setMaxLines(Font.lines);
        body.setMaxEms(Font.width / Font.textSize + 1);
        body.setText(bookPageList.get(position).getContent());

        //底部当前位置
        TextView textView1 = cur.findViewById(R.id.pageCount);
        float path = (float) bookPageList.get(position).getPage() / StaticConstant.ZJCOUNT * 100;
        BigDecimal b = new BigDecimal(path);
        path = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        textView1.setText(path + "%");
        if (cur.getParent() != container) {
            container.addView(cur);
        }
        //更新下一章
        if (position % 10 == 2) {
            myOnScrollListener.update();
        }
        return cur;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public interface MyOnScrollListener {
        //更新下一章
        void update();
    }

    public void setMyOnScrollListener(MyOnScrollListener myOnScrollListener) {
        this.myOnScrollListener = myOnScrollListener;
    }
}
