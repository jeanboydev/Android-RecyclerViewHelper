package com.jeanboy.component.pagination.constants;

/**
 * Created by jeanboy on 2020/04/01 16:50.
 */
public interface ViewType {
    int ITEM = 0;
    int MASK = 1;
    int LOAD_MORE = 1 << 2;
    int HEADER = 1 << 3;
    int FOOTER = 1 << 4;
    // 偏移量
    int OFFSET = 5;
}

