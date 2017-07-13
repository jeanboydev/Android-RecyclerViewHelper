package com.jeanboy.recyclerviewhelper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jeanboy.recyclerviewhelper.adapter.HelperAdapter;
import com.jeanboy.recyclerviewhelper.adapter.ViewType;
import com.jeanboy.recyclerviewhelper.footer.FooterState;
import com.jeanboy.recyclerviewhelper.footer.LoadMoreView;
import com.jeanboy.recyclerviewhelper.listener.LoadMoreListener;
import com.jeanboy.recyclerviewhelper.listener.OnFooterChangeListener;
import com.jeanboy.recyclerviewhelper.listener.OnViewBindListener;
import com.jeanboy.recyclerviewhelper.listener.RecyclerViewScrollListener;
import com.jeanboy.recyclerviewhelper.listener.TipsListener;

/**
 * Created by jeanboy on 2017/7/4.
 */

public class RecyclerViewHelper {

    private final RecyclerView recyclerView;
    private final RecyclerView.Adapter itemAdapter;
    private final RecyclerView.LayoutManager layoutManager;

    private HelperAdapter helperAdapter;
    private LoadMoreListener loadMoreListener;

    private boolean hasMore = true;
    private boolean isLoading = false;
    private boolean isUseDefaultFooter = false;
    private int footerState = FooterState.NORMAL;

    public RecyclerViewHelper(RecyclerView recyclerView, RecyclerView.Adapter itemAdapter) {
        this(recyclerView, itemAdapter, null);
    }

    public RecyclerViewHelper(RecyclerView recyclerView, RecyclerView.Adapter itemAdapter,
                              RecyclerView.LayoutManager layoutManager) {
        this.recyclerView = recyclerView;
        this.itemAdapter = itemAdapter;

        if (layoutManager == null) {
            this.layoutManager = new LinearLayoutManager(recyclerView.getContext());
        } else {
            this.layoutManager = layoutManager;
        }

        setup();
    }

    private void setup() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);//设置item固定宽高可提高性能
        helperAdapter = new HelperAdapter(itemAdapter);
        recyclerView.setAdapter(helperAdapter);

        recyclerView.addOnScrollListener(new RecyclerViewScrollListener() {
            @Override
            public void loadMore(RecyclerView recyclerView) {
                if (isLoading || itemAdapter.getItemCount() == 0)
                    return;

                if (hasMore) {//设置footer为加载中...
                    if (loadMoreListener != null) {
                        isLoading = true;
                        updateFooterState(FooterState.LOADING);
                        loadMoreListener.loadMore();
                    }
                } else {//设置footer为加载完成
                    updateFooterState(FooterState.NO_MORE);
                }
            }
        });


        helperAdapter.setOnViewBindListener(new OnViewBindListener() {
            @Override
            public void onBind(RecyclerView.ViewHolder holder, int viewType) {

                switch (viewType) {
                    case ViewType.TYPE_FOOTER:
                        if (isUseDefaultFooter) {
                            updateDefaultFooterView(holder);
                        }

                        if (footerChangeListener != null) {
                            footerChangeListener.onChange(holder, footerState);
                        }
                        break;
                    case ViewType.TYPE_EMPTY:
                    case ViewType.TYPE_ERROR:
                        if (tipsListener != null) {
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    helperAdapter.setTipsLoading();
                                    tipsListener.retry();
                                }
                            });
                        }
                        break;
                }

                if (onViewBindListener != null) {
                    onViewBindListener.onBind(holder, viewType);
                }
            }
        });
    }

    private void updateDefaultFooterView(RecyclerView.ViewHolder holder) {
        View view = holder.itemView.findViewById(R.id.load_more);
        if (view == null) return;
        if (view instanceof LoadMoreView) {
            LoadMoreView loadMoreView = (LoadMoreView) view;
            loadMoreView.setState(footerState);
            if (FooterState.ERROR == footerState) {
                loadMoreView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (loadMoreListener != null) {
                            updateFooterState(FooterState.LOADING);
                            loadMoreListener.loadMore();
                        }
                    }
                });
            }
        }
    }

    private void updateFooterState(int state) {
        this.footerState = state;
        if (helperAdapter != null) {
            helperAdapter.notifyDataSetChanged();
        }
    }

    public RecyclerViewHelper setTipsEmptyView(int layoutId) {
        if (helperAdapter != null) {
            helperAdapter.setView(layoutId, ViewType.TYPE_EMPTY);
        }
        return this;
    }

    public RecyclerViewHelper setTipsLoadingView(int layoutId) {
        if (helperAdapter != null) {
            helperAdapter.setView(layoutId, ViewType.TYPE_LOADING);
            helperAdapter.setTipsLoading();
        }
        return this;
    }

    public RecyclerViewHelper setTipsErrorView(int layoutId) {
        if (helperAdapter != null) {
            helperAdapter.setView(layoutId, ViewType.TYPE_ERROR);
        }
        return this;
    }

    public RecyclerViewHelper setHeaderView(int layoutId) {
        if (helperAdapter != null) {
            helperAdapter.setView(layoutId, ViewType.TYPE_HEADER);
        }
        return this;
    }

    public RecyclerViewHelper setFooterView(int layoutId) {
        isUseDefaultFooter = false;
        if (helperAdapter != null) {
            helperAdapter.setView(layoutId, ViewType.TYPE_FOOTER);
        }
        return this;
    }

    public void useDefaultFooter() {
        if (recyclerView == null) return;
        isUseDefaultFooter = true;
        helperAdapter.setView(R.layout.view_default_footer, ViewType.TYPE_FOOTER);
    }


    public void loadStart() {
        this.hasMore = true;
        this.isLoading = true;
        if (itemAdapter.getItemCount() > 0) {
            if (!hasMore) {
                updateFooterState(FooterState.LOADING);
            }
            helperAdapter.notifyDataSetChanged();
        } else {
            helperAdapter.setTipsLoading();
        }
    }


    public void loadComplete(boolean hasMore) {
        this.hasMore = hasMore;
        this.isLoading = false;

        if (itemAdapter.getItemCount() > 0) {
            if (!hasMore) {
                updateFooterState(FooterState.NO_MORE);
            }
            helperAdapter.notifyDataSetChanged();
        } else {
            helperAdapter.setTipsComplete();
        }
    }

    public void loadError() {
        this.hasMore = true;
        this.isLoading = false;

        itemAdapter.notifyDataSetChanged();

        if (itemAdapter.getItemCount() > 0) {
            updateFooterState(FooterState.ERROR);
        } else {
            helperAdapter.setTipsError();
        }
    }

    private TipsListener tipsListener;

    public void setTipsListener(TipsListener tipsListener) {
        this.tipsListener = tipsListener;
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    private OnFooterChangeListener footerChangeListener;

    public void setFooterChangeListener(OnFooterChangeListener listener) {
        this.footerChangeListener = listener;
    }

    private OnViewBindListener onViewBindListener;

    public void setOnViewBindListener(OnViewBindListener onViewBindListener) {
        this.onViewBindListener = onViewBindListener;
    }
}
