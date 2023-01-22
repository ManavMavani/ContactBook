package com.example.demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.databinding.ActivityMain3Binding;

public class MainActivity3 extends AppCompatActivity {

    private ActivityMain3Binding binding;
    private int idData;
    private byte[] imageInBytes;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DBHelper(MainActivity3.this);

        try {
            Intent intent = getIntent();
            idData = intent.getIntExtra("id", -1);
            binding.name.setText(intent.getStringExtra("name").toString());
            binding.number.setText(intent.getStringExtra("number").toString());
            binding.name.requestFocus();
            imageInBytes = intent.getByteArrayExtra("image");
            binding.imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageInBytes, 0, imageInBytes.length));
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void updateData(View view) {
        try {
            String nameData = binding.name.getText().toString();
            String numberData = binding.number.getText().toString();
            if (nameData.isEmpty() || numberData.isEmpty() || imageInBytes == null) {
                if (nameData.isEmpty())
                    binding.name.setError("Please Enter Name");
                else if (numberData.isEmpty())
                    binding.number.setError("Please Enter Number");
                else
                    Toast.makeText(this, "Update ImageView", Toast.LENGTH_SHORT).show();
            } else {
                DBModel model = new DBModel();
                model.setId(idData);
                model.setName(nameData);
                model.setNumber(numberData);
                boolean flag = dbHelper.updateData(model);
                if (flag) {
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity3.this, MainActivity.class));
                    MainActivity.setRecyclerView(MainActivity3.this);
                } else
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "saveData()" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}