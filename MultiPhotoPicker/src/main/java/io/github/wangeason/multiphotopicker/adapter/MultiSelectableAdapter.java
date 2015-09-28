package io.github.wangeason.multiphotopicker.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.github.wangeason.multiphotopicker.entity.MultiSelectedPhoto;
import io.github.wangeason.multiphotopicker.entity.Photo;
import io.github.wangeason.multiphotopicker.entity.PhotoDirectory;
import io.github.wangeason.multiphotopicker.event.MultiSelectable;

public abstract class MultiSelectableAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements MultiSelectable {

    private static final String TAG = MultiSelectableAdapter.class.getSimpleName();

    protected List<PhotoDirectory> photoDirectories;
    protected List<MultiSelectedPhoto> selectedPhotos = new ArrayList<>();

    public int currentDirectoryIndex = 0;


    public MultiSelectableAdapter() {
        photoDirectories = new ArrayList<>();
    }


    /**
     * Indicates how many times is the photo get selected
     *
     * @param photo Photo of the item to select
     * @return selected times
     */
    @Override
    public int selectedTimes(Photo photo) {

        return photo.getSelectedTimes();
    }


    @Override
    public void add(Photo photo) {
        photo.add();
        selectedPhotos.add(new MultiSelectedPhoto(photo.getPath(), photo));
    }


    public void del(Photo photo) {
        for (Iterator<MultiSelectedPhoto> iter = selectedPhotos.iterator(); iter.hasNext();) {
            MultiSelectedPhoto item = iter.next();
            if(item.getPhoto().equals(photo)){
                item.getPhoto().del();
                iter.remove();
                break;
            }
        }
    }
    @Override
    public void del(int position) {
        MultiSelectedPhoto multiSelectedPhoto = selectedPhotos.get(position);
        multiSelectedPhoto.getPhoto().del();
        selectedPhotos.remove(position);

    }


    public void setCurrentDirectoryIndex(int currentDirectoryIndex) {
        this.currentDirectoryIndex = currentDirectoryIndex;
    }


    public List<Photo> getCurrentPhotos() {
        return photoDirectories.get(currentDirectoryIndex).getPhotos();
    }


    public List<String> getCurrentPhotoPaths() {
        List<String> currentPhotoPaths = new ArrayList<>(getCurrentPhotos().size());
        for (Photo photo : getCurrentPhotos()) {
            currentPhotoPaths.add(photo.getPath());
        }
        return currentPhotoPaths;
    }

    @Override
    public void clearSelection() {
        for(MultiSelectedPhoto item: selectedPhotos){
            item.getPhoto().setSelectedTimes(0);
        }
        selectedPhotos.clear();
    }

    @Override
    public int getSelectedItemCount() {
        return selectedPhotos.size();
    }

    @Override
    public List<MultiSelectedPhoto> getSelectedPhotos() {
        return selectedPhotos;
    }

    public List<PhotoDirectory> getPhotoDirectories() {
        return photoDirectories;
    }
}