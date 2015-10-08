# MultiPhotoPicker

Thanks to [PhotoPicker](https://github.com/donglua/PhotoPicker)

[MultiPhotoPicker Demo](http://fir.im/MultiPhotoPicker)

Modifications:

*  Selected pictures maintains after taking picture
*  Selected pictures in All folder is also selected in its own folder
*  Color, Theme, Icon editable
*  Material Design

This project is open to all contributors, if you find any bug or want to add feature to it.

Appreciate your star.

---

# Example
![](https://github.com/wangeason/MultiPhotoPicker/blob/master/pic/Screenshot_2015-09-28-17-38-58.png)
![](https://github.com/wangeason/MultiPhotoPicker/blob/master/pic/Screenshot_2015-09-28-17-39-16.png)
![](https://github.com/wangeason/MultiPhotoPicker/blob/master/pic/Screenshot_2015-09-28-17-39-31.png)

---

# Usage

### Gradle

```groovy
dependencies {
    compile 'io.github.wangeason:MultiPhotoPicker:0.2.0'
}
```

### eclipse
![GO HOME](https://github.com/wangeason/MultiPhotoPicker/blob/master/pic/5e9a81dbgw1eu90m08v86j20dw09a3yu.jpg)

### Pick Photo Multiple Times
```java
PhotoPickerIntent intent = new PhotoPickerIntent(MainActivity.this);
intent.setPhotoCount(9);
intent.setShowCamera(true);
intent.setShowGif(true);
intent.setMultiChoose(true)
startActivityForResult(intent, REQUEST_CODE);
```

### Preview Photo

```java
Intent intent = new Intent(mContext, PhotoPagerActivity.class);
intent.putExtra(PhotoPagerActivity.EXTRA_CURRENT_ITEM, position);
intent.putExtra(PhotoPagerActivity.EXTRA_PHOTOS, photoPaths);
startActivityForResult(intent, REQUEST_CODE);
```

### onActivityResult
```java
@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  super.onActivityResult(requestCode, resultCode, data);

  if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
    if (data != null) {
      ArrayList<String> photos = 
          data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
    }
  }
}
```

### manifest
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    >
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
  <uses-feature android:name="android.hardware.camera" android:required="true" />

  <application
    ...
    >
    ...
    
    <activity android:name="io.github.wangeason.multiphotopicker.PhotoPickerActivity"
      android:theme="@style/Theme.AppCompat.NoActionBar" 
       />

    <activity android:name="io.github.wangeason.multiphotopicker.PhotoPagerActivity"
      android:theme="@style/Theme.AppCompat.NoActionBar"/>
    
  </application>
</manifest>
```
### Custom style
```xml
<style name="photoPickerTheme" parent="photoPickerTheme.Base"/>
    <style name="photoPickerTheme.Base" parent="Theme.AppCompat.NoActionBar">
    	 <!--actionbar color -->
        <item name="colorPrimary">@color/primary</item> 
        <!--actionbar dark color -->
        <item name="colorPrimaryDark">@color/primaryDark</item> //actionbar color
        <!-- selected backgroud color -->
        <item name="colorAccent">#ff669900</item>
        <!-- del icon color -->
        <item name="del_color">@android:color/white</item>
        <!-- manify color -->
        <item name="zoom_color">@android:color/white</item>
        <!-- camera menu drawable -->
        <item name="camera">@drawable/camera</item>
    </style>
```

---


# License

    Copyright 2015 Wang Yansong

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.