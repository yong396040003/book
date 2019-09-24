package book.yong.cn.book.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import book.yong.cn.book.jutil.StaticConstant;

public class BookViewPager extends ViewPager {
    private int downX;
    private int downY;

    private Boolean m = false;

    private MyOnTouchLister myOnTouchLister;

    public BookViewPager(@NonNull Context context) {
        super(context);
    }

    public BookViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!m) {
                    myOnTouchLister.hide();
                    m = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                int x = (int) (ev.getX() - downX);
                int y = (int) (ev.getY() - downY);
                if (x == 0 && y == 0 && m) {
                    myOnTouchLister.show();
                    m = false;
                } else if (x == 0 && y == 0 && !m) {
                    myOnTouchLister.hide();
                    m = true;
                }
                if (count <= 1 && ev.getX() > downX && nowPage == 0) {
                    Toast.makeText(getContext(), "当前已是第一页", Toast.LENGTH_SHORT).show();
                } else if (count == StaticConstant.ZJCOUNT) {
                    Toast.makeText(getContext(), "你正在左翻", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(ev);
    }

    public void setOnTouchLister(MyOnTouchLister myOnTouchLister) {
        this.myOnTouchLister = myOnTouchLister;
    }

    private int count;
    private int nowPage;

    public void setCount(int count, int nowPage) {
        this.count = count;
        this.nowPage = nowPage;
    }

    public interface MyOnTouchLister {
        void show();

        void hide();
    }
}
