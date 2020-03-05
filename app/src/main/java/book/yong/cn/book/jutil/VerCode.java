package book.yong.cn.book.jutil;

import java.util.Random;

/**
 * 验证码工具类
 *
 * @author yong
 * @since 1.0.0
 */
public class VerCode {
    /**
     * 随机在字典中取验证码
     */
    private static final Random random = new Random();
    /**
     * 单例模式
     */
    private static VerCode verCode;
    /**
     * 验证码的宽和高
     */
    private int width, height;
    /**
     * 验证码字典
     */
    private String code;
    /**
     * 验证码个数
     */
    private int num;

    /**
     * 构造私有
     */
    private VerCode() {
        code = "0123456789qwertyuiopasdfghjklzxcvbnm";
        num = 6;
    }

    /**
     * 单列模式获取类对象
     *
     * @return
     */
    public static VerCode getInstance() {
        if (verCode == null) {
            verCode = new VerCode();
        }
        return verCode;
    }

    public void set(int width, int height, String code, int num) {
        this.width = width;
        this.height = height;
        this.code = code;
        this.num = num;
    }

    public void set(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    /**
     * 随机生成验证码字符
     *
     * @return
     */
    public String generationCheckCode() {
        StringBuffer cc = new StringBuffer();
        for (int i = 0; i < num; i++) {
            cc.append(code.charAt(random.nextInt(code.length())));
        }
        return cc.toString();
    }
}
