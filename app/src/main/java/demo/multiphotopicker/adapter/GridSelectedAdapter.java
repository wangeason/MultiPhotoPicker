package demo.multiphotopicker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import demo.multiphotopicker.R;
import demo.multiphotopicker.event.OnImgClickListener;

/**
 * Created by wangeason on 15/9/28.
 */
public class GridSelectedAdapter extends RecyclerView.Adapter<GridSelectedAdapter.ViewHolder> {
    ArrayList<String> imgPaths;
    private Context mContext;
    private LayoutInflater inflater;
    int maxNum;

    public OnImgClickListener getOnImgClickListener() {
        return onImgClickListener;
    }

    public void setOnImgClickListener(OnImgClickListener onImgClickListener) {
        this.onImgClickListener = onImgClickListener;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    private OnImgClickListener onImgClickListener;

    public GridSelectedAdapter(Context context, ArrayList<String> imgPaths, int maxNum) {
        this.imgPaths = imgPaths;
        this.mContext = context;
        this.maxNum = maxNum;
        inflater= LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_selected, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(position < imgPaths.size()) {
            Glide.with(mContext)
                    .load(new File(imgPaths.get(position)))
                    .centerCrop()
                    .thumbnail(0.1f)
                    .into(holder.imgSelected);

        }else if(position == imgPaths.size()){
            Glide.with(mContext)
                    .load(R.drawable.icon_addpic)
                    .centerCrop()
                    .into(holder.imgSelected);
        }

        holder.imgSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onImgClickListener != null) {
                    onImgClickListener.onClick(holder, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(imgPaths.size() < maxNum){
            return imgPaths.size() +1;
        }else{
            return maxNum;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.img_selected)
        ImageView imgSelected;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            imgSelected = (ImageView)itemView.findViewById(R.id.img_selected);
        }
    }
}
