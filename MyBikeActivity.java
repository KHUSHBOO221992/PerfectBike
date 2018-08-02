package com.example.prith.perfectbike;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import java.io.IOException;

public class MyBikeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    EditText edtbrand, edtyear;
    ImageView bimage;
    ImageButton btnimage;
    Button btnsave;
    Button btndisplay;
    Button btnupdate;

    Uri selectedImage;
    private Bitmap bitmap;

    DBBike dbBike;
    SQLiteDatabase BikeDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bike);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabbtnBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuIntent = new Intent(MyBikeActivity.this, MenuActivity.class);
                startActivity(menuIntent);
            }
        });

        edtbrand = (EditText) findViewById(R.id.edtBrand);
        edtyear = (EditText) findViewById(R.id.edtYear);

        bimage = (ImageView) findViewById(R.id.imgBImage);
        bimage.setOnClickListener(this);

        btnimage = (ImageButton) findViewById(R.id.btnImage);
        btnimage.setOnClickListener(this);

        btnsave = (Button) findViewById(R.id.btnSave);
        btnsave.setOnClickListener(this);

        btnupdate = (Button) findViewById(R.id.btnUpdate);
        btnupdate.setOnClickListener(this);

        btndisplay = (Button) findViewById(R.id.btnDisplay);
        btndisplay.setOnClickListener(this);

        dbBike = new DBBike(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == bimage.getId() || view.getId() == btnimage.getId())
        {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, RESULT_LOAD_IMAGE);

        }
        else if(view.getId() == btnsave.getId()) {
              insertData();
              displayData();
        }
        else if(view.getId() == btndisplay.getId()) {
             displayData();
        }
        else if(view.getId() == btnupdate.getId()) {
            Intent ubikeintent = new Intent(this,UpdateBikeActivity.class);
            startActivity(ubikeintent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            selectedImage = data.getData();

            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");

            bimage.setImageBitmap(bitmap);
        }
    }

    private void insertData()
    {
        String brand = edtbrand.getText().toString();
        String year = edtyear.getText().toString();

        if(edtbrand.equals("") || edtyear.equals(""))
        {
            Toast.makeText(this, "Data Missing!" , Toast.LENGTH_LONG).show();
        }
        else
        {
            String uploadImage = getStringImage(bitmap);


            ContentValues cv = new ContentValues();
            cv.put("Brand", brand);
            cv.put("Year", year);
            cv.put("BImage", uploadImage);

            try
            {
                BikeDB = dbBike.getWritableDatabase();
                BikeDB.insert("BikeInfo" ,null,cv);
            }
            catch(Exception e)
            {
                Log.e("INSERT BIKE INFORMATION", e.getMessage());
            }
            BikeDB.close();
            Toast.makeText(this, "Data Updated Successful! Bike Brand: " + brand , Toast.LENGTH_LONG).show();
            Intent PageIntent = new Intent(this, MyBikeActivity.class);
            startActivity(PageIntent);
        }

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    private void displayData()
    {
        try{
            BikeDB = dbBike.getReadableDatabase();
            String columns[] = {"Brand","Year","BImage"};
            Cursor cursor = BikeDB.query("BikeInfo",columns,null,null,null,null,null);

            while(cursor.moveToNext())
            {
                String brand = cursor.getString(cursor.getColumnIndex( "Brand"));
                String year = cursor.getString(cursor.getColumnIndex("Year"));
                byte[] bikeimage = cursor.getBlob(cursor.getColumnIndex("BImage"));
                String bikeInfo = brand + "\n" + year + "\n" + bikeimage;
                Toast.makeText(this,bikeInfo, Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e)
        {
            Log.e("BikeActivity:", "Unable To Fetch Records");
        }
    }
}
