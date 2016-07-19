package com.example.apple.drawingcanvas;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Rawght Steven on 6/19/16, 13.
 * Email:rawghtsteven@gmail.com
 */
public class DrawView extends View {

    float preX,preY;
    float endX = 0,endY = 0;
    private Path path;
    public Paint paint;
    Bitmap cacheBitmap =  null;
    Canvas cacheCanvas = null;
    public static int DRAWING_TYPE = 0;

    public static void setDrawingType(int drawingType) {
        DRAWING_TYPE = drawingType;
    }

    public DrawView(Context context, int width, int height){
        super(context);
        cacheBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        path = new Path();
        cacheCanvas.setBitmap(cacheBitmap);
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                preX = x;
                preY = y;
                switch (DRAWING_TYPE){
                    case 1:
                        path.moveTo(preX,preY);//圆
                        break;
                    case 2:
                        path.moveTo(preX,preY);//矩形
                        break;
                    case 3:
                        path.moveTo(preX,preY);//椭圆
                        break;
                    case 4:
                        path.moveTo(preX,preY);//直线
                        break;
                    case 5:
                        if (endX==0&&endY==0) {
                            path.moveTo(preX, preY);//钢笔
                        }else
                        {
                            path.moveTo(endX,endY);
                        }
                        break;
                    case 0:path.moveTo(x,y);//铅笔
                        break;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                switch (DRAWING_TYPE){
                    case 1:
                        path.addCircle(preX,preY, (float) Math.sqrt(Math.pow(Math.abs(preX-x),2)+Math.pow(Math.abs(preY-y),2)), Path.Direction.CW);
                        //cacheCanvas.drawCircle(preX,preY, (float) Math.sqrt(Math.pow(Math.abs(preX-x),2)+Math.pow(Math.abs(preY-y),2)),paint);
                        break;
                    case 2:
                        path.addRect(x,y,preX,preY,Path.Direction.CW);
                        break;
                    case 3:
                        path.addOval(x,y,preX,preY, Path.Direction.CW);
                        break;
                    case 4:
                        path.lineTo(x,y);
                        break;
                    case 5:
                        path.lineTo(x,y);
                        break;
                    case 0:
                        path.quadTo(preX,preY,x,y);
                        preX = x;
                        preY = y;
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e("DRAWINGTYPE", String.valueOf(DRAWING_TYPE));
                switch (DRAWING_TYPE){
                    case 1:
                        cacheCanvas.drawCircle(preX,preY, (float) Math.sqrt(Math.pow(Math.abs(preX-x),2)+Math.pow(Math.abs(preY-y),2)),paint);
                        path.reset();
                        break;
                    case 2:
                        cacheCanvas.drawRect(x,y,preX,preY,paint);
                        path.reset();
                        break;
                    case 3:
                        cacheCanvas.drawOval(x,y,preX,preY,paint);
                        path.reset();
                        break;
                    case 4:cacheCanvas.drawLine(preX,preY,x,y,paint);
                        path.reset();
                        break;
                    case 5:
                        if (endX==0&&endY==0) {
                            cacheCanvas.drawLine(preX, preY, x, y, paint);
                            endX = x;
                            endY = y;
                            path.reset();
                        }else{
                            cacheCanvas.drawLine(endX,endY,x,y,paint);
                            endX = x;
                            endY = y;
                            path.reset();
                        }
                        break;
                    case 0:
                        cacheCanvas.drawPath(path,paint);
                        path.reset();
                        break;
                }
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint bmpPaint = new Paint();
        canvas.drawBitmap(cacheBitmap,0,0,bmpPaint);
        canvas.drawPath(path,paint);
    }
}
