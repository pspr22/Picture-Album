package com.example.picturealbum;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditActivity extends AppCompatActivity {

    ImageView imageselu;
    EditText etTitleu,etDescpu;

    ActivityResultLauncher<Intent> activityResultLauncherForSelectImage;

    private Bitmap selectedImage;
    private Bitmap scaledImage;

    private String title,descriptioon;
    private int id;
    private byte[] image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        registerActivityForSelectImage();

        imageselu = findViewById(R.id.imgsele);
        etTitleu = findViewById(R.id.etitlee);
        etDescpu = findViewById(R.id.etdescpe);


        id = getIntent().getIntExtra("id",-1);
        title = getIntent().getStringExtra("title");
        descriptioon = getIntent().getStringExtra("description");
        image = getIntent().getByteArrayExtra("image");

        etTitleu.setText(title);
        etDescpu.setText(descriptioon);
        imageselu.setImageBitmap(BitmapFactory.decodeByteArray(image,0,
                image.length));

        imageselu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherForSelectImage.launch(intent);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.tick,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.tickmark:
                updateData();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    public void updateData(){


        if(id == -1){
            Toast.makeText(EditActivity.this, "There is a problem", Toast.LENGTH_SHORT).show();

        }else{

            String uptitle = etTitleu.getText().toString();
            String updescription = etDescpu.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("id",id);
            intent.putExtra("uptitle",uptitle);
            intent.putExtra("updescription",updescription);

            if(selectedImage == null){
                intent.putExtra("image",image);

            }else{


                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                scaledImage = makesmall(selectedImage,200);
                scaledImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
                scaledImage.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
                byte[] image = outputStream.toByteArray();
                intent.putExtra("image",image);


            }
            setResult(RESULT_OK,intent);
            finish();
        }

    }

    public void registerActivityForSelectImage(){

        activityResultLauncherForSelectImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        if(resultCode == RESULT_OK && data != null){

                            try {
                                selectedImage = MediaStore.
                                        Images.Media.getBitmap(getContentResolver(),data.getData());
                                imageselu.setImageBitmap(selectedImage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

    }

    public Bitmap makesmall(Bitmap image,int maxSize){

        int width = image.getWidth();
        int height = image.getHeight();

        float ratio = (float) width/(float) height;

        if(ratio >1){
            width = maxSize;
            height = (int) (width/ratio);
        }else{
            height = maxSize;
            width = (int) (height * ratio);
        }

        return Bitmap.createScaledBitmap(image,width,height,true);
    }


}