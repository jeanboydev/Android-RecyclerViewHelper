package com.jeanboy.component.pagination.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.jeanboy.component.pagination.listener.LoadListener;


/**
 * Created by jeanboy on 2020/04/01 16:50.
 */
public abstract class HolderLayout {

    protected void convertListener(View view, final boolean isError, final LoadListener listener) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onLoad(isError);
                }
            }
        });
    }

    public abstract int getLayoutId();

    public abstract void convert(RecyclerView.ViewHolder holder, int state, LoadListener listener);
}
