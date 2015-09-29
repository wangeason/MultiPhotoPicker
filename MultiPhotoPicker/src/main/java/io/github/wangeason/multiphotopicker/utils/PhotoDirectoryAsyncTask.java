package io.github.wangeason.multiphotopicker.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import io.github.wangeason.multiphotopicker.PhotoPickerActivity;
import io.github.wangeason.multiphotopicker.R;
import io.github.wangeason.multiphotopicker.entity.Photo;
import io.github.wangeason.multiphotopicker.entity.PhotoDirectory;
import io.github.wangeason.multiphotopicker.event.PhotosResultCallback;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.MIME_TYPE;
import static io.github.wangeason.multiphotopicker.PhotoPickerActivity.EXTRA_SHOW_GIF;

/**
 * Created by wangeason on 15/9/29.
 */
public class PhotoDirectoryAsyncTask extends AsyncTask<String, Integer, Cursor> {
    final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
    };

    boolean showGif;
    Context mContext;
    private PhotosResultCallback resultCallback;

    public PhotoDirectoryAsyncTask(Context ctx, Bundle args, PhotosResultCallback resultCallback) {
        showGif = args.getBoolean(EXTRA_SHOW_GIF, false);
        mContext = ctx;
        this.resultCallback = resultCallback;
    }

    @Override
    protected Cursor doInBackground(String[] params) {
        String[] selectionArgs;
        if (showGif) {
            selectionArgs = new String[] { "image/jpeg", "image/png", "image/gif" };
        } else {
            selectionArgs = new String[] { "image/jpeg", "image/png" };
        }
        Cursor cursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_PROJECTION,
                MIME_TYPE + "=? or " + MIME_TYPE + "=? " + (showGif ? ("or " + MIME_TYPE + "=?") : ""),
                selectionArgs,
                MediaStore.Images.Media.DATE_ADDED + " DESC");
        if (cursor != null) {
            // Ensure the cursor window is filled
            cursor.getCount();
        }
        return cursor;
    }

    @Override
    protected void onPostExecute(Cursor data) {
        super.onPostExecute(data);
        if(data == null){
            return;
        }

        List<PhotoDirectory> directories = new ArrayList<>();
        PhotoDirectory photoDirectoryAll = new PhotoDirectory();
        photoDirectoryAll.setName(mContext.getString(R.string.all_image));
        photoDirectoryAll.setId("ALL");

        while (data.moveToNext()) {

            int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
            String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
            String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
            String path = data.getString(data.getColumnIndexOrThrow(DATA));

            PhotoDirectory photoDirectory = new PhotoDirectory();
            photoDirectory.setId(bucketId);
            photoDirectory.setName(name);

            Photo newPhoto = new Photo(imageId, path);

            if (!directories.contains(photoDirectory)) {
                photoDirectory.setCoverPath(path);
                photoDirectory.addPhoto(newPhoto);
                photoDirectory.setDateAdded(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));
                directories.add(photoDirectory);
            } else {
                directories.get(directories.indexOf(photoDirectory)).addPhoto(newPhoto);
            }

            photoDirectoryAll.addPhoto(newPhoto);
        }
        if (photoDirectoryAll.getPhotoPaths().size() > 0) {
            photoDirectoryAll.setCoverPath(photoDirectoryAll.getPhotoPaths().get(0));
        }
        directories.add(PhotoPickerActivity.INDEX_ALL_PHOTOS, photoDirectoryAll);
        if (resultCallback != null) {
            resultCallback.onResultCallback(directories);
        }
    }
}
