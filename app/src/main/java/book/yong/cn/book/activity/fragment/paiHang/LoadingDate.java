package book.yong.cn.book.activity.fragment.paiHang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import book.yong.cn.book.jutil.Http;
import book.yong.cn.book.jutil.JsonUtil;
import book.yong.cn.book.jutil.StaticConstant;
import book.yong.cn.book.pojo.Book;

/**
 * 排行加载数据共用方法
 *
 * @author yong
 * @time 2019/8/9 9:29
 */
public class LoadingDate {
    private Context context;
    private Handler mHandler;

    private String HITS;

    public LoadingDate(Context context, Handler mHandler, String HITS) {
        this.context = context;
        this.mHandler = mHandler;
        this.HITS = HITS;
    }


    public void loadingDate() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        List<Book> books = null;
                        String jsonString = Http.sendPost(StaticConstant.URL_PAIHANG, "hits=" + HITS);
                        Message message = new Message();
                        if (jsonString != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(jsonString);
                                books = new ArrayList<>();
                                JSONArray array = jsonObject.getJSONArray("paiHang");
                                for (int i = 0; i < array.length(); i++) {
                                    Book book = new Book();
                                    String str = array.get(i).toString();
                                    JSONObject json = new JSONObject(str);

                                    book = (Book) JsonUtil.jsonToPojo(json, book);

                                    File file = new File(context.getCacheDir().toString() + "/img/" + book.getNumber() + ".jpg");
                                    if (file.exists()) {
                                        book.setImg(BitmapFactory.decodeFile(context.getCacheDir().toString() + "/img/" + book.getNumber() + ".jpg"));
                                    }

                                    if (book != null) {
                                        books.add(book);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            message.obj = books;
                            message.arg1 = 1;
                            mHandler.sendMessage(message);
                        } else {
                            message.obj = null;
                            message.arg1 = 1;
                            mHandler.sendMessage(message);
                        }

                        final List<Book> finalBooks = books;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (finalBooks != null) {
                                    for (int i = 0; i < finalBooks.size(); i++) {
                                        if (finalBooks.get(i).getImg() == null) {
                                            Bitmap bitmap = Http.getRemoteImage(finalBooks.get(i).getImgUrl());
                                            if (bitmap != null) {
                                                finalBooks.get(i).setImg(bitmap);
                                                File file = new File(context.getCacheDir().toString() + "/img/" + finalBooks.get(i).getNumber() + ".jpg");
                                                if (!file.exists()) {
                                                    try {
                                                        file.createNewFile();
                                                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

                                                        fileOutputStream.flush();
                                                        fileOutputStream.close();

                                                        Message message = new Message();
                                                        message.obj = bitmap;
                                                        message.arg1 = 2;
                                                        message.arg2 = i;
                                                        mHandler.sendMessage(message);

                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }).start();

                    }
                }
        ).start();
    }
}
