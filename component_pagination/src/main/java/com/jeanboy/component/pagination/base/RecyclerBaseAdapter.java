package com.jeanboy.component.pagination.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeanboy on 2020/04/01 16:50.
 */
public abstract class RecyclerBaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    protected List<T> dataList;
    private final int itemLayoutId;

    public RecyclerBaseAdapter(List<T> dataList, @LayoutRes int itemLayoutId) {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        this.dataList = dataList;
        this.itemLayoutId = itemLayoutId;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(getLayoutView(parent, itemLayoutId));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        convert(holder, dataList.get(position), holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        setItemClickListener(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        clearItemClickListener(holder);
    }

    protected View getLayoutView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    protected void setItemClickListener(final BaseViewHolder holder) {
        if (onItemClickListener == null) {
            return;
        }
        if (dataList.isEmpty()) return;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    onItemClickListener.onItemClick(v, holder, position);
                }
            }
        });
    }

    protected void clearItemClickListener(BaseViewHolder holder) {
        holder.itemView.setOnClickListener(null);
    }

    public abstract void convert(BaseViewHolder holder, T t, int position);

    protected OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, BaseViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void addMoreData(List<T> sourceList) {
        dataList.addAll(sourceList);
        notifyDataSetChanged();
    }

    public void setData(List<T> sourceList) {
        dataList.clear();
        dataList.addAll(sourceList);
        notifyDataSetChanged();
    }

    public T getDataByPosition(int position) {
        return dataList.get(position);
    }

    public boolean contains(T t) {
        return dataList.contains(t);
    }
}
