package com.jeanboy.app.test;


import androidx.annotation.NonNull;

import com.jeanboy.component.pagination.base.BaseViewHolder;
import com.jeanboy.component.pagination.base.RecyclerBaseAdapter;

import java.util.List;

/**
 * Created by Next on 2016/8/9.
 */
public class ListAdapter extends RecyclerBaseAdapter<String> {

    public ListAdapter(@NonNull List<String> dataList) {
        super(dataList, R.layout.item_list);
    }

    @Override
    public void convert(BaseViewHolder holder, String s, int position) {

    }
}
