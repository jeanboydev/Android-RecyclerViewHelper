package com.jeanboy.recyclerviewhelper.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by jeanboy on 2017/7/4.
 */

public interface OnViewBindListener {

    void onBind(RecyclerView.ViewHolder holder, int viewType);
}
