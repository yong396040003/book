package book.yong.cn.book.jutil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Description:MD5加密
 * Date: 16:09 2018/10/23
 *
 * @author yong
 * @see
 */
public class MD5 {

    /**
     * 16位，十六进制
     */
    final static char chars[] = {'0','1','2','3','4','5',
            '6','7','8','9','A','B','C','D','E','F'};
    /**
     * 自己的密令
     */
    final static String MI_LIN = "yong666yong";

    /**
     * MD5加密后返回字符串
     *
     * @param s
     * @return
     */
    public static String MD5toString(String s) {

        //对传进来的字符串进行加密
        String str = s + MI_LIN;
        //字符串转为二进制字节
        byte bytes[] = str.getBytes();

        try {
            MessageDigest messageDigest;
            messageDigest = MessageDigest.getInstance("MD5");
            // 通过使用 update 方法处理数据,使指定的 byte数组更新摘要
            messageDigest.update(bytes);
            // 获得密文完成哈希计算,产生128 位的长整数
            byte md[] = messageDigest.digest();

            int j = md.length;
            // 每个字节用 16 进制表示的话，使用两个字符
            char strs[] = new char[j*2];
            int count = 0;
            // 从第一个字节开始，对每一个字节,转换成 16 进制字符的转换
            for(int i = 0; i < j; i++){
                byte byte0 = md[i];
                // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
                strs[count++] = chars[byte0 >>> 4 & 0xf];
                // 取字节中低 4 位的数字转换
                strs[count++] = chars[byte0 & 0xf];
            }
            String s1 = new String(strs);
            //截取字符串
            String s2 = s1.substring(5,15);
            //返回加密后的字符串
            return s2;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }







///**
//// *测试一下
//// */
////    public static void main(String[] args){
////        String m = "12345";
////        System.out.println(MD5.MD5toString(m));
////
////    }
}
