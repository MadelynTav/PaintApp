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
import android.widget.Toast;
import java.io.File;
import java.util.UUID;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.brush_button)FloatingActionButton brushButton;
    @Bind(R.id.color_button)FloatingActionButton colorButton;
    @Bind(R.id.erase)FloatingActionButton eraseButton;
    @Bind(R.id.trash)FloatingActionButton trashButton;
    @Bind(R.id.save)FloatingActionButton saveButton;
    @Bind(R.id.share)FloatingActionButton shareButton;
    private CustomView customView;
    Dialog colorDialog;
    Context context = this;
    Dialog brushDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        customView = (CustomView) findViewById(R.id.drawing_view);
        customView.setDrawingCacheEnabled(true);
        ButterKnife.bind(this);
        setUpClickListeners();


    }

    private void setUpClickListeners() {
        brushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView.setErase(false);
                brushDialog = new Dialog(context);
                brushDialog.setContentView(R.layout.brush_size_picker);
                brushDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                brushDialog.setCanceledOnTouchOutside(true);
                brushDialog.show();
            }
        });

        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView.setErase(false);
                colorDialog = new Dialog(context);
                colorDialog.setContentView(R.layout.color_picker);
                colorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                colorDialog.setCanceledOnTouchOutside(true);
                colorDialog.show();
            }
        });

        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView.setErase(true);
                brushDialog = new Dialog(context);
                brushDialog.setContentView(R.layout.eraser_size_picker);
                brushDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                brushDialog.setCanceledOnTouchOutside(true);
                brushDialog.show();
            }
        });

        trashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView.clearCanvas();

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
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

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File sharefile = customView.shareImage(context);
                Intent share = new Intent(Intent.ACTION_SEND);
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
