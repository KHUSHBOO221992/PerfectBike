package com.example.prith.perfectbike;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.RestrictionEntry;
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
import java.io.IOException;
import java.sql.Blob;

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 100;
    EditText edtfname, edtlname;
    ImageView pimage;
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
        setContentView(R.layout.activity_my_profile);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabbtnBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuIntent = new Intent(MyProfileActivity.this, MenuActivity.class);
                startActivity(menuIntent);
            }
        });

        edtfname = (EditText) findViewById(R.id.edtFName);
        edtlname = (EditText) findViewById(R.id.edtLName);

        pimage = (ImageView) findViewById(R.id.imgPImage);
        pimage.setOnClickListener(this);

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
    public void onClick(View view) {
        if (view.getId() == pimage.getId() || view.getId() == btnimage.getId()) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, RESULT_LOAD_IMAGE);
        }
        else if (view.getId() == btnsave.getId()) {
            insertData();
            displayData();
        }
        else if (view.getId() == btndisplay.getId()) {
            displayData();
        }
        else if (view.getId() == btnupdate.getId()) {
            Intent uprofileintent = new Intent(this, UpdateProfileActivity.class);
            startActivity(uprofileintent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            selectedImage = data.getData();

            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");

            pimage.setImageBitmap(bitmap);
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void insertData() {
        String fname = edtfname.getText().toString();
        String lname = edtlname.getText().toString();

        if (edtfname.equals("") || edtlname.equals("")) {
            Toast.makeText(this, "Data Missing!", Toast.LENGTH_LONG).show();
        } else {
            String uploadImage = getStringImage(bitmap);

            ContentValues cv = new ContentValues();
            cv.put("PImage", uploadImage);
            cv.put("FName", fname);
            cv.put("LName", lname);

            try {
                BikeDB = dbBike.getWritableDatabase();
                BikeDB.insert("ProfileInfo", null, cv);
            } catch (Exception e) {
                Log.e("INSERT USER PROFILE", e.getMessage());
            }
            BikeDB.close();
            Toast.makeText(this, "Data Updated Successful! ProfileName: " + fname, Toast.LENGTH_LONG).show();
            Intent PageIntent = new Intent(this, MyProfileActivity.class);
            startActivity(PageIntent);
        }

    }

    private void displayData() {
        try {
            BikeDB = dbBike.getReadableDatabase();
            String columns[] = {"PImage", "FName", "LName"};
            Cursor cursor = BikeDB.query("ProfileInfo", columns, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                byte[] profileimage = cursor.getBlob(cursor.getColumnIndex("PImage"));
                String firstname = cursor.getString(cursor.getColumnIndex("FName"));
                String lastname = cursor.getString(cursor.getColumnIndex("LName"));
                String profileInfo = profileimage + "\n" + firstname + "\n" + lastname;
                Toast.makeText(this, profileInfo, Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e)
        {
            Log.e("MyProfileActivity", "Unable to fetch the records");
        }
        BikeDB.close();
    }
}
