package com.example.picturealbum;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.imageHolder>{


    List<MyImages> imagesList = new ArrayList<>();

    public void setListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    private OnImageClickListener listener;

    public void setImagesList(List<MyImages> imagesList) {
        this.imagesList = imagesList;
        notifyDataSetChanged();
    }

    public  interface OnImageClickListener{

        void onImageClick(MyImages myImages);
    }

    public MyImages getPostion(int position){

        return imagesList.get(position);
    }





    @NonNull
    @Override
    public imageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);


        return new imageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull imageHolder holder, int position) {

        MyImages myImages = imagesList.get(position);
        holder.Title.setText(myImages.getImage_title());
        holder.Descp.setText(myImages.getImage_description());
        holder.image.setImageBitmap(BitmapFactory.decodeByteArray(myImages.getImage(), 0,
                myImages.getImage().length));


    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class imageHolder extends RecyclerView.ViewHolder{

        TextView Title;
        TextView Descp;
        ImageView image;

        public imageHolder(@NonNull View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.imagetitle);
            Descp = itemView.findViewById(R.id.imagedescp);
            image = itemView.findViewById(R.id.imagecv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    if(listener != null && position != RecyclerView.NO_POSITION){

                        listener.onImageClick(imagesList.get(position));

                    }

                }
            });

        }
    }
}
