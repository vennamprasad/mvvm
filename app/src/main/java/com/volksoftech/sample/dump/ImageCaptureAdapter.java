package com.volksoftech.sample.dump;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.volksoftech.sample.R;

import java.util.ArrayList;

public class ImageCaptureAdapter extends RecyclerView.Adapter<ImageCaptureAdapter.ImageDataViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;
    private OnItemClickListener mItemClickListener;
    //we are storing all the products in a list
    private ArrayList<ImagesData> imagesDataArrayList;

    //getting the context and product list with constructor
    public ImageCaptureAdapter(Context mCtx, ArrayList<ImagesData> dataArrayList) {
        this.mCtx = mCtx;
        this.imagesDataArrayList = dataArrayList;
    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public ImageCaptureAdapter.ImageDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.items, null);
        return new ImageDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageCaptureAdapter.ImageDataViewHolder holder, int position) {
        //getting the product of the specified position
        ImagesData imagesData = imagesDataArrayList.get(position);

        //binding the data with the viewholder views
        holder.imageView.setImageBitmap(imagesData.getImage());
    }

    @Override
    public int getItemCount() {
        return imagesDataArrayList.size();
    }

    public void setImageInItem(int position, Bitmap imageSrc, String imagePath) {
        ImagesData dataSet = imagesDataArrayList.get(position);
        dataSet.setImage(imageSrc);
        dataSet.setPath(imagePath);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClickListener(View view, int position, ImagesData myData);
    }

    public class ImageDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        Button button;
        ImageDataViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            imageView.setOnClickListener(this);
            button = itemView.findViewById(R.id.camera);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClickListener(v, getAdapterPosition(), imagesDataArrayList.get(getAdapterPosition()));
        }
    }
}
