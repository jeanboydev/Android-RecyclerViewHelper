package com.jeanboy.simple.recyclerviewhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.jeanboy.recyclerviewhelper.RecyclerViewHelper;
import com.jeanboy.recyclerviewhelper.listener.LoadMoreListener;
import com.jeanboy.recyclerviewhelper.listener.TipsListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView list_container;

    private List<String> dataList;

    private ListAdapter listAdapter;


    private RecyclerViewHelper recyclerViewHelper;

    private int loadCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        list_container = (RecyclerView) findViewById(R.id.list_container);
        dataList = new ArrayList<>();

        listAdapter = new ListAdapter(dataList);

        //使用helper实现分页加载和加载的Tips
        recyclerViewHelper = new RecyclerViewHelper(list_container, listAdapter);

        //设置没有数据的Tips
        recyclerViewHelper.setTipsEmptyView(R.layout.view_data_empty);
        //设置加载中的Tips
        recyclerViewHelper.setTipsLoadingView(R.layout.view_data_loading);
        //设置加载失败的Tips
        recyclerViewHelper.setTipsErrorView(R.layout.view_data_error);

        //加载失败，没有数据时Tips的接口
        recyclerViewHelper.setTipsListener(new TipsListener() {
            @Override
            public void retry() {
                initData();
            }
        });

        //设置header
//        recyclerViewHelper.setHeaderView(R.layout.view_header);

        //加载更多的接口
        recyclerViewHelper.setLoadMoreListener(new LoadMoreListener() {
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
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadCount % 2 != 0) {
                            //分页数据加载失败
                            recyclerViewHelper.loadMoreError();
                        } else if (loadCount < 7) {
                            for (int i = 0; i < 10; i++) {
                                dataList.add(String.valueOf(i));
                            }
                            //分页数据加载成功，还有下一页
                            recyclerViewHelper.loadMoreFinish(true);
                        } else {
                            //分页数据加载成功，没有更多。即全部加载完成
                            recyclerViewHelper.loadMoreFinish(false);
                        }

                        loadCount++;
                    }
                });
            }
        }).start();

    }

    private void initData() {
        dataList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadCount == 0) {
                            //首次加载数据成功
                            recyclerViewHelper.loadTipsComplete();
                        } else if (loadCount == 1) {
                            //首次数据记载失败
                            recyclerViewHelper.loadTipsError();
                        } else {
                            for (int i = 0; i < 10; i++) {
                                dataList.add(String.valueOf(i));
                            }
                            recyclerViewHelper.loadTipsComplete();
                        }
                        loadCount++;
                    }
                });
            }
        }).start();
    }
}
