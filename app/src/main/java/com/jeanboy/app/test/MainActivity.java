package com.jeanboy.app.test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.jeanboy.app.test.layout.MaskLayout;
import com.jeanboy.component.pagination.PaginationHelper;
import com.jeanboy.component.pagination.layout.FooterLayout;
import com.jeanboy.component.pagination.listener.LoadListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView list_container;
    private final List<String> dataList = new ArrayList<>();
    private ListAdapter listAdapter;
    private PaginationHelper paginationHelper;
    private int loadCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_container = findViewById(R.id.list_container);

        listAdapter = new ListAdapter(dataList);

        // 使用 helper 实现分页加载和加载中的提示
        paginationHelper = new PaginationHelper(list_container, listAdapter);
        // 设置加载中 View
        paginationHelper.setMaskLayout(new MaskLayout());
        // 设置加载更多 View
        paginationHelper.setLoadMoreLayout(new FooterLayout());

        // 设置刷新的接口
        paginationHelper.setRefreshListener(new LoadListener() {
            @Override
            public void onLoad(boolean isError) {
                if (isError) {
                    toLoadData();
                } else {
                    toLoadData();
                }
            }
        });

        // 设置加载更多的接口
        paginationHelper.setLoadMoreListener(new LoadListener() {
            @Override
            public void onLoad(boolean isError) {
                toLoadMore();
            }
        });

        toLoadData();
    }

    private void toLoadMore() {
        paginationHelper.setLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadCount % 2 != 0) {
                            // 分页数据加载失败
                            paginationHelper.setLoadError();
                        } else if (loadCount < 7) {
                            for (int i = 0; i < 3; i++) {
                                dataList.add(String.valueOf(i));
                            }
                            // 分页数据加载成功，还有下一页
                            paginationHelper.setLoadCompleted(true);
                        } else {
                            // 分页数据加载成功，没有更多，即全部加载完成
                            paginationHelper.setLoadCompleted(false);
                        }
                        loadCount++;
                    }
                });
            }
        }).start();
    }

    private void toLoadData() {
        dataList.clear();
        paginationHelper.setLoading();
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
                            // 首次数据记载失败
                            paginationHelper.setLoadError();
                        } else if (loadCount == 1) {
                            // 首次加载数据成功
                            paginationHelper.setLoadCompleted(true);
                        } else {
                            for (int i = 0; i < 2; i++) {
                                dataList.add(String.valueOf(i));
                            }
                            paginationHelper.setLoadCompleted(true);
                        }
                        loadCount++;
                    }
                });
            }
        }).start();
    }
}