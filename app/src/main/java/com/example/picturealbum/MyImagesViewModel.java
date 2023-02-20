package com.example.picturealbum;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MyImagesViewModel extends AndroidViewModel {

    private MyImagesRepository repository;
    private LiveData<List<MyImages>> imageList;

    public MyImagesViewModel(@NonNull Application application) {
        super(application);

        repository = new MyImagesRepository(application);
        imageList  = repository.getAllImages();

    }

    public void Insert(MyImages myimages){
        repository.Insert(myimages);


    }

    public void Update(MyImages myimages){
        repository.Update(myimages);

    }

    public void Delete(MyImages myimages){

        repository.Delete(myimages
        );

    }

    public LiveData<List<MyImages>> getAllImages(){
        return imageList;
    }



}
