package com.jeanboy.simple.recyclerviewhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.jeanboy.recyclerviewhelper.RecyclerViewHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView list_container;

    private List<String> dataList;

    private ListAdapter listAdapter;


    private RecyclerViewHelper loadMoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        list_container = (RecyclerView) findViewById(R.id.list_container);
        dataList = new ArrayList<>();
        listAdapter = new ListAdapter(this, dataList, R.layout.item_list);

        //设置RecyclerView的layoutManager
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        loadMoreHelper = RecyclerViewHelper.build(this, list_container, layoutManager, listAdapter);

        //不传layoutManager默认为LinearLayoutManager
        loadMoreHelper = RecyclerViewHelper.build(this, list_container, listAdapter);

        loadMoreHelper.addLoadMoreListener(new RecyclerViewHelper.LoadMoreCallback() {
            @Override
            public void loadMore() {
                loadNext();
            }
        });

        initData();
    }

    private void loadNext() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            dataList.add(String.valueOf(i));
                        }
                        loadMoreHelper.loadComplete();//通知helper加载完成
                        loadMoreHelper.notifyDataSetChanged();//刷新数据
                        loadMoreHelper.hasNext(true);//是否还有下一页
                    }
                });
            }
        }).start();

    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            dataList.add(String.valueOf(i));
        }
        listAdapter.notifyDataSetChanged();
    }
}
