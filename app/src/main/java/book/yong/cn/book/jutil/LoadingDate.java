package book.yong.cn.book.jutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import book.yong.cn.book.pojo.Book;

/**
 * 加载数据
 *
 * @author yong
 * @time 2019/8/9 9:48
 */
public class LoadingDate {
    private Context context;
    private List<Book> books;
    private Handler mHandler;
    private String parameter;
    private String URL;
    public volatile Boolean exit = false;

    public LoadingDate(Context context, String URL, Handler mHandler, List<Book> books, String parameter) {
        this.books = books;
        this.mHandler = mHandler;
        this.parameter = parameter;
        this.context = context;
        this.URL = URL;
    }

    public void loadingDate() {
        LoadingDate.MyRunnable myRunnable = new LoadingDate.MyRunnable();
        LoadingDate.MyRunnable1 myRunnable1 = new LoadingDate.MyRunnable1();

        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(1);
        cachedThreadPool.execute(myRunnable);
        cachedThreadPool.execute(myRunnable1);
    }

    public class MyRunnable implements Runnable {
        @Override
        public void run() {
            if (!exit) {
                int what = 0;
                if (null == books || books.size() == 0) {
                    books = new ArrayList<>();
                } else {
                    what = 1;
                }
                Message message = new Message();
                try {
                    String jsonString = Http.sendGet(URL + parameter);
                    if (jsonString != null) {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        JSONArray array = jsonObject.getJSONArray("data");
                        if (array.length() == 0) {
                            message.what = what;
                            mHandler.sendMessage(message);
                            return;
                        }
                        for (int i = 0; i < array.length(); i++) {
                            Book book = new Book();
                            String str = array.get(i).toString();
                            JSONObject json = new JSONObject(str);

                            book = (Book) JsonUtil.jsonToPojo(json, book);

                            File file = new File(context.getCacheDir().toString() + "/img/" + book.getNumber() + ".jpg");
                            if (file.exists()) {
                                book.setImg(BitmapFactory.decodeFile(context.getCacheDir().toString() + "/img/" + book.getNumber() + ".jpg"));
                            }

                            if (book.getNumber() > 0) {
                                books.add(book);
                            }
                        }
                        message.obj = books;
                        message.what = what;
                        mHandler.sendMessage(message);
                    } else {
                        message.what = what;
                        mHandler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("success", "终止线程1");
            }
        }
    }

    public class MyRunnable1 implements Runnable{
        public void run() {
            int i = 0;
            while (i < books.size() && !exit && books != null) {
                if (books.get(i).getImg() == null) {
                    Bitmap bitmap = Http.getRemoteImage(books.get(i).getImgUrl());
                    if (bitmap != null && context != null && books != null) {
                        books.get(i).setImg(bitmap);
                        File file = new File(context.getCacheDir().toString() + "/img/" + books.get(i).getNumber() + ".jpg");
                        i++;
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

                                fileOutputStream.flush();
                                fileOutputStream.close();

                                Message msg = new Message();
                                msg.obj = books;
                                msg.what = 1;
                                mHandler.sendMessage(msg);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
