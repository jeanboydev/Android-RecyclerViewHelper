package com.jeanboy.component.pagination;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jeanboy.component.pagination.base.BaseViewHolder;
import com.jeanboy.component.pagination.base.HolderLayout;
import com.jeanboy.component.pagination.constants.ViewState;
import com.jeanboy.component.pagination.constants.ViewType;
import com.jeanboy.component.pagination.listener.LoadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianbo on 2023/7/13 14:49.
 */
public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private volatile int currentState = ViewState.EMPTY;
    private final List<HolderLayout> headerList = new ArrayList<>();
    private final List<HolderLayout> footerList = new ArrayList<>();
    private HolderLayout loadMoreLayout;
    private HolderLayout maskLayout;

    private LoadListener refreshListener;
    private LoadListener loadMoreListener;

    private final RecyclerView.Adapter itemAdapter;

    private volatile int lastItemCount = 0;

    public PaginationAdapter(@NonNull RecyclerView.Adapter itemAdapter) {
        this.itemAdapter = itemAdapter;
    }

    private View getLayoutView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (ViewType.MASK == viewType) {
            return new BaseViewHolder(getLayoutView(parent, maskLayout.getLayoutId()));
        } else if (ViewType.LOAD_MORE == viewType) {
            return new BaseViewHolder(getLayoutView(parent, loadMoreLayout.getLayoutId()));
        } else if (ViewType.HEADER == viewType) {
            HolderLayout holderLayout = headerList.get(viewType >> ViewType.OFFSET);
            return new BaseViewHolder(getLayoutView(parent, holderLayout.getLayoutId()));
        } else if (ViewType.FOOTER == viewType) {
            HolderLayout holderLayout = footerList.get(viewType >> ViewType.OFFSET);
            return new BaseViewHolder(getLayoutView(parent, holderLayout.getLayoutId()));
        } else {
            return itemAdapter.createViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (ViewType.MASK == viewType) {
            maskLayout.convert(holder, currentState, refreshListener);
        } else if (ViewType.LOAD_MORE == viewType) {
            loadMoreLayout.convert(holder, currentState, loadMoreListener);
        } else if (ViewType.HEADER == viewType) {
            HolderLayout holderLayout = headerList.get(viewType >> ViewType.OFFSET);
            holderLayout.convert(holder, currentState, null);
        } else if (ViewType.FOOTER == viewType) {
            HolderLayout holderLayout = footerList.get(viewType >> ViewType.OFFSET);
            holderLayout.convert(holder, currentState, null);
        } else {
            itemAdapter.onBindViewHolder(holder, position - getHeaderCount());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (itemAdapter.getItemCount() == 0 && getMaskCount() > 0) {
            return ViewType.MASK;
        }

        int headerCount = getHeaderCount();
        if (headerCount > 0 && position < headerCount) {
            return ViewType.HEADER + (position << ViewType.OFFSET);
        }

        int footerCount = getFooterCount();
        int footerBefore = headerCount + itemAdapter.getItemCount();
        if (footerCount > 0 && position >= footerBefore && position < (footerBefore + footerCount)) {
            return ViewType.FOOTER + ((position - footerBefore) << ViewType.OFFSET);
        }

        int nextCount = getLoadMoreCount();
        int nextBefore = footerBefore + footerCount;
        if (nextCount > 0 && position >= nextBefore && position < (nextBefore + nextCount)) {
            return ViewType.LOAD_MORE;
        }

        return itemAdapter.getItemViewType(position - headerCount);
    }

    @Override
    public int getItemCount() {
        if (itemAdapter.getItemCount() == 0) {
            return getMaskCount();
        }

        int headerCount = getHeaderCount();
        int footerCount = getFooterCount();
        int loadMoreCount = getLoadMoreCount();

        return headerCount + itemAdapter.getItemCount() + footerCount + loadMoreCount;
    }

    @SuppressLint("ResourceType")
    private int getViewCount(@LayoutRes int layoutId) {
        return layoutId > 0 ? 1 : 0;
    }

    public int getMaskCount() {
        if (maskLayout == null) return 0;
        return getViewCount(maskLayout.getLayoutId());
    }

    private int getLoadMoreCount() {
        if (loadMoreLayout == null) return 0;
        return getViewCount(loadMoreLayout.getLayoutId());
    }

    private int getHeaderCount() {
        return headerList.size();
    }

    private int getFooterCount() {
        return footerList.size();
    }

    public int getDataSize() {
        return itemAdapter.getItemCount();
    }

    public int getCurrentState() {
        return currentState;
    }

    public LoadListener getLoadMoreListener() {
        return loadMoreListener;
    }

    public void setLoading() {
        currentState = ViewState.LOADING;
        if (itemAdapter.getItemCount() == 0 && getMaskCount() == 1) {
            notifyDataSetChanged();
        } else {
            int positionStart = getPositionStart(1);
            notifyItemRangeChanged(positionStart, 1);
        }
    }

    public void setLoadError() {
        currentState = ViewState.ERROR;
        if (itemAdapter.getItemCount() == 0 && getMaskCount() == 1) {
            notifyDataSetChanged();
        } else {
            int positionStart = getPositionStart(1);
            notifyItemRangeChanged(positionStart, 1);
        }
    }

    public void setLoadCompleted(boolean hasMore) {
        setLoadCompleted(-1, hasMore);
    }

    public void setLoadCompleted(int changeSize, boolean hasMore) {
        int itemCount = itemAdapter.getItemCount();
        if (itemCount == 0 && getMaskCount() == 1) {
            currentState = ViewState.EMPTY;
            notifyDataSetChanged();
        } else {
            currentState = hasMore ? ViewState.DATA : ViewState.EMPTY;
            if (changeSize == -1) {
                notifyDataSetChanged();
            } else {
                if (itemCount < lastItemCount) { // 数据减少了，刷新的情况
                    notifyDataSetChanged();
                } else {
                    int positionStart = getPositionStart(changeSize);
                    if (positionStart == 0) {
                        notifyItemChanged(0);
                    }
                    if (changeSize > 0) {
                        notifyItemRangeInserted(positionStart, changeSize);
                    } else {
                        notifyItemChanged(positionStart);
                    }
                }
            }
        }
        lastItemCount = itemAdapter.getItemCount();
    }

    private int getPositionStart(int changeSize) {
        int itemCount = getItemCount();
        int positionStart = itemCount - changeSize - getLoadMoreCount() - getFooterCount();
        positionStart = Math.max(positionStart, 0);
        positionStart = Math.min(positionStart, itemCount - 1);
        return positionStart;
    }

    public void setMaskLayout(HolderLayout maskLayout) {
        this.maskLayout = maskLayout;
    }

    public void setLoadMoreLayout(HolderLayout loadMoreLayout) {
        this.loadMoreLayout = loadMoreLayout;
    }

    public void addHeader(HolderLayout headerLayout) {
        headerList.add(headerLayout);
    }

    public void addFooter(HolderLayout footerLayout) {
        footerList.add(footerLayout);
    }

    public void setRefreshListener(LoadListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void setLoadMoreListener(LoadListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
