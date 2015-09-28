package io.github.wangeason.multiphotopicker.entity;

/**
 * Created by wangeason on 15/9/22.
 */
public class MultiSelectedPhoto {
    String path;
    Photo photo;

    public MultiSelectedPhoto(String path, Photo photo) {
        this.path = path;
        this.photo = photo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
}
