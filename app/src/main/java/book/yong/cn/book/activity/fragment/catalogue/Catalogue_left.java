package book.yong.cn.book.activity.fragment.catalogue;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import book.yong.cn.book.R;
import book.yong.cn.book.activity.BookPageActivity;
import book.yong.cn.book.activity.BookPageActivity1;
import book.yong.cn.book.jutil.Http;
import book.yong.cn.book.jutil.StaticConstant;
import book.yong.cn.book.pojo.Catalogue_bean;

@SuppressLint("ValidFragment")
public class Catalogue_left extends Fragment implements AdapterView.OnItemClickListener {
    private String json;
    private String bookName;

    private ListView listView;
    private SimpleAdapter simpleAdapter;

    private List<Map<String, Object>> mapList;
    private List<Catalogue_bean> catalogue_beans;

    private String jsonString;
    private Handler mHandler;

    private View loading;
    private ViewGroup viewGroup;


    @SuppressLint("ValidFragment")
    Catalogue_left(String json,String bookName) {
        this.json = json;
        this.bookName = bookName;
    }

    public static Catalogue_left getInstance(String json,String bookName) {
        return new Catalogue_left(json,bookName);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.catalogue_list, container, false);
        loading = inflater.inflate(R.layout.dialog_loading, container, false);
        TextView textView = loading.findViewById(R.id.loading_text);
        textView.setText("章节过多，请稍后...");
        viewGroup.addView(loading);
        init();
        return viewGroup;
    }

    private void init() {
        listView = viewGroup.findViewById(R.id.catalogue_list);

        //获取章节
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (json != null) {
                    jsonString = Http.sendPost(StaticConstant.URL_BOOK_DETAIL, "bookNumber=" + json);
                    Message message = new Message();
                    message.obj = jsonString;
                    mHandler.sendMessage(message);
                }
            }
        }).start();

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.obj != null) {
                    updateCatalogue((String) msg.obj);
                }
            }
        };
    }

    public void updateCatalogue(String jsonString) {
        if (getContext() == null){
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("catalogueList");
            mapList = new ArrayList<>();
            catalogue_beans = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                Catalogue_bean catalogue = new Catalogue_bean();
                Map<String, Object> map = new HashMap<>();
                JSONObject json = (JSONObject) jsonArray.get(i);

                catalogue.setNumber(json.getInt("number"));
                catalogue.setName(json.getString("name"));
                catalogue.setCount(json.getInt("count"));
                catalogue.setUrl(json.getString("url"));

                catalogue_beans.add(catalogue);

                map.put("name", json.get("name"));
                map.put("cache", "");
                mapList.add(map);
            }
            viewGroup.removeView(loading);
            String[] from = {"name", "cache"};
            int[] to = {R.id.catalogue_name, R.id.catalogue_cache};

            simpleAdapter = new SimpleAdapter(getContext(), mapList, R.layout.catalogue_list_adapter, from, to);
            listView.setAdapter(simpleAdapter);

            listView.setOnItemClickListener(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始位置
     */
    public void sort_d() {
        listView.setSelection(0);
    }

    /**
     * 末尾
     */
    public void sort_u() {
        listView.setSelection(mapList.size());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Catalogue_bean catalogueBean = catalogue_beans.get(position);
        String bookNumber = String.valueOf(catalogueBean.getNumber());
        Intent intent = new Intent(getActivity(), BookPageActivity.class);
        intent.putExtra("page", 0);
        intent.putExtra("count", catalogueBean.getCount());
        intent.putExtra("catalogueList", bookNumber);
        intent.putExtra("bookName", bookName);
        startActivity(intent);
        getActivity().finish();
    }
}
