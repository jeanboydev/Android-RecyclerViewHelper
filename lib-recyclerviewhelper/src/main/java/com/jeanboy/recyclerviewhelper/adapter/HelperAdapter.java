package com.jeanboy.recyclerviewhelper.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeanboy.recyclerviewhelper.listener.TipsListener;

/**
 * Created by Next on 2016/8/9.
 */
public class HelperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = HelperAdapter.class.getSimpleName();

    public static final int HEADER_VIEW = 0x00000111;
    public static final int FOOTER_VIEW = 0x00000222;
    public static final int LOADING_VIEW = 0x00000333;
    public static final int EMPTY_VIEW = 0x00000444;
    public static final int ERROR_VIEW = 0x00000555;

    /**
     * 第一次加载提示状态
     */
    public enum Tips {
        LOADING, EMPTY, ERROR, NORMAL
    }


    private Tips tips = Tips.LOADING;


    private int headerLayoutId = 0;
    private int emptyLayoutId = 0;
    private int loadingLayoutId = 0;
    private int errorLayoutId = 0;

    private View footerView;


    private final RecyclerView.Adapter<RecyclerView.ViewHolder> itemAdapter;

    public HelperAdapter(@NonNull RecyclerView.Adapter itemAdapter) {
        this.itemAdapter = itemAdapter;
    }


    /**
     * 添加Header
     *
     * @param headerLayout
     */
    public void setHeaderView(int headerLayout) {
        this.headerLayoutId = headerLayout;
    }

    /**
     * 添加Footer
     *
     * @param footerView
     */
    public void setFooterView(View footerView) {
        this.footerView = footerView;
    }

    public View getFooterView() {
        return footerView;
    }


    /**
     * 设置空数据显示view
     *
     * @param emptyLayout
     */
    public void setTipsEmptyView(int emptyLayout) {
        this.emptyLayoutId = emptyLayout;
    }

    /**
     * 设置第一次加载时的view
     *
     * @param loadingLayout
     */
    public void setTipsLoadingView(int loadingLayout) {
        this.loadingLayoutId = loadingLayout;
    }

    /**
     * 设置第一次加载错误view
     *
     * @param errorLayout
     */
    public void setTipsErrorView(int errorLayout) {
        this.errorLayoutId = errorLayout;
    }


    /**
     * 加载完成
     */
    public void loadTipsComplete() {
        if (itemAdapter.getItemCount() > 0) {
            tips = Tips.NORMAL;
        } else {
            tips = Tips.EMPTY;
        }
        notifyDataSetChanged();
    }

    /**
     * 加载失败
     */
    public void loadTipsError() {
        if (itemAdapter.getItemCount() == 0) {
            tips = Tips.ERROR;
        }
        notifyDataSetChanged();
    }

    /**
     * 第一次加载重试
     */
    public void loadTipsRetry() {
        if (itemAdapter.getItemCount() == 0) {
            tips = Tips.LOADING;
        }
        notifyDataSetChanged();
    }


    private int getHeaderViewCount() {
        return getViewCount(headerLayoutId);
    }

    private int getFooterViewCount() {
        return footerView == null ? 0 : 1;
    }

    private int getEmptyViewCount() {
        return getViewCount(emptyLayoutId);
    }

    private int getLoadingViewCount() {
        return getViewCount(loadingLayoutId);
    }

    private int getErrorViewCount() {
        return getViewCount(errorLayoutId);
    }


    private int getViewCount(int layoutId) {
        return layoutId > 0 ? 1 : 0;
    }

    public View getLayoutView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    /**
     * 没有数据的view显示逻辑
     *
     * @return
     */
    public int getEmptyViewType() {
        if (tips == Tips.LOADING && getLoadingViewCount() > 0) {
            return LOADING_VIEW;
        } else if (tips == Tips.ERROR && getErrorViewCount() > 0) {
            return ERROR_VIEW;
        } else {
            return EMPTY_VIEW;
        }
    }

    /**
     * 获取显示类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        if (getHeaderViewCount() == 0) {
            if (itemAdapter.getItemCount() == 0 && getEmptyViewCount() > 0) {
                return getEmptyViewType();
            }
        } else {
            if (position == 0) {
                return HEADER_VIEW;
            } else if (position == 1 && itemAdapter.getItemCount() == 0 && getEmptyViewCount() > 0) {
                return getEmptyViewType();
            }
        }

        if (position >= (getHeaderViewCount() + itemAdapter.getItemCount())) {
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position - getHeaderViewCount());
    }

    /**
     * 加载holder显示布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == LOADING_VIEW) {
            return new BaseViewHolder(getLayoutView(parent, loadingLayoutId));
        } else if (viewType == ERROR_VIEW) {
            BaseViewHolder holder = new BaseViewHolder(getLayoutView(parent, errorLayoutId));
            if (tipsListener != null) {
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadTipsRetry();
                        tipsListener.retry();
                    }
                });
            }
            return holder;
        } else if (viewType == EMPTY_VIEW) {
            BaseViewHolder holder = new BaseViewHolder(getLayoutView(parent, emptyLayoutId));
            if (tipsListener != null) {
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadTipsRetry();
                        tipsListener.retry();
                    }
                });
            }
            return holder;
        } else if (viewType == HEADER_VIEW) {
            return new BaseViewHolder(getLayoutView(parent, headerLayoutId));
        } else if (viewType == FOOTER_VIEW) {
            return new BaseViewHolder(footerView);
        } else {
            return itemAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    /**
     * 将数据绑定到布局上
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int headerCount = getHeaderViewCount();
        if (itemAdapter.getItemCount() > 0 && position >= headerCount && position < headerCount + itemAdapter.getItemCount()) {
            itemAdapter.onBindViewHolder(holder, position - headerCount);
        } else {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (itemAdapter.getItemCount() == 0) {//没有数据，只显示header+emptyView
            return getHeaderViewCount() + getEmptyViewCount();
        } else {//有数据不显示emptyView
            return getHeaderViewCount() + getFooterViewCount() + itemAdapter.getItemCount();
        }
    }


    private TipsListener tipsListener;

    public void setTipsListener(TipsListener tipsListener) {
        this.tipsListener = tipsListener;
    }
}
