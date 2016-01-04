package nyc.apprentice.madelyntavarez.paintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by madelyntavarez on 1/4/16.
 */



public class CustomView extends View {

    private Path drawPath;
    private Paint canvasPaint, drawPaint;
    private int paintColor= 0xFF660000;
    private Canvas drawCanvas;
    private Bitmap bitmap;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpDrawing();
    }

    public void setUpDrawing(){
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20f);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }


}
