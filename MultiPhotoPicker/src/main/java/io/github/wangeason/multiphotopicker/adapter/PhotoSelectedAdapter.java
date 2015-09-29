package io.github.wangeason.multiphotopicker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.wangeason.multiphotopicker.R;
import io.github.wangeason.multiphotopicker.entity.MultiSelectedPhoto;
import io.github.wangeason.multiphotopicker.event.OnSelectedItemClickListener;
import io.github.wangeason.multiphotopicker.event.OnSelectedItemDelListener;

/**
 * Created by donglua on 15/5/31.
 */
public class PhotoSelectedAdapter extends RecyclerView.Adapter<PhotoSelectedAdapter.PhotoViewHolder> {

    private LayoutInflater inflater;

    private Context mContext;

    private OnSelectedItemClickListener onSelectedItemClickListener = null;
    private OnSelectedItemDelListener onSelectedItemDelListener = null;

    List<MultiSelectedPhoto> selectedPhotos;

    public PhotoSelectedAdapter(Context mContext, List<MultiSelectedPhoto> selectedPhotos) {
        this.selectedPhotos = selectedPhotos;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }




    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_selected_photo, parent, false);
        PhotoViewHolder holder = new PhotoViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {


            final MultiSelectedPhoto photo;


                photo = selectedPhotos.get(position);


            Glide.with(mContext)
                    .load(new File(photo.getPath()))
                    .centerCrop()
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.ic_photo_black_48dp)
                    .error(R.drawable.ic_broken_image_black_48dp)
                    .into(holder.ivPhoto);


            holder.vDel.setSelected(false);
            holder.ivPhoto.setSelected(false);

            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onSelectedItemClickListener != null) {
                        onSelectedItemClickListener.onClick(view, position);
                    }
                }
            });
            holder.vDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onSelectedItemDelListener != null) {
                        onSelectedItemDelListener.onClick(position, photo);
                    }
                }
            });
    }


    @Override
    public int getItemCount() {
        int photosCount =
                selectedPhotos.size();
        return photosCount;
    }


    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vDel;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            vDel = itemView.findViewById(R.id.v_del);
        }
    }


    public void setOnSelectedItemClickListener(OnSelectedItemClickListener onSelectedItemClickListener) {
        this.onSelectedItemClickListener = onSelectedItemClickListener;
    }

    public void setOnSelectedItemDelListener(OnSelectedItemDelListener onSelectedItemDelListener) {
        this.onSelectedItemDelListener = onSelectedItemDelListener;
    }

    public ArrayList<String> getSelectedPhotoPaths() {
        ArrayList<String> selectedPhotoPaths = new ArrayList<>(selectedPhotos.size());

        for (MultiSelectedPhoto photo : selectedPhotos) {
            selectedPhotoPaths.add(photo.getPath());
        }

        return selectedPhotoPaths;
    }


}
