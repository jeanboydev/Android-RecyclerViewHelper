package com.jeanboy.recyclerviewhelper;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jeanboy.recyclerviewhelper.adapter.HelperAdapter;
import com.jeanboy.recyclerviewhelper.footer.LoadMoreView;
import com.jeanboy.recyclerviewhelper.listener.LoadMoreListener;
import com.jeanboy.recyclerviewhelper.listener.RecyclerViewScrollListener;
import com.jeanboy.recyclerviewhelper.listener.TipsListener;
import com.jeanboy.recyclerviewhelper.utils.LoadMoreUtil;

/**
 * Created by Next on 2016/8/10.
 */
public class RecyclerViewHelper {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private HelperAdapter helperAdapter;
    private LoadMoreListener loadMoreListener;

    private boolean hasMore = true;
    private boolean isLoading = false;


    public RecyclerViewHelper(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        this(recyclerView, null, adapter);
    }

    public RecyclerViewHelper(@NonNull RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager,
                              @NonNull RecyclerView.Adapter adapter) {
        this.recyclerView = recyclerView;
        this.layoutManager = layoutManager;
        this.adapter = adapter;

        if (layoutManager == null) {//不传layoutManager默认为LinearLayoutManager
            this.layoutManager = new LinearLayoutManager(recyclerView.getContext());
        } else {
            this.layoutManager = layoutManager;
        }

        setup();
    }

    private void setup() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);//设置item固定宽高可提高性能
        helperAdapter = new HelperAdapter(adapter);
        recyclerView.setAdapter(helperAdapter);

        recyclerView.addOnScrollListener(new RecyclerViewScrollListener() {
            @Override
            public void loadMore(RecyclerView recyclerView) {

                if (isLoading || adapter.getItemCount() == 0)
                    return;

                if (hasMore) {//设置footer为加载中...
                    if (loadMoreListener != null) {
                        LoadMoreUtil.updateState(recyclerView, LoadMoreView.State.LOADING, null);
                        isLoading = true;
                        loadMoreListener.loadMore();
                    }
                } else {//设置footer为加载完成
                    LoadMoreUtil.updateState(recyclerView, LoadMoreView.State.NO_MORE, null);
                }
            }
        });
    }


    public RecyclerViewHelper setTipsEmptyView(int layoutId) {
        if (helperAdapter != null) {
            helperAdapter.setTipsEmptyView(layoutId);
        }
        return this;
    }

    public RecyclerViewHelper setTipsLoadingView(int layoutId) {
        if (helperAdapter != null) {
            helperAdapter.setTipsLoadingView(layoutId);
        }
        return this;
    }

    public RecyclerViewHelper setTipsErrorView(int layoutId) {
        if (helperAdapter != null) {
            helperAdapter.setTipsErrorView(layoutId);
        }
        return this;
    }

    public RecyclerViewHelper setHeaderView(int layoutId) {
        if (helperAdapter != null) {
            helperAdapter.setHeaderView(layoutId);
        }
        return this;
    }


    public void loadTipsComplete() {
        if (helperAdapter != null) {
            helperAdapter.loadTipsComplete();
        }
    }

    /**
     * 加载失败
     */
    public void loadTipsError() {
        if (helperAdapter != null) {
            helperAdapter.loadTipsError();
        }
    }


    public void loadMoreFinish(boolean hasMore) {
        this.hasMore = hasMore;
        this.isLoading = false;
        if (!hasMore) {
            LoadMoreUtil.updateState(recyclerView, LoadMoreView.State.NO_MORE, null);
        }
        if (helperAdapter != null) {
            helperAdapter.notifyDataSetChanged();
        }
    }

    public void loadMoreError() {//设置footer为加载失败
        this.hasMore = true;
        this.isLoading = false;
        LoadMoreUtil.updateState(recyclerView, LoadMoreView.State.ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadMoreUtil.updateState(recyclerView, LoadMoreView.State.LOADING, null);
                if (loadMoreListener != null) {
                    loadMoreListener.loadMore();
                }
            }
        });
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setTipsListener(TipsListener tipsListener) {
        if (helperAdapter != null) {
            helperAdapter.setTipsListener(tipsListener);
        }
    }
}
