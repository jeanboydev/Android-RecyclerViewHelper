package com.jeanboy.recyclerviewhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Next on 2016/8/5.
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    private List<T> dataList;
    private int layoutId;

    private OnItemClickListener onItemClickListener;

    public CommonAdapter(Context context, List<T> dataList, int layoutId) {
        this.context = context;
        this.dataList = dataList;
        this.layoutId = layoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = ViewHolder.get(context, parent, layoutId);
        setListener(holder);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        convert(holder, dataList.get(position), holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setListener(final ViewHolder viewHolder) {
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    onItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });

    }

    public abstract void convert(ViewHolder holder, T t, int position);

    public interface OnItemClickListener {
        void onItemClick(View view, ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
