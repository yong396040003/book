package book.yong.cn.book.pojo;

import java.io.Serializable;

/**
 * Description:
 * 图书目录实体类
 * Date: 21:39 2019/1/20
 *
 * @author yong
 * @see
 */
public class Catalogue_bean implements Serializable {
    /**
     * id:主键
     */
    private int id;
    /**
     * number:书号
     */
    private int number;
    /**
     * 章节名
     */
    private String name;
    /**
     * 章节名索引，加快查询速度
     */
    private int count;
    /**
     * 章节地址
     */
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "CatalogueActivity{" +
                "id=" + id +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", url='" + url + '\'' +
                '}';
    }
}
