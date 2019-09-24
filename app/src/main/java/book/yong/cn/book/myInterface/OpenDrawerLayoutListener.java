package book.yong.cn.book.myInterface;

/**
 *侧滑监听
 *@author yong
 *@time 2019/8/8 16:21
 *
 */
public interface OpenDrawerLayoutListener {
    /**
     * 根据boolean值判断是否允许打开侧滑
     * @param isOpen
     */
    void isOpenDrawerLayout(Boolean isOpen);
}
