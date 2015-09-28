package io.github.wangeason.multiphotopicker.event;

import java.util.List;

import io.github.wangeason.multiphotopicker.entity.MultiSelectedPhoto;
import io.github.wangeason.multiphotopicker.entity.Photo;

/**
 * Created by donglua on 15/6/30.
 */
public interface MultiSelectable {


    /**
     * Indicates if the item at position position is selected
     *
     * @param photo Photo of the item to check
     * @return true if the item is selected, false otherwise
     */
    int selectedTimes(Photo photo);

    /**
     * Toggle the selection status of the item at a given position
     *
     * @param photo Photo of the item to toggle the selection status for
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
