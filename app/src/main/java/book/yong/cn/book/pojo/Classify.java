package book.yong.cn.book.pojo;

import java.io.Serializable;
import java.util.Objects;

public class Classify implements Serializable {

    /**
     * 名
     */
    private String classifyName;

    /**
     * 书籍总数
     */
    private String classifyCount;

    /**
     * 图片id
     */
    private int classifyImgId;

    public int getClassifyImgIdBig() {
        return classifyImgIdBig;
    }

    public void setClassifyImgIdBig(int classifyImgIdBig) {
        this.classifyImgIdBig = classifyImgIdBig;
    }

    /**
     * 图片id big
     */
    private int classifyImgIdBig;

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getClassifyCount() {
        return classifyCount;
    }

    public void setClassifyCount(String classifyCount) {
        this.classifyCount = classifyCount;
    }

    public int getClassifyImgId() {
        return classifyImgId;
    }

    public void setClassifyImgId(int classifyImgId) {
        this.classifyImgId = classifyImgId;
    }

    @Override
    public String toString() {
        return "Classify{" +
                "classifyName='" + classifyName + '\'' +
                ", classifyCount='" + classifyCount + '\'' +
                ", classifyImgId=" + classifyImgId +
                ", classifyImgIdBig=" + classifyImgIdBig +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Classify classify = (Classify) o;
        return classifyImgId == classify.classifyImgId &&
                classifyImgIdBig == classify.classifyImgIdBig &&
                Objects.equals(classifyName, classify.classifyName) &&
                Objects.equals(classifyCount, classify.classifyCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classifyName, classifyCount, classifyImgId, classifyImgIdBig);
    }
}
