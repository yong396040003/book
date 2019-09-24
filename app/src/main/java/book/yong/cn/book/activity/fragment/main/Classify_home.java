package book.yong.cn.book.activity.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import book.yong.cn.book.R;
import book.yong.cn.book.activity.ClassifyActivity;
import book.yong.cn.book.activity.fragment.classify.LoadingDate;
import book.yong.cn.book.adapter.ClassifyGridViewAdapter;
import book.yong.cn.book.jutil.FontTextView;
import book.yong.cn.book.jutil.GridSpacingItemDecoration;
import book.yong.cn.book.jutil.Http;
import book.yong.cn.book.jutil.StaticConstant;
import book.yong.cn.book.myInterface.RecyclerViewOnItemClickListener;
import book.yong.cn.book.pojo.Classify;
import butterknife.Bind;
import butterknife.ButterKnife;

public class Classify_home extends Fragment {
    @Bind(R.id.left_text)
    FontTextView leftText;
    @Bind(R.id.middle_text)
    TextView middleText;
    @Bind(R.id.right_text)
    FontTextView rightText;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.main_activity_top_nav)
    AppBarLayout mainActivityTopNav;
    @Bind(R.id.recycler)
    RecyclerView classify_grid;

    //classify列表
    private ListView classify_list;
    //grid列表
    private List<Classify> classifyList;

    private ClassifyGridViewAdapter classifyGridViewAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String fantasy = jsonObject.getString("fantasy");
                    String martial = jsonObject.getString("martial");
                    String city = jsonObject.getString("city");
                    String colleagues = jsonObject.getString("colleagues");
                    String science = jsonObject.getString("science");
                    String online = jsonObject.getString("online");
                    String history = jsonObject.getString("history");
                    String girl = jsonObject.getString("girl");
                    String supperNatural = jsonObject.getString("supperNatural");

                    classifyList.get(0).setClassifyCount(fantasy + "本书");
                    classifyList.get(1).setClassifyCount(martial + "本书");
                    classifyList.get(2).setClassifyCount(city + "本书");
                    classifyList.get(3).setClassifyCount(history + "本书");
                    classifyList.get(4).setClassifyCount(online + "本书");
                    classifyList.get(5).setClassifyCount(science + "本书");
                    classifyList.get(6).setClassifyCount(supperNatural + "本书");
                    classifyList.get(7).setClassifyCount(girl + "本书");
                    classifyList.get(8).setClassifyCount(colleagues + "本书");

                    classifyGridViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        middleText.setText("分类");

        showGrid();

        //开启一个线程 获取分类书籍数量
        MyThread myThread = new MyThread();
        myThread.start();
    }

    /**
     * 分类以list形式显示 （因美观问题 已淘汰）
     */
    private void init() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        //获取资源文件的数组
        final String[] category = getResources().getStringArray(R.array.classifyCategoryList);
        int[] imgId = {R.mipmap.one, R.mipmap.two, R.mipmap.three, R.mipmap.four,
                R.mipmap.five, R.mipmap.six, R.mipmap.seven, R.mipmap.eight, R.mipmap.nine};
        for (int i = 0; i < category.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("img", imgId[i]);
            map.put("category", category[i]);
            map.put("total", "9999 >本");
            mapList.add(map);
        }
        String[] from = {"img", "category", "total"};
        int[] to = {R.id.classify_list_img, R.id.classify_list_category, R.id.classify_list_total};
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), mapList, R.layout.classify_list, from, to);
        classify_list.setAdapter(simpleAdapter);
        classify_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ClassifyActivity.class);
                intent.putExtra("data", category[position]);
                startActivity(intent);
            }
        });
    }

    /**
     * 分类以卡片形式显示
     */
    private void showGrid() {
        int[] imgId = {R.mipmap.one, R.mipmap.two, R.mipmap.three, R.mipmap.four,
                R.mipmap.five, R.mipmap.six, R.mipmap.seven, R.mipmap.eight, R.mipmap.nine};
        int[] imgIdBig = {R.mipmap.one1, R.mipmap.two1, R.mipmap.three1, R.mipmap.four1,
                R.mipmap.five1, R.mipmap.six1, R.mipmap.seven1, R.mipmap.eight1, R.mipmap.nine1};
        final String[] category = getResources().getStringArray(R.array.classifyCategoryList);

        classifyList = new ArrayList<>();
        for (int i = 0; i < imgId.length; i++) {
            Classify classify = new Classify();
            classify.setClassifyCount("999本");
            classify.setClassifyName(category[i]);
            classify.setClassifyImgId(imgId[i]);
            classify.setClassifyImgIdBig(imgIdBig[i]);
            classifyList.add(classify);
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        //设置显示方式垂直或水平
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        //正反
        gridLayoutManager.setReverseLayout(false);

        classify_grid.setLayoutManager(gridLayoutManager);
        classify_grid.addItemDecoration(new GridSpacingItemDecoration(2, 40, true));
        classifyGridViewAdapter = new ClassifyGridViewAdapter(getActivity(), classifyList);
        classify_grid.setAdapter(classifyGridViewAdapter);

        classifyGridViewAdapter.setRecyclerViewOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ClassifyActivity.class);
                intent.putExtra("data", category[position]);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class MyThread extends Thread {
        @Override
        public void run() {
            super.run();

            String json = Http.sendPost(StaticConstant.URL_CLASSIFY_Count, null);
            if (json != null) {
                Message message = new Message();
                message.what = 1;
                message.obj = json;
                mHandler.sendMessage(message);
            }
        }
    }
}
