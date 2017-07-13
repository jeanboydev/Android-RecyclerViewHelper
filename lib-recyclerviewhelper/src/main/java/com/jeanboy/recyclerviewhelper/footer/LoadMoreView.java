package com.jeanboy.recyclerviewhelper.footer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeanboy.recyclerviewhelper.R;


/**
 * Created by jeanboy on 2016/8/11.
 */
public class LoadMoreView extends RelativeLayout {

    private int state = FooterState.NORMAL;

    private ProgressBar progressBar;
    private ImageView iv_error;
    private TextView tv_msg;

    public LoadMoreView(Context context) {
        this(context, null);
    }

    public LoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setup(context);
    }

    public void setup(Context context) {
        inflate(context, R.layout.view_load_more, this);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        iv_error = (ImageView) findViewById(R.id.iv_error);
        tv_msg = (TextView) findViewById(R.id.tv_msg);

        setOnClickListener(null);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        if (this.state == state) return;
        this.state = state;

        if (progressBar != null) {
            progressBar.setVisibility(GONE);
        }
        if (iv_error != null) {
            iv_error.setVisibility(GONE);
        }
        if (tv_msg != null) {
            tv_msg.setVisibility(VISIBLE);
        }
        switch (state) {
            case FooterState.NORMAL:
                setOnClickListener(null);
                if (tv_msg != null) {
                    tv_msg.setVisibility(GONE);
                }
                break;
            case FooterState.LOADING:
                setOnClickListener(null);
                if (progressBar != null) {
                    progressBar.setVisibility(VISIBLE);
                }
                tv_msg.setText(R.string.msg_load_more_loading);
                break;
            case FooterState.NO_MORE:
                setOnClickListener(null);
                tv_msg.setText(R.string.msg_load_more_loaded_no_more);
                break;
            case FooterState.ERROR:
                if (iv_error != null) {
                    iv_error.setVisibility(VISIBLE);
                }
                if (tv_msg != null) {
                    tv_msg.setText(R.string.msg_load_more_error);
                }
                break;
        }
    }
}
