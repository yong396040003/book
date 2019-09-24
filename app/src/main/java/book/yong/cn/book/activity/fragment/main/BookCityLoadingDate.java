package book.yong.cn.book.activity.fragment.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import book.yong.cn.book.jutil.Http;
import book.yong.cn.book.jutil.JsonUtil;
import book.yong.cn.book.pojo.Book;

/**
 * 书城数据加载
 *
 * @author yong
 * @time 2019/8/18 18:29
 */
public class BookCityLoadingDate {
    private Context context;
    private List<Book> books;
    private Handler mHandler;
    private String parameter;
    private String URL;
    public volatile Boolean exit = false;

    public BookCityLoadingDate(Context context, String URL, Handler mHandler, List<Book> books, String parameter) {
        this.books = books;
        this.mHandler = mHandler;
        this.parameter = parameter;
        this.context = context;
        this.URL = URL;
    }

    public void loadingDate() {
        BookCityLoadingDate.MyRunnable myRunnable = new BookCityLoadingDate.MyRunnable();
        BookCityLoadingDate.MyRunnable1 myRunnable1 = new BookCityLoadingDate.MyRunnable1();

        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(1);
        cachedThreadPool.execute(myRunnable);
        cachedThreadPool.execute(myRunnable1);
    }

    public class MyRunnable implements Runnable {
        @Override
        public void run() {
            int what = 0;
            if (null == books || books.size() == 0) {
                books = new ArrayList<>();
            } else {
                what = 1;
            }
            Message message = new Message();
            try {
                String jsonString = Http.sendPost(URL, parameter);
                if (jsonString != null) {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray newBook = jsonObject.getJSONArray("newBook");
                    JSONArray hotBook = jsonObject.getJSONArray("hotBook");
                    JSONArray myBook = jsonObject.getJSONArray("myBook");
                    JSONArray endBook = jsonObject.getJSONArray("endBook");
                    if (newBook.length() == 0 || myBook.length() == 0 || endBook.length() == 0 || hotBook.length() == 0) {
                        message.what = what;
                        mHandler.sendMessage(message);
                        return;
                    }

                    addBook(hotBook);
                    addBook(myBook);
                    addBook(newBook);
                    addBook(endBook);

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
        }
    }

    private void addBook(JSONArray newBook) throws JSONException {
        for (int i = 0; i < newBook.length(); i++) {
            Book book = new Book();
            String str = newBook.get(i).toString();
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
    }

    public class MyRunnable1 implements Runnable {
        public void run() {
            int i = 0;
            if (books != null) {
                while (i < books.size() && !exit) {
                    if (books.get(i).getImg() == null) {
                        Bitmap bitmap = Http.getRemoteImage(books.get(i).getImgUrl());
                        if (bitmap != null && context != null && books != null) {
                            books.get(i).setImg(bitmap);
                            File file = new File(context.getCacheDir().toString() + "/img/" + books.get(i).getNumber() + ".jpg");
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
                    i++;
                }
            }
        }
    }
}
