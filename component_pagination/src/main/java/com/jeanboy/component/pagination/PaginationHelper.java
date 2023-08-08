package com.jeanboy.component.pagination;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.jeanboy.component.pagination.base.HolderLayout;
import com.jeanboy.component.pagination.constants.ViewState;
import com.jeanboy.component.pagination.listener.LoadListener;

/**
 * Created by jianbo on 2023/7/13 17:59.
 */
public class PaginationHelper {

    private final RecyclerView recyclerView;
    private final RecyclerView.Adapter itemAdapter;
    private final PaginationAdapter paginationAdapter;

    public PaginationHelper(@NonNull RecyclerView recyclerView, RecyclerView.Adapter itemAdapter) {
        this.recyclerView = recyclerView;
        this.itemAdapter = itemAdapter;
        this.paginationAdapter = new PaginationAdapter(itemAdapter);

        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        }
        // 设置 item 固定宽高可提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(paginationAdapter);

        addWatcher(recyclerView);
    }

    private void addWatcher(RecyclerView recyclerView) {
        if (recyclerView == null) return;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int offsetY = 0;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (ViewState.LOADING == paginationAdapter.getCurrentState()) return;
                if (paginationAdapter.getDataSize() > 0 && ViewState.EMPTY == paginationAdapter.getCurrentState()) {
                    return;
                }

                if (offsetY >= 0) { // 向上滑动，向后加载
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        int lastPosition = 0;
                        if (layoutManager instanceof LinearLayoutManager) {
                            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                            lastPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                            StaggeredGridLayoutManager gridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                            int[] lastPositions = new int[gridLayoutManager.getSpanCount()];
                            int[] lastVisibleItemPositions = gridLayoutManager.findLastVisibleItemPositions(lastPositions);
                            lastPosition = lastVisibleItemPositions[0];
                            for (int value : lastVisibleItemPositions) {
                                if (value > lastPosition) {
                                    lastPosition = value;
                                }
                            }
                        }

                        if (lastPosition >= layoutManager.getItemCount() - 1) {
                            if (paginationAdapter.getDataSize() == 0 && paginationAdapter.getMaskCount() > 0) return;
                            paginationAdapter.setLoading();
                            LoadListener loadMoreListener = paginationAdapter.getLoadMoreListener();
                            if (loadMoreListener != null) {
                                loadMoreListener.onLoad(false);
                            }
                        }
                    }
                }

//                if (offsetY >= 0) { // 向上滑动，向后加载
//                    int verticalScrollExtent = recyclerView.computeVerticalScrollExtent();
//                    int verticalScrollOffset = recyclerView.computeVerticalScrollOffset();
//                    int verticalScrollRange = recyclerView.computeVerticalScrollRange();
//                    if (verticalScrollExtent + verticalScrollOffset >= verticalScrollRange) {
//                        if (dataList.isEmpty() && getMaskCount() > 0) return;
//                        setLoading();
//                        if (loadMoreListener != null) {
//                            loadMoreListener.onLoad();
//                        }
//                    }
//                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                offsetY = dy;
            }
        });
    }

    public void setLoading() {
        paginationAdapter.setLoading();
    }

    public void setLoadError() {
        paginationAdapter.setLoadError();
    }

    public void setLoadCompleted(boolean hasMore) {
        paginationAdapter.setLoadCompleted(hasMore);
    }

    public void setLoadCompleted(int changeSize, boolean hasMore) {
        paginationAdapter.setLoadCompleted(changeSize, hasMore);
    }

    public void setMaskLayout(HolderLayout layout) {
        paginationAdapter.setMaskLayout(layout);
    }

    public void setLoadMoreLayout(HolderLayout layout) {
        paginationAdapter.setLoadMoreLayout(layout);
    }

    public void addHeader(HolderLayout layout) {
        paginationAdapter.addHeader(layout);
    }

    public void addFooter(HolderLayout layout) {
        paginationAdapter.addFooter(layout);
    }

    public void setRefreshListener(LoadListener refreshListener) {
        paginationAdapter.setRefreshListener(refreshListener);
    }

    public void setLoadMoreListener(LoadListener loadMoreListener) {
        paginationAdapter.setLoadMoreListener(loadMoreListener);
    }
}
