package book.yong.cn.book.jutil;

/**
 * 静态常量类
 */
public class StaticConstant {
    //排行 post
    public final static String URL_PAIHANG = "http://47.105.125.214/selectBookByHits.do";
    //分类 post
    public final static String URL_CLASSIFY = "http://47.105.125.214/selectBook.do";
    //根据书籍编号查询目录 post
    public final static String URL_BOOK_DETAIL  = "http://47.105.125.214/selectBookByNumber.do";
    //根据书号和count获取该章节
    public final static String URL_BOOK_DETAIL_NUMBER_COUNT = "http://47.105.125.214/getCatalog.do";
    //根据编号查询最后一章
    public final static String URL_BOOK_LastDETAIL  = "http://47.105.125.214/getLastCatalog.do";
    //根据url查询章节的内容 post
    public final static String URL_BOOK_BODY = "http://47.105.125.214/selectBookBody.do";
    //images图片存储的文件
    public final static String IMAGES = "/data/data/book.yong.cn.book/files/images/";
    //根据作者查询对应的书籍 get
    public final static String URL_BOOK_AUTHOR = "http://47.105.125.214/selectBookByAuthor.do";
    //根据输入搜索
    public final static String URL_BOOK_Search = "http://47.105.125.214/selectBookLikeAuthorOrName.do";
    //查询每一类书籍数量
    public final static String URL_CLASSIFY_Count = "http://47.105.125.214/getClassifyCount.do";
    //书城
    public final static String URL_BOOK_CITY = "http://47.105.125.214/getBookCityBook.do";
    //章节数量
    public static int ZJCOUNT = 10;

}
