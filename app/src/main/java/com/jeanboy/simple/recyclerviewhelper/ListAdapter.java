package com.jeanboy.simple.recyclerviewhelper;


import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Next on 2016/8/9.
 */
public class ListAdapter extends CommonAdapter<String> {


    public ListAdapter(@NonNull List<String> dataList) {
        super(dataList, R.layout.item_list);
    }

    @Override
    public void convert(BaseViewHolder holder, String s, int position) {

    }
}
