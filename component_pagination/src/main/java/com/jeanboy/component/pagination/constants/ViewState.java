package com.jeanboy.component.pagination.constants;

/**
 * Created by jeanboy on 2020/04/01 16:50.
 */
public interface ViewState {
    int LOADING = 0x1001; // 加载中
    int DATA = 0x1002; // 显示中
    int EMPTY = 0x1003; // 没有数据，全部加载完毕，没有更多
    int ERROR = 0x1004; // 加载失败
}
