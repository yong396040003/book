package book.yong.cn.book.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import book.yong.cn.book.jutil.Http;
import book.yong.cn.book.jutil.JsonUtil;
import book.yong.cn.book.jutil.StaticConstant;
import book.yong.cn.book.pojo.Book;

/**
 * 书籍详情页面
 */
public class BookDetailFragmentModel {

    /**
     * 根据作者查询其对应的书籍
     *
     * @param author
     * @return
     */
    public static List<Book> getAuthorBook(String author) throws JSONException {
        String jsonBook = Http.sendGet(StaticConstant.URL_BOOK_AUTHOR + "?author=" + author);
        if (jsonBook != null) {
            JSONObject jsonObject = new JSONObject(jsonBook);
            JSONArray array = jsonObject.getJSONArray("author");
            if (array.length() == 0) {
                return null;
            } else {
                List<Book> bookList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    Book book = new Book();
                    String str = array.get(i).toString();
                    book = (Book) JsonUtil.jsonToPojo(new JSONObject(str), book);
                    bookList.add(book);
                }
                return bookList;
            }
        } else {
            return null;
        }
    }
}
