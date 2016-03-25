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

  private byte[] fftBytes, waveBytes;
  private float[] fftDrawPoints, waveDrawPoints;
  private Rect screenRect = new Rect();
  private Paint fftPaint = new Paint(), wavePaint = new Paint();
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
    fftBytes = null;
    waveBytes = null;

    fftPaint.setStrokeWidth(5f);
    fftPaint.setAntiAlias(true);
    fftPaint.setColor(Color.rgb(0, 250, 0));

    wavePaint.setStrokeWidth(2f);
    wavePaint.setAntiAlias(true);
    wavePaint.setColor(Color.rgb(200, 0, 0));
  }

  public void updateVisualizerFft(byte[] bytes) {
    fftBytes = bytes;
    invalidate();
  }

  public void updateVisualizerWave(byte[] bytes) {
    waveBytes = bytes;
    invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    screenRect.set(0, 0, getWidth(), getHeight());  //Redefine screen view.

    if (fftBytes != null) {

      if (fftDrawPoints == null || fftDrawPoints.length < fftBytes.length * 4) {
        fftDrawPoints = new float[fftBytes.length * 2];
      }

      for (int i = 0; i < fftBytes.length / divisions; i++) {
        fftDrawPoints[i * 4] = i * 4 * divisions;
        fftDrawPoints[i * 4 + 2] = i * 4 * divisions;
        byte rfk = fftBytes[divisions * i];
        byte ifk = fftBytes[divisions * i + 1];
        float magnitude = (rfk * rfk + ifk * ifk);
        int dbValue = (int) (10 * Math.log10(magnitude));

        if (false) {
          fftDrawPoints[i * 4 + 1] = 0;
          fftDrawPoints[i * 4 + 3] = (dbValue * 4 - 10);
        } else {
          fftDrawPoints[i * 4 + 1] = ((screenRect.height()/2)+screenRect.height()/4) + (dbValue * 3);
          fftDrawPoints[i * 4 + 3] = ((screenRect.height()/2)+screenRect.height()/4) - (dbValue * 3);
          //fftDrawPoints[i * 4 + 1] = screenRect.height();
          //fftDrawPoints[i * 4 + 3] = screenRect.height() - (dbValue * 4 - 10);
        }
      }
    }

    if (waveBytes != null) {

      if (waveDrawPoints == null || waveDrawPoints.length < waveBytes.length * 4) {
        waveDrawPoints = new float[waveBytes.length * 4];
      }
      screenRect.set(0, 0, getWidth(), getHeight());
      for (int i = 0; i < waveBytes.length - 1; i++) {
        waveDrawPoints[i * 4] = screenRect.width() * i / (waveBytes.length - 1);
        waveDrawPoints[i * 4 + 1] = screenRect.height() / 4
            + ((byte) (waveBytes[i] + 128)) * (screenRect.height() / 2) / 128;
        waveDrawPoints[i * 4 + 2] = screenRect.width() * (i + 1) / (waveBytes.length - 1);
        waveDrawPoints[i * 4 + 3] = screenRect.height() / 4
            + ((byte) (waveBytes[i + 1] + 128)) * (screenRect.height() / 2) / 128;
      }
    }

    canvas.drawLines(fftDrawPoints, fftPaint);
    canvas.drawLines(waveDrawPoints, wavePaint);
  }
}