package io.github.wangeason.multiphotopicker;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.wangeason.multiphotopicker.adapter.PhotoMultiGridAdapter;
import io.github.wangeason.multiphotopicker.adapter.PhotoSelectedAdapter;
import io.github.wangeason.multiphotopicker.entity.MultiSelectedPhoto;
import io.github.wangeason.multiphotopicker.entity.Photo;
import io.github.wangeason.multiphotopicker.entity.PhotoDirectory;
import io.github.wangeason.multiphotopicker.event.OnItemClickListener;
import io.github.wangeason.multiphotopicker.event.OnSelectedItemClickListener;
import io.github.wangeason.multiphotopicker.event.OnSelectedItemDelListener;
import io.github.wangeason.multiphotopicker.fragment.PhotoPickerFragment;
import io.github.wangeason.multiphotopicker.fragment.ImagePagerFragment;
import io.github.wangeason.multiphotopicker.utils.ImageCaptureManager;

import static android.widget.Toast.LENGTH_LONG;

public class PhotoPickerActivity extends AppCompatActivity {

    public static final int INDEX_ALL_PHOTOS = 0;
    private ImageCaptureManager captureManager;

    private PhotoPickerFragment pickerFragment;
    private ImagePagerFragment imagePagerFragment;
    private ImagePagerFragment selectedPagerFragment;

    public final static String EXTRA_MAX_COUNT = "MAX_COUNT";
    public final static String EXTRA_SHOW_CAMERA = "SHOW_CAMERA";
    public final static String EXTRA_SHOW_GIF = "SHOW_GIF";
    public final static String EXTRA_MULTI_CHOOSE = "MULTI_CHOOSE";

    public final static String KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS";

    private Button btnDoneItem;
    private MenuItem menuAddItem, menuCamera;

    public final static int DEFAULT_MAX_COUNT = 9;

    private int maxCount = DEFAULT_MAX_COUNT;

    /**
     * to prevent multiple calls to inflate menu
     */
    private boolean menuIsInflated = false;

    private boolean showGif = false;

    private boolean multiChoose = false;

    boolean showCamera;
    private PhotoSelectedAdapter selectedPhotoAdapter;
    private PhotoMultiGridAdapter multiGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showCamera = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        boolean showGif = getIntent().getBooleanExtra(EXTRA_SHOW_GIF, false);
        multiChoose = getIntent().getBooleanExtra(EXTRA_MULTI_CHOOSE, false);
        setShowGif(showGif);

        setContentView(R.layout.activity_photo_picker);

