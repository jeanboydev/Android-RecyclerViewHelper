package com.jeanboy.recyclerviewhelper;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jeanboy.recyclerviewhelper.headerandfooter.EndlessRecyclerOnScrollListener;
import com.jeanboy.recyclerviewhelper.headerandfooter.utils.RecyclerViewStateUtils;
import com.jeanboy.recyclerviewhelper.headerandfooter.widget.LoadingFooter;


/**
 * Created by Next on 2016/8/5.
 */
public abstract class LoadMoreListener extends EndlessRecyclerOnScrollListener {

    private Activity activity;
    private RecyclerView recyclerView;

    /**
     * 是否有下一页
     */
    private boolean hasNext = true;

    /**
     * 每页显示的条数，默认10条
     */
    private int pageSize = 10;

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void hasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public LoadMoreListener(Activity activity, RecyclerView recyclerView) {
        this.activity = activity;
        this.recyclerView = recyclerView;
    }

    @Override
    public void onLoadNextPage(View view) {
        super.onLoadNextPage(view);

        LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(recyclerView);
        if (state == LoadingFooter.State.Loading) {//加载中不重复加载
            return;
        }

        if (hasNext) {// loading more
            RecyclerViewStateUtils.setFooterViewState(activity, recyclerView, pageSize, LoadingFooter.State.Loading, null);
            loadMore();
        } else {//the end
            RecyclerViewStateUtils.setFooterViewState(activity, recyclerView, pageSize, LoadingFooter.State.TheEnd, null);
        }

    }

    /**
     * 加载完成更新状态
     */
    public void loadComplete() {
        RecyclerViewStateUtils.setFooterViewState(recyclerView, LoadingFooter.State.Normal);
    }

    /**
     * 获取更多数据
     */
    public abstract void loadMore();
}
