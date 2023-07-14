package com.jeanboy.component.pagination.decoration;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jeanboy on 2020/04/27 14:50.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int edgeSpace; // 两边间距
    private int itemSpace; // 中间间距
    private int mode;

    public static final int HIDE_NONE = 0;
    public static final int HIDE_MIDDLE = 1;
    public static final int HIDE_HEAD = 1 << 2;
    public static final int HIDE_TAIL = 1 << 3;

    @IntDef({HIDE_NONE, HIDE_MIDDLE, HIDE_HEAD, HIDE_TAIL})
    @Retention(RetentionPolicy.SOURCE)
    @interface Mode {
    }

    public SpaceItemDecoration(int space) {
        this(space, HIDE_NONE);
    }

    public SpaceItemDecoration(int space, @Mode int mode) {
        this(space, space, mode);
    }

    public SpaceItemDecoration(int edgeSpace, int itemSpace, @Mode int mode) {
        this.edgeSpace = edgeSpace;
        this.itemSpace = itemSpace;
        this.mode = mode;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        int itemCount = parent.getAdapter() == null ? parent.getChildCount() : parent.getAdapter().getItemCount();

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            setGridLayoutOffsets(outRect, (GridLayoutManager) layoutManager, view, position, itemCount);
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            setStaggeredGridLayoutOffsets(outRect, (StaggeredGridLayoutManager) layoutManager, view, position, itemCount);
        } else if (layoutManager instanceof LinearLayoutManager) {
            setLinearLayoutOffsets(outRect, (LinearLayoutManager) layoutManager, view, position, itemCount);
        }
    }

    private void setGridLayoutOffsets(Rect rect, GridLayoutManager layoutManager, View view, int position, int itemCount) {
        int headSpace = edgeSpace;
        int footSpace = edgeSpace;
        int centerSpace = itemSpace;
        if ((mode & HIDE_HEAD) == HIDE_HEAD) {
            headSpace = 0;
        }
        if ((mode & HIDE_TAIL) == HIDE_TAIL) {
            footSpace = 0;
        }
        if ((mode & HIDE_MIDDLE) == HIDE_MIDDLE) {
            centerSpace = 0;
        }

        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();

        int columnCount = layoutManager.getSpanCount(); // 列的数量
        int columnIndex = layoutParams.getSpanIndex(); // 当前列下标
        int spanSize = layoutParams.getSpanSize(); // 当前行跨的列数

        if (spanSize == columnCount) { // 跨列的情况
            if (position >= 0 && position < columnCount) { // 第一行
                rect.top = headSpace;
            } else if (position == (itemCount - 1)) { // 最后一行
                rect.top = centerSpace;
                rect.bottom = footSpace;
            }
        } else { // 不跨列
            if (position >= 0 && position < columnCount && position == columnIndex) { // 第一行
                rect.top = headSpace;
            } else if (position >= (itemCount - columnCount) && position <= itemCount) { // 最后一行
                rect.top = centerSpace;
                rect.bottom = footSpace;
            } else { // 中间行
                rect.top = centerSpace;
            }
            if (columnIndex >= 0 && columnIndex < columnCount) { // 每行列
                int halfCenterSpace = centerSpace / 2;
                rect.left = columnIndex == 0 ? 0 : halfCenterSpace;
                rect.right = columnIndex == (columnCount - 1) ? 0 : halfCenterSpace;
            }
        }
    }

    private void setStaggeredGridLayoutOffsets(Rect rect, StaggeredGridLayoutManager layoutManager, View view, int position, int itemCount) {
        int headSpace = edgeSpace;
        int footSpace = edgeSpace;
        int centerSpace = itemSpace;
        if ((mode & HIDE_HEAD) == HIDE_HEAD) {
            headSpace = 0;
        }
        if ((mode & HIDE_TAIL) == HIDE_TAIL) {
            footSpace = 0;
        }
        if ((mode & HIDE_MIDDLE) == HIDE_MIDDLE) {
            centerSpace = 0;
        }

        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();

        int columnCount = layoutManager.getSpanCount(); // 列的数量
        int columnIndex = layoutParams.getSpanIndex(); // 当前列下标
        int spanSize = layoutParams.getSpanSize(); // 当前行跨的列数

        Log.e(SpaceItemDecoration.class.getSimpleName(), itemCount + "----------------------------position--" + position
                + "-columnCount--" + columnCount
                + "-columnIndex--" + columnIndex
                + "-spanSize--" + spanSize);

        if (spanSize == columnCount) { // 跨列的情况
            if (position >= 0 && position < columnCount) { // 第一行
                rect.top = headSpace;
            } else if (position == (itemCount - 1)) { // 最后一行
                rect.top = centerSpace;
                rect.bottom = footSpace;
            }
        } else { // 不跨列
            if (position >= 0 && position < columnCount && position == columnIndex) { // 第一行
                rect.top = headSpace;
            } else if (position >= (itemCount - columnCount) && position <= itemCount) { // 最后一行
                rect.top = centerSpace;
                rect.bottom = footSpace;
            } else { // 中间行
                rect.top = centerSpace;
            }
            if (columnIndex >= 0 && columnIndex < columnCount) { // 每行列
                int halfCenterSpace = centerSpace / 2;
                rect.left = columnIndex == 0 ? 0 : halfCenterSpace;
                rect.right = columnIndex == (columnCount - 1) ? 0 : halfCenterSpace;
            }
        }
    }

    private void setLinearLayoutOffsets(Rect rect, LinearLayoutManager layoutManager, View view, int position, int itemCount) {
        int headSpace = edgeSpace;
        int footSpace = edgeSpace;
        int centerSpace = itemSpace;
        if ((mode & HIDE_HEAD) == HIDE_HEAD) {
            headSpace = 0;
        }
        if ((mode & HIDE_TAIL) == HIDE_TAIL) {
            footSpace = 0;
        }
        if ((mode & HIDE_MIDDLE) == HIDE_MIDDLE) {
            centerSpace = 0;
        }

        int orientation = layoutManager.getOrientation();
        if (orientation == LinearLayoutManager.VERTICAL) { // 垂直方向
            if (position == 0) { // 第一行
                rect.top = headSpace;
            } else if (position == (itemCount - 1)) { // 最后一行
                rect.top = centerSpace;
                rect.bottom = footSpace;
            } else { // 中间
                rect.top = centerSpace;
            }
        } else { // 水平方向
            if (position == 0) { // 第一行
                rect.left = headSpace;
            } else if (position == (itemCount - 1)) { // 最后一行
                rect.left = centerSpace;
                rect.right = footSpace;
            } else { // 中间
                rect.left = centerSpace;
            }
        }
    }
}
