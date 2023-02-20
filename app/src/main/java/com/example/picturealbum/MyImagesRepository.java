package com.example.picturealbum;


import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyImagesRepository {

    private MyImagesDAO myImagesDAO;
    private LiveData<List<MyImages>> imagesList;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MyImagesRepository(Application application){

        MyImageDatabase database = MyImageDatabase.getInstance(application);
        myImagesDAO = database.myImagesDAO();
        imagesList = myImagesDAO.getAllImages();


    }


    public void Insert(MyImages myimages){

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDAO.Insert(myimages);
            }
        });

    }

    public void Update(MyImages myimages){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDAO.Update(myimages);
            }
        });

    }

    public void Delete(MyImages myimages){

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                myImagesDAO.Delete(myimages);
            }
        });
    }

    public LiveData<List<MyImages>> getAllImages(){
        return imagesList;
    }

}
