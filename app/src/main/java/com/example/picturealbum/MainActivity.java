package com.example.picturealbum;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyImagesViewModel myImagesViewModel;
    RecyclerView rv;
    FloatingActionButton fab;
    private ActivityResultLauncher<Intent> activityResultLauncherForAddImage;

    private ActivityResultLauncher<Intent> activityResultLauncherForEditImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.floating);
        registerActivityForAddImage();
        registerActivityForEditImage();
        rv.setLayoutManager(new LinearLayoutManager(this));

        ImageAdapter adapter = new ImageAdapter();
        rv.setAdapter(adapter);

        myImagesViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(MyImagesViewModel.class);
        myImagesViewModel.getAllImages().observe(MainActivity.this, new Observer<List<MyImages>>() {
            @Override
            public void onChanged(List<MyImages> myImages) {

                adapter.setImagesList(myImages);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                activityResultLauncherForAddImage.launch(intent);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0
        ,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                myImagesViewModel.Delete(adapter.getPostion(viewHolder.getAdapterPosition()));

            }
        }).attachToRecyclerView(rv);

        adapter.setListener(new ImageAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(MyImages myImages) {

                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("id",myImages.getImage_id());
                intent.putExtra("title",myImages.getImage_title());
                intent.putExtra("description",myImages.getImage_description());
                intent.putExtra("image",myImages.getImage());
                activityResultLauncherForEditImage.launch(intent);


            }
        });
        
    }
    
    public void registerActivityForAddImage(){
        
        activityResultLauncherForAddImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if(resultCode == RESULT_OK && data != null){

                            String title = data.getStringExtra("title");
                            String description = data.getStringExtra("description");
                            byte[] image = data.getByteArrayExtra("image");

                            MyImages myImages = new MyImages(title,description,image);
                            myImagesViewModel.Insert(myImages);

                        }


                    }
                });
        
    }

    public void registerActivityForEditImage(){
        activityResultLauncherForEditImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if(resultCode == RESULT_OK && data != null){

                            String title = data.getStringExtra("uptitle");
                            String description = data.getStringExtra("updescription");
                            byte[] image = data.getByteArrayExtra("image");
                            int id = data.getIntExtra("id",-1);

                            MyImages myImages = new MyImages(title,description,image);
                            myImages.setImage_id(id);
                            myImagesViewModel.Update(myImages);

                        }

                    }
                });
    }
    
}