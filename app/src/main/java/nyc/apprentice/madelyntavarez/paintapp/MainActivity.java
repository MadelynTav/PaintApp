package nyc.apprentice.madelyntavarez.paintapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    FloatingActionButton currBrushSize;
    FloatingActionButton currColor;
    FloatingActionButton erase;
    FloatingActionButton trash;
    FloatingActionButton save;
    FloatingActionButton share;
    float smallBrush;
    float mediumBrush;
    float largeBrush;
    private CustomView customView;
    Dialog colorDialog;
    Context context = this;
    Dialog brushDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        currColor = (FloatingActionButton) findViewById(R.id.color_button);
        currBrushSize = (FloatingActionButton) findViewById(R.id.brush_button);
        share = (FloatingActionButton) findViewById(R.id.share);
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
                brushDialog.setCanceledOnTouchOutside(true);
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
                colorDialog.setCanceledOnTouchOutside(true);
                colorDialog.show();
            }
        });

        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView.setErase(true);
                brushDialog = new Dialog(context);
                brushDialog.setContentView(R.layout.eraser_size_picker);
                brushDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                brushDialog.setCanceledOnTouchOutside(true);
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
                        UUID.randomUUID().toString() + ".png", "drawing");

                if (imgSaved != null) {
                    Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Unable To Save Image! Try Again", Toast.LENGTH_SHORT).show();
                }

                customView.destroyDrawingCache();
            }

        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File sharefile = customView.shareImage(context);
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("image/png");
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + sharefile));
                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });
    }

    public void changeBrushSize(View v) {
        float tag = Integer.parseInt(String.valueOf(v.getTag()));
        customView.setBrushSize(tag);
        if (!customView.isErase()) {
            customView.setLastBrushSize(tag);
            Log.i("size", "UP");
        }
        brushDialog.dismiss();
    }

    public void changeBrushColor(View v) {
        String color = v.getTag().toString();
        int color1 = Color.parseColor(color);
        customView.setPaintColor(color1);
        colorDialog.dismiss();
    }

}
