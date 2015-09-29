package io.github.wangeason.multiphotopicker.event;

import io.github.wangeason.multiphotopicker.entity.Photo;

/**
 * Created by wangeason on 15/9/24.
 */
public interface OnItemClickListener {

    void onItemClick(int position, Photo path, int selectedItemCount, int selectTimes);
}
