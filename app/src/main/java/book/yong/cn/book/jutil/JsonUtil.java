package book.yong.cn.book.jutil;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import book.yong.cn.book.pojo.Book;

public class JsonUtil {

    /**
     * pojo转成json
     *
     * @return
     */
    public static JSONObject pojoToJson(Object o) {

        if (o instanceof Book) {
            Book book = (Book) o;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", book.getId());
                jsonObject.put("number", book.getNumber());
                jsonObject.put("category", book.getCategory());
                jsonObject.put("name", book.getName());
                jsonObject.put("author", book.getAuthor());
                jsonObject.put("status", book.getStatus());
                jsonObject.put("collection", book.getCollection());
                jsonObject.put("wordNumber", book.getWordNumber());
                jsonObject.put("data", book.getData());
                jsonObject.put("totalHits", book.getTotalHits());
                jsonObject.put("monthlyClicks", book.getMonthlyClicks());
                jsonObject.put("weeklyClicks", book.getWeeklyClicks());
                jsonObject.put("totalRecommendedNumber", book.getTotalRecommendedNumber());
                jsonObject.put("monthlyRecommendedNumber", book.getMonthlyRecommendedNumber());
                jsonObject.put("weekRecommendedNumber", book.getWeekRecommendedNumber());
                jsonObject.put("img", book.getImgUrl());
                jsonObject.put("synopsis", book.getSynopsis());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
        return null;
    }

    /**
     * json to pojo
     *
     * @param json
     * @return
     */
    public static Object jsonToPojo(JSONObject json, Object o) {
        if (o instanceof Book) {
            Book book = new Book();
            try {
                book.setId(json.getInt("id"));
                book.setNumber(json.getInt("number"));
                book.setName(json.getString("name"));
                book.setAuthor(json.getString("author"));
                book.setStatus(json.getString("status"));
                book.setImgUrl(json.getString("img"));
                book.setCategory(json.getString("category"));
                book.setWordNumber(json.getString("wordNumber"));
                book.setData(json.getString("data"));
                String synopsis = json.getString("synopsis");
                synopsis = synopsis.replaceAll("<br />", "");
                synopsis = synopsis.replaceAll(" ", "");
                book.setSynopsis("        " + synopsis);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (book != null) {
                return book;
            } else {
                return null;
            }
        }
        return null;
    }
}
