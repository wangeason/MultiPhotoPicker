package demo.multiphotopicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import demo.multiphotopicker.adapter.GridSelectedAdapter;
import demo.multiphotopicker.event.OnImgClickListener;
import io.github.wangeason.multiphotopicker.PhotoPagerActivity;
import io.github.wangeason.multiphotopicker.PhotoPickerActivity;
import io.github.wangeason.multiphotopicker.utils.PhotoPickerIntent;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_CHOOSE = 0;
    private static final int MODIFY_CHOOSE = 1;
    private static final int MAX_PIC_NUM = 9;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rv_photos)
    RecyclerView rvPhotos;

    boolean isCameraOn;
    boolean isMulti;
    private MenuItem menuItemCamera;

    private GridSelectedAdapter gridSelectedAdapter;
    private ArrayList<String> imgPaths = new ArrayList<>();
    private boolean menuIsInflated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
        }

        gridSelectedAdapter = new GridSelectedAdapter(this,imgPaths, MAX_PIC_NUM);
        gridSelectedAdapter.setOnImgClickListener(new OnImgClickListener() {
            @Override
            public void onClick(GridSelectedAdapter.ViewHolder holder, int position) {
                if(position < imgPaths.size()){
                    Intent intent = new Intent(MainActivity.this, PhotoPagerActivity.class);
                    intent.putExtra(PhotoPagerActivity.EXTRA_CURRENT_ITEM, position);
                    intent.putExtra(PhotoPagerActivity.EXTRA_PHOTOS, imgPaths);
                    startActivityForResult(intent, MODIFY_CHOOSE);
                }else{
                    //Add Photo
                    PhotoPickerIntent intent = new PhotoPickerIntent(MainActivity.this);
                    intent.setPhotoCount(MAX_PIC_NUM - imgPaths.size());
                    intent.setMultiChoose(isMulti);
                    intent.setShowCamera(isCameraOn);
                    startActivityForResult(intent, ADD_CHOOSE);
                }
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        rvPhotos.setLayoutManager(layoutManager);
        rvPhotos.setAdapter(gridSelectedAdapter);
        rvPhotos.setItemAnimator(new DefaultItemAnimator());

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!menuIsInflated) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            menuItemCamera = menu.findItem(R.id.camera_switch);
            menuItemCamera.setTitle(getString(R.string.camera_switch, getString(isCameraOn?R.string.on:R.string.off)));


            return true;
        }
        return false;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        //noinspection SimplifiableIfStatement
        if (id == R.id.multi) {
            isMulti = true;
        }else if(id == R.id.single){
            isMulti = false;
        }else if(id == R.id.camera_switch){
            isCameraOn = !isCameraOn;
            menuItemCamera.setTitle(getString(R.string.camera_switch, getString(isCameraOn?R.string.on:R.string.off)));
        }

        imgPaths.clear();
        gridSelectedAdapter.notifyDataSetChanged();

        PhotoPickerIntent intent = new PhotoPickerIntent(MainActivity.this);
        intent.setPhotoCount(MAX_PIC_NUM);
        intent.setMultiChoose(isMulti);
        intent.setShowCamera(isCameraOn);
        startActivityForResult(intent, ADD_CHOOSE);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<String> photos = null;
        if (resultCode == RESULT_OK && requestCode == MODIFY_CHOOSE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }
            imgPaths.clear();

            if (photos != null) {

                imgPaths.addAll(photos);
            }
            gridSelectedAdapter.notifyDataSetChanged();
        } else if (resultCode == RESULT_OK && requestCode == ADD_CHOOSE) {

            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            }

            if (photos != null) {
                imgPaths.addAll(photos);
            }
            gridSelectedAdapter.notifyDataSetChanged();
        }
    }

}
