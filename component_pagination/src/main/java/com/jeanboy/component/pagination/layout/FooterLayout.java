package com.jeanboy.component.pagination.layout;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jeanboy.component.pagination.R;
import com.jeanboy.component.pagination.base.HolderLayout;
import com.jeanboy.component.pagination.constants.ViewState;
import com.jeanboy.component.pagination.listener.LoadListener;

/**
 * Created by jeanboy on 2020/04/01 16:50.
 */
public class FooterLayout extends HolderLayout {

    @Override
    public int getLayoutId() {
        return R.layout.item_footer;
    }

    @Override
    public void convert(RecyclerView.ViewHolder holder, int state, LoadListener listener) {
        View pb_progress = holder.itemView.findViewById(R.id.pb_progress);
        TextView tv_text = holder.itemView.findViewById(R.id.tv_text);
        holder.itemView.setVisibility(View.GONE);
        pb_progress.setVisibility(View.GONE);
        switch (state) {
            case ViewState.LOADING:
                holder.itemView.setVisibility(View.VISIBLE);
                pb_progress.setVisibility(View.VISIBLE);
                tv_text.setText(R.string.data_loading);
                break;
            case ViewState.ERROR:
                holder.itemView.setVisibility(View.VISIBLE);
                pb_progress.setVisibility(View.GONE);
                tv_text.setText(R.string.data_error_retry);
                convertListener(holder.itemView, true, listener);
                break;
            case ViewState.EMPTY:
                pb_progress.setVisibility(View.GONE);
                tv_text.setText(R.string.data_no_more);
                break;
        }
    }
}
