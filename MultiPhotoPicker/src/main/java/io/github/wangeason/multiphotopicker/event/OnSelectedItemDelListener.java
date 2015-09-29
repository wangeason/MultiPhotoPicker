package io.github.wangeason.multiphotopicker.event;

import io.github.wangeason.multiphotopicker.entity.MultiSelectedPhoto;

/**
 * Created by wangeason on 15/9/24.
 */
public interface OnSelectedItemDelListener {
    void onClick(int position, MultiSelectedPhoto path);
}
