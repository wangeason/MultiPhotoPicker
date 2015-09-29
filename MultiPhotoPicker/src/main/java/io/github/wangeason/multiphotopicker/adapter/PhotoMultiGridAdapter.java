package io.github.wangeason.multiphotopicker.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.wangeason.multiphotopicker.R;
import io.github.wangeason.multiphotopicker.entity.MultiSelectedPhoto;
import io.github.wangeason.multiphotopicker.entity.Photo;
import io.github.wangeason.multiphotopicker.entity.PhotoDirectory;
import io.github.wangeason.multiphotopicker.event.OnItemClickListener;
import io.github.wangeason.multiphotopicker.event.OnZoomListener;

/**
 * Created by wangeason on 15/9/22.
 */
public class PhotoMultiGridAdapter extends MultiSelectableAdapter<PhotoMultiGridAdapter.PhotoViewHolder> {

    private LayoutInflater inflater;

    private Context mContext;

    private OnZoomListener onZoomListener = null;
    private OnItemClickListener onItemClickListener = null;

    private int accentColor = -1;

    public PhotoMultiGridAdapter(Context mContext, List<PhotoDirectory> photoDirectories) {
        this.photoDirectories = photoDirectories;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_multi_photo, parent, false);
        PhotoViewHolder holder = new PhotoViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        List<Photo> photos = getCurrentPhotos();
        final Photo photo = photos.get(position);
        
        Glide.with(mContext)
                .load(new File(photo.getPath()))
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(R.drawable.ic_photo_black_48dp)
                .error(R.drawable.ic_broken_image_black_48dp)
                .into(holder.ivPhoto);


        final int selectedTimes = photo.getSelectedTimes();

        Drawable mDrawable = mContext.getResources().getDrawable(R.drawable.photo_bg);
        if(selectedTimes>0) {
            mDrawable.setColorFilter(new
                    PorterDuffColorFilter(fetchAccentColor(), PorterDuff.Mode.MULTIPLY));
        }
        holder.ivPhoto.setBackgroundDrawable(mDrawable);

        holder.txtTimes.setVisibility(selectedTimes>0 ? View.VISIBLE : View.INVISIBLE);
        holder.txtTimes.setBackgroundColor(fetchAccentColor());
        holder.txtTimes.setText(""+photo.getSelectedTimes());

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, photo,
                            getSelectedItemCount(), selectedTimes);
                }
            }
        });
        holder.vZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onZoomListener != null) {
                    onZoomListener.onClick(view, position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        int photosCount =
                photoDirectories.size() == 0 ? 0 : getCurrentPhotos().size();
        return photosCount;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vZoom;
        private TextView txtTimes;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            vZoom = itemView.findViewById(R.id.v_zoom);
            txtTimes = (TextView) itemView.findViewById(R.id.txt_selected);
        }
    }
    public ArrayList<String> getSelectedPhotoPaths() {
        ArrayList<String> selectedPhotoPaths = new ArrayList<>(getSelectedItemCount());

        for (MultiSelectedPhoto photoPath : selectedPhotos) {
            selectedPhotoPaths.add(photoPath.getPath());
        }

        return selectedPhotoPaths;
    }

    private int fetchAccentColor() {
        if(accentColor != -1){
            return accentColor;
        }
        TypedValue typedValue = new TypedValue();

        TypedArray a = mContext.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorAccent });
        int accentColor = a.getColor(0, 0);

        a.recycle();

        return accentColor;
    }

    public void setOnZoomListener(OnZoomListener onZoomListener) {
        this.onZoomListener = onZoomListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
