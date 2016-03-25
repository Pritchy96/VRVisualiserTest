package com.example.tom.vrvisualisertest;

import android.annotation.TargetApi;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private VisualizerView visualiserView;
  private Visualizer visualiser;
  private AudioRecord audioRecorder;
  private static final int RECORDING_SAMPLE_RATE = 44100;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    visualiserView = (VisualizerView) findViewById(R.id.myvisualizerview);

    //Set audio adjustment to Media instead of phone ringing volume.
    setVolumeControlStream(AudioManager.STREAM_MUSIC);

    //Setup the visualiser, choosing whether to use audio from another app or microphone.
    setupVisualiser(false
        , false, true); //True: Use microphone, collect waveform data, convert to fft.
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (isFinishing()) {
      visualiser.release();
    }
  }

    @Override
    protected void onResume() {
      super.onResume();
    }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    visualiserView.setEnabled(false);
  }

  private void initMicrophone() {
    int bufferSize = AudioRecord.getMinBufferSize(
        RECORDING_SAMPLE_RATE,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    );

    audioRecorder = new AudioRecord(
        MediaRecorder.AudioSource.MIC,
        RECORDING_SAMPLE_RATE,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        bufferSize
    );
    //audioRecorder.startRecording();
  }


  private void setupVisualiser(Boolean useMicrophone, Boolean getWaveform, Boolean getFft) {
    // Create the Visualizer object and attach it to our input.
    if (useMicrophone) {
      initMicrophone();
      visualiser = new Visualizer(audioRecorder.getAudioSessionId());
    } else {
      visualiser = new Visualizer(0);
    }

    visualiser.setCaptureSize(Visualizer.getCaptureSizeRange()[1]); //Set capture size to max.

    //Setup listener to fill bytes with waveform data every sampleRate time.
    visualiser.setDataCaptureListener(
        new Visualizer.OnDataCaptureListener() {
          public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
            //visualiserView.updateVisualizer(bytes);
          }

          public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
            visualiserView.updateVisualizerWithFft(bytes);
          }
        }, Visualizer.getMaxCaptureRate() / 2, getWaveform, getFft);  //Choose to get waveform, fft output.
    visualiser.setEnabled(true); //Enabled only when needed, after setCaptureSize.
  }
}