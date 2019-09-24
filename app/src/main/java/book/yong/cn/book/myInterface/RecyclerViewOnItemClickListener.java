package book.yong.cn.book.myInterface;

import android.view.View;

/**
 * RecyclerView点击接口
 *
 * @author yong
 * @time 2019/7/30 22:50
 */
public interface RecyclerViewOnItemClickListener {
    void onItemClick(View view, int position);

    void onLongItemClick(View view, int position);
}
