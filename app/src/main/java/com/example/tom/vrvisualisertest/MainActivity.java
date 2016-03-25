package com.example.tom.vrvisualisertest;
import android.support.v7.app.AppCompatActivity;
import android.media.AudioManager;
import android.media.audiofx.Visualizer;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private VisualizerView visualiserView;
    private Visualizer visualiser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        visualiserView = (VisualizerView) findViewById(R.id.myvisualizerview);

        //Set audio adjustment to Media instead of phone ringing volume.
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //Setup the visualiser, choosing whether to use audio from another app or microphone.
        setupVisualiser(false); //True = use microphone.
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            visualiser.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        visualiserView.setEnabled(false);
    }

    private void setupVisualiser(Boolean useMicrophone) {
        // Create the Visualizer object and attach it to our input.
        if (useMicrophone) {
            //visualiser = new Visualizer(CODE TO SETUP MICROPHONE INPUT HERE);
        } else {
            visualiser = new Visualizer(0);
        }

        visualiser.setCaptureSize(Visualizer.getCaptureSizeRange()[1]); //Set capture size to max.

        //Setup listener to fill bytes with waveform data every sampleRate time.
        visualiser.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        visualiserView.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);

        visualiser.setEnabled(true); //Enabled only when needed, after setCaptureSize.
    }
}