package book.yong.cn.book.jutil;

/**
 * 静态常量类
 */
public class StaticConstant {
    //排行 post
    public final static String URL_PAIHANG = "http://49.233.93.71/selectBookByHits.do";
    //分类 post
    public final static String URL_CLASSIFY = "http://49.233.93.71/selectBook.do";
    //根据书籍编号查询目录 post
    public final static String URL_BOOK_DETAIL  = "http://49.233.93.71/selectBookByNumber.do";
    //根据书号和count获取该章节
    public final static String URL_BOOK_DETAIL_NUMBER_COUNT = "http://49.233.93.71/getCatalog.do";
    //根据编号查询最后一章
    public final static String URL_BOOK_LastDETAIL  = "http://49.233.93.71/getLastCatalog.do";
    //根据url查询章节的内容 post
    public final static String URL_BOOK_BODY = "http://49.233.93.71/selectBookBody.do";
    //images图片存储的文件
    public final static String IMAGES = "/data/data/book.yong.cn.book/files/images/";
    //根据作者查询对应的书籍 get
    public final static String URL_BOOK_AUTHOR = "http://49.233.93.71/selectBookByAuthor.do";
    //根据输入搜索
    public final static String URL_BOOK_Search = "http://49.233.93.71/selectBookLikeAuthorOrName.do";
    //查询每一类书籍数量
    public final static String URL_CLASSIFY_Count = "http://49.233.93.71/getClassifyCount.do";
    //书城
    public final static String URL_BOOK_CITY = "http://49.233.93.71/getBookCityBook.do";
    //章节数量
    public static int ZJCOUNT = 10;
    //用户注册
    public final static String URL_USER_REGISTER = "http://49.233.93.71/register.do";
    public final static String URL_USER_PHONE_LOGIN = "http://49.233.93.71/phoneLogin.do";
}
