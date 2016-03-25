package com.example.tom.vrvisualisertest;

/**
 * Created by Tom on 24/03/2016.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class VisualizerView extends View {

  private byte[] audioBytes;
  private float[] drawPoints;
  private Rect screenRect = new Rect();
  private Paint paint = new Paint();
  private int divisions = 32; //Power of 2!


  public VisualizerView(Context context) {
    super(context);
    init();
  }

  public VisualizerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public VisualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    audioBytes = null;
    paint.setStrokeWidth(5f);
    paint.setAntiAlias(true);
    paint.setColor(Color.rgb(0, 128, 0));
  }

  public void updateVisualizer(byte[] bytes) {
    audioBytes = bytes;
    invalidate();
  }

  public void updateVisualizerWithFft(byte[] bytes) {
    audioBytes = bytes;
    invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (audioBytes == null) {
      return;
    }
    if (drawPoints == null || drawPoints.length < audioBytes.length * 4) {
      drawPoints = new float[audioBytes.length * 2];
    }

    screenRect.set(0, 0, getWidth(), getHeight());  //Redefine screen view.

    int offset = screenRect.width()/5;
    for (int i = 0; i < audioBytes.length / divisions; i++) {
      drawPoints[i * 4] = (((screenRect.width() - offset) / divisions) * i) + offset/2;
      drawPoints[i * 4 + 2] = (((screenRect.width() - offset) / divisions) * i) + offset/2;
      byte rfk = audioBytes[divisions * i];
      byte ifk = audioBytes[divisions * i + 1];
      float magnitude = (rfk * rfk + ifk * ifk);
      int dbValue = (int) (40 * Math.log10(magnitude));

      if(true)
      {
        drawPoints[i * 4 + 1] = 0;
        drawPoints[i * 4 + 3] = (dbValue * 2 - 10);
      }
      else
      {
        drawPoints[i * 4 + 1] = screenRect.height();
        drawPoints[i * 4 + 3] = screenRect.height() - (dbValue * 2 - 10);
      }
    }
  
    canvas.drawLines(drawPoints, paint);
}

}


/*


    super.onDraw(canvas);
    if (audioBytes == null) {
      return;
    }
    if (drawPoints == null || drawPoints.length < audioBytes.length * 4) {
      drawPoints = new float[audioBytes.length * 2];
    }

    screenRect.set(0, 0, getWidth(), getHeight());  //Redefine screen view.

    //Create array of Points to draw.
    for (int i = 0; i < audioBytes.length - 1; i++) {
      //Point 1 x.
      drawPoints[i * 2] = screenRect.width() * i / (audioBytes.length - 1);
      //Point 1 y. Add screenRect.Height/2 to make it centered on screen, + 128 to get it to 0 - 256.
      drawPoints[i * 2 + 1] = (screenRect.height() / 2) + ((byte) (audioBytes[i] + 128)) * (screenRect.height() / 2) / 128;

      //System.out.println(audioBytes[i]);  //DEBUG.
    }
    canvas.drawPoints(drawPoints, paint);
 */