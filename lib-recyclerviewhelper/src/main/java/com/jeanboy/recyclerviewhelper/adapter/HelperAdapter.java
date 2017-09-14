package com.jeanboy.recyclerviewhelper.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeanboy.recyclerviewhelper.listener.OnViewBindListener;


/**
 * Created by jeanboy on 2017/7/4.
 */

public class HelperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int headerLayout;
    private int footerLayout;

    private int loadingLayout;
    private int emptyLayout;
    private int errorLayout;

    private static final int LOADING = 0x1006;
    private static final int EMPTY = 0x1007;
    private static final int ERROR = 0x1008;
    private static final int NONE = 0x1009;

    private int tipsState = NONE;

    private final RecyclerView.Adapter itemAdapter;

    public HelperAdapter(RecyclerView.Adapter itemAdapter) {
        this.itemAdapter = itemAdapter;
    }

    public void setView(int layoutId, int type) {
        if (ViewType.TYPE_LOADING == type) {
            loadingLayout = layoutId;
        } else if (ViewType.TYPE_EMPTY == type) {
            emptyLayout = layoutId;
        } else if (ViewType.TYPE_ERROR == type) {
            errorLayout = layoutId;
        } else if (ViewType.TYPE_HEADER == type) {
            headerLayout = layoutId;
        } else if (ViewType.TYPE_FOOTER == type) {
            footerLayout = layoutId;
        }
    }

    public void setTipsComplete() {
        if (itemAdapter.getItemCount() > 0) {
            tipsState = NONE;
        } else {
            tipsState = EMPTY;
        }
        notifyDataSetChanged();
    }

    public void setTipsError() {
        if (itemAdapter.getItemCount() > 0) {
            tipsState = NONE;
        } else {
            tipsState = ERROR;
        }
        notifyDataSetChanged();
    }

    public void setTipsLoading() {
        if (itemAdapter.getItemCount() > 0) {
            tipsState = NONE;
        } else {
            tipsState = LOADING;
        }
        notifyDataSetChanged();
    }


    private int getHeaderViewCount() {
        return getViewCount(headerLayout);
    }

    private int getFooterViewCount() {
        return getViewCount(footerLayout);
    }

    private int getEmptyViewCount() {
        return getViewCount(emptyLayout);
    }

    private int getLoadingViewCount() {
        return getViewCount(loadingLayout);
    }

    private int getErrorViewCount() {
        return getViewCount(errorLayout);
    }

    private int getViewCount(int layoutId) {
        return layoutId > 0 ? 1 : 0;
    }

    private View getLayoutView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    private int getTipsViewType() {
        if (tipsState == LOADING && getLoadingViewCount() > 0) {
            return ViewType.TYPE_LOADING;
        } else if (tipsState == ERROR && getErrorViewCount() > 0) {
            return ViewType.TYPE_ERROR;
        } else {
            return ViewType.TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (itemAdapter.getItemCount() == 0) {
            if (getEmptyViewCount() > 0) {
                return getEmptyViewCount();
            } else {
                return getHeaderViewCount() + getEmptyViewCount();
            }
        } else {
            return getHeaderViewCount() + getFooterViewCount() + itemAdapter.getItemCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (itemAdapter.getItemCount() == 0 && getEmptyViewCount() > 0) {
            return getTipsViewType();
        }
        if (getHeaderViewCount() > 0 && position < getHeaderViewCount()) {
            return ViewType.TYPE_HEADER;
        }
        if (getFooterViewCount() > 0 && position >= (getHeaderViewCount() + itemAdapter.getItemCount())) {
            return ViewType.TYPE_FOOTER;
        }
        return super.getItemViewType(position - getHeaderViewCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (ViewType.TYPE_HEADER == viewType) {
            return new HelperViewHolder(getLayoutView(parent, headerLayout));
        } else if (ViewType.TYPE_FOOTER == viewType) {
            return new HelperViewHolder(getLayoutView(parent, footerLayout));
        } else if (ViewType.TYPE_LOADING == viewType) {
            return new HelperViewHolder(getLayoutView(parent, loadingLayout));
        } else if (ViewType.TYPE_ERROR == viewType) {
            return new HelperViewHolder(getLayoutView(parent, errorLayout));
        } else if (ViewType.TYPE_EMPTY == viewType) {
            return new HelperViewHolder(getLayoutView(parent, emptyLayout));
        } else {
            return itemAdapter.createViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position >= getHeaderViewCount() && position < (getHeaderViewCount() + itemAdapter.getItemCount())) {
            itemAdapter.onBindViewHolder(holder, position - getHeaderViewCount());
        } else {
            int itemViewType = getItemViewType(position);
            if (onViewBindListener != null) {
                onViewBindListener.onBind(holder, itemViewType);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        itemAdapter.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);

                    if (ViewType.TYPE_HEADER == viewType) {
                        return gridLayoutManager.getSpanCount();
                    } else if (ViewType.TYPE_FOOTER == viewType) {
                        return gridLayoutManager.getSpanCount();
                    } else if (ViewType.TYPE_LOADING == viewType) {
                        return gridLayoutManager.getSpanCount();
                    } else if (ViewType.TYPE_ERROR == viewType) {
                        return gridLayoutManager.getSpanCount();
                    } else if (ViewType.TYPE_EMPTY == viewType) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        itemAdapter.onViewAttachedToWindow(holder);

        int position = holder.getLayoutPosition();
        int viewType = getItemViewType(position);

        if (ViewType.TYPE_HEADER == viewType ||
                ViewType.TYPE_FOOTER == viewType ||
                ViewType.TYPE_LOADING == viewType ||
                ViewType.TYPE_ERROR == viewType ||
                ViewType.TYPE_EMPTY == viewType) {

            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
            }
        }
    }

    public int getHeaderCount() {
        return getHeaderViewCount();
    }

    private OnViewBindListener onViewBindListener;

    public void setOnViewBindListener(OnViewBindListener onViewBindListener) {
        this.onViewBindListener = onViewBindListener;
    }


}
