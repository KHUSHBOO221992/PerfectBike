package com.example.prith.perfectbike;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class UpdateBikeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 100;
    EditText updatebrand, updateyear;

    ImageView bimage;
    ImageButton btnimage;

    Uri selectedImage;
    private Bitmap bitmap;

    Button btnsave;
    String data;

    DBBike dbHelper;
    SQLiteDatabase bikeDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bike);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabbtnBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuIntent = new Intent(UpdateBikeActivity.this, MyBikeActivity.class);
                startActivity(menuIntent);
            }
        });

        SharedPreferences sp = getSharedPreferences("com.example.prith.perfectbike.shared", Context.MODE_PRIVATE);
        data = sp.getString("username","Data Missing");

        updatebrand =(EditText)findViewById(R.id.UedtBrand);
        updateyear = (EditText)findViewById(R.id.UedtYear);

        bimage = (ImageView) findViewById(R.id.UimgBImage);
        bimage.setOnClickListener(this);

        btnimage = (ImageButton) findViewById(R.id.UbtnImage);
        btnimage.setOnClickListener(this);

        btnsave = (Button) findViewById(R.id.btnSave);
        btnsave.setOnClickListener(this);

        dbHelper = new DBBike(this);
        displayData();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == bimage.getId() || view.getId() == btnimage.getId())
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, RESULT_LOAD_IMAGE);
        }
        else if(view.getId() == btnsave.getId()) {
            updateData();
            Toast.makeText(this,"Bike Details has been update succssfully",Toast.LENGTH_LONG).show();
            Intent ubikeintent = new Intent(this,MyBikeActivity.class);
            startActivity(ubikeintent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            selectedImage = data.getData();

            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");

            bimage.setImageBitmap(bitmap);
        }
    }

    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void displayData() {
        try {
            bikeDB = dbHelper.getReadableDatabase();
            String columns[] = {"Brand", "Year", "BImage"};
            Cursor cursor = bikeDB.query("BikeInfo", columns, "Brand", null, null, null, null, null);
            while (cursor.moveToNext()) {
                updatebrand.setText(cursor.getString(cursor.getColumnIndex("Brand")));
                updateyear.setText(cursor.getString(cursor.getColumnIndex("Year")));
                //bimage.setImageData(cursor.getBlob(cursor.getColumnIndex("BImage")));
            }
        }catch(Exception e)
        {
            Log.e("MyBikeActivity", "Unable to fetch the records");
        }
        bikeDB.close();
    }

    private void updateData()
    {
        String uploadImage = getStringImage(bitmap);
        String updbrand = updatebrand.getText().toString();
        String updyear = updateyear.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put("Brand",updbrand);
        cv.put("Year",updyear);
        cv.put("BImage",uploadImage);
        try{
            bikeDB = dbHelper.getWritableDatabase();
            bikeDB.update("BikeInfo",cv,"Brand = ?",new String[]{data});
            Log.v("Update record","Successful");
        }catch (Exception e){
            Log.e("Update Bike Details",e.getMessage());
        }
        bikeDB.close();
    }
}
