package book.yong.cn.book.pojo;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Bookshelf implements Serializable {
    private int _id;
    private int number;
    private String img;
    private String name;
    private String data;
    private String catalogue;
    private int count;
    private int page;
    private Bitmap bitmap;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(String catalogue) {
        this.catalogue = catalogue;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "Bookshelf{" +
                "_id=" + _id +
                ", number=" + number +
                ", img='" + img + '\'' +
                ", name='" + name + '\'' +
                ", data='" + data + '\'' +
                ", catalogue='" + catalogue + '\'' +
                ", count=" + count +
                ", page=" + page +
                ", bitmap=" + bitmap +
                '}';
    }
}
