package com.volksoftech.sample.dump;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.volksoftech.sample.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;


public class RecyclerViewWithImageCapture extends AppCompatActivity {
    ArrayList<ImagesData> dataArrayList = new ArrayList<>();
    RecyclerView recyclerView = null;
    ImageCaptureAdapter myAdapter = null;
    int position;
    String imageTempName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        ImagesData imagesData = new ImagesData();
        ImagesData imagesData1 = new ImagesData();
        ImagesData imagesData2 = new ImagesData();
        ImagesData imagesData3 = new ImagesData();
        imagesData.setName("CustomerPhoto");
        imagesData.setPath("CustomerPhoto");
        imagesData1.setName("HusbandPhoto");
        imagesData1.setPath("HusbandPhoto");
        imagesData2.setName("SpousePhoto");
        imagesData2.setPath("SpousePhoto");
        imagesData3.setName("KYCPhoto");
        imagesData3.setPath("KYCPhoto");
        dataArrayList.add(imagesData);
        dataArrayList.add(imagesData1);
        dataArrayList.add(imagesData2);
        dataArrayList.add(imagesData3);
        myAdapter = new ImageCaptureAdapter(RecyclerViewWithImageCapture.this, dataArrayList);
        recyclerView.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener((view, position, myData) -> {
            switch (view.getId()) {
                case R.id.image_view:
                    setImage(myData.getPath());
                    break;
                case R.id.camera:
                    captureImage(position, myData.getName());
                    break;
            }
        });
    }

    public void setImage(String path) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // builder.setTitle("Image");
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater != null ? inflater.inflate(R.layout.imageview, null) : null;
            // View view = getLayoutInflater().inflate(R.layout.imageview, null);
            Bitmap bitmapOriginal = BitmapFactory.decodeFile(path);
            ImageView imageView1 = null;
            if (view != null) {
                imageView1 = view.findViewById(R.id.iv_image);
                imageView1.setImageBitmap(bitmapOriginal);
            }
            builder.setView(view);
            builder.setPositiveButton("Close", (dialogInterface, arg1) -> {
                return;
            });
            builder.setCancelable(false);
            builder.create();
            builder.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void captureImage(int pos, String imageName) {
        position = pos;
        imageTempName = imageName;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != AppCompatActivity.RESULT_CANCELED) {
            if (requestCode == 100) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) Objects.requireNonNull(extras).get("data");
        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        Uri tempUri = getImageUri(getApplicationContext(), Objects.requireNonNull(imageBitmap), imageTempName);
        String picturePath = getRealPathFromURI(tempUri);
        myAdapter.setImageInItem(position, imageBitmap, picturePath);

    }

    public Uri getImageUri(Context inContext, Bitmap inImage, String imageName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, imageName, null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        Objects.requireNonNull(cursor).moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
