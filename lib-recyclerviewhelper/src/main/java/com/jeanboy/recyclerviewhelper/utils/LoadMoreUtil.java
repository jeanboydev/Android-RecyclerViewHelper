package com.jeanboy.recyclerviewhelper.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jeanboy.recyclerviewhelper.adapter.HelperAdapter;
import com.jeanboy.recyclerviewhelper.footer.LoadMoreView;

/**
 * Created by Next on 2016/8/11.
 */
public class LoadMoreUtil {


    public static void updateState(RecyclerView recyclerView, final LoadMoreView.State state, View.OnClickListener listener) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null || !(adapter instanceof HelperAdapter)) return;
        HelperAdapter helperAdapter = (HelperAdapter) adapter;

        View footerView = helperAdapter.getFooterView();
        final LoadMoreView loadMoreView;
        if (footerView == null) {
            loadMoreView = new LoadMoreView(recyclerView.getContext());
            helperAdapter.setFooterView(loadMoreView);
        } else {
            loadMoreView = (LoadMoreView) footerView;
        }
        loadMoreView.setState(state);
        if (state == LoadMoreView.State.ERROR) {
            loadMoreView.setOnClickListener(listener);
        }
        recyclerView.scrollToPosition(helperAdapter.getItemCount() - 1);
    }

    public static LoadMoreView.State getState(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null && adapter instanceof HelperAdapter) {
            HelperAdapter helperAdapter = (HelperAdapter) adapter;
            View footerView = helperAdapter.getFooterView();
            if (footerView != null) {
                return ((LoadMoreView) footerView).getState();
            }
        }
        return LoadMoreView.State.NORMAL;
    }
}
