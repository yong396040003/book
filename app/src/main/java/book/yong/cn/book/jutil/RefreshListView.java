package book.yong.cn.book.jutil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import book.yong.cn.book.R;

public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    private Context context;

    private View view;
    private View view1;

    private TextView headView;
    private int headView_Height;
    private final int bottom = 50;

    private TextView footView;
    private int footView_Height;

    private OnRefreshListener onRefreshListener;
    private OnLoadMoreListener onLoadMoreListener;

    //下拉刷新三种状态
    private int PULL_DOWN = 0;
    private int RELEASE = 1;
    private int REFRRESH = 2;
    //当前状态
    private int NOW_STATUS = PULL_DOWN;

    //上拉加载两种状态
    private Boolean LOADING = false;
    private Boolean LOADING1 = false;

    private int down_Y;
    private int moveY;

    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        //初始化头部
        initHeadView();
        setOnScrollListener(this);
    }

    private void initHeadView() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.header_view, null);
        headView = view.findViewById(R.id.refurbish);
        headView.measure(0, 0);
        headView_Height = headView.getMeasuredHeight() + bottom;
        headView.setPadding(0, -(headView_Height), 0, bottom);

        view1 = layoutInflater.inflate(R.layout.footer_view, null);
        footView = view1.findViewById(R.id.loadMore);
        footView.measure(0, 0);
        footView_Height = footView.getMeasuredHeight() + bottom;
        footView.setPadding(0, -footView_Height, 0, 0);

        addHeaderView(view);
        addFooterView(view1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (isLoad()) {
                    return true;
                }

                headView.setText("下拉刷新");
                down_Y = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                if (isLoad()) {
                    return true;
                }

                int nowY = (int) ev.getY();
                moveY = nowY - down_Y;

                //下拉刷新
                if (NOW_STATUS != REFRRESH) {
                    //判断如果列表id为0 且是向下滑动则头部区域慢慢显示
                    if (getFirstVisiblePosition() == 0 && moveY > 0) {
                        if (moveY >= (3 * headView_Height)) {
                            headView.setPadding(0, (3 * headView_Height) - headView_Height, 0, bottom);
                        } else if (moveY > 1.5 * headView_Height && moveY < (3 * headView_Height)) {
                            headView.setText("释放刷新");
                            NOW_STATUS = RELEASE;
                            headView.setPadding(0, -headView_Height + moveY, 0, bottom);
                        } else if (moveY <= 1.5 * headView_Height) {
                            headView.setText("下拉刷新");
                            NOW_STATUS = PULL_DOWN;
                            headView.setPadding(0, -headView_Height + moveY, 0, bottom);
                        }
                    }

                }
                break;
            case MotionEvent.ACTION_UP:

                if (isLoad()) {
                    return true;
                }

                if (NOW_STATUS == PULL_DOWN) {
                    headView.setPadding(0, -headView_Height, 0, bottom);
                } else if (moveY > 1.5 * headView_Height) {
                    headView.setText("正在刷新...");
                    NOW_STATUS = REFRRESH;
                    headView.setPadding(0, headView_Height - bottom, 0, bottom);
                    if (onRefreshListener != null) {
                        onRefreshListener.onRefresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 当正在加载数据时屏蔽掉点击事件
     *
     * @return
     */
    private Boolean isLoad() {
        if (LOADING1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 用于监听滑动状态
     *
     * @param view
     * @param scrollState 滚动状态
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        SCROLL_STATE_TOUCH_SCROLL(1)：表示正在滚动。当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1
//        SCROLL_STATE_FLING(2) ：表示手指做了抛的动作（手指离开屏幕前，用力滑了一下，屏幕产生惯性滑动）。
//        SCROLL_STATE_IDLE(0) ：表示屏幕已停止。屏幕停止滚动时为0。
        if (scrollState == SCROLL_STATE_FLING || SCROLL_STATE_IDLE == scrollState) {
            if (getLastVisiblePosition() == getCount() - 1 && !LOADING) {
                //谨防多次滑动加载重复数据 true:正在加载
                LOADING = true;
                LOADING1 = true;
                footView.setPadding(0, bottom, 0, bottom);
                setSelection(getCount());
                if (onLoadMoreListener != null) {
                    onLoadMoreListener.onLoadMore();
                }
            }
        }
    }

    /**
     * 用于监听屏幕滚动
     *
     * @param view
     * @param firstVisibleItem 当前可见的第一个列表id
     * @param visibleItemCount 当前窗口所能看见的列表个数
     * @param totalItemCount   当前列表总数
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    /**
     * 提供个回调方法
     *
     * @param onRefreshListener
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    /**
     * 结束下拉刷新
     */
    public void finishRefresh() {
        headView.setPadding(0, -headView_Height, 0, bottom);
        NOW_STATUS = PULL_DOWN;
    }

    /**
     * 结束上拉刷新
     */
    public void finishLoad() {
        footView.setPadding(0, -bottom, 0, bottom);
        LOADING = false;
        LOADING1 = false;
    }

    /**
     * 没了
     */
    public void end() {
        footView.setPadding(0,bottom,0,bottom);
        footView.setText("没有更多了");
        LOADING = true;
        LOADING1 = false;
    }
}