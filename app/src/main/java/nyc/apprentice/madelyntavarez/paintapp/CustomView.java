package nyc.apprentice.madelyntavarez.paintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by madelyntavarez on 1/4/16.
 */

public class CustomView extends View {

    private Path drawPath;
    Paint canvasPaint, drawPaint;
    private int paintColor = 0xFF0000ff;
    private Canvas drawCanvas;
    float brushSize;
    float lastBrushSize;
    private Bitmap canvasBitmap;
    private boolean erase;
    private int lastColor;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpDrawing();
    }

    public void setUpDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;
        lastColor = paintColor;
        drawPaint.setStrokeWidth(brushSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    //save bitmap to file in order to shareButton via intent
    public File shareImage(Context context) {
        File cache = context.getExternalCacheDir();
        File sharefile = new File(cache, "toshare.png");

        drawCanvas.setBitmap(canvasBitmap);
        try {
            FileOutputStream out = new FileOutputStream(sharefile);
            canvasBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            Log.e("err", e.toString());
        }

        return sharefile;
    }

    public void setBrushSize(float newSize) {
        invalidate();
        if (!isErase()) {
            drawPaint.setColor(lastColor);
        }
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, getResources().getDisplayMetrics());
        brushSize = pixelAmount;
        lastBrushSize = brushSize;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastBrushSize) {
        this.lastBrushSize = lastBrushSize;
        setBrushSize(lastBrushSize);
    }

    public void setPaintColor(int paintColor) {
        invalidate();
        brushSize = lastBrushSize;
        drawPaint.setStrokeWidth(brushSize);
        this.paintColor = paintColor;
        lastColor = paintColor;
        drawPaint.setColor(paintColor);

    }

    public boolean isErase() {
        return erase;
    }

    public void setErase(boolean erase) {
        this.erase = erase;

        if (isErase()) {
            drawPaint.setColor(Color.WHITE);
        } else drawPaint.setXfermode(null);
    }

    public void clearCanvas() {
        invalidate();
        drawCanvas.drawColor(Color.WHITE);
    }
}
