package com.jeanboy.recyclerviewhelper;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jeanboy.recyclerviewhelper.headerandfooter.HeaderAndFooterRecyclerViewAdapter;


/**
 * Created by Next on 2016/8/5.
 */
public class RecyclerViewHelper {

    private Activity activity;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private HeaderAndFooterRecyclerViewAdapter recyclerViewAdapter;

    private LoadMoreListener loadMoreListener;

    public static RecyclerViewHelper build(Activity activity, RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        return new RecyclerViewHelper(activity, recyclerView, null, adapter);
    }

    public static RecyclerViewHelper build(Activity activity, RecyclerView recyclerView,
                                           RecyclerView.LayoutManager layoutManager, RecyclerView.Adapter adapter) {
        return new RecyclerViewHelper(activity, recyclerView, layoutManager, adapter);
    }

    private RecyclerViewHelper(@NonNull Activity activity, @NonNull RecyclerView recyclerView,
                               RecyclerView.LayoutManager layoutManager, @NonNull RecyclerView.Adapter adapter) {
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.adapter = adapter;

        if (layoutManager == null) {//不传layoutManager默认为LinearLayoutManager
            this.layoutManager = new LinearLayoutManager(activity);
        } else {
            this.layoutManager = layoutManager;
        }

        setup();
    }

    private void setup() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    /**
     * 刷新数据
     */
    public void notifyDataSetChanged() {
        if (recyclerViewAdapter != null) {
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 需要加载更多数据
     *
     * @param loadMoreCallback
     */
    public void addLoadMoreListener(final LoadMoreCallback loadMoreCallback) {
        loadMoreListener = new LoadMoreListener(activity, recyclerView) {
            @Override
            public void loadMore() {
                if (loadMoreCallback != null) {
                    loadMoreCallback.loadMore();
                }
            }
        };
        recyclerView.addOnScrollListener(loadMoreListener);
    }

    /**
     * 加载完成
     */
    public void loadComplete() {
        if (loadMoreListener != null) {
            loadMoreListener.loadComplete();
        }
    }

    /**
     * 设置是否还有下一页
     *
     * @param hasNext
     */
    public void hasNext(boolean hasNext) {
        if (loadMoreListener != null) {
            loadMoreListener.hasNext(hasNext);
        }
    }

    /**
     * 设置每一页显示的条数
     *
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        if (loadMoreListener != null) {
            loadMoreListener.setPageSize(pageSize);
        }
    }

    public interface LoadMoreCallback {
        void loadMore();
    }

}
