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

  protected short[] mAudioData = new short[1024];
  private byte[] audioBytes;
  private float[] drawPoints;
  private Rect screenRect = new Rect();
  private Paint paint = new Paint();
  private int divisions = 2; //Power of 2!


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

    for (int i = 0; i < audioBytes.length / divisions; i++) {
      drawPoints[i * 4] = i * 4 * divisions;
      drawPoints[i * 4 + 2] = i * 4 * divisions;
      byte rfk = audioBytes[divisions * i];
      byte ifk = audioBytes[divisions * i + 1];
      float magnitude = (rfk * rfk + ifk * ifk);
      int dbValue = (int) (10 * Math.log10(magnitude));

      if (true) {
        drawPoints[i * 4 + 1] = 0;
        drawPoints[i * 4 + 3] = (dbValue * 4 - 10);
      } else {
        drawPoints[i * 4 + 1] = screenRect.height();
        drawPoints[i * 4 + 3] = screenRect.height() - (dbValue * 4 - 10);
      }
    }

    canvas.drawLines(drawPoints, paint);
  }
}