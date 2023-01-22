package com.example.demo;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "MyDataBase";
    private static final String TABLE_NAME = "TableContact";
    private static final String COL1 = "ID";
    private static final String COL2 = "NAME";
    private static final String COL3 = "NUMBER";
    private static final String COL4 = "IMAGE";

    private final Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + "ID INTEGER PRIMARY KEY AUTOINCREMENT," + "NAME TEXT," + "NUMBER TEXT," + "IMAGE BLOB" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    protected boolean insertData(DBModel model) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, model.getName());
        contentValues.put(COL3, model.getNumber());
        contentValues.put(COL4, bitmapToByteArray(model.getImage()));

        long flag = database.insert(TABLE_NAME, null, contentValues);

        return flag != -1;
    }

    protected boolean deleteData(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        int flag = database.delete(TABLE_NAME, "ID = ?", new String[]{String.valueOf(id)});

        return flag != -1;
    }



    protected List<DBModel> getData() {
        try {
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            List<DBModel> itemList = new ArrayList<>();
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    int idData = cursor.getInt(0);
                    String nameData = cursor.getString(1);
                    String numberData = cursor.getString(2);
                    byte[] imageInBytes = cursor.getBlob(3);

                    Bitmap imageInBitmap = BitmapFactory.decodeByteArray(imageInBytes, 0, imageInBytes.length);

                    DBModel model = new DBModel();
                    model.setId(idData);
                    model.setName(nameData);
                    model.setNumber(numberData);
                    model.setImage(imageInBitmap);

                    itemList.add(model);
                }
            } else {
                showDialog();
            }
            return itemList;
        } catch (Exception e) {
            Toast.makeText(context, "DBHelper->getData() " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    protected static byte[] bitmapToByteArray(Bitmap imageInBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageInBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Contact not available...");
        builder.setMessage("Are you want to add contact ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(context, MainActivity2.class));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected boolean updateData(DBModel model) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, model.getName());
        contentValues.put(COL3, model.getNumber());

        int flag = database.update(TABLE_NAME, contentValues, "ID = ?", new String[]{String.valueOf(model.getId())});

        return flag != -1;
    }
}



















