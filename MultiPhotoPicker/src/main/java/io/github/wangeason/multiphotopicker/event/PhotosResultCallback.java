package io.github.wangeason.multiphotopicker.event;

import java.util.List;

import io.github.wangeason.multiphotopicker.entity.PhotoDirectory;

/**
 * Created by wangeason on 15/9/29.
 */
public interface PhotosResultCallback {
    void onResultCallback(List<PhotoDirectory> directories);
}
