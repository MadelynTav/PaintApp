package nyc.apprentice.madelyntavarez.paintapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import butterknife.Bind;
import butterknife.BindDimen;

public class MainActivity extends AppCompatActivity {


    FloatingActionButton currBrushSize;
    FloatingActionButton currColor;
    FloatingActionButton  erase;
    FloatingActionButton  trash;
    FloatingActionButton save;
    FloatingActionButton share;
    float smallBrush;
    float mediumBrush;
    float largeBrush;
    private CustomView customView;
    Dialog colorDialog;
    Context context=this;
    Dialog brushDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        currColor = (FloatingActionButton) findViewById(R.id.color_button);
        currBrushSize= (FloatingActionButton) findViewById(R.id.brush_button);
        customView = (CustomView) findViewById(R.id.drawing_view);
        erase = (FloatingActionButton) findViewById(R.id.erase);
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);
        save = (FloatingActionButton) findViewById(R.id.save);
        trash = (FloatingActionButton) findViewById(R.id.trash);
        customView.setDrawingCacheEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();

        currBrushSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView.setErase(false);
                brushDialog = new Dialog(context);
                brushDialog.setContentView(R.layout.brush_size_picker);
                brushDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                brushDialog.show();
            }
        });

        currColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView.setErase(false);
                colorDialog = new Dialog(context);
                colorDialog.setContentView(R.layout.color_picker);
                colorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                colorDialog.show();
            }
        });

        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView.setErase(true);
                brushDialog = new Dialog(context);
                brushDialog.setContentView(R.layout.brush_size_picker);
                brushDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                brushDialog.show();
            }
        });

        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView.clearCanvas();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView.setDrawingCacheEnabled(true);
                String imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(), customView.getDrawingCache(),
                        UUID.randomUUID().toString()+".png", "drawing");

                if (imgSaved!=null){
                    Toast.makeText(context, "Image Saved!",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Unable To Save Image! Try Again",Toast.LENGTH_SHORT).show();
                }

                customView.destroyDrawingCache();
            }

        });

//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    //Bitmap bitmap= customView.getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File("/mnt/sdcard/arun.jpg")));
//                } catch (Exception e) {
//                    Log.e("Error--------->", e.toString());
//                }
//            }
//        });
    }

    public void changeBrushSize(View v){
        float tag= Integer.parseInt(String.valueOf(v.getTag()));
        customView.setBrushSize(tag);

        if (!customView.isErase()) {
            customView.setLastBrushSize(tag);
            Log.i("size","UP");
        }

        brushDialog.dismiss();
    }

    public void changeBrushColor(View v){
        String color=v.getTag().toString();
        int color1= Color.parseColor(color);
        customView.setPaintColor(color1);
        colorDialog.dismiss();
    }
}
