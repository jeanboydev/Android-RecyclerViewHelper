package com.jeanboy.app.test.layout;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.jeanboy.app.test.R;
import com.jeanboy.component.pagination.base.HolderLayout;
import com.jeanboy.component.pagination.constants.ViewState;
import com.jeanboy.component.pagination.listener.LoadListener;

/**
 * Created by jianbo on 2023/7/14 16:23.
 */
public class MaskLayout extends HolderLayout {
    @Override
    public int getLayoutId() {
        return R.layout.item_mask;
    }

    @Override
    public void convert(RecyclerView.ViewHolder holder, int state, LoadListener listener) {
        View view_data_empty = holder.itemView.findViewById(R.id.view_data_empty);
        View view_data_error = holder.itemView.findViewById(R.id.view_data_error);
        View view_data_loading = holder.itemView.findViewById(R.id.view_data_loading);
        view_data_empty.setVisibility(View.GONE);
        view_data_error.setVisibility(View.GONE);
        view_data_loading.setVisibility(View.GONE);
        switch (state) {
            case ViewState.LOADING:
                view_data_loading.setVisibility(View.VISIBLE);
                break;
            case ViewState.EMPTY:
                view_data_empty.setVisibility(View.VISIBLE);
                // TODO: 2023/7/14 为了演示增加点击事件，一般不需要
                convertListener(holder.itemView, false, listener);
                break;
            case ViewState.ERROR:
                view_data_error.setVisibility(View.VISIBLE);
                convertListener(holder.itemView, true, listener);
                break;
        }
    }
}
