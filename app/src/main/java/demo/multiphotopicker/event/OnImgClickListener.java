package demo.multiphotopicker.event;

import demo.multiphotopicker.adapter.GridSelectedAdapter;

/**
 * Created by wangeason on 15/9/28.
 */
public interface OnImgClickListener {
    void onClick(GridSelectedAdapter.ViewHolder holder, int position);
}
