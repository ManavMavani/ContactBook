package com.example.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private static final int REQUEST_CODE = 100;

    private final Context context;
    private List<DBModel> itemList;
    private DBHelper dbHelper;

    public Context getContext() {
        return context;
    }

    public ItemAdapter(Context context, List<DBModel> itemList, DBHelper dbHelper) {
        this.context = context;
        this.itemList = itemList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setImageBitmap(itemList.get(position).getImage());
        holder.name.setText(itemList.get(position).getName());
        holder.number.setText(itemList.get(position).getNumber());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int index = holder.getAdapterPosition();
                    phoneIntent(itemList.get(index).getNumber());
                } catch (Exception e) {
                    Toast.makeText(context, "In Function Call" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try {
                    Intent intent = new Intent(context, MainActivity3.class);
                    intent.putExtra("id", itemList.get(holder.getAdapterPosition()).getId());
                    intent.putExtra("name", itemList.get(holder.getAdapterPosition()).getName());
                    intent.putExtra("number", itemList.get(holder.getAdapterPosition()).getNumber());
                    intent.putExtra("image", DBHelper.bitmapToByteArray(itemList.get(holder.getAdapterPosition()).getImage()));
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "ItemAdapter-> " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private void phoneIntent(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
            return;
        }
        context.startActivity(callIntent);
    }

    public void deleteData(int position) {
        boolean flag = dbHelper.deleteData(itemList.get(position).getId());
        Toast.makeText(context, itemList.get(position).getId() + "", Toast.LENGTH_LONG).show();
        if (flag) {
            MainActivity.setRecyclerView(context);
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, number;
        CircleImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
        }
    }

}
