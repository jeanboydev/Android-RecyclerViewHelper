package com.jeanboy.app.test.layout;

import androidx.recyclerview.widget.RecyclerView;

import com.jeanboy.app.test.R;
import com.jeanboy.component.pagination.base.HolderLayout;
import com.jeanboy.component.pagination.listener.LoadListener;

/**
 * Created by jianbo on 2023/7/14 16:23.
 */
public class FooterLayout extends HolderLayout {
    @Override
    public int getLayoutId() {
        return R.layout.item_footer;
    }

    @Override
    public void convert(RecyclerView.ViewHolder holder, int state, LoadListener listener) {

    }
}
