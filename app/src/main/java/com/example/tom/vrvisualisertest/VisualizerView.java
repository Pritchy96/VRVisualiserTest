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
    private Paint mForePaint = new Paint();

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
        mForePaint.setStrokeWidth(1.5f);
        mForePaint.setAntiAlias(true);
        mForePaint.setColor(Color.rgb(0, 128, 0 ));
    }

    public void updateVisualizer(byte[] bytes) {
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
            drawPoints = new float[audioBytes.length * 4];
        }
        screenRect.set(0, 0, getWidth(), getHeight());
        for (int i = 0; i < audioBytes.length - 1; i++) {
            drawPoints[i * 4] = screenRect.width() * i / (audioBytes.length - 1);
            drawPoints[i * 4 + 1] = screenRect.height() / 2
                    + ((byte) (audioBytes[i] + 128)) * (screenRect.height() / 2) / 128;
            drawPoints[i * 4 + 2] = screenRect.width() * (i + 1) / (audioBytes.length - 1);
            drawPoints[i * 4 + 3] = screenRect.height() / 2
                    + ((byte) (audioBytes[i + 1] + 128)) * (screenRect.height() / 2)
                    / 128;
        }
        canvas.drawLines(drawPoints, mForePaint);
    }

}