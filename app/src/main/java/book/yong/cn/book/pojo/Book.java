package book.yong.cn.book.pojo;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Description:
 * 图书实体类
 * Date: 16:53 2019/1/19
 *
 * @author yong
 * @see
 */
public class Book implements Serializable {
    /**
     * id:主键
     */
    private int id;
    /**
     * number:书号
     */
    private int number;
    /**
     * 小说类别
     */
    private String category;
    /**
     * name:书名
     */
    private String name;
    /**
     * author:作者
     */
    private String author;
    /**
     * status:更新状态
     */
    private String status;
    /**
     * 收藏人数
     */
    private int collection;
    /**
     * wordNumber:字数
     */
    private String wordNumber;
    /**
     * 更新时间
     */
    private String data;
    /**
     * 总点击数
     */
    private int totalHits;
    /**
     * 月点击数
     */
    private int monthlyClicks;
    /**
     * 周点击数
     */
    private int weeklyClicks;
    /**
     * 总推荐数
     */
    private int totalRecommendedNumber;
    /**
     * 月推荐数
     */
    private int monthlyRecommendedNumber;
    /**
     * 周点击数
     */
    private int weekRecommendedNumber;
    /**
     * img:图片地址
     */
    private String imgUrl;
    /**
     * 图片
     */
    private Bitmap img;
    /**
     * synopsis:简介
     */
    private String synopsis;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public String getWordNumber() {
        return wordNumber;
    }

    public void setWordNumber(String wordNumber) {
        this.wordNumber = wordNumber;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public int getMonthlyClicks() {
        return monthlyClicks;
    }

    public void setMonthlyClicks(int monthlyClicks) {
        this.monthlyClicks = monthlyClicks;
    }

    public int getWeeklyClicks() {
        return weeklyClicks;
    }

    public void setWeeklyClicks(int weeklyClicks) {
        this.weeklyClicks = weeklyClicks;
    }

    public int getTotalRecommendedNumber() {
        return totalRecommendedNumber;
    }

    public void setTotalRecommendedNumber(int totalRecommendedNumber) {
        this.totalRecommendedNumber = totalRecommendedNumber;
    }

    public int getMonthlyRecommendedNumber() {
        return monthlyRecommendedNumber;
    }

    public void setMonthlyRecommendedNumber(int monthlyRecommendedNumber) {
        this.monthlyRecommendedNumber = monthlyRecommendedNumber;
    }

    public int getWeekRecommendedNumber() {
        return weekRecommendedNumber;
    }

    public void setWeekRecommendedNumber(int weekRecommendedNumber) {
        this.weekRecommendedNumber = weekRecommendedNumber;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", number=" + number +
                ", category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", status='" + status + '\'' +
                ", collection=" + collection +
                ", wordNumber='" + wordNumber + '\'' +
                ", data='" + data + '\'' +
                ", totalHits=" + totalHits +
                ", monthlyClicks=" + monthlyClicks +
                ", weeklyClicks=" + weeklyClicks +
                ", totalRecommendedNumber=" + totalRecommendedNumber +
                ", monthlyRecommendedNumber=" + monthlyRecommendedNumber +
                ", weekRecommendedNumber=" + weekRecommendedNumber +
                ", imgUrl='" + imgUrl + '\'' +
                ", img=" + img +
                ", synopsis='" + synopsis + '\'' +
                '}';
    }
}
