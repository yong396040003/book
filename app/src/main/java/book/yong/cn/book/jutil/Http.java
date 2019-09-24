package book.yong.cn.book.jutil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http请求工具类
 * 作用向web服务器发送请求（get,post）
 * 所用类库HttpURLConnection(v 6.0)
 */
public class Http {
    /**
     * 发送get请求
     * 返回数据String
     *
     * @return
     */
    public static String sendGet(String urlString) {
        String result = null;
        try {
            //urlString 转为 url
            //1.找水源
            URL url = new URL(urlString);
            //2.开水闸 openConnection
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //3.建管道 InputStream(字节流)
                InputStream inputStream = httpURLConnection.getInputStream();
                //4.建蓄水池蓄水 InputStreamReader(字符流)
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                //5.水桶盛水 BufferedReader(缓冲字符流)
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();
                String str = null;

                while ((str = bufferedReader.readLine()) != null) {
                    //取水 若不为空将一直取
                    stringBuffer.append(str);
                }

                httpURLConnection.disconnect();
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();

                result = stringBuffer.toString();
            } else if (httpURLConnection.getResponseCode() > 400) {
                return String.valueOf(httpURLConnection.getErrorStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送post请求
     * 返回数据String
     *
     * @return
     */
    public static String sendPost(String urlString, String parameter) {
        String result = null;
        try {
            //urlString 转为 url
            //1.找水源
            URL url = new URL(urlString);
            //2.开水闸 openConnection
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            //和get的区别
            //1.需要设置请求方式
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            if (parameter != null) {
                //url连接进行键输入（通俗一点就是设置为true才能设置请求参数）
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(parameter.getBytes());
                outputStream.close();
            }

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //3.建管道 InputStream(字节流)
                InputStream inputStream = httpURLConnection.getInputStream();
                //4.建蓄水池蓄水 InputStreamReader(字符流)
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                //5.水桶盛水 BufferedReader(缓冲字符流)
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();
                String str = null;

                while ((str = bufferedReader.readLine()) != null) {
                    //取水 若不为空将一直取
                    stringBuffer.append(str);
                }

                httpURLConnection.disconnect();
                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();

                result = stringBuffer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap getRemoteImage(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                bufferedInputStream.close();
                httpURLConnection.disconnect();
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

