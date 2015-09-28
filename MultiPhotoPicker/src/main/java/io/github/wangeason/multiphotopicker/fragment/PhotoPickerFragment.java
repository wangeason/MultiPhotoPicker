package io.github.wangeason.multiphotopicker.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.wangeason.multiphotopicker.PhotoPickerActivity;
import io.github.wangeason.multiphotopicker.R;
import io.github.wangeason.multiphotopicker.adapter.PhotoMultiGridAdapter;
import io.github.wangeason.multiphotopicker.adapter.PopupDirectoryListAdapter;
import io.github.wangeason.multiphotopicker.entity.PhotoDirectory;
import io.github.wangeason.multiphotopicker.event.OnZoomListener;
import io.github.wangeason.multiphotopicker.utils.MediaStoreHelper;

/**
 * Created by donglua on 15/5/31.
 */
public class PhotoPickerFragment extends Fragment {

    private PhotoMultiGridAdapter photoGridAdapter;

    private PopupDirectoryListAdapter listAdapter;
    private List<PhotoDirectory> directories;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        directories = new ArrayList<>();

        setRetainInstance(true);
        Bundle mediaStoreArgs = new Bundle();
        if (getActivity() instanceof PhotoPickerActivity) {
            mediaStoreArgs.putBoolean(PhotoPickerActivity.EXTRA_SHOW_GIF, ((PhotoPickerActivity) getActivity()).isShowGif());
        }

        MediaStoreHelper.getPhotoDirs(getActivity(), mediaStoreArgs,
                new MediaStoreHelper.PhotosResultCallback() {
                    @Override
                    public void onResultCallback(List<PhotoDirectory> dirs, Loader<Cursor> loader) {
                        directories.clear();
                        directories.addAll(dirs);
                        photoGridAdapter.notifyDataSetChanged();
                        listAdapter.notifyDataSetChanged();
                        loader.stopLoading();
                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        final View rootView = inflater.inflate(R.layout.fragment_photo_picker, container, false);

        photoGridAdapter = new PhotoMultiGridAdapter(getActivity(), directories);
        listAdapter = new PopupDirectoryListAdapter(getActivity(), directories);


        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_photos);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(photoGridAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        photoGridAdapter.setOnZoomListener(new OnZoomListener() {
            @Override
            public void onClick(View v, int position) {

                List<String> photos = photoGridAdapter.getCurrentPhotoPaths();

                int[] screenLocation = new int[2];
                v.getLocationOnScreen(screenLocation);
                ImagePagerFragment imagePagerFragment =
                        ImagePagerFragment.newInstance(photos, position, screenLocation,
                                v.getWidth(), v.getHeight());

                ((PhotoPickerActivity) getActivity()).addImagePagerFragment(imagePagerFragment, false);
            }
        });
        return rootView;
    }





    public PhotoMultiGridAdapter getPhotoGridAdapter() {
        return photoGridAdapter;
    }




    public ArrayList<String> getSelectedPhotoPaths() {
        return photoGridAdapter.getSelectedPhotoPaths();
    }

    public PopupDirectoryListAdapter getListAdapter() {
        return listAdapter;
    }

    public List<PhotoDirectory> getDirectories() {
        return directories;
    }


}