        captureManager = new ImageCaptureManager(getActivity());

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.images);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionBar.setElevation(25);
        }

        maxCount = getIntent().getIntExtra(EXTRA_MAX_COUNT, DEFAULT_MAX_COUNT);

        btnDoneItem = (Button) findViewById(R.id.done);
        btnDoneItem.setText(getString(R.string.done_with_count, 0, maxCount));
        btnDoneItem.setEnabled(false);
        btnDoneItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ArrayList<String> selectedPhotos = pickerFragment.getPhotoGridAdapter().getSelectedPhotoPaths();
                intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, selectedPhotos);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        final Button btSwitchDirectory = (Button) findViewById(R.id.button);


        pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentById(R.id.photoPickerFragment);

        final ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
        listPopupWindow.setAnchorView(findViewById(R.id.ll_control));
        listPopupWindow.setAdapter(pickerFragment.getListAdapter());
        listPopupWindow.setModal(true);
        listPopupWindow.setDropDownGravity(Gravity.BOTTOM);
        listPopupWindow.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();

                PhotoDirectory directory = pickerFragment.getDirectories().get(position);

                btSwitchDirectory.setText(directory.getName());

                pickerFragment.getPhotoGridAdapter().setCurrentDirectoryIndex(position);
                pickerFragment.getPhotoGridAdapter().notifyDataSetChanged();
            }
        });

        btSwitchDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listPopupWindow.isShowing()) {
                    listPopupWindow.dismiss();
                } else if (!getActivity().isFinishing()) {
                    listPopupWindow.setHeight(Math.round(pickerFragment.getView().getHeight() * 0.8f));
                    listPopupWindow.show();
                }

            }
        });



        multiGridAdapter =
                pickerFragment.getPhotoGridAdapter();
        multiGridAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, Photo photo, int selectedItemCount, int selectTimes) {
                if (multiChoose) {
                    if (photo.getSelectedTimes() > 0) {
                        if (selectedItemCount >= maxCount) {
                            multiGridAdapter.del(photo);
                            multiGridAdapter.notifyItemChanged(position);
                        }else{
                            multiGridAdapter.add(photo);
                            multiGridAdapter.notifyItemChanged(position);
                        }
                    } else {
                        if (selectedItemCount >= maxCount) {
                            Toast.makeText(getActivity(), getString(R.string.over_max_count_tips, maxCount),
                                    LENGTH_LONG).show();
                        } else {
                            multiGridAdapter.add(photo);
                            multiGridAdapter.notifyItemChanged(position);
                        }
                    }
                } else {
                    if (maxCount <= 1) {
                        if (photo.getSelectedTimes() > 0) {
                            multiGridAdapter.del(0);
                            multiGridAdapter.notifyItemChanged(position);
                        } else {
                            multiGridAdapter.clearSelection();
                            multiGridAdapter.add(photo);
                            multiGridAdapter.notifyDataSetChanged();
                        }
                    } else {
                        if (photo.getSelectedTimes() > 0) {
                            multiGridAdapter.del(photo);
                            multiGridAdapter.notifyItemChanged(position);
                        } else {
                            if (selectedItemCount >= maxCount) {
                                Toast.makeText(getActivity(), getString(R.string.over_max_count_tips, maxCount),
                                        LENGTH_LONG).show();
                            } else {
                                multiGridAdapter.add(photo);
                                multiGridAdapter.notifyItemChanged(position);
                            }
                        }
                    }
                }
                selectedPhotoAdapter.notifyDataSetChanged();
                refreshDoneBtn();

            }
        });

        selectedPhotoAdapter = new PhotoSelectedAdapter(getActivity(), multiGridAdapter.getSelectedPhotos());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.selected_photos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(selectedPhotoAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        selectedPhotoAdapter.setOnSelectedItemClickListener(new OnSelectedItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (selectedPagerFragment != null && selectedPagerFragment.isVisible()) {
                    selectedPagerFragment.getViewPager().setCurrentItem(position);
                }else {
                    List<String> photos = multiGridAdapter.getSelectedPhotoPaths();

                    int[] screenLocation = new int[2];
                    v.getLocationOnScreen(screenLocation);
                    ImagePagerFragment imagePagerFragment =
                            ImagePagerFragment.newInstance(photos, position, screenLocation,
                                    v.getWidth(), v.getHeight());

                    addImagePagerFragment(imagePagerFragment, true);
                }
            }
        });
        selectedPhotoAdapter.setOnSelectedItemDelListener(new OnSelectedItemDelListener() {
            @Override
            public void onClick(int position, MultiSelectedPhoto path) {
                multiGridAdapter.getSelectedPhotos().get(position).getPhoto().del();
                multiGridAdapter.getSelectedPhotos().remove(position);
                multiGridAdapter.notifyDataSetChanged();
                selectedPhotoAdapter.notifyDataSetChanged();

                refreshDoneBtn();
            }
        });
    }

    private void refreshDoneBtn() {

        int selectedCount = multiGridAdapter.getSelectedItemCount();
        btnDoneItem.setEnabled(selectedCount > 0);
        btnDoneItem.setText(getString(R.string.done_with_count, selectedCount, maxCount));
    }


    /**
     * Overriding this method allows us to run our exit animation first, then exiting
     * the activity when it complete.
     */
    @Override
    public void onBackPressed() {
        if (imagePagerFragment != null && imagePagerFragment.isVisible()) {
            imagePagerFragment.runExitAnimation(new Runnable() {
                public void run() {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    }
                }
            });

            menuAddItem.setVisible(false);
            menuCamera.setVisible(showCamera);
        } else {
            super.onBackPressed();
        }
    }


    public void addImagePagerFragment(ImagePagerFragment imagePagerFragment, boolean isSelected) {
        if(isSelected){
            this.selectedPagerFragment = imagePagerFragment;
            if (this.imagePagerFragment != null && this.imagePagerFragment.isVisible()) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                }
            }
        }else{
            this.imagePagerFragment = imagePagerFragment;
            if (this.selectedPagerFragment != null && this.selectedPagerFragment.isVisible()) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                }
            }

        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, imagePagerFragment)
                .addToBackStack(null)
                .commit();


        //TODO
