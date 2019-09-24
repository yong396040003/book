package book.yong.cn.book.jutil;

import java.io.File;

/**
 *清除缓存
 *@author yong
 *@time 2019/9/17 16:09
 *
 */
public class CleanCache {
    /**
     * 获取文件夹大小
     *
     * @param file
     * @return
     */
    public static long getFolderSize(File file) {
        long size = 0;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size / 1024 / 1024;
    }

    /**
     * 清除缓存
     *
     * @param file
     */
    public static void cleanCache(File file) {
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                cleanCache(fileList[i]);
            } else {
                fileList[i].delete();
            }
        }
    }
}
