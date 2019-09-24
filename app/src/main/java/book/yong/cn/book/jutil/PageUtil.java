package book.yong.cn.book.jutil;

import android.util.Log;

import java.util.regex.Pattern;

public class PageUtil {

    /**
     * @param body      文章内容
     * @param lines     文章行数
     * @param width     手机宽度
     * @param textWidth 每个字体的宽度
     * @return
     */
    public final static String pageContent(String body, int lines, int width, float textWidth) {
        //每行最多显示的字数
        int textLength = (int) (width / textWidth) - 1;
        int curLine = 0;
        //判断每行最后一个字后面是否有标点
        int count;

        String regex = "[A-z\\u4e00-\\u9fa5]";

        String[] bodys = body.split("\n");

        Boolean isEnd;

        //每页显示的内容
        StringBuffer content = new StringBuffer();

        //遍历所有段落
        for (int i = 0; i < bodys.length; i++) {
            //如果当前段落没超过每行限定字数，则全显示到一行（注意前面有两个空格）
            if (bodys[i].length() <= textLength) {
                content.append(bodys[i]);
                if (i + 1 < bodys.length) {
                    content.append("\n\n");
                }
                curLine = curLine + 2;
                if (curLine >= lines) {
                    return content.toString();
                }
            } else { //如果字数超过上限，那么就换到下一行
                //mLines 该段落显示的行数
                float m = bodys[i].length() % textLength;
                int mLines = bodys[i].length() / textLength;
                if (m > 0) {
                    mLines += 1;
                }

                count = 0;
                for (int j = 0; j < mLines; j++) {
                    if (j == 0) {//第一行
                        String c = bodys[i].substring(0, textLength);
                        content.append(c);
                        isEnd = false;
                        curLine++;
                    } else if (j == mLines - 1) {//最后一行
                        String c = bodys[i].substring(textLength * j, bodys[i].length());
                        content.append(c);
                        if (textLength - c.length() >= count && (curLine + 2) >= lines) {
                            count = 0;
                        }
                        isEnd = true;
                        curLine = curLine + 2;
                    } else {
                        String c = bodys[i].substring(textLength * j, textLength * (j + 1));
                        content.append(c);
                        //判断是否最后一个字后面是否有标点符号
                        String str = c;
                        c = c.substring(textLength - 1, textLength);
                        String c1 = str.substring(0, 1);
                        Boolean isCount = Pattern.matches(regex, c);
                        //判断是否第一个字前面是否有标点符号
                        Boolean isCount1 = Pattern.matches(regex, c1);
                        if (!isCount && !isCount1) {
                            count += 2;
                        } else if (!isCount1) {
                            count++;
                        } else if (!isCount) {
                            if (count > 0) {
                                count++;
                            }
                        }
                        isEnd = false;
                        curLine++;
                    }
                    if (curLine >= lines) {
                        if (count > 0) {
                            return content.toString().substring(0, content.toString().length() - count);
                        } else {
                            if (isEnd && i + 1 < bodys.length) {
                                return content.toString() + "\n\n";
                            } else {
                                return content.toString();
                            }
                        }
                    }
                }
                //最后一页
                if (i + 1 == bodys.length) {
                    return content.toString();
                }
                content.append("\n\n");
            }

            //最后一页 防止未显示指定行数而导致出错
            if (i + 1 == bodys.length) {
                return content.toString();
            }
        }

        return null;
    }

}