//        if(isSelected){
//            menuAddItem.setVisible(false);
//            menuCamera.setVisible(false);
//        }else {
//            menuAddItem.setVisible(false);
//            menuCamera.setVisible(false);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!menuIsInflated) {
            getMenuInflater().inflate(R.menu.menu_picker, menu);
            menuCamera = menu.findItem(R.id.camera);
            menuCamera.setEnabled(true);
            menuCamera.setVisible(showCamera);
            menuAddItem = menu.findItem(R.id.add);
            menuAddItem.setEnabled(true);
            menuAddItem.setVisible(false);

            menuIsInflated = true;
            return true;
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        if (item.getItemId() == R.id.add) {

            return true;
        }else if(item.getItemId() == R.id.camera){
            try {
                Intent intent = captureManager.dispatchTakePictureIntent();
                startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            captureManager.galleryAddPic(null);
//            MediaStoreHelper.setIsBlock(true);
            if (multiGridAdapter.getPhotoDirectories().size() > 0) {
                String path = captureManager.getCurrentPhotoPath();
                PhotoDirectory directory = multiGridAdapter.getPhotoDirectories().get(INDEX_ALL_PHOTOS);
                Photo newPhoto = new Photo(path.hashCode(), path);
                directory.getPhotos().add(INDEX_ALL_PHOTOS, newPhoto);
                directory.setCoverPath(path);

                for(PhotoDirectory item:multiGridAdapter.getPhotoDirectories()){
                    if(item.getName().equals(Environment.DIRECTORY_PICTURES)){
                        item.getPhotos().add(INDEX_ALL_PHOTOS, newPhoto);
                        item.setCoverPath(path);
                        break;
                    }
                }

                if (multiChoose) {
                    if (multiGridAdapter.getSelectedItemCount() >= maxCount) {
                        Toast.makeText(getActivity(), getString(R.string.over_max_count_tips, maxCount),
                                LENGTH_LONG).show();
                    } else {
                        multiGridAdapter.add(newPhoto);
                    }
                } else {
                    if (maxCount <= 1) {
                        multiGridAdapter.clearSelection();
                        multiGridAdapter.add(newPhoto);
                    } else {
                        if (multiGridAdapter.getSelectedItemCount() >= maxCount) {
                            Toast.makeText(getActivity(), getString(R.string.over_max_count_tips, maxCount),
                                    LENGTH_LONG).show();
                        } else {
                            multiGridAdapter.add(newPhoto);
                        }
                    }
                }

                selectedPhotoAdapter.notifyDataSetChanged();
                refreshDoneBtn();

                multiGridAdapter.notifyDataSetChanged();
                pickerFragment.getListAdapter().notifyDataSetChanged();
            }
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        captureManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        captureManager.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }



    public PhotoPickerActivity getActivity() {
        return this;
    }

    public boolean isShowGif() {
        return showGif;
    }

    public void setShowGif(boolean showGif) {
        this.showGif = showGif;
    }

    public boolean isMultiChoose() {
        return multiChoose;
    }

    public void setMultiChoose(boolean multiChoose) {
        this.multiChoose = multiChoose;
    }
}
