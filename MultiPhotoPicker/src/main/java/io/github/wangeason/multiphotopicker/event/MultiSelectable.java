package io.github.wangeason.multiphotopicker.event;

import java.util.List;

import io.github.wangeason.multiphotopicker.entity.MultiSelectedPhoto;
import io.github.wangeason.multiphotopicker.entity.Photo;

/**
 * Created by wangeason on 15/9/24.
 */
public interface MultiSelectable {


    /**
     * Indicates the selected times of the item
     *
     * @param photo Photo of the item
     */
    int selectedTimes(Photo photo);

    /**
     * increase of decrease the selected times
     *
     * @param photo Photo of the item to operate
     */
    void add(Photo photo);

    void del(int position);

    /**
     * Clear the selection status for all items
     */
    void clearSelection();

    /**
     * Count the selected items
     *
     * @return Selected items count
     */
    int getSelectedItemCount();

    /**
     * Indicates the list of selected photos
     *
     * @return List of selected photos
     */
    List<MultiSelectedPhoto> getSelectedPhotos();

}
