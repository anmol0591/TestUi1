package com.pawcare.source.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;



/**
 * Created by igupta on 7/18/2015.
 */
public class ImageCapture {

    /**
     *  Displays the selected or captured image in the ImageView.
     * @param viewImage
     * @param data
     * @param context
     * @param requestCode
     */
    public static byte[] displayImage(ImageView viewImage, Intent data,final Context context, int requestCode) {
        byte[] imageBytes = null;
        File f = null;
        if (requestCode == 1) {
            Log.d("PAWED", "Ishita: Option Take Photo is selected. Save new.");
            f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }
            try {
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inSampleSize = 2;

                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                        bitmapOptions);

                int height = bitmap.getHeight(), width = bitmap.getWidth();

                if (height > 1280 && width > 960) {
                    Bitmap displayBitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    viewImage.setImageBitmap(displayBitmap);

                    Log.d("PAWED", "Ishita: Bitmap too large. Resizing.");

                } else {
                    viewImage.setImageBitmap(bitmap);
                    Log.d("PAWED", "Ishita: No need to resize.");
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageBytes = stream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 2) {
            Log.d("PAWED", "Ishita: Option Choose from gallery is selected.");
            Uri selectedImage = data.getData();
            f = new File(selectedImage.getPath());
            Bitmap bitmap = null;
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = 2;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);

                viewImage.setImageBitmap(bitmap);

                Log.d("PAWED", "Ishita: Bitmap too large. Resizing.");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                imageBytes = stream.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageBytes;
    }
}
