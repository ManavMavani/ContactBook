package com.example.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setRecyclerView(MainActivity.this);

        binding.floatingButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, MainActivity2.class)));
    }

    protected static void setRecyclerView(Context context) {
        try {
            DBHelper dbHelper = new DBHelper(context);
            ItemAdapter adapter = new ItemAdapter(context, dbHelper.getData(), dbHelper);

            binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            binding.recyclerView.setHasFixedSize(true);
            binding.recyclerView.setAdapter(adapter);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));
            itemTouchHelper.attachToRecyclerView(binding.recyclerView);
        } catch (Exception e) {
            Toast.makeText(context, "MainActivity->setRecyclerView() " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

