package com.example.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity2 extends AppCompatActivity {

    private CircleImageView imageView;
    private EditText name, number;
    private DBHelper dbHelper;
    private Bitmap imageInBitmap;
    private static final int IMAGE_REQUEST_CODE = 200;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        imageView = findViewById(R.id.imageView);
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);

        name.requestFocus();

        dbHelper = new DBHelper(MainActivity2.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && requestCode == IMAGE_REQUEST_CODE && data != null && data.getData() != null) {
                Uri imageInUri = data.getData();
//                imageInBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageInUri);
                imageInBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageInUri));
                imageView.setImageBitmap(imageInBitmap);
            }
        } catch (Exception e) {
            Toast.makeText(this, "MainActivity2->nActivityResult() " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void chooseImage(View view) {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image..."), IMAGE_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "chooseImage()" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void saveData(View view) {
        try {
            String nameData = name.getText().toString();
            String numberData = number.getText().toString();

            if (nameData.isEmpty() || numberData.isEmpty() || (imageView.getDrawable() == null || imageInBitmap == null)) {
                if (nameData.isEmpty())
                    name.setError("Please Enter Name");
                else if (numberData.isEmpty())
                    number.setError("Please Enter Number");
                else if (imageView.getDrawable() == null)
                    Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
                else if (imageInBitmap == null)
                    imageInBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contact_logo1);

            } else {
                DBModel model = new DBModel();
                model.setName(nameData);
                model.setNumber(numberData);
                model.setImage(imageInBitmap);
                boolean flag = dbHelper.insertData(model);
                if (flag) {
                    Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();
                    name.setText("");
                    number.setText("");
                    imageView.setImageResource(R.drawable.contact_logo1);
                    name.requestFocus();
                    MainActivity.setRecyclerView(MainActivity2.this);
                } else
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "saveData()" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}