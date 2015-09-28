package io.github.wangeason.multiphotopicker.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import io.github.wangeason.multiphotopicker.PhotoPickerActivity;

/**
 * Created by donglua on 15/7/2.
 * Modified by wangeason on 15/9/24
 */
public class PhotoPickerIntent extends Intent {

  private PhotoPickerIntent() {
  }

  private PhotoPickerIntent(Intent o) {
    super(o);
  }

  private PhotoPickerIntent(String action) {
    super(action);
  }

  private PhotoPickerIntent(String action, Uri uri) {
    super(action, uri);
  }

  private PhotoPickerIntent(Context packageContext, Class<?> cls) {
    super(packageContext, cls);
  }

  public PhotoPickerIntent(Context packageContext) {
    super(packageContext, PhotoPickerActivity.class);
  }

  public void setPhotoCount(int photoCount) {
    this.putExtra(PhotoPickerActivity.EXTRA_MAX_COUNT, photoCount);
  }

  public void setShowCamera(boolean showCamera) {
    this.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, showCamera);
  }

  public void setShowGif(boolean showGif) {
    this.putExtra(PhotoPickerActivity.EXTRA_SHOW_GIF, showGif);
  }

    public void setMultiChoose(boolean multiChoose){
        this.putExtra(PhotoPickerActivity.EXTRA_MULTI_CHOOSE, multiChoose);
    }

}
