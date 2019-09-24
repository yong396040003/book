package book.yong.cn.book.pojo;

import java.io.Serializable;
import java.util.Objects;

/**
 * 页面bean
 *
 * @author yong
 * @time 2019/6/12 17:51
 */
public class BookPage implements Serializable {
    //第几章
    private int id;
    //章节名
    private String zjName;
    //书名
    private String bookName;
    //是否为某一章节第一页
    private Boolean isOne;
    //章节内容
    private String content;
    //当前章节第几页
    private int page;
    //第几章
    private int count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZjName() {
        return zjName;
    }

    public void setZjName(String zjName) {
        this.zjName = zjName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Boolean getOne() {
        return isOne;
    }

    public void setOne(Boolean one) {
        isOne = one;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "BookPage{" +
                "id=" + id +
                ", zjName='" + zjName + '\'' +
                ", bookName='" + bookName + '\'' +
                ", isOne=" + isOne +
                ", page=" + page +
                ", count=" + count +
                '}';
    }
}
